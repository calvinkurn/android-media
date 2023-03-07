package com.tokopedia.privacycenter.ui.main.section.recommendation

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
import com.tokopedia.media.loader.loadImage
import com.tokopedia.privacycenter.R
import com.tokopedia.privacycenter.databinding.BottomSheetRecommendationFriendBinding
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.utils.lifecycle.autoClearedNullable

class RecommendationFriendBottomSheet : BottomSheetUnify() {

    private var binding: BottomSheetRecommendationFriendBinding? by autoClearedNullable()
    private var btnVerificationClickedListener: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetRecommendationFriendBinding.inflate(inflater, container, false)
        setChild(binding?.root)
        clearContentPadding = true
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewBanner()
        setViewDescCheckBox()
        initListener()
    }

    private fun initListener() {
        binding?.cbVerification?.setOnCheckedChangeListener { _, isChecked ->
            binding?.btnVerification?.isEnabled = isChecked
        }

        binding?.btnVerification?.setOnClickListener {
            btnVerificationClickedListener?.invoke()
        }
    }

    fun setOnVerificationClickedListener(listener: () -> Unit) {
        btnVerificationClickedListener = listener
    }

    private fun setViewBanner() {
        binding?.carouselClarification?.slideToShow = CAROUSEL_SLIDE_TO_SHOW

        val itemParam = { item: View, data: Any ->
            val img = item.findViewById<ImageUnify>(R.id.image_banner_clarification_data_usage)
            img.loadImage(data.toString())
        }
        val listImageBanner = arrayListOf<Any>(
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_1),
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_2),
            getString(R.string.privacy_account_bottom_sheet_clarification_image_banner_3)
        )
        binding?.carouselClarification?.addItems(R.layout.item_custom_image_banner_privacy_center, listImageBanner, itemParam)
    }

    private fun setViewDescCheckBox() {
        val message = getString(R.string.privacy_center_recommendation_friend_checkbox_desc)
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
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
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
                            com.tokopedia.unifyprinciples.R.color.Unify_G500
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
            activity,
            String.format(
                STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                "${TokopediaUrl.getInstance().MOBILEWEB}$TERM_AND_CONDITION_PATH"
            )
        )
    }

    private fun goToPrivacyPolicy() {
        RouteManager.route(
            activity,
            String.format(
                STRING_FORMAT,
                ApplinkConst.WEBVIEW,
                "${TokopediaUrl.getInstance().MOBILEWEB}$PRIVACY_POLICY_PATH"
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.cbVerification?.setOnCheckedChangeListener(null)
        binding?.btnVerification?.setOnClickListener(null)
    }

    companion object {
        private const val CAROUSEL_SLIDE_TO_SHOW = 1f
        private const val STRING_FORMAT = "%s?url=%s"
        private const val TERM_AND_CONDITION_PATH = "help/article/syarat-dan-ketentuan-kupon-megacashback"
        private const val PRIVACY_POLICY_PATH = "privacy"
        private const val TERM_AND_CONDITION = "Syarat & Ketentuan"
        private const val PRIVACY_POLICY = "Kebijakan Privasi"
    }
}
