package com.tokopedia.sellerorder.confirmshipping.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.analytics.SomAnalytics
import com.tokopedia.sellerorder.common.errorhandler.SomErrorHandler
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_AGENCY_ID
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_SHIPPING_REF
import com.tokopedia.sellerorder.common.util.SomConsts.INPUT_SP_ID
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_ORDER_ID
import com.tokopedia.sellerorder.common.util.SomConsts.RESULT_CONFIRM_SHIPPING
import com.tokopedia.sellerorder.common.util.Utils
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.sellerorder.confirmshipping.di.SomConfirmShippingComponent
import com.tokopedia.sellerorder.confirmshipping.presentation.activity.SomConfirmShippingActivity
import com.tokopedia.sellerorder.confirmshipping.presentation.adapter.SomBottomSheetCourierListAdapter
import com.tokopedia.sellerorder.confirmshipping.presentation.viewmodel.SomConfirmShippingViewModel
import com.tokopedia.sellerorder.detail.presentation.fragment.SomDetailFragment
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottomsheet_secondary.view.*
import kotlinx.android.synthetic.main.fragment_som_confirm_shipping.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class SomConfirmShippingFragment : BaseDaggerFragment(), SomBottomSheetCourierListAdapter.ActionListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private var currOrderId = ""
    private var currShipmentId = 0L
    private var currShipmentProductId = "0"
    private var currIsChangeShipping = false
    private var confirmShippingResponseMsg = ""
    private var courierListResponse = listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()
    private var changeCourierResponseMsg = ""
    private lateinit var somBottomSheetCourierListAdapter: SomBottomSheetCourierListAdapter
    private lateinit var bottomSheetUnify: BottomSheetUnify

    private val somConfirmShippingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomConfirmShippingViewModel::class.java]
    }

    companion object {
        private const val ERROR_CONFIRM_SHIPPING = "Error when confirm shipping."
        private const val ERROR_GET_COURIER_LIST = "Error when get courier list."
        private const val ERROR_CHANGE_COURIER = "Error when change courier."

        private const val TAG_BOTTOMSHEET = "bottomSheet"

        @JvmStatic
        fun newInstance(bundle: Bundle): SomConfirmShippingFragment {
            return SomConfirmShippingFragment().apply {
                arguments = Bundle().apply {
                    putString(PARAM_ORDER_ID, bundle.getString(PARAM_ORDER_ID))
                    putBoolean(PARAM_CURR_IS_CHANGE_SHIPPING, bundle.getBoolean(PARAM_CURR_IS_CHANGE_SHIPPING))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currOrderId = arguments?.getString(PARAM_ORDER_ID).toString()
        arguments?.getBoolean(PARAM_CURR_IS_CHANGE_SHIPPING)?.let {
            currIsChangeShipping = it
        }
        getCourierList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (currIsChangeShipping) (activity as SomConfirmShippingActivity).supportActionBar?.title = getString(R.string.title_som_change_courier)
        else (activity as SomConfirmShippingActivity).supportActionBar?.title = getString(R.string.title_som_confirm_shipping)
        return inflater.inflate(R.layout.fragment_som_confirm_shipping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
        observingCourierList()
        observingChangeCourier()
    }

    override fun onPause() {
        super.onPause()
        (fragmentManager?.findFragmentByTag(TAG_BOTTOMSHEET) as? BottomSheetUnify)?.let {
            if (it.isVisible) it.dismiss()
        }
    }

    private fun setupLayout() {
        bottomSheetUnify = BottomSheetUnify()
        tf_no_resi?.apply {
            setLabelStatic(true)
            textFiedlLabelText.text = getString(R.string.nomor_resi)
            setMessage(getString(R.string.tf_no_resi_message))
            setPlaceholder(getString(R.string.tf_no_resi_placeholder))
            setFirstIcon(R.drawable.ic_scanbarcode)
        }

        if (currIsChangeShipping) {
            switch_change_courier?.isChecked = true
            setBtnToChangeCourier()
        } else {
            switch_change_courier?.isChecked = false
            setBtnToConfirmShipping()
        }
    }

    private fun setupListeners() {
        // set onclick scan resi
        tf_no_resi?.getFirstIcon()?.setOnClickListener {
            requestBarcodeScanner(activity as Activity, CaptureActivity::class.java)
        }

        if (currIsChangeShipping) {
            switch_change_courier?.isSelected = true
            setBtnToChangeCourier()
        } else {
            setBtnToConfirmShipping()
        }

        // set onchange switch
        switch_change_courier?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setBtnToChangeCourier()
            } else {
                setBtnToConfirmShipping()
            }
        }
    }

    private fun setBtnToChangeCourier() {
        cl_change_courier?.visibility = View.VISIBLE
        btn_confirm_shipping?.setOnClickListener {
            val rawQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_som_change_courier)
            val queryString = rawQuery
                    .replace(INPUT_ORDER_ID, currOrderId)
                    .replace(INPUT_SHIPPING_REF, tf_no_resi?.textFieldInput?.text.toString())
                    .replace(INPUT_AGENCY_ID, currShipmentId.toString())
                    .replace(INPUT_SP_ID, currShipmentProductId)
            processChangeCourier(queryString)
        }
    }

    private fun setBtnToConfirmShipping() {
        cl_change_courier?.visibility = View.GONE
        btn_confirm_shipping?.setOnClickListener {
            val rawQuery = GraphqlHelper.loadRawString(resources, R.raw.gql_som_confirm_shipping)
            val queryString = rawQuery
                    .replace(INPUT_ORDER_ID, currOrderId)
                    .replace(INPUT_SHIPPING_REF, tf_no_resi?.textFieldInput?.text.toString())
            processConfirmShipping(queryString)
        }
        observingConfirmShipping()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomConfirmShippingComponent::class.java).inject(this)
    }

    private fun requestBarcodeScanner(activity: Activity, customClass: Class<*>) {
        val intentIntegrator = IntentIntegrator(activity)
        intentIntegrator.setCaptureActivity(customClass).initiateScan()
    }

    private fun processConfirmShipping(queryString: String) {
        somConfirmShippingViewModel.confirmShipping(queryString)
    }

    private fun processChangeCourier(queryString: String) {
        somConfirmShippingViewModel.changeCourier(queryString)
    }

    private fun getCourierList() {
        somConfirmShippingViewModel.getCourierList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_courier_list))
    }

    private fun observingConfirmShipping() {
        somConfirmShippingViewModel.confirmShippingResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    SomAnalytics.eventClickKonfirmasi(true)
                    confirmShippingResponseMsg = if (it.data.listMessage.isNotEmpty()) {
                        it.data.listMessage.first()
                    } else {
                        getString(R.string.default_confirm_shipping_success)
                    }
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_CONFIRM_SHIPPING, confirmShippingResponseMsg)
                    })
                    activity?.finish()
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_CONFIRM_SHIPPING)
                    SomAnalytics.eventClickKonfirmasi(false)
                    Utils.showToasterError(it.throwable.localizedMessage, view)
                }
            }
        })
    }

    private fun observingCourierList() {
        somConfirmShippingViewModel.courierListResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    courierListResponse = it.data

                    if (courierListResponse.isNotEmpty()) {
                        currShipmentId = courierListResponse.first().shipmentId.toLongOrZero()
                        label_choosen_courier?.text = courierListResponse.first().shipmentName

                        val listServiceCourier = courierListResponse.first().listShipmentPackage
                        if (listServiceCourier.isNotEmpty()) {
                            currShipmentProductId = listServiceCourier.first().spId
                            label_choosen_courier_service?.text = listServiceCourier.first().name
                        }
                    }

                    label_choosen_courier?.setOnClickListener { showBottomSheetCourier(false) }
                    iv_choose_courier?.setOnClickListener { showBottomSheetCourier(false) }

                    label_choosen_courier_service?.setOnClickListener { showBottomSheetCourier(true) }
                    iv_choose_courier_service?.setOnClickListener { showBottomSheetCourier(true) }
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_COURIER_LIST)
                    Utils.showToasterError(getString(R.string.global_error), view)
                }
            }
        })
    }

    private fun observingChangeCourier() {
        somConfirmShippingViewModel.changeCourierResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.mpLogisticChangeCourier.listMessage.isNotEmpty()) {
                        changeCourierResponseMsg = it.data.mpLogisticChangeCourier.listMessage.first()
                    }
                    activity?.setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(RESULT_CONFIRM_SHIPPING, changeCourierResponseMsg)
                    })
                    activity?.finish()
                }
                is Fail -> {
                    SomErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_CHANGE_COURIER)
                    Utils.showToasterError(it.throwable.localizedMessage, view)
                }
            }
        })
    }

    private fun showBottomSheetCourier(isCourierService: Boolean) {
        somBottomSheetCourierListAdapter = SomBottomSheetCourierListAdapter(this)
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        val viewBottomSheet = View.inflate(context, R.layout.bottomsheet_secondary, null)
        viewBottomSheet.rv_bottomsheet_secondary?.apply {
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
            adapter = somBottomSheetCourierListAdapter
        }
        viewBottomSheet.tf_extra_notes?.visibility = View.GONE

        bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
        bottomSheetUnify.setChild(viewBottomSheet)
        fragmentManager?.let { bottomSheetUnify.show(it, TAG_BOTTOMSHEET) }

        if (isCourierService) {
            setCourierServiceListData(currShipmentId)
        } else {
            setCourierListData()
        }
    }

    private fun setCourierServiceListData(shipmentId: Long) {
        bottomSheetUnify.setTitle(SomConsts.TITLE_JENIS_LAYANAN)
        courierListResponse.forEach {
            if (it.shipmentId.toLongOrZero() == shipmentId) {
                somBottomSheetCourierListAdapter.listCourierService = it.listShipmentPackage.toMutableList()
                somBottomSheetCourierListAdapter.isServiceCourier = true
                somBottomSheetCourierListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setCourierListData() {
        bottomSheetUnify.setTitle(SomConsts.TITLE_KURIR_PENGIRIMAN)
        somBottomSheetCourierListAdapter.listCourier = courierListResponse.toMutableList()
        somBottomSheetCourierListAdapter.isServiceCourier = false
        somBottomSheetCourierListAdapter.notifyDataSetChanged()
    }

    override fun onChooseCourierAgent(shipmentId: Long, courierName: String) {
        bottomSheetUnify.dismiss()
        currShipmentId = shipmentId
        label_choosen_courier?.text = courierName
        courierListResponse.forEach {
            if (it.shipmentId.toLongOrZero() == shipmentId) {
                label_choosen_courier_service?.text = it.listShipmentPackage.first().name
                currShipmentProductId = it.listShipmentPackage.first().spId
            }
        }
    }

    override fun onChooseCourierService(spId: String, courierServiceName: String) {
        bottomSheetUnify.dismiss()
        currShipmentProductId = spId
        label_choosen_courier_service?.text = courierServiceName
    }
}