package com.tokopedia.people.views.fragment.bottomsheet

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
import com.tokopedia.people.databinding.BottomSheetReviewOnboardingBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.people.R
import com.tokopedia.people.utils.getBoldSpan
import com.tokopedia.people.utils.getClickableSpan
import com.tokopedia.people.utils.getGreenColorSpan
import com.tokopedia.unifyprinciples.Typography

/**
 * Created By : Jonathan Darwin on May 11, 2023
 */
class UserProfileReviewOnboardingBottomSheet : BottomSheetUnify() {

    private var _binding: BottomSheetReviewOnboardingBinding? = null

    private val binding: BottomSheetReviewOnboardingBinding
        get() = _binding!!

    private var mListener: Listener? = null

    private val clickablePolicy = object : ClickableSpan() {
        override fun onClick(p0: View) {

        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    private val boldSpan: StyleSpan
        get() = StyleSpan(Typeface.BOLD)

    private val colorSpan: ForegroundColorSpan
        get() = ForegroundColorSpan(MethodChecker.getColor(requireContext(), com.tokopedia.unifyprinciples.R.color.Unify_GN500))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupBottomSheet() {
        _binding = BottomSheetReviewOnboardingBinding.inflate(
            LayoutInflater.from(requireContext()),
        )
        setChild(binding.root)
    }

    private fun setupView() {
        resources.getStringArray(R.array.up_profile_review_onboarding_desc).forEach {
            val layout = LayoutInflater.from(requireContext()).inflate(
                R.layout.layout_review_onboarding_desc, null, false
            ).apply {
                findViewById<Typography>(R.id.tv_desc).text = it
            }

            binding.llDesc.addView(layout)
        }

        binding.tvFooter.text = getFooterText()
        binding.tvFooter.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun setupListener() {
        binding.btnOpenReviewTab.setOnClickListener {
            mListener?.onClickOpenReviewTab()
            dismiss()
        }
    }

    private fun getFooterText(): CharSequence {
        val footerText = SpannableStringBuilder()

        val fullText = getString(R.string.up_profile_review_onboarding_hide_review)
        val targetSpanText = getString(R.string.up_profile_settings_title)

        footerText.append(fullText)
        footerText.setSpanOnText(targetSpanText,
            getClickableSpan {
                mListener?.onClickOpenProfileSettingsPage()
                dismiss()
            }, getBoldSpan(),
            getGreenColorSpan(requireContext())
        )

        return footerText
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, TAG)
    }

    companion object {
        private const val TAG = "UserProfileReviewOnboardingBottomSheet"


        fun getOrCreate(
            fragmentManager: FragmentManager,
            classLoader: ClassLoader
        ): UserProfileReviewOnboardingBottomSheet {
            return fragmentManager.findFragmentByTag(TAG) as? UserProfileReviewOnboardingBottomSheet ?: run {
                val fragmentFactory = fragmentManager.fragmentFactory
                fragmentFactory.instantiate(
                    classLoader,
                    UserProfileReviewOnboardingBottomSheet::class.java.name
                ) as UserProfileReviewOnboardingBottomSheet
            }
        }
    }

    interface Listener {
        fun onClickOpenReviewTab()

        fun onClickOpenProfileSettingsPage()
    }
}
