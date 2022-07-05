package com.tokopedia.feedcomponent.onboarding.view.bottomsheet.base

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
abstract class BaseFeedUserOnboardingBottomSheet : BottomSheetUnify() {

    protected var mListener: Listener? = null

    private val clickablePolicy = object : ClickableSpan() {
        override fun onClick(p0: View) {

        }
    }

    private val clickableTnc = object : ClickableSpan() {
        override fun onClick(p0: View) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        showCloseIcon = false
        showKnob = true
        showHeader = false

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun getTncText(): CharSequence {
        val result = SpannableStringBuilder()

        val mainText = getString(R.string.feed_ugc_onboarding_accept_tnc)
        val privacyPolicy = getString(R.string.feed_ugc_onboarding_accept_tnc_privacy_policy)
        val terms = getString(R.string.feed_ugc_onboarding_accept_tnc_terms)

        val privacyPolicyStart = mainText.indexOf(privacyPolicy)
        val privacyPolicyEnd = privacyPolicyStart + privacyPolicy.length

        val termsStart = mainText.indexOf(terms)
        val termsEnd = termsStart + terms.length

        result.append(mainText)
        result.setSpan(clickablePolicy, privacyPolicyStart, privacyPolicyEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        result.setSpan(clickableTnc, termsStart, termsEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        /** TODO: make extension */

        return result
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    interface Listener {
        fun onSuccess()
    }
}