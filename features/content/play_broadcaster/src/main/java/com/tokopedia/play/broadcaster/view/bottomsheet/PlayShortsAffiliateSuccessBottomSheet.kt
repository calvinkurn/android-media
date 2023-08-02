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
import com.tokopedia.play.broadcaster.databinding.BottomSheetPlayShortsXAffiliateSuccessBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import javax.inject.Inject
import com.tokopedia.content.common.R as contentCommonR
import com.tokopedia.unifyprinciples.R as unifyPrinciplesR

class PlayShortsAffiliateSuccessBottomSheet @Inject constructor(
    private val router: Router,
) : BottomSheetUnify() {

    private var _binding: BottomSheetPlayShortsXAffiliateSuccessBinding? = null
    private val binding: BottomSheetPlayShortsXAffiliateSuccessBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private val boldSpan = StyleSpan(Typeface.BOLD)
    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(
            MethodChecker.getColor(
                requireContext(),
                unifyPrinciplesR.color.Unify_GN500
            )
        )

    private var mUserName: String = ""

    private val clickableLearnMore = object : ClickableSpan() {
        override fun onClick(p0: View) {
            router.route(
                requireContext(),
                generateWebViewApplink(getString(R.string.play_shorts_affiliate_success_learn_more_web_link))
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
        _binding = BottomSheetPlayShortsXAffiliateSuccessBinding.inflate(
            LayoutInflater.from(requireContext())
        )
        setChild(binding.root)
    }

    private fun setupView() {
        binding.layoutSuccess.tvSuccessShortsAffiliateTitle.text =
            String.format(getString(R.string.play_shorts_affiliate_success_title), mUserName)
        binding.tvSuccessShortsAffiliateLearnMore.apply {
            text = getLearnMoreText()
            movementMethod = LinkMovementMethod.getInstance()
        }

        binding.btnNext.setOnClickListener {
            if (it.isEnabled) {
                mListener?.onClickNext()
                dismiss()
            }
        }
    }

    fun setupData(userName: String) {
        mUserName = userName
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        if (!isAdded) showNow(fragmentManager, TAG)
    }

    private fun getLearnMoreText(): CharSequence {
        val result = SpannableStringBuilder()

        val mainText = getString(R.string.play_shorts_affiliate_success_description_learn_more)
        val privacyPolicy = getString(R.string.play_shorts_affiliate_success_description_learn_more_action)

        result.append(mainText)
        result.setSpanOnText(privacyPolicy, clickableLearnMore, boldSpan, colorSpan)

        return result
    }

    interface Listener {
        fun onClickNext()
    }

    companion object {
        private const val TAG = "PlayShortsXAffiliateSuccessBottomSheet"

        fun getFragment(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader,
        ): PlayShortsAffiliateSuccessBottomSheet {
            val oldInstance =
                fragmentManager.findFragmentByTag(TAG) as? PlayShortsAffiliateSuccessBottomSheet
            return oldInstance ?: (fragmentManager.fragmentFactory.instantiate(
                classLoader,
                PlayShortsAffiliateSuccessBottomSheet::class.java.name
            ) as PlayShortsAffiliateSuccessBottomSheet)
        }
    }
}
