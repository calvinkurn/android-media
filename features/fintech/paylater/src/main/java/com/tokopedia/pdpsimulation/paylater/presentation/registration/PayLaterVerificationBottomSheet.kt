package com.tokopedia.pdpsimulation.paylater.presentation.registration

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.paylater.domain.model.PayLaterApplicationDetail
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp
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
        applicationDetail?.payLaterStatusContent?.let {
            tvPopUpDetail.text = it.verificationContentPopUpDetail

            if (!it.verificationContentInfo.isNullOrEmpty()) {
                if (applicationDetail?.payLaterExpirationDate.isNullOrEmpty()) {
                    tvAdditionalInfo.text = it.verificationContentInfo
                } else {
                    val builder = SpannableStringBuilder()
                    builder.append(it.verificationContentInfo)
                    builder.append(applicationDetail?.payLaterExpirationDate)
                    builder.setSpan(StyleSpan(Typeface.BOLD), it.verificationContentInfo.length, builder.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    tvAdditionalInfo.text = builder
                }
            } else tvAdditionalInfo.gone()

            if (!it.verificationContentPhoneNumber.isNullOrEmpty()) {
                tvPhone.text = it.verificationContentPhoneNumber
            } else {
                tvPhone.gone()
                ivPhone.gone()
            }
            if (!it.verificationContentEmail.isNullOrEmpty()) {
                tvEmail.text = it.verificationContentEmail
            } else {
                tvEmail.gone()
                ivEmail.gone()
            }

        }
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
        private const val TAG = "PayLaterVerificationBottomSheet"
        const val APPLICATION_STATUS = "application_status"

        fun show(bundle: Bundle, childFragmentManager: FragmentManager) {
            val payLaterFaqBottomSheet = PayLaterVerificationBottomSheet().apply {
                arguments = bundle
            }
            payLaterFaqBottomSheet.show(childFragmentManager, TAG)
        }
    }
}