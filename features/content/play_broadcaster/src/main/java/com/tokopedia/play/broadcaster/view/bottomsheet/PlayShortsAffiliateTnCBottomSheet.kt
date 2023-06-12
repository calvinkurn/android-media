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
import com.tokopedia.content.common.util.Router
import com.tokopedia.content.common.util.setSpanOnText
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayShortsXAffiliateTncBinding
import com.tokopedia.play_common.lifecycle.viewLifecycleBound
import com.tokopedia.play_common.util.PlayToaster
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

class PlayShortsAffiliateTnCBottomSheet @Inject constructor(
    private val router: Router
) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayShortsXAffiliateTncBinding? = null
    private val binding: BottomSheetPlayShortsXAffiliateTncBinding
        get() = _binding!!

    private val boldSpanPrivacyPolicy = StyleSpan(Typeface.BOLD)
    private val boldSpanTerms = StyleSpan(Typeface.BOLD)
    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(MethodChecker.getColor(requireContext(), unifyPrinciplesR.color.Unify_GN500))

    private var mListener: Listener? = null

    private val clickablePolicy = object : ClickableSpan() {
        override fun onClick(p0: View) {
            router.route(
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
        override fun onClick(p0: View) {
            router.route(
                requireContext(),
                generateWebViewApplink(getString(R.string.play_shorts_affiliate_success_tnc_web_link))
            )
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private val toaster by viewLifecycleBound(
        creator = { PlayToaster(binding.toasterLayout, it.viewLifecycleOwner) }
    )

    init {
        overlayClickDismiss = false
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
        _binding = BottomSheetPlayShortsXAffiliateTncBinding.inflate(
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
            if (binding.layoutTnc.cbxTnc.isChecked) mListener?.onCheckBoxChecked()
            binding.btnContinue.isEnabled = binding.layoutTnc.cbxTnc.isChecked
        }
        binding.btnContinue.setOnClickListener {
            if (it.isEnabled) {
                updateButtonState(true)
                mListener?.onSubmitTnc()
            }
        }
    }

    private fun updateButtonState(isLoading: Boolean) {
        binding.btnContinue.isLoading = isLoading
        binding.btnContinue.isClickable = !isLoading
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    fun showErrorToast(throwable: Throwable) {
        updateButtonState(false)
        toaster.showError(
            throwable,
            duration = Toaster.LENGTH_INDEFINITE,
            actionLabel = getString(R.string.play_broadcast_try_again),
            actionListener = { mListener?.onSubmitTnc() }
        )
    }

    private fun getTncText(): CharSequence {
        val result = SpannableStringBuilder()

        val mainText = getString(contentCommonR.string.ugc_onboarding_accept_tnc)
        val privacyPolicy = getString(contentCommonR.string.ugc_onboarding_accept_tnc_privacy_policy)
        val terms = getString(contentCommonR.string.ugc_onboarding_accept_tnc_terms)

        result.append(mainText)
        result.setSpanOnText(privacyPolicy, clickablePolicy, boldSpanPrivacyPolicy, colorSpan)
        result.setSpanOnText(terms, clickableTnc, boldSpanTerms, colorSpan)

        return result
    }

    interface Listener {
        fun onCheckBoxChecked()
        fun onSubmitTnc()
    }

    companion object {
        private const val TAG = "PlayShortsXAffiliateTncBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): PlayShortsAffiliateTnCBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayShortsAffiliateTnCBottomSheet
            return oldInstance ?: (
                fragmentManager.fragmentFactory.instantiate(
                    classLoader,
                    PlayShortsAffiliateTnCBottomSheet::class.java.name
                ) as PlayShortsAffiliateTnCBottomSheet
                )
        }
    }
}
