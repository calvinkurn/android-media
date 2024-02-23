package com.tokopedia.logisticseller.ui.confirmshipping.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.zxing.integration.android.IntentIntegrator
import com.journeyapps.barcodescanner.CaptureActivity
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_CURR_IS_CHANGE_SHIPPING
import com.tokopedia.logisticseller.common.LogisticSellerConst.PARAM_ORDER_ID
import com.tokopedia.logisticseller.common.Utils
import com.tokopedia.logisticseller.common.Utils.updateShopActive
import com.tokopedia.logisticseller.common.errorhandler.LogisticSellerErrorHandler
import com.tokopedia.logisticseller.ui.confirmshipping.ConfirmShippingScreen
import com.tokopedia.logisticseller.ui.confirmshipping.data.ConfirmShippingAnalytics
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingEvent
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingResult
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingState
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.di.ConfirmShippingComponent
import com.tokopedia.logisticseller.ui.confirmshipping.di.DaggerConfirmShippingComponent
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-11-15.
 *
 * this used for
 * - confirm shipping
 * - change courier
 */
class ConfirmShippingComposeActivity : AppCompatActivity(),
    BottomSheetCourierListAdapter.ActionListener {

    companion object {
        private const val ERROR_CONFIRM_SHIPPING = "Error when confirm shipping."
        private const val ERROR_GET_COURIER_LIST = "Error when get courier list."
        private const val ERROR_CHANGE_COURIER = "Error when change courier."

        private const val TAG_BOTTOMSHEET = "bottomSheet"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private var currOrderId = ""
    private var currShipmentId = 0L
    private var currShipmentProductId = "0"
    private var currIsChangeShipping = false
    private var confirmShippingResponseMsg = ""
    private var courierListResponse =
        listOf<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>()
    private var changeCourierResponseMsg = ""
    private lateinit var somBottomSheetCourierListAdapter: BottomSheetCourierListAdapter
    private lateinit var bottomSheetUnify: BottomSheetUnify

    private val confirmShippingViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ConfirmShippingComposeViewModel::class.java]
    }

    private val orderId by lazy { intent.extras?.getString(PARAM_ORDER_ID).orEmpty() }
    private val mode: ConfirmShippingMode by lazy {
        if (intent.extras?.getBoolean(
                PARAM_CURR_IS_CHANGE_SHIPPING
            ).orFalse()
        ) ConfirmShippingMode.CHANGE_COURIER else ConfirmShippingMode.CONFIRM_SHIPPING
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)

        confirmShippingViewModel.onEvent(ConfirmShippingEvent.OnCreate(orderId, mode))
        bottomSheetUnify = BottomSheetUnify()

        setContent {
            val view = LocalView.current
            val state = confirmShippingViewModel.uiState.collectAsState()
            LaunchedEffect(key1 = confirmShippingViewModel.result, block = {
                confirmShippingViewModel.result.collectLatest {
                    when (it) {
                        is ConfirmShippingResult.CourierChanged -> {
                            setResultAndFinish(it.message.orEmpty())
                        }

                        is ConfirmShippingResult.ShippingConfirmed -> {
                            ConfirmShippingAnalytics.eventClickKonfirmasi(true)
                            setResultAndFinish(it.message.orEmpty()
                                .ifEmpty { getString(R.string.default_confirm_shipping_success) })
                        }

                        is ConfirmShippingResult.FailedChangeCourier -> handleErrorChangeCourier(
                            it.throwable,
                            view
                        )

                        is ConfirmShippingResult.FailedConfirmShipping -> handleErrorConfirmShipping(
                            it.throwable,
                            view
                        )
                    }
                }
            })
            LaunchedEffect(key1 = confirmShippingViewModel.error, block = {
                confirmShippingViewModel.error.collectLatest {
                    Utils.showToasterError(
                        LogisticSellerErrorHandler.getErrorMessage(
                            it,
                            this@ConfirmShippingComposeActivity
                        ), view
                    )
                }
            })

            NestTheme {
                ConfirmShippingScreen(
                    state = state.value,
                    source = mode,
                    pressBack = ::finish,
                    onClickBarcodeIcon = ::requestBarcodeScanner,
                    onSwitchChanged = {
                        confirmShippingViewModel.onEvent(
                            ConfirmShippingEvent.SwitchMode(
                                it
                            )
                        )
                    },
                    onClickCourier = { showBottomSheetCourier(it, false) },
                    onClickService = { showBottomSheetCourier(it, true) },
                    onChangeRefNum = {
                        confirmShippingViewModel.onEvent(
                            ConfirmShippingEvent.ChangeRefNum(
                                it
                            )
                        )
                    },
                    openWebview = ::goToWebView,
                    onSubmit = {
                        confirmShippingViewModel.onEvent(
                            ConfirmShippingEvent.Submit(
                                orderId
                            )
                        )
                    }
                )
            }
        }
    }

    private fun handleErrorConfirmShipping(throwable: Throwable, view: View) {
        LogisticSellerErrorHandler.logExceptionToCrashlytics(
            throwable, ERROR_CONFIRM_SHIPPING
        )
        ConfirmShippingAnalytics.eventClickKonfirmasi(false)

        Utils.showToasterError(
            LogisticSellerErrorHandler.getErrorMessage(
                throwable,
                this
            ), view
        )

        LogisticSellerErrorHandler.logExceptionToServer(
            errorTag = LogisticSellerErrorHandler.SOM_TAG,
            throwable = throwable,
            errorType =
            LogisticSellerErrorHandler.SomMessage.CONFIRM_SHIPPING_ERROR,
            deviceId = userSession.deviceId.orEmpty()
        )
    }

    private fun handleErrorChangeCourier(throwable: Throwable, view: View) {
        LogisticSellerErrorHandler.logExceptionToCrashlytics(
            throwable,
            ERROR_CHANGE_COURIER
        )
        Utils.showToasterError(
            LogisticSellerErrorHandler.getErrorMessage(
                throwable,
                this
            ), view
        )

        LogisticSellerErrorHandler.logExceptionToServer(
            errorTag = LogisticSellerErrorHandler.SOM_TAG,
            throwable = throwable,
            errorType =
            LogisticSellerErrorHandler.SomMessage.CHANGE_COURIER_ERROR,
            deviceId = userSession.deviceId.orEmpty()
        )
    }

    private fun setResultAndFinish(message: String) {
        setResult(
            Activity.RESULT_OK,
            Intent().apply {
                putExtra(
                    LogisticSellerConst.RESULT_CONFIRM_SHIPPING,
                    message.orEmpty()
                )
            }
        )
        finish()
    }

    override fun onResume() {
        super.onResume()
        updateShopActive()
    }

    private fun getBarcode(requestCode: Int, resultCode: Int, data: Intent): String {
        val scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        return if (scanResult?.contents != null) {
            scanResult.contents
        } else {
            ""
        }
    }

    private fun initInjector() {
        val component: ConfirmShippingComponent = DaggerConfirmShippingComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
        component.inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        data?.let {
            val barcode = getBarcode(requestCode, resultCode, it)
            confirmShippingViewModel.onEvent(ConfirmShippingEvent.ChangeRefNum(barcode))
            super.onActivityResult(requestCode, resultCode, it)
        }
    }

    private fun requestBarcodeScanner() {
        val intentIntegrator = IntentIntegrator(this)
        intentIntegrator.setCaptureActivity(CaptureActivity::class.java).initiateScan()
    }

    private fun goToWebView(url: String) {
        RouteManager.route(
            this,
            ApplinkConst.WEBVIEW.plus("?url=$url")
        )
    }

    private fun showBottomSheetCourier(state: ConfirmShippingState, isCourierService: Boolean) {
        somBottomSheetCourierListAdapter = BottomSheetCourierListAdapter(this)
        if (bottomSheetUnify.isAdded) bottomSheetUnify.dismiss()
        View.inflate(this, R.layout.bottomsheet_secondary_confirm_shipping, null).run {
            findViewById<RecyclerView>(R.id.rv_bottomsheet_secondary)?.apply {
                layoutManager =
                    LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
                adapter = somBottomSheetCourierListAdapter
            }
            findViewById<View>(R.id.tf_extra_notes)?.visibility = View.GONE

            bottomSheetUnify.setCloseClickListener { bottomSheetUnify.dismiss() }
            bottomSheetUnify.setChild(this)
            supportFragmentManager.let {
                bottomSheetUnify.show(
                    it,
                    TAG_BOTTOMSHEET
                )
            }
        }

        if (isCourierService) {
            setCourierServiceListData(state)
        } else {
            setCourierListData(state)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCourierServiceListData(state: ConfirmShippingState) {
        state.chosenCourier?.listShipmentPackage?.run {
            bottomSheetUnify.setTitle(LogisticSellerConst.TITLE_JENIS_LAYANAN)
            somBottomSheetCourierListAdapter.isServiceCourier = true
            somBottomSheetCourierListAdapter.listCourierService = this.toMutableList()
            somBottomSheetCourierListAdapter.notifyDataSetChanged()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setCourierListData(state: ConfirmShippingState) {
        state.courierList?.run {
            bottomSheetUnify.setTitle(LogisticSellerConst.TITLE_KURIR_PENGIRIMAN)
            somBottomSheetCourierListAdapter.listCourier = this.toMutableList()
            somBottomSheetCourierListAdapter.isServiceCourier = false
            somBottomSheetCourierListAdapter.notifyDataSetChanged()
        }
    }

    override fun onChooseCourierAgent(shipmentId: Long, courierName: String) {
        bottomSheetUnify.dismiss()
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.ChooseCourier(
                shipmentId,
                courierName
            )
        )
    }

    override fun onChooseCourierService(spId: String, courierServiceName: String) {
        bottomSheetUnify.dismiss()
        confirmShippingViewModel.onEvent(
            ConfirmShippingEvent.ChooseService(
                spId,
                courierServiceName
            )
        )
    }
}
