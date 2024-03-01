package com.tokopedia.logisticseller.ui.confirmshipping.ui

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
import com.tokopedia.logisticseller.ui.confirmshipping.data.ConfirmShippingAnalytics
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingErrorStateSource
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingEvent
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingMode
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.ConfirmShippingResult
import com.tokopedia.logisticseller.ui.confirmshipping.di.ConfirmShippingComponent
import com.tokopedia.logisticseller.ui.confirmshipping.di.DaggerConfirmShippingComponent
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

/**
 * this used for
 * - confirm shipping
 * - change courier
 */
class ConfirmShippingComposeActivity : AppCompatActivity() {

    companion object {
        private const val ERROR_CONFIRM_SHIPPING = "Error when confirm shipping."
        private const val ERROR_GET_COURIER_LIST = "Error when get courier list."
        private const val ERROR_CHANGE_COURIER = "Error when change courier."
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

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
                    when (it.source) {
                        ConfirmShippingErrorStateSource.COURIER_LIST -> handleErrorCourierList(
                            it.throwable,
                            view
                        )

                        ConfirmShippingErrorStateSource.TARGETED_TICKER -> {
                            Utils.showToasterError(
                                LogisticSellerErrorHandler.getErrorMessage(
                                    it.throwable,
                                    this@ConfirmShippingComposeActivity
                                ), view
                            )
                        }
                    }
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
                    onClickCourier = {
                        confirmShippingViewModel.onEvent(
                            ConfirmShippingEvent.ChooseCourier(
                                it
                            )
                        )
                    },
                    onClickService = {
                        confirmShippingViewModel.onEvent(
                            ConfirmShippingEvent.ChooseService(
                                it
                            )
                        )
                    },
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

    private fun handleErrorCourierList(throwable: Throwable, view: View) {
        LogisticSellerErrorHandler.logExceptionToCrashlytics(
            throwable,
            ERROR_GET_COURIER_LIST
        )
        Utils.showToasterError(getString(R.string.global_error), view)
        LogisticSellerErrorHandler.logExceptionToServer(
            errorTag = LogisticSellerErrorHandler.SOM_TAG,
            throwable = throwable,
            errorType =
            LogisticSellerErrorHandler.SomMessage.GET_COURIER_LIST_ERROR,
            deviceId = userSession.deviceId.orEmpty()
        )
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
                    message
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
}
