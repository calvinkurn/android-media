package com.tokopedia.paylater.presentation.widget

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
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
import com.tokopedia.paylater.domain.model.PayLaterPartnerUsageDetails
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentFaqAdapter
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
    private var faqData: ArrayList<PayLaterPartnerFaq>? = ArrayList()
    private var faqUrl: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
        getArgumentData()
    }

    private fun getArgumentData() {
        arguments?.let {
            faqData = it.getParcelableArrayList(FAQ_DATA)
            faqUrl = it.getString(FAQ_SEE_MORE_URL) ?: ""
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (faqUrl.isEmpty()) btnSeeMore.gone()
        initAdapter()
        initListeners()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
        setTitle(getString(R.string.paylater_find_out_more_heading))
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
        rvPaylaterFaq.adapter = PayLaterPaymentFaqAdapter(faqData ?: populateFaqData())
        rvPaylaterFaq.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun initListeners() {
        btnSeeMore.setOnClickListener {
            openUrlWebView(faqUrl)
        }
    }

    private fun populateFaqData(): ArrayList<PayLaterPartnerFaq> {
        val faqList = ArrayList<PayLaterPartnerFaq>()
        for (i in 1..5)
            faqList.add(PayLaterPartnerFaq("Berapa lama proses persetujuan aplikasi kredit online Kredivo?",
                "Kredivo-mu sudah aktif. Kamu tinggal pilih Kredivo di halaman pembayaran untuk menggunakannya", false))
        return faqList
    }

    private fun openUrlWebView(urlString: String) {
        val webViewApplink = ApplinkConst.WEBVIEW + "?url=" + urlString
        RouteManager.route(context, webViewApplink)
    }


    companion object {
        private const val TITLE = "Hal yang sering ditanyakan"
        private const val TAG = "FT_TAG"
        const val FAQ_DATA = "faqData"
        const val FAQ_SEE_MORE_URL = "faqUrl"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterFaqBottomSheet = PayLaterFaqBottomSheet().apply {
                arguments = bundle
            }
            payLaterFaqBottomSheet.show(childFragmentManager, TAG)
        }
    }
}