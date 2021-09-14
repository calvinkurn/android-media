package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterActionStepsAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_action_steps_bottomsheet_widget.*
import java.net.URLEncoder

class PayLaterActionStepsBottomSheet : BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private var pdpSimulationCallback: PdpSimulationCallback? = null
    private val childLayoutRes = R.layout.paylater_action_steps_bottomsheet_widget
    private var actionUrl: String = ""
    private var productUrl: String = ""
    private var partnerName: String? = ""
    private var listOfSteps: ArrayList<String>? = ArrayList()
    private var noteData: String = ""
    private var titleText: String = ""
    private var tenure = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()

        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            val payLaterItemProductData: Detail? = it.getParcelable(STEPS_DATA)
            productUrl = it.getString(PRODUCT_URL) ?: ""
            setDataFromArguments(payLaterItemProductData)
        } ?: dismiss()
    }

    private fun setDataFromArguments(payLaterItemProductData: Detail?) {
        payLaterItemProductData?.let {
            partnerName = it.gateway_detail?.name ?: ""
            actionUrl = it.cta?.android_url ?: ""
            tenure = payLaterItemProductData.tenure ?: 0
            if (it.cta?.cta_type == 4) {
                if (it.gateway_detail?.how_toUse?.notes?.size != 0)
                    noteData = it.gateway_detail?.how_toUse?.notes?.get(0) ?: ""
                it.gateway_detail?.how_toUse?.let { howToUseDetail ->
                    listOfSteps = howToUseDetail.steps as ArrayList<String>
                }
                titleText =
                    "${resources.getString(R.string.pay_later_how_to_use)} ${partnerName ?: ""}"
            } else {
                if (it.gateway_detail?.how_toApply?.notes?.size != 0)
                    noteData = it.gateway_detail?.how_toApply?.notes?.get(0) ?: ""
                it.gateway_detail?.how_toApply?.let { howToApplyDetail ->
                    listOfSteps = howToApplyDetail.steps as ArrayList<String>
                }
                titleText =
                    "${resources.getString(R.string.pay_later_how_to_register)} ${partnerName ?: ""}"
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!noteData.isNullOrEmpty())
            tickerPaylaterRegister.setTextDescription(noteData)
        else
            tickerPaylaterRegister.gone()
        initListeners()
        setButtonType()
        setDefaultParams()
        initAdapter()
        setTitle(titleText)
        sendImpressionAnalytics()
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
        customPeekHeight = (getScreenHeight() / 2).toDp()

    }

    private fun setButtonType() {

        btnRegister.buttonSize = UnifyButton.Size.SMALL
        btnRegister.buttonType = UnifyButton.Type.ALTERNATE
        btnRegister.buttonVariant = UnifyButton.Variant.GHOST
        btnRegister.text = context?.getString(R.string.pay_later_action_find_more)

    }

    private fun initListeners() {
        btnRegister.setOnClickListener {
            sendClickEventAnalytics()
            if (actionUrl.isNotEmpty())
                sendClickEventAnalytics()
            openUrlWebView(actionUrl)
        }
    }

    private fun sendClickEventAnalytics() {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.MainBottomSheetClickEvent(
                partnerName ?: "",
                tenure,
                actionUrl
            )
        )
    }

    private fun sendImpressionAnalytics() {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.MainBottomSheetImpression(
                partnerName
                    ?: "",
                tenure
            )
        )

    }

    private fun initAdapter() {

        listOfSteps?.let {
            rvPayLaterRegisterSteps.adapter = PayLaterActionStepsAdapter(it)
            rvPayLaterRegisterSteps.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


    }

    private fun openUrlWebView(urlString: String) {
        if (urlString.isNotEmpty()) {
            val webViewAppLink =
                ApplinkConst.WEBVIEW + "?url=" + URLEncoder.encode(urlString, "UTF-8")
            RouteManager.route(context, webViewAppLink)
        }
    }

    companion object {

        private const val TAG = "PayLaterActionStepsBottomSheet"
        const val STEPS_DATA = "stepsData"
        const val PRODUCT_URL = "productUrl"
        const val APPLICATION_STATUS_DATA = "applicationStatusData"

        fun show(
            bundle: Bundle,
            pdpSimulationCallback: PdpSimulationCallback,
            childFragmentManager: FragmentManager
        ) {
            val actionStepsBottomSheet = PayLaterActionStepsBottomSheet().apply {
                arguments = bundle
            }
            actionStepsBottomSheet.pdpSimulationCallback = pdpSimulationCallback
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }
}