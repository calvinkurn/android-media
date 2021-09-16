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
import com.tokopedia.pdpsimulation.paylater.domain.model.Faq
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterPaymentFaqAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_card_faq_bottomsheet_widget.*

class PayLaterFaqBottomSheet : BottomSheetUnify() {

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

    private val childLayoutRes = R.layout.paylater_card_faq_bottomsheet_widget
    private var faqData: ArrayList<Faq>? = null
    private var faqUrl: String = ""
    private var parterName: String? = ""
    private var tenure: Int? = 0
    private var pdpSimulationCallback: PdpSimulationCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getArgumentData()
        setDefaultParams()
        initBottomSheet()
    }

    private fun getArgumentData() {
        arguments?.let {
            faqData = it.getParcelableArrayList(FAQ_DATA) ?: arrayListOf()
            faqUrl = it.getString(FAQ_SEE_MORE_URL) ?: ""
            parterName = it.getString(PARTNER_NAME) ?: ""
            tenure = it.getInt(TENURE, 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (faqUrl.isEmpty()) btnSeeMore.gone()
        initAdapter()
        initListeners()
        sendImpressionAnalytic()
    }

    private fun sendImpressionAnalytic() {
        pdpSimulationCallback?.sendAnalytics(
            PdpSimulationEvent.PayLater.FaqImpression(
                parterName ?: "", tenure ?: 0
            )
        )
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(
            childLayoutRes,
            null, false
        )
        setChild(childView)
        setTitle(getString(R.string.pay_later_find_out_more_heading))
    }


    private fun setDefaultParams() {
        setTitle(TITLE)
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }


    private fun initAdapter() {
        faqData?.let {
            rvPaylaterFaq.adapter = PayLaterPaymentFaqAdapter(it)
            rvPaylaterFaq.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    private fun initListeners() {
        btnSeeMore.setOnClickListener {
            pdpSimulationCallback?.sendAnalytics(
                PdpSimulationEvent.PayLater.FaqClickWebImpression(
                    parterName ?: "",
                    tenure ?: 0,
                    faqUrl
                )
            )
            openUrlWebView(faqUrl)
        }
    }


    private fun openUrlWebView(urlString: String) {
        val webViewAppLink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewAppLink)
    }


    companion object {
        private const val TITLE = "Hal yang sering ditanyakan"
        private const val TAG = "PayLaterFaqBottomSheet"
        const val FAQ_DATA = "faqData"
        const val FAQ_SEE_MORE_URL = "faqUrl"
        const val PARTNER_NAME = "partnerName"
        const val TENURE = "tenure"

        fun show(
            bundle: Bundle,
            pdpSimulationCallback: PdpSimulationCallback,
            childFragmentManager: FragmentManager
        ) {
            val payLaterFaqBottomSheet = PayLaterFaqBottomSheet().apply {
                arguments = bundle
            }
            payLaterFaqBottomSheet.pdpSimulationCallback = pdpSimulationCallback
            payLaterFaqBottomSheet.show(childFragmentManager, TAG)
        }
    }
}