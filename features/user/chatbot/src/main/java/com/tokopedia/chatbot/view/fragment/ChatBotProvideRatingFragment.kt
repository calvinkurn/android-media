package com.tokopedia.chatbot.view.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.chatbot.R
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.bot_reason_layout.*
import kotlinx.android.synthetic.main.chatbot_fragment_rating_provide.*

class ChatBotProvideRatingFragment: BaseFragmentProvideRating() {

    companion object {
        const val BOT_OTHER_REASON= "bot_other_reason"
        const val OTHER_REASON_TITLE= "otherReasonTitle"
        const val IS_SHOW_OTHER_REASON = "is_show_other_reason"
        const val TIME_STAMP = "time_stamp"
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
                        if (s.toString().length in 1..29) {
                            disableSubmitButton()
                            warning_text.show()
                        } else {
                            warning_text.hide()
                            enableSubmitButton()
                        }
                    }

                })
            }

        }
    }

    private fun findViews(view: View) {
        mTxtHelpTitle = view.findViewById(getTextHelpTitleId())
        mSmileLayout = view.findViewById(getSmilleLayoutId())
        mTxtSmileSelected = view.findViewById(getSmileSelectedId())
        mTxtFeedbackQuestion = view.findViewById(getFeedbackQuestionId())
        mTxtFinished = view.findViewById(getTextFinishedId())
        mFilterReview = view.findViewById(getFilterReviewId())
    }

    override fun getTextHelpTitleId():Int = R.id.txt_help_title
    override fun getSmilleLayoutId():Int = R.id.smile_layout
    override fun getSmileSelectedId():Int = R.id.txt_smile_selected
    override fun getFeedbackQuestionId():Int = R.id.txt_feedback_question
    override fun getTextFinishedId():Int = R.id.txt_finished
    override fun getFilterReviewId():Int = R.id.filter_review

    override fun onSuccessSubmit(intent: Intent) {
        intent.putExtra(BOT_OTHER_REASON, et_state.text.toString())
        intent.putExtra(TIME_STAMP, arguments?.getString(TIME_STAMP) ?: "")
        super.onSuccessSubmit(intent)
    }

}
