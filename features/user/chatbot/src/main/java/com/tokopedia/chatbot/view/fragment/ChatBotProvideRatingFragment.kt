package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.chatbot.ColorUtil
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.databinding.FragmentChatbotRatingProvideBinding
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import javax.inject.Inject

private const val ACTION_KIRIM_CSAT_SMILEY_BUTTON_CLICKED = "click kirim csat smiley button"
private const val ACTION_CSAT_SMILEY_REASON_BUTTON_CLICKED = "click csat smiley reason button"

class ChatBotProvideRatingFragment : BaseFragmentProvideRating() {

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

    private var _viewBinding: FragmentChatbotRatingProvideBinding? = null
    private fun getBindingView() = _viewBinding!!

    companion object {
        const val BOT_OTHER_REASON = "bot_other_reason"
        const val OTHER_REASON_TITLE = "otherReasonTitle"
        const val IS_SHOW_OTHER_REASON = "is_show_other_reason"
        const val TIME_STAMP = "time_stamp"
        const val minLength = 1
        const val maxLength = 29
        const val minimumLines = 4
        fun newInstance(bundle: Bundle?): ChatBotProvideRatingFragment {
            val fragment = ChatBotProvideRatingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentChatbotRatingProvideBinding.inflate(inflater, container, false)
        return getBindingView().root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        super.onViewCreated(view, savedInstanceState)
        initChatbotInjector()
        with(getBindingView().topBotReasonLayout.etState) {
            minLine = minimumLines
            editText.setHintTextColor(
                ContextCompat.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_NN300
                )
            )
        }

        arguments?.let {
            if (!((it.getBoolean(IS_SHOW_OTHER_REASON)) ?: false)) {
                getBindingView().topBotReasonLayout.reasonLayout.hide()
            } else {
                val firstString = it.getString(OTHER_REASON_TITLE)
                val finalString = renderReasonText(firstString)
                getBindingView().topBotReasonLayout.botReasonText.text =
                    MethodChecker.fromHtml(finalString)
                getBindingView().topBotReasonLayout.etState.editText.addTextChangedListener(object :
                        TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            val reviewLength = s.toString().findLength()
                            updateReviewLength(reviewLength)
                            if (reviewLength in minLength..maxLength) {
                                getBindingView().topBotReasonLayout.etState.setMessage(
                                    context?.getString(
                                        R.string.minimum_30_character
                                    ) ?: ""
                                )
                            } else {
                                getBindingView().topBotReasonLayout.etState.setMessage("")
                            }
                        }
                    })
            }
        }
    }

    private fun renderReasonText(firstString: String?): String {
        var finalString = firstString.toBlankOrString()
        context?.let {
            val secondString = it.resources.getString(R.string.chatbot_csat_opsional).toBlankOrString()
            finalString = "<b>$firstString</b>" + getOptionalText(secondString)
        }

        return finalString
    }

    private fun getOptionalText(secondString: String): String {
        return "<font color='${
        context?.let { it1 ->
            ColorUtil.getColorFromResToString(
                it1,
                R.color.chatbot_dms_optional_text
            )
        }
        }'> $secondString </font>"
    }

    // Calculates the length of alphanumeric characters
    private fun String.findLength(): Int {
        return this.filter {
            it.isLetterOrDigit()
        }.length
    }

    private fun findViews(view: View) {
        mTxtHelpTitle = view.findViewById(getTextHelpTitleId())
        mSmileLayout = view.findViewById(getSmilleLayoutId())
        mTxtSmileSelected = view.findViewById(getSmileSelectedId())
        mTxtFeedbackQuestion = view.findViewById(getFeedbackQuestionId())
        mTxtFinished = view.findViewById(getTextFinishedId())
        mFilterReview = view.findViewById(getFilterReviewId())
    }

    override fun getLayoutManager(filterList: List<BadCsatReasonListItem>): RecyclerView.LayoutManager {
        val gridLayoutManager = GridLayoutManager(context, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(i: Int): Int {
                return if (filterList.size % 2 == 0) {
                    1
                } else {
                    if (i == filterList.size - 1) 2 else 1
                }
            }
        }
        return gridLayoutManager
    }

    override fun getTextHelpTitleId(): Int = getBindingView().txtHelpTitle.id
    override fun getSmilleLayoutId(): Int = getBindingView().smileLayout.id
    override fun getSmileSelectedId(): Int = getBindingView().txtSmileSelected.id
    override fun getFeedbackQuestionId(): Int = getBindingView().txtFeedbackQuestion.id
    override fun getTextFinishedId(): Int = getBindingView().txtFinished.id
    override fun getFilterReviewId(): Int = getBindingView().filterReview.id

    override fun onSuccessSubmit(intent: Intent) {
        chatbotAnalytics.get().eventClick(ACTION_KIRIM_CSAT_SMILEY_BUTTON_CLICKED)
        intent.putExtra(
            BOT_OTHER_REASON,
            getBindingView().topBotReasonLayout.etState.editText.text.toString()
        )
        intent.putExtra(TIME_STAMP, arguments?.getString(TIME_STAMP) ?: "")
        super.onSuccessSubmit(intent)
    }

    override fun sendEventClickReason(message: String?) {
        chatbotAnalytics.get().eventClick(ACTION_CSAT_SMILEY_REASON_BUTTON_CLICKED, message ?: "")
    }

    private fun initChatbotInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent
            )
                .chatbotModule(context?.let { ChatbotModule(it) })
                .build()

            chatbotComponent.inject(this)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }
}
