package com.tokopedia.logisticseller.ui.confirmshipping.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_ORDER_ID
import com.tokopedia.logisticseller.common.Utils
import com.tokopedia.logisticseller.common.errorhandler.LogisticSellerErrorHandler
import com.tokopedia.logisticseller.ui.confirmshipping.data.ConfirmShippingAnalytics
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.di.ConfirmShippingComponent
import com.tokopedia.logisticseller.ui.confirmshipping.di.DaggerConfirmShippingComponent
import com.tokopedia.targetedticker.domain.TargetedTickerParamModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * Created by fwidjaja on 2019-11-15.
 *
 * this used for
 * - confirm shipping
 * - change courier
 */
class ConfirmShippingComposeActivity : AppCompatActivity(),
    BottomSheetCourierListAdapter.ActionListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.run {
            decorView.setBackgroundColor(
                MethodChecker.getColor(
                    this@ConfirmShippingComposeActivity,
                    unifyprinciplesR.color.Unify_Background
                )
            )
        }
    }

    rivate
    fun getBarcode(requestCode: Int, resultCode: Int, data: Intent): String {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        return if (scanResult?.contents != null) {
            scanResult.contents
        } else {
            ""
        }
    }

    private fun setupLayout() {
        bottomSheetUnify = BottomSheetUnify()
//        binding?.tfNoResi?.apply {
//            setLabelStatic(true)
//            textFiedlLabelText.text = getString(R.string.nomor_resi)
//            setMessage(getString(R.string.tf_no_resi_message))
//            setPlaceholder(getString(R.string.tf_no_resi_placeholder))
//            setFirstIcon(R.drawable.ic_scanbarcode)
//        }

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

    fun initInjector() {
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

    private fun processChangeCourier(
        orderId: String,
        shippingRef: String,
        agencyId: Long,
        spId: Long
    ) {
        confirmShippingViewModel.changeCourier(orderId, shippingRef, agencyId, spId)
    }

    private fun getCourierList(orderId: String) {
        confirmShippingViewModel.getCourierList(orderId)
    }

    private fun observingConfirmShipping() {
        confirmShippingViewModel.confirmShippingResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        ConfirmShippingAnalytics.eventClickKonfirmasi(true)
                        confirmShippingResponseMsg = if (it.data.listMessage.isNotEmpty()) {
                            it.data.listMessage.first()
                        } else {
                            getString(R.string.default_confirm_shipping_success)
                        }
                        activity?.setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(
                                    LogisticSellerConst.RESULT_CONFIRM_SHIPPING,
                                    confirmShippingResponseMsg
                                )
                            }
                        )
                        activity?.finish()
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(
                            it.throwable,
                            ConfirmShippingFragment.ERROR_CONFIRM_SHIPPING
                        )
                        ConfirmShippingAnalytics.eventClickKonfirmasi(false)
                        context?.run {
                            Utils.showToasterError(
                                LogisticSellerErrorHandler.getErrorMessage(
                                    it.throwable,
                                    this
                                ), view
                            )
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
                        courierListResponse = it.data.listShipment

                        if (courierListResponse.isNotEmpty()) {
                            currShipmentId = courierListResponse.first().shipmentId.toLongOrZero()
                            binding?.labelChoosenCourier?.text =
                                courierListResponse.first().shipmentName

                            val listServiceCourier = courierListResponse.first().listShipmentPackage
                            if (listServiceCourier.isNotEmpty()) {
                                currShipmentProductId = listServiceCourier.first().spId
                                binding?.labelChoosenCourierService?.text =
                                    listServiceCourier.first().name
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

                        binding?.labelChoosenCourierService?.setOnClickListener {
                            showBottomSheetCourier(
                                true
                            )
                        }
                        binding?.ivChooseCourierService?.setOnClickListener {
                            showBottomSheetCourier(
                                true
                            )
                        }

                        initTicker(it.data.tickerUnificationParams)
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(
                            it.throwable,
                            ConfirmShippingFragment.ERROR_GET_COURIER_LIST
                        )
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

    private fun initTicker(params: SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.TickerUnificationParams) {
        binding?.ticker?.apply { ->
            setTickerShape(Ticker.SHAPE_FULL)

            val template = TargetedTickerParamModel.Template().copy(
                contents =
                params.template.contents.map {
                    TargetedTickerParamModel.Template.Content(it.key, it.value)
                }
            )
            val target = params.target.map {
                TargetedTickerParamModel.Target(it.type, it.values)
            }

            val param = TargetedTickerParamModel(
                page = params.page,
                template = template,
                target = target
            )
            loadAndShow(param)
        }
    }

    private fun observingChangeCourier() {
        confirmShippingViewModel.changeCourierResult.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.mpLogisticChangeCourier.listMessage.isNotEmpty()) {
                            changeCourierResponseMsg =
                                it.data.mpLogisticChangeCourier.listMessage.first()
                        }
                        activity?.setResult(
                            Activity.RESULT_OK,
                            Intent().apply {
                                putExtra(
                                    LogisticSellerConst.RESULT_CONFIRM_SHIPPING,
                                    changeCourierResponseMsg
                                )
                            }
                        )
                        activity?.finish()
                    }

                    is Fail -> {
                        LogisticSellerErrorHandler.logExceptionToCrashlytics(
                            it.throwable,
                            ConfirmShippingFragment.ERROR_CHANGE_COURIER
                        )
                        context?.run {
                            Utils.showToasterError(
                                LogisticSellerErrorHandler.getErrorMessage(
                                    it.throwable,
                                    this
                                ), view
                            )
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
                layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierListAdapter
            }
            findViewById<View>(R.id.tf_extra_notes)?.visibility = View.GONE

            bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
            bottomSheetUnify.setChild(this)
            childFragmentManager.let {
                bottomSheetUnify.show(
                    it,
                    ConfirmShippingFragment.TAG_BOTTOMSHEET
                )
            }
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
                somBottomSheetCourierListAdapter.listCourierService =
                    it.listShipmentPackage.toMutableList()
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
//    override fun getNewFragment(): Fragment? {
//        var bundle = Bundle()
//        if (intent.extras != null) {
//            bundle = intent.extras ?: Bundle()
//        } else {
//            bundle.putString(PARAM_ORDER_ID, "")
//            bundle.putBoolean(PARAM_CURR_IS_CHANGE_SHIPPING, false)
//        }
//        return ConfirmShippingFragment.newInstance(bundle)
//    }
}
