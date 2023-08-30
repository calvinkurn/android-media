package com.tokopedia.logisticseller.ui.confirmshipping.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_ORDER_ID
import com.tokopedia.logisticseller.common.LogisticSellerConst.RESULT_CONFIRM_SHIPPING
import com.tokopedia.logisticseller.common.Utils
import com.tokopedia.logisticseller.common.Utils.updateShopActive
import com.tokopedia.logisticseller.common.errorhandler.LogisticSellerErrorHandler
import com.tokopedia.logisticseller.databinding.FragmentSomConfirmShippingBinding
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.di.ConfirmShippingComponent
import com.tokopedia.logisticseller.ui.confirmshipping.di.DaggerConfirmShippingComponent
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 */
class ConfirmShippingFragment : BaseDaggerFragment(), BottomSheetCourierListAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var currOrderId = ""
    private var currShipmentId = 0L
    private var currShipmentProductId = "0"
    private var currIsChangeShipping = false
    private var confirmShippingResponseMsg = ""
    private var courierListResponse = listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()
    private var changeCourierResponseMsg = ""
    private lateinit var somBottomSheetCourierListAdapter: BottomSheetCourierListAdapter
    private lateinit var bottomSheetUnify: BottomSheetUnify

    private val confirmShippingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ConfirmShippingViewModel::class.java]
    }

    @SuppressLint("ClickableViewAccessibility")
    private val hideKeyboardTouchListener = View.OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            view?.hideKeyboard()
        }
        false
    }

    private val binding by viewBinding(FragmentSomConfirmShippingBinding::bind)

    companion object {
        private const val ERROR_CONFIRM_SHIPPING = "Error when confirm shipping."
        private const val ERROR_GET_COURIER_LIST = "Error when get courier list."
        private const val ERROR_CHANGE_COURIER = "Error when change courier."

        private const val TAG_BOTTOMSHEET = "bottomSheet"

        @JvmStatic
        fun newInstance(bundle: Bundle): ConfirmShippingFragment {
            return ConfirmShippingFragment().apply {
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
        if (currIsChangeShipping) {
            (activity as ConfirmShippingActivity).supportActionBar?.title =
                getString(R.string.title_som_change_courier)
        } else {
            (activity as ConfirmShippingActivity).supportActionBar?.title = getString(R.string.title_som_confirm_shipping)
        }
        return inflater.inflate(R.layout.fragment_som_confirm_shipping, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLayout()
        setupListeners()
        observingCourierList()
        observingChangeCourier()
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    override fun onPause() {
        super.onPause()
        (fragmentManager?.findFragmentByTag(TAG_BOTTOMSHEET) as? BottomSheetUnify)?.let {
            if (it.isVisible) it.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let { it ->
            val barcode = getBarcode(requestCode, resultCode, it)
            binding?.tfNoResi?.textFieldInput?.setText(barcode)
            super.onActivityResult(requestCode, resultCode, it)
        }
    }

    private fun getBarcode(requestCode: Int, resultCode: Int, data: Intent): String {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        return if (scanResult?.contents != null) {
            scanResult.contents
        } else {
            ""
        }
    }

    private fun setupLayout() {
        bottomSheetUnify = BottomSheetUnify()
        binding?.tfNoResi?.apply {
            setLabelStatic(true)
            textFiedlLabelText.text = getString(R.string.nomor_resi)
            setMessage(getString(R.string.tf_no_resi_message))
            setPlaceholder(getString(R.string.tf_no_resi_placeholder))
            setFirstIcon(R.drawable.ic_scanbarcode)
        }

        if (currIsChangeShipping) {
            binding?.switchChangeCourier?.isChecked = true
            setBtnToChangeCourier()
        } else {
            binding?.switchChangeCourier?.isChecked = false
            setBtnToConfirmShipping()
        }
        view?.setOnTouchListener(hideKeyboardTouchListener)
        setupHeader()
    }

    private fun setupHeader() {
        (activity as? AppCompatActivity)?.run {
            supportActionBar?.hide()
            setSupportActionBar(binding?.headerSomConfirmShipping)
        }
    }

    private fun setupListeners() {
        // set onclick scan resi
        binding?.tfNoResi?.getFirstIcon()?.setOnClickListener {
            requestBarcodeScanner()
        }

        if (currIsChangeShipping) {
            binding?.switchChangeCourier?.isSelected = true
            setBtnToChangeCourier()
        } else {
            setBtnToConfirmShipping()
        }

        // set onchange switch
        binding?.switchChangeCourier?.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setBtnToChangeCourier()
            } else {
                setBtnToConfirmShipping()
            }
        }
    }

    private fun setBtnToChangeCourier() {
        binding?.clChangeCourier?.visibility = View.VISIBLE
        binding?.btnConfirmShipping?.setOnClickListener {
            processChangeCourier(
                currOrderId,
                binding?.tfNoResi?.textFieldInput?.text.toString(),
                currShipmentId,
                currShipmentProductId.toLongOrZero()
            )
        }
    }

    private fun setBtnToConfirmShipping() {
        binding?.clChangeCourier?.visibility = View.GONE
        binding?.btnConfirmShipping?.setOnClickListener {
            processConfirmShipping(currOrderId, binding?.tfNoResi?.textFieldInput?.text.toString())
        }
        observingConfirmShipping()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        val component: ConfirmShippingComponent = DaggerConfirmShippingComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    private fun requestBarcodeScanner() {
        val intentIntegrator = IntentIntegrator.forSupportFragment(this)
        intentIntegrator.setCaptureActivity(CaptureActivity::class.java).initiateScan()
    }

    private fun processConfirmShipping(orderId: String, shippingRef: String) {
        confirmShippingViewModel.confirmShipping(orderId, shippingRef)
    }

    private fun processChangeCourier(orderId: String, shippingRef: String, agencyId: Long, spId: Long) {
        confirmShippingViewModel.changeCourier(orderId, shippingRef, agencyId, spId)
    }

    private fun getCourierList() {
        confirmShippingViewModel.getCourierList()
    }

    private fun observingConfirmShipping() {
        confirmShippingViewModel.confirmShippingResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
//                    SomAnalytics.eventClickKonfirmasi(true)
                        confirmShippingResponseMsg = if (it.data.listMessage.isNotEmpty()) {
                            it.data.listMessage.first()
                        } else {
                            getString(R.string.default_confirm_shipping_success)
                        }
                        activity?.setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(RESULT_CONFIRM_SHIPPING, confirmShippingResponseMsg)
                            }
                        )
                        activity?.finish()
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_CONFIRM_SHIPPING)
//                    SomAnalytics.eventClickKonfirmasi(false)
                        context?.run {
                            Utils.showToasterError(LogisticSellerErrorHandler.getErrorMessage(it.throwable, this), view)
                        }
                        LogisticSellerErrorHandler.logExceptionToServer(
                            errorTag = LogisticSellerErrorHandler.SOM_TAG,
                            throwable = it.throwable,
                            errorType =
                            LogisticSellerErrorHandler.SomMessage.CONFIRM_SHIPPING_ERROR,
                            deviceId = userSession.deviceId.orEmpty()
                        )
                    }
                }
            }
        )
    }

    private fun observingCourierList() {
        confirmShippingViewModel.courierListResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        courierListResponse = it.data

                        if (courierListResponse.isNotEmpty()) {
                            currShipmentId = courierListResponse.first().shipmentId.toLongOrZero()
                            binding?.labelChoosenCourier?.text = courierListResponse.first().shipmentName

                            val listServiceCourier = courierListResponse.first().listShipmentPackage
                            if (listServiceCourier.isNotEmpty()) {
                                currShipmentProductId = listServiceCourier.first().spId
                                binding?.labelChoosenCourierService?.text = listServiceCourier.first().name
                            }
                        }

                        binding?.labelChoosenCourier?.setOnClickListener {
                            view?.hideKeyboard()
                            showBottomSheetCourier(false)
                        }
                        binding?.ivChooseCourier?.setOnClickListener {
                            view?.hideKeyboard()
                            showBottomSheetCourier(false)
                        }

                        binding?.labelChoosenCourierService?.setOnClickListener { showBottomSheetCourier(true) }
                        binding?.ivChooseCourierService?.setOnClickListener { showBottomSheetCourier(true) }
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_GET_COURIER_LIST)
                        Utils.showToasterError(getString(R.string.global_error), view)
                        LogisticSellerErrorHandler.logExceptionToServer(
                            errorTag = LogisticSellerErrorHandler.SOM_TAG,
                            throwable = it.throwable,
                            errorType =
                            LogisticSellerErrorHandler.SomMessage.GET_COURIER_LIST_ERROR,
                            deviceId = userSession.deviceId.orEmpty()
                        )
                    }
                }
            }
        )
    }

    private fun observingChangeCourier() {
        confirmShippingViewModel.changeCourierResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.mpLogisticChangeCourier.listMessage.isNotEmpty()) {
                            changeCourierResponseMsg = it.data.mpLogisticChangeCourier.listMessage.first()
                        }
                        activity?.setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(RESULT_CONFIRM_SHIPPING, changeCourierResponseMsg)
                            }
                        )
                        activity?.finish()
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(it.throwable, ERROR_CHANGE_COURIER)
                        context?.run {
                            Utils.showToasterError(LogisticSellerErrorHandler.getErrorMessage(it.throwable, this), view)
                        }
                        LogisticSellerErrorHandler.logExceptionToServer(
                            errorTag = LogisticSellerErrorHandler.SOM_TAG,
                            throwable = it.throwable,
                            errorType =
                            LogisticSellerErrorHandler.SomMessage.CHANGE_COURIER_ERROR,
                            deviceId = userSession.deviceId.orEmpty()
                        )
                    }
                }
            }
        )
    }

    private fun showBottomSheetCourier(isCourierService: Boolean) {
        somBottomSheetCourierListAdapter = BottomSheetCourierListAdapter(this)
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        View.inflate(context, R.layout.bottomsheet_secondary_confirm_shipping, null).run {
            findViewById<RecyclerView>(R.id.rv_bottomsheet_secondary)?.apply {
                layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierListAdapter
            }
            findViewById<View>(R.id.tf_extra_notes)?.visibility = View.GONE

            bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
            bottomSheetUnify.setChild(this)
            childFragmentManager.let { bottomSheetUnify.show(it, TAG_BOTTOMSHEET) }
        }

        if (isCourierService) {
            setCourierServiceListData(currShipmentId)
        } else {
            setCourierListData()
        }
    }

    private fun setCourierServiceListData(shipmentId: Long) {
        bottomSheetUnify.setTitle(LogisticSellerConst.TITLE_JENIS_LAYANAN)
        courierListResponse.forEach {
            if (it.shipmentId.toLongOrZero() == shipmentId) {
                somBottomSheetCourierListAdapter.listCourierService = it.listShipmentPackage.toMutableList()
                somBottomSheetCourierListAdapter.isServiceCourier = true
                somBottomSheetCourierListAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun setCourierListData() {
        bottomSheetUnify.setTitle(LogisticSellerConst.TITLE_KURIR_PENGIRIMAN)
        somBottomSheetCourierListAdapter.listCourier = courierListResponse.toMutableList()
        somBottomSheetCourierListAdapter.isServiceCourier = false
        somBottomSheetCourierListAdapter.notifyDataSetChanged()
    }

    override fun onChooseCourierAgent(shipmentId: Long, courierName: String) {
        bottomSheetUnify.dismiss()
        currShipmentId = shipmentId
        binding?.labelChoosenCourier?.text = courierName
        courierListResponse.forEach {
            if (it.shipmentId.toLongOrZero() == shipmentId) {
                binding?.labelChoosenCourierService?.text = it.listShipmentPackage.first().name
                currShipmentProductId = it.listShipmentPackage.first().spId
            }
        }
    }

    override fun onChooseCourierService(spId: String, courierServiceName: String) {
        bottomSheetUnify.dismiss()
        currShipmentProductId = spId
        binding?.labelChoosenCourierService?.text = courierServiceName
    }
}
