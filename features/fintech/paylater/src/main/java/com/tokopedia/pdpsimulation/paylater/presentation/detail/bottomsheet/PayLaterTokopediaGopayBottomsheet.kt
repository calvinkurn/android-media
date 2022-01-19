package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.common.utils.Utils
import com.tokopedia.pdpsimulation.paylater.domain.model.Cta
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_gopay_activation_bottomsheet.*

class PayLaterTokopediaGopayBottomsheet : BottomSheetUnify() {

 /*   private var pdpSimulationCallback: PdpSimulationCallback? = null
    private lateinit var cta: Cta
    private var isWebLink = true


    private var parterName: String? = ""
    private var tenure: Int? = 0
    private var montlyInstallment: Double? = 0.0
    private var productId: String? = ""
    private var redirectPosition = 0


    private val childLayoutRes = R.layout.paylater_gopay_activation_bottomsheet


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()

    }


    private fun getArgumentData() {
        arguments?.let {
            //cta = it.getParcelable<Cta>(GOPAY_BOTTOMSHEET_DETAIL) as Cta
            parterName = it.getString(PARTER_NAME) ?: ""
            tenure = it.getInt(TENURE)
            montlyInstallment = it.getDouble(EMI_AMOUNT)
            productId = it.getString(PRODUCT_ID)
            //redirectPosition = it.getInt(PAYLATER_PARTNER_POSITION)

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        renderUI()
        initListners()
        sendImpressionAnalytics()
    }

    private fun renderUI() {
        if (::cta.isInitialized) {
            imageDisplayer.setImageUrl(cta.bottomSheet?.imageUrl ?: "")
            tokoToGoPayHeader.text = cta.bottomSheet?.bottomSheetTitle ?: ""
            tokoToGoPaySubHeader.text = cta.bottomSheet?.bottomSheetDescription ?: ""
            btnRegister.text = cta.bottomSheet?.bottomSheetButtonText ?: ""
            if (cta.cta_type == REDIRECT_TOKO_ENV)
                isWebLink = false
        }
    }

    private fun sendImpressionAnalytics() {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.GopayBottomSheetImpression(
                productId ?: "",
                tenure.toString(),
                parterName ?: "",
                montlyInstallment.toString()
            )
        )
    }

    private fun initListners() {
        btnRegister.setOnClickListener {
            sendClickAnalytics()
            if (::cta.isInitialized)
                openRouteView(cta.android_url)

        }
    }

    private fun openRouteView(androidUrl: String?) {
        if (isWebLink) {
            pdpSimulationCallback?.setViewModelData(Utils.UpdateViewModelVariable.RefreshType, true)
            pdpSimulationCallback?.setViewModelData(
                Utils.UpdateViewModelVariable.PartnerPosition,
                redirectPosition
            )
            val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + androidUrl
            RouteManager.route(context, webViewAppLink)
        } else {
            RouteManager.route(context, androidUrl)
        }
        dismiss()

    }

    private fun sendClickAnalytics() {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.GopayBottomSheetButtonClick(
                productId ?: "",
                tenure.toString(),
                parterName ?: "",
                montlyInstallment.toString(),
                cta.android_url ?: ""
            )
        )
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight()).toDp()
    }*/

    companion object {

        private const val TAG = "PayLaterTokopediaGopayBottomsheet"
        const val GOPAY_BOTTOMSHEET_DETAIL = "gopayBottomsheetDetail"
        const val PARTER_NAME = "partnerName"
        const val TENURE = " tenure"
        const val EMI_AMOUNT = "emiAmount"
        const val PRODUCT_ID = "productID"
        const val REDIRECT_TOKO_ENV = 1

        fun show(
            bundle: Bundle,
            pdpSimulationCallback: PdpSimulationCallback,
            childFragmentManager: FragmentManager
        ) {
            val payLaterTokopediaGopayBottomsheet = PayLaterTokopediaGopayBottomsheet().apply {
                arguments = bundle
            }
            payLaterTokopediaGopayBottomsheet.show(childFragmentManager, TAG)
            //payLaterTokopediaGopayBottomsheet.pdpSimulationCallback = pdpSimulationCallback
        }
    }

}