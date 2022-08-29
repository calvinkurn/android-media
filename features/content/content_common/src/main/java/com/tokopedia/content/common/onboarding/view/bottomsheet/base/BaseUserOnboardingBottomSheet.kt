package com.tokopedia.content.common.onboarding.view.bottomsheet.base

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.R
import com.tokopedia.content.common.onboarding.view.fragment.UGCOnboardingParentFragment
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class BaseUserOnboardingBottomSheet : BottomSheetUnify() {

    protected var mListener: Listener? = null

    protected val usernameArg: String
        get() = arguments?.getString(UGCOnboardingParentFragment.KEY_USERNAME).orEmpty()

    private val clickablePolicy = object : ClickableSpan() {
        override fun onClick(p0: View) {
            RouteManager.route(
                requireContext(),
                generateWebviewApplink(getString(R.string.ugc_onboarding_privacy_policy_link))
            )
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private val clickableTnc = object : ClickableSpan() {
        override fun onClick(p0: View) {
            RouteManager.route(
                requireContext(),
                generateWebviewApplink(getString(R.string.ugc_onboarding_tnc_link))
            )
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private val boldSpan: StyleSpan
        get() = StyleSpan(Typeface.BOLD)

    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(MethodChecker.getColor(requireContext(), unifyR.color.Unify_GN500))

    protected val offset16 by lazy {
        requireContext().resources.getDimensionPixelOffset(unifyR.dimen.spacing_lvl4)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        showCloseIcon = false
        showKnob = true
        showHeader = false
        isDragable = true
        isSkipCollapseState = true
        isHideable = true
        clearContentPadding = true

        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun generateWebviewApplink(url: String): String {
        return getString(R.string.up_webview_template, ApplinkConst.WEBVIEW, url)
    }

    protected fun getTncText(): CharSequence {
        val result = SpannableStringBuilder()

        val mainText = getString(R.string.ugc_onboarding_accept_tnc)
        val privacyPolicy = getString(R.string.ugc_onboarding_accept_tnc_privacy_policy)
        val terms = getString(R.string.ugc_onboarding_accept_tnc_terms)

        result.append(mainText)
        result.setSpanOnText(privacyPolicy, clickablePolicy, boldSpan, colorSpan)
        result.setSpanOnText(terms, clickableTnc, boldSpan, colorSpan)

        return result
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onSuccess()
    }
}