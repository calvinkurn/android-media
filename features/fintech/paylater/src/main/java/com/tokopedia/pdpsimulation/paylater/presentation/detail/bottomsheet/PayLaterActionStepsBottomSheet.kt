package com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_action_steps_bottomsheet_widget.*
import java.net.URLEncoder

class PayLaterActionStepsBottomSheet : BottomSheetUnify() {

    private var pdpSimulationCallback: PdpSimulationCallback? = null
    private val childLayoutRes = R.layout.paylater_action_steps_bottomsheet_widget
    private var actionUrl: String = ""
    private var partnerName: String? = ""
    private var listOfSteps: ArrayList<String>? = ArrayList()
    private var noteData: String = ""
    private var titleText: String = ""
    private var tenure = 0
    private var isWebUrl = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()

        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            val payLaterItemProductData: Detail? = it.getParcelable(STEPS_DATA)
            setDataFromArguments(payLaterItemProductData)
        } ?: dismiss()
    }

    /**
     * this contains all the ui set value for the bundle response and logic behind what to display
     * @param payLaterItemProductData this is all the product detail of the selected partner
     */

    private fun setDataFromArguments(payLaterItemProductData: Detail?) {
        payLaterItemProductData?.let {
            partnerName = it.gatewayDetail?.name ?: ""
            actionUrl = it.cta.android_url ?: ""
            tenure = payLaterItemProductData.tenure ?: 0
            if (it.cta.cta_type == REDIRECT_TOKO_ENV) {
                isWebUrl = false
            } else {
                if (it.gatewayDetail?.how_toUse?.notes?.size != 0)
                    noteData = it.gatewayDetail?.how_toUse?.notes?.get(0) ?: ""
                it.gatewayDetail?.how_toUse?.let { howToUseDetail ->
                    listOfSteps = howToUseDetail.steps as ArrayList<String>
                }
                titleText =
                    "${resources.getString(R.string.pay_later_how_to_use)} ${partnerName ?: ""}"
            }


        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!noteData.isEmpty())
            tickerPaylaterRegister.setTextDescription(noteData)
        else
            tickerPaylaterRegister.gone()
        initListeners()
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
        customPeekHeight = (getScreenHeight()).toDp()
    }


    private fun initListeners() {
        btnRegister.setOnClickListener {
            sendClickEventAnalytics()
            if (actionUrl.isNotEmpty())
                openUrlView(actionUrl)
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

    private fun openUrlView(urlString: String) {
        if (isWebUrl) {
            if (urlString.isNotEmpty()) {
                val webViewAppLink =
                    ApplinkConst.WEBVIEW + "?url=" + URLEncoder.encode(urlString, "UTF-8")
                RouteManager.route(context, webViewAppLink)
            }
        } else {
            RouteManager.route(context, urlString)
        }
    }

    companion object {

        private const val TAG = "PayLaterActionStepsBottomSheet"
        const val STEPS_DATA = "stepsData"
        const val REDIRECT_TOKO_ENV = 1

        fun show(
            bundle: Bundle,
            childFragmentManager: FragmentManager
        ) {
            val actionStepsBottomSheet = PayLaterActionStepsBottomSheet().apply {
                arguments = bundle
            }
            actionStepsBottomSheet.show(childFragmentManager, TAG)
        }
    }
}