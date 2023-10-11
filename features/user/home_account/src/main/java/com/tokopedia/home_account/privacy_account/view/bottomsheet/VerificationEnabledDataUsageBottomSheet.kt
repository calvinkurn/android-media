package com.tokopedia.home_account.privacy_account.view.bottomsheet

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
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.home_account.R
import com.tokopedia.home_account.databinding.LayoutBottomSheetPrivacyAccountEnabledDataUsageBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable

class VerificationEnabledDataUsageBottomSheet : BottomSheetUnify() {

    private var _binding: LayoutBottomSheetPrivacyAccountEnabledDataUsageBinding? by autoClearedNullable()
    private val binding get() = _binding

    private var btnVerificationClickedListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LayoutBottomSheetPrivacyAccountEnabledDataUsageBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setView()
        setViewDescCheckBox()

        binding?.btnVerification?.setOnClickListener {
            btnVerificationClickedListener?.invoke()
        }
    }

    private fun setView() {
        binding?.imgHeader?.loadImage(getString(R.string.privacy_account_bottom_sheet_verification_image_header))

        binding?.cbVerification?.setOnCheckedChangeListener { _, isChecked ->
            binding?.btnVerification?.isEnabled = isChecked
        }
    }

    fun setOnVerificationClickedListener(listener: () -> Unit) {
        btnVerificationClickedListener = listener
    }

    private fun setViewDescCheckBox() {
        val message = getString(R.string.opt_bottom_sheet_verification_enabled_checkbox_desc)
        val spannable = SpannableString(message)
        spannable.apply {
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        goToTermAndCondition()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isFakeBoldText = true
                        ds.color = MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
                    }
                },
                message.indexOf(TERM_AND_CONDITION),
                message.indexOf(TERM_AND_CONDITION) + TERM_AND_CONDITION.length,
                0
            )
            setSpan(
                object : ClickableSpan() {
                    override fun onClick(view: View) {
                        goToPrivacyPolicy()
                    }

                    override fun updateDrawState(ds: TextPaint) {
                        ds.isFakeBoldText = true
                        ds.color = MethodChecker.getColor(
                            context,
                            com.tokopedia.unifyprinciples.R.color.Unify_GN500
                        )
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

    private fun goToTermAndCondition() {
        RouteManager.route(
            activity, String.format(
                STRING_FORMAT, ApplinkConst.WEBVIEW,
                "${TokopediaUrl.getInstance().MOBILEWEB}$TERM_AND_CONDITION_PATH"
            )
        )
    }

    private fun goToPrivacyPolicy() {
        RouteManager.route(
            activity, String.format(
                STRING_FORMAT, ApplinkConst.WEBVIEW,
                "${TokopediaUrl.getInstance().MOBILEWEB}$PRIVACY_POLICY_PATH"
            )
        )
    }

    companion object {
        private const val STRING_FORMAT = "%s?url=%s"
        private const val TERM_AND_CONDITION_PATH = "help/article/syarat-dan-ketentuan-kupon-megacashback"
        private const val PRIVACY_POLICY_PATH = "privacy"
        private const val TERM_AND_CONDITION = "Syarat & Ketentuan"
        private const val PRIVACY_POLICY = "Kebijakan Privasi"
    }
}