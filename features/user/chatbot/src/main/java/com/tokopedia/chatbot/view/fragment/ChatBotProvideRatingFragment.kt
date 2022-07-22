package com.tokopedia.chatbot.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.di.ChatbotModule
import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.bot_reason_layout.*
import kotlinx.android.synthetic.main.chatbot_fragment_rating_provide.*
import javax.inject.Inject

private const val ACTION_KIRIM_CSAT_SMILEY_BUTTON_CLICKED = "click kirim csat smiley button"
private const val ACTION_CSAT_SMILEY_REASON_BUTTON_CLICKED = "click csat smiley reason button"

class ChatBotProvideRatingFragment: BaseFragmentProvideRating() {

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

    companion object {
        const val BOT_OTHER_REASON= "bot_other_reason"
        const val OTHER_REASON_TITLE= "otherReasonTitle"
        const val IS_SHOW_OTHER_REASON = "is_show_other_reason"
        const val TIME_STAMP = "time_stamp"
        const val minLength = 1
        const val maxLength = 29
        fun newInstance(bundle: Bundle?): ChatBotProvideRatingFragment {
            val fragment = ChatBotProvideRatingFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chatbot_fragment_rating_provide, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        findViews(view)
        super.onViewCreated(view, savedInstanceState)
        initChatbotInjector()
        arguments?.let {
            if (!((it.getBoolean(IS_SHOW_OTHER_REASON))?:false)) {
                top_bot_reason_layout.hide()
            } else {
                bot_reason_text.text = it.getString(OTHER_REASON_TITLE)
                et_state.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {

                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val reviewLength = s.toString().findLength()
                        handleSubmitButtonState(reviewLength)
                        if (reviewLength in minLength..maxLength) {
                            warning_text.show()
                        } else {
                            warning_text.hide()
                        }
                    }

                })
            }

        }
    }

    //Calculates the length of alphanumeric characters
    private fun String.findLength() : Int {
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


    override fun getTextHelpTitleId():Int = R.id.txt_help_title
    override fun getSmilleLayoutId():Int = R.id.smile_layout
    override fun getSmileSelectedId():Int = R.id.txt_smile_selected
    override fun getFeedbackQuestionId():Int = R.id.txt_feedback_question
    override fun getTextFinishedId():Int = R.id.txt_finished
    override fun getFilterReviewId():Int = R.id.filter_review

    override fun onSuccessSubmit(intent: Intent) {
        chatbotAnalytics.get().eventClick(ACTION_KIRIM_CSAT_SMILEY_BUTTON_CLICKED)
        intent.putExtra(BOT_OTHER_REASON, et_state.text.toString())
        intent.putExtra(TIME_STAMP, arguments?.getString(TIME_STAMP) ?: "")
        super.onSuccessSubmit(intent)
    }

    override fun sendEventClickReason(message: String?) {
        chatbotAnalytics.get().eventClick(ACTION_CSAT_SMILEY_REASON_BUTTON_CLICKED, message ?: "")
    }

    private fun initChatbotInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
                ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                .chatbotModule(context?.let { ChatbotModule(it) })
                .build()

            chatbotComponent.inject(this)
        }
    }
}
