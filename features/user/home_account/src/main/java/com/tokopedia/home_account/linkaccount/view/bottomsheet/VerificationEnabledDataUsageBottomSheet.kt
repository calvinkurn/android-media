package com.tokopedia.home_account.linkaccount.view.bottomsheet

import android.os.Bundle
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutBottomSheetEnabledDataUsageBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify

class VerificationEnabledDataUsageBottomSheet : BottomSheetUnify() {

    private var _binding : LayoutBottomSheetEnabledDataUsageBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetEnabledDataUsageBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setView1()
    }

    private fun setView() {
        binding?.imgHeader?.loadImage(SOURCE_IMAGE_HEADER)

        binding?.cbVerification?.setOnCheckedChangeListener { _, isChecked ->
            binding?.btnVerification?.isEnabled = isChecked
        }
    }

    fun setOnVerificationClickedListener(onVerificationClickedListener: () -> Unit) {
        binding?.btnVerification?.setOnClickListener {
            onVerificationClickedListener()
        }
    }

    private fun setView1() {
        val message = getString(R.string.opt_bottom_sheet_verification_enabled_checkbox_desc)
        val spannable = SpannableString(message)
        spannable.apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        //show
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                    }
                },
                message.indexOf(TERM_AND_CONDITION),
                message.indexOf(TERM_AND_CONDITION) + TERM_AND_CONDITION.length,
                0
            )
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        //show
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                    }
                },
                message.indexOf(PRIVACY_POLICY),
                message.indexOf(PRIVACY_POLICY) + PRIVACY_POLICY.length,
                0
            )
        }

        binding?.txtDescCheckbox?.movementMethod = LinkMovementMethod.getInstance()
        binding?.txtDescCheckbox?.setText(spannable, TextView.BufferType.SPANNABLE)
    }

    companion object {
        private const val TERM_AND_CONDITION = "Syarat & Ketentuan"
        private const val PRIVACY_POLICY = "Kebijakan Privasi"
        private const val SOURCE_IMAGE_HEADER = "https://images.tokopedia.net/img/android/user/optinout/img_privacy_account_verification.png"
    }
}