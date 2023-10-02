package com.tokopedia.tokochat.common.view.chatroom.customview.bottomsheet

import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatGuideChatBottomsheetBinding
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.GUIDE_CHAT
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable


class TokoChatGuideChatBottomSheet: BottomSheetUnify() {

    private var binding by autoClearedNullable<TokochatGuideChatBottomsheetBinding>()

    init {
        clearContentPadding = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tokochat_guide_chat_bottomsheet, container, false)
        binding = TokochatGuideChatBottomsheetBinding.bind(view)
        setChild(view)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        this.setTitle(getString(R.string.tokochat_message_censored_bottomsheet_title))
        setImage()
        setupTermsConditionSpan()
    }

    private fun setImage() {
        binding?.tokochatImageGuideChat?.loadImage(GUIDE_CHAT)
    }

    private fun setupTermsConditionSpan() {
        val completeString = getString(R.string.tokochat_message_censored_terms_condition)
        val spannableString = SpannableString(completeString)
        val termsConditionSpan = getString(R.string.tokochat_message_censored_terms_condition_span)
        try {
            val startPosition = completeString.indexOf(termsConditionSpan)
            val endPosition = completeString.lastIndexOf(termsConditionSpan) + termsConditionSpan.length
            spannableString.setSpan(
                StyleSpan(Typeface.BOLD),
                startPosition,
                endPosition,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                getTermsConditionClick(),
                startPosition,
                endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            binding?.tokochatTvTermsConditionGuideChat?.movementMethod = LinkMovementMethod.getInstance()
        } catch (ignored: Throwable) {}
        binding?.tokochatTvTermsConditionGuideChat?.text = spannableString
    }

    private fun getTermsConditionClick(): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(widget: View) {
                RouteManager.route(context, TokoChatUrlUtil.TNC)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)
                ds.isUnderlineText = false
            }
        }
    }

    fun show(fragmentManager: FragmentManager?) {
        fragmentManager?.let {
            if (!isVisible) {
                show(it, TAG)
            }
        }
    }

    companion object {
        private val TAG = TokoChatGuideChatBottomSheet::class.simpleName
    }
}

