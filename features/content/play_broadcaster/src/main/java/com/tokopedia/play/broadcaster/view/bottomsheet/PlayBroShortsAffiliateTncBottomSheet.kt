package com.tokopedia.play.broadcaster.view.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayShortsAffiliateTncBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

class PlayBroShortsAffiliateTncBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetPlayShortsAffiliateTncBinding? = null
    private val binding: BottomSheetPlayShortsAffiliateTncBinding
        get() = _binding!!

    private val boldSpan: StyleSpan
        get() = StyleSpan(Typeface.BOLD)

    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(MethodChecker.getColor(requireContext(), unifyPrinciplesR.color.Unify_GN500))

    private val clickablePolicy = object : ClickableSpan() {
        override fun onClick(p0: View) {
            RouteManager.route(
                requireContext(),
                generateWebViewApplink(getString(contentCommonR.string.ugc_onboarding_privacy_policy_link))
            )
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private val clickableTnc = object : ClickableSpan() {
        /** TODO
         * change the tnc web-view app link later
         */
        override fun onClick(p0: View) {
            RouteManager.route(
                requireContext(),
                generateWebViewApplink(getString(contentCommonR.string.ugc_onboarding_tnc_link))
            )
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private fun generateWebViewApplink(url: String): String {
        return getString(contentCommonR.string.up_webview_template, ApplinkConst.WEBVIEW, url)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetPlayShortsAffiliateTncBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.layoutTnc.tvAcceptTnc.apply {
            text = getTncText()
            movementMethod = LinkMovementMethod.getInstance()
        }

        binding.layoutTnc.cbxTnc.setOnCheckedChangeListener { _, _ ->
            binding.btnContinue.isEnabled = binding.layoutTnc.cbxTnc.isChecked
        }
        binding.btnContinue.setOnClickListener {
            /** TODO
             * submit the tnc
             * BE team is not ready yet
             */
            if (binding.btnContinue.isEnabled) dismiss()
        }
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) show(fragmentManager, TAG)
    }

    private fun getTncText(): CharSequence {
        val result = SpannableStringBuilder()

        val mainText = getString(contentCommonR.string.ugc_onboarding_accept_tnc)
        val privacyPolicy = getString(contentCommonR.string.ugc_onboarding_accept_tnc_privacy_policy)
        val terms = getString(contentCommonR.string.ugc_onboarding_accept_tnc_terms)

        result.append(mainText)
        result.setSpanOnText(privacyPolicy, clickablePolicy, boldSpan, colorSpan)
        result.setSpanOnText(terms, clickableTnc, boldSpan, colorSpan)

        return result
    }

    companion object {
        const val TAG = "PlayBroShortsAffiliateTncBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayBroShortsAffiliateTncBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayBroShortsAffiliateTncBottomSheet
            return oldInstance ?: fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayBroShortsAffiliateTncBottomSheet::class.java.name
            ) as PlayBroShortsAffiliateTncBottomSheet
        }
    }
}
