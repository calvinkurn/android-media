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
import com.tokopedia.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.paylater.domain.model.PayLaterPartnerFaq
import com.tokopedia.paylater.presentation.adapter.PayLaterPaymentFaqAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.paylater_card_faq_bottomsheet_widget.*
import kotlinx.android.synthetic.main.paylater_verification_bottomsheet_widget.*

class PayLaterVerificationBottomSheet : BottomSheetUnify() {

    private val applicationDetail: PayLaterApplicationDetail? by lazy {
        arguments?.getParcelable(APPLICATION_STATUS)
    }

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

    private val childLayoutRes = R.layout.paylater_verification_bottomsheet_widget


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvApplicationStatus.text = "${context?.getString(R.string.payLater_verification_description_waiting)} ${applicationDetail?.payLaterExpirationDate}"
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }


    private fun setDefaultParams() {
        setTitle("Daftar ${applicationDetail?.payLaterGatewayName ?: ""}")
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }



    companion object {
        private const val TAG = "FT_TAG"
        const val APPLICATION_STATUS = "application_status"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterFaqBottomSheet = PayLaterVerificationBottomSheet().apply {
                arguments = bundle
            }
            payLaterFaqBottomSheet.show(childFragmentManager, TAG)
        }
    }
}