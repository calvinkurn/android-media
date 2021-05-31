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
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterItemProductData
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
    private var partnerUsageData: PayLaterPartnerStepDetails? = null
    private var sheetTitle: String = ""
    private var actionUrl: String = ""
    private var productUrl: String = ""
    private var partnerName: String? = ""
    private var isUsageType = false

    private val applicationStatusData: PayLaterApplicationDetail? by lazy {
        arguments?.getParcelable(APPLICATION_STATUS_DATA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            val payLaterItemProductData: PayLaterItemProductData? = it.getParcelable(STEPS_DATA)
            productUrl = it.getString(PRODUCT_URL) ?: ""
            setDataFromArguments(payLaterItemProductData)
        } ?: dismiss()
    }

    private fun setDataFromArguments(payLaterItemProductData: PayLaterItemProductData?) {
        payLaterItemProductData?.let {
            partnerName = it.partnerName ?: ""
            actionUrl = it.actionWebUrl ?: ""
            when (PayLaterPartnerTypeMapper.getPayLaterPartnerType(it, applicationStatusData)) {
                is RegisterStepsPartnerType -> {
                    if (!it.actionWebUrl.isNullOrEmpty())
                        actionUrl = "${it.actionWebUrl}?URL=$productUrl"
                    sheetTitle = "${context?.getString(R.string.pay_later_how_to_register)} ${it.partnerName}"
                    partnerUsageData = it.partnerApplyDetails
                    isUsageType = false
                }
                is UsageStepsPartnerType -> {
                    sheetTitle = "${context?.getString(R.string.pay_later_how_to_use)} ${it.partnerName}"
                    partnerUsageData = it.partnerUsageDetails
                    isUsageType = true
                }
                else -> dismiss()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val notesData = partnerUsageData?.partnerNotes?.getOrNull(0)
        if (!notesData.isNullOrEmpty())
            tickerPaylaterRegister.setTextDescription(MethodChecker.fromHtml(notesData))
        else tickerPaylaterRegister.gone()
        initListeners()
        setButtonType()
        initAdapter()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
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
        if (isUsageType) {
            btnRegister.buttonSize = UnifyButton.Size.SMALL
            btnRegister.buttonType = UnifyButton.Type.ALTERNATE
            btnRegister.buttonVariant = UnifyButton.Variant.GHOST
            btnRegister.text = context?.getString(R.string.pay_later_action_find_more)
        } else {
            btnRegister.buttonSize = UnifyButton.Size.SMALL
            btnRegister.buttonType = UnifyButton.Type.MAIN
            btnRegister.buttonVariant = UnifyButton.Variant.FILLED
            btnRegister.text = context?.getString(R.string.pay_later_default_subtitle)
        }
    }

    private fun initListeners() {
        btnRegister.setOnClickListener {
            sendAnalytics()
            openUrlWebView(actionUrl)
        }
    }

    private fun sendAnalytics() {
        if (!isUsageType)
            pdpSimulationCallback?.sendAnalytics(PdpSimulationEvent.PayLater.RegisterPayLaterOptionClickEvent(partnerName
                    ?: ""))
    }

    private fun initAdapter() {
        partnerUsageData?.partnerSteps?.let {
            rvPayLaterRegisterSteps.adapter = PayLaterActionStepsAdapter(it)
            rvPayLaterRegisterSteps.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun openUrlWebView(urlString: String) {
        if (urlString.isNotEmpty()) {
            val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + URLEncoder.encode(urlString, "UTF-8")
            RouteManager.route(context, webViewAppLink)
        }
    }

    companion object {

        private const val TAG = "PayLaterActionStepsBottomSheet"
        const val STEPS_DATA = "stepsData"
        const val PRODUCT_URL = "productUrl"
        const val APPLICATION_STATUS_DATA = "applicationStatusData"

        fun show(bundle: Bundle, pdpSimulationCallback: PdpSimulationCallback, childFragmentManager: FragmentManager) {
            val actionStepsBottomSheet = PayLaterActionStepsBottomSheet().apply {
                arguments = bundle
            }
            actionStepsBottomSheet.pdpSimulationCallback = pdpSimulationCallback
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }
}