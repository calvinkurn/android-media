package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationEvent
import com.tokopedia.pdpsimulation.common.listener.PdpSimulationCallback
import com.tokopedia.pdpsimulation.paylater.domain.model.Detail
import com.tokopedia.pdpsimulation.paylater.domain.model.HowToUse
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterPartnerStepDetails
import com.tokopedia.pdpsimulation.paylater.mapper.PayLaterPartnerTypeMapper
import com.tokopedia.pdpsimulation.paylater.mapper.RegisterStepsPartnerType
import com.tokopedia.pdpsimulation.paylater.mapper.UsageStepsPartnerType
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
    private var sheetTitle: String = ""
    private var actionUrl: String = ""
    private var productUrl: String = ""
    private var partnerName: String? = ""
    private lateinit var  howToUseList:HowToUse
    private lateinit var noteData:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
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
            actionUrl = it.cta?.android_url?:""
             noteData = it.gateway_detail?.how_toUse?.notes?.get(0)?:""
            it.gateway_detail?.how_toUse?.let { howToUseDetail->
                howToUseList = howToUseDetail
            }

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if(!noteData.isNullOrEmpty())
              tickerPaylaterRegister.setTextDescription(noteData)
            else
                tickerPaylaterRegister.gone()
        sheetTitle = "${context?.getString(R.string.pay_later_how_to_use)} ${partnerName ?: ""}"
        initListeners()
        setButtonType()
        initAdapter()
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
        setTitle(sheetTitle)
    }

    private fun setButtonType() {

            btnRegister.buttonSize = UnifyButton.Size.SMALL
            btnRegister.buttonType = UnifyButton.Type.ALTERNATE
            btnRegister.buttonVariant = UnifyButton.Variant.GHOST
            btnRegister.text = context?.getString(R.string.pay_later_action_find_more)

    }

    private fun initListeners() {
        btnRegister.setOnClickListener {
          //  sendAnalytics()
            if(actionUrl.isNotEmpty())
                openUrlWebView(actionUrl)
        }
    }

//    private fun sendAnalytics() {
//        if (!isUsageType)
//            pdpSimulationCallback?.sendAnalytics(
//                PdpSimulationEvent.PayLater.RegisterPayLaterOptionClickEvent(
//                    partnerName
//                        ?: ""
//                )
//            )
//    }

    private fun initAdapter() {

        howToUseList.steps?.let {
            rvPayLaterRegisterSteps.adapter = PayLaterActionStepsAdapter(it as ArrayList<String>)
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