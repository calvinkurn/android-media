package com.tokopedia.logisticorder.view

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.logisticCommon.data.constant.PodConstant
import com.tokopedia.logisticCommon.ui.DelayedEtaBottomSheetFragment
import com.tokopedia.logisticorder.R
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent
import com.tokopedia.logisticorder.uimodel.ProofModel
import com.tokopedia.logisticorder.uimodel.TippingModel
import com.tokopedia.logisticorder.uimodel.TrackingPageEvent
import com.tokopedia.logisticorder.utils.TippingConstant
import com.tokopedia.logisticorder.view.bottomsheet.DriverInfoBottomSheet
import com.tokopedia.logisticorder.view.component.TrackingPageScreen
import com.tokopedia.nest.principles.ui.NestTheme
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import com.tokopedia.logisticorder.R as logisticorderR

class TrackingPageComposeActivity : AppCompatActivity() {

    companion object {
        private const val ARGUMENTS_ORDER_ID = "ARGUMENTS_ORDER_ID"
        private const val ARGUMENTS_ORDER_TX_ID = "ARGUMENTS_ORDER_TX_ID"
        private const val ARGUMENTS_GROUP_TYPE = "ARGUMENTS_GROUP_TYPE"
        private const val ARGUMENTS_TRACKING_URL = "ARGUMENTS_TRACKING_URL"
        private const val ARGUMENTS_CALLER = "ARGUMENTS_CALLER"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)[TrackingPageComposeViewModel::class.java]
    }

    private val orderId by lazy { intent.extras?.getString(ARGUMENTS_ORDER_ID).orEmpty() }
    private val urlLiveTracking by lazy {
        intent.extras?.getString(ARGUMENTS_TRACKING_URL).orEmpty()
    }
    private val orderCaller by lazy { intent.extras?.getString(ARGUMENTS_CALLER).orEmpty() }
    private val orderTxId by lazy { intent?.extras?.getString(ARGUMENTS_ORDER_TX_ID).orEmpty() }
    private val groupType by lazy { intent?.extras?.getInt(ARGUMENTS_GROUP_TYPE) ?: 0 }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectComponent()
        super.onCreate(savedInstanceState)
        viewModel.onEvent(
            TrackingPageEvent.LoadTrackingData(
                orderId,
                orderTxId,
                groupType,
                urlLiveTracking,
                orderCaller
            )
        )

        setContent {
            val view = LocalView.current
            LaunchedEffect(key1 = viewModel.error, block = {
                viewModel.error.collectLatest {
                    showToaster(view, it.message.orEmpty(), type = Toaster.TYPE_ERROR)
                }
            })
            NestTheme {
                val state = viewModel.uiState.collectAsState()

                TrackingPageScreen(
                    state = state.value,
                    openWebview = ::openWebView,
                    onClickTippingButton = ::onClickTipping,
                    openTippingInfo = ::showDriverInfo,
                    pressBack = ::finish,
                    callDriver = ::goToCallIntent,
                    seeProofOfDelivery = ::navigateToPodActivity,
                    copyShippingRefNumber = { initClickToCopy(view, it) },
                    seeEtaChangesInfo = ::showEtaBottomSheet,
                    onEvent = { viewModel.onEvent(it) }
                )
            }
        }
    }

    private fun injectComponent() {
        DaggerTrackingPageComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    private fun openWebView(url: String) {
        val cleanedUrl = HtmlLinkHelper(this, url).spannedString
        RouteManager.route(
            this,
            "${ApplinkConst.WEBVIEW}?url=$cleanedUrl"
        )
    }

    private fun onClickTipping(tippingModel: TippingModel) {
        when (tippingModel.status) {
            TippingConstant.SUCCESS_PAYMENT, TippingConstant.SUCCESS_TIPPING, TippingConstant.OPEN -> {
                RouteManager.route(
                    this,
                    "${ApplinkConstInternalLogistic.TIPPING_DRIVER}?${TippingConstant.PARAM_ORDER_ID}=$orderId&${TippingConstant.PARAM_REF_NUM}=${tippingModel.refNumber}"
                )
            }

            TippingConstant.WAITING_PAYMENT -> {
                RouteManager.route(this, ApplinkConst.PMS)
            }

            TippingConstant.REFUND_TIP -> {
                RouteManager.route(this, ApplinkConst.SALDO)
            }

            else -> {
                // no ops
            }
        }
    }

    private fun goToCallIntent(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            this.data = Uri.parse("tel:$phoneNumber")
        }
        try {
            startActivity(callIntent)
        } catch (_: ActivityNotFoundException) {
            // no-op
        }
    }

    private fun navigateToPodActivity(proofModel: ProofModel) {
        val appLink = Uri.parse(ApplinkConstInternalLogistic.PROOF_OF_DELIVERY).buildUpon()
            .appendQueryParameter(PodConstant.QUERY_IMAGE_ID, proofModel.imageId)
            .appendQueryParameter(PodConstant.QUERY_DESCRIPTION, proofModel.description)
            .build()
            .toString()
        val intent = RouteManager.getIntent(this, appLink, orderId)

        startActivity(intent)
    }

    private fun initClickToCopy(composeView: View, referenceNumber: String) {
        val clipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(
            ClipData.newPlainText(
                getString(logisticorderR.string.label_copy_reference_number),
                referenceNumber
            )

        )
        showToaster(composeView, getString(R.string.success_copy_reference_number))
    }

    private fun showToaster(composeView: View, message: String, type: Int = Toaster.TYPE_NORMAL) {
        Toaster.build(
            composeView,
            message,
            Toaster.LENGTH_SHORT,
            type
        ).show()
    }

    private fun showEtaBottomSheet(description: String) {
        val delayedEtaBottomSheetFragment = DelayedEtaBottomSheetFragment.newInstance(description)
        delayedEtaBottomSheetFragment.show(supportFragmentManager, "")
    }

    private fun showDriverInfo() {
        DriverInfoBottomSheet().show(supportFragmentManager)
    }
}
