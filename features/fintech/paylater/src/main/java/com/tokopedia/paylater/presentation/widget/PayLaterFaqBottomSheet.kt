package com.tokopedia.paylater.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.paylater.R
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
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
        populateFaqData()
        rvPaylaterFaq.adapter = PayLaterPaymentFaqAdapter(populateFaqData())
        rvPaylaterFaq.layoutManager =  LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun populateFaqData(): ArrayList<PayLaterPartnerFaq> {
        val faqList = ArrayList<PayLaterPartnerFaq>()
        for (i in 1..5)
            faqList.add(PayLaterPartnerFaq("Berapa lama proses persetujuan aplikasi kredit online Kredivo?",
                "Kredivo-mu sudah aktif. Kamu tinggal pilih Kredivo di halaman pembayaran untuk menggunakannya", false))
        return faqList
    }


    companion object {
        private const val TITLE = "Hal yang sering ditanyakan"
        private const val TAG = "FT_TAG"
        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterFaqBottomSheet = PayLaterFaqBottomSheet().apply {
                arguments = bundle
            }
            payLaterFaqBottomSheet.show(childFragmentManager, TAG)
        }
    }
}