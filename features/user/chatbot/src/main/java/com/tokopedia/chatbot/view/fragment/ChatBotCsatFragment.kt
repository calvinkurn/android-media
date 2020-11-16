package com.tokopedia.chatbot.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.view.activity.ChatBotCsatActivity
import com.tokopedia.csat_rating.fragment.BaseFragmentProvideRating

class ChatBotCsatFragment : BaseFragmentProvideRating() {

    companion object {
        fun newInstance(bundle: Bundle?): ChatBotCsatFragment {
            val fragment = ChatBotCsatFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.chatbot_fragment_csat, container, false)
    }

    override fun getTextHelpTitleId(): Int = R.id.txt_help_title
    override fun getSmilleLayoutId(): Int = R.id.smile_layout
    override fun getSmileSelectedId(): Int = R.id.txt_smile_selected
    override fun getFeedbackQuestionId(): Int = R.id.txt_feedback_question
    override fun getTextFinishedId(): Int = R.id.txt_finished
    override fun getFilterReviewId(): Int = R.id.filter_review

    override fun onSuccessSubmit(intent: Intent) {
        intent.putExtra(ChatBotCsatActivity.CASE_CHAT_ID, arguments?.getString(ChatBotCsatActivity.CASE_CHAT_ID))
        intent.putExtra(ChatBotCsatActivity.CASE_ID, arguments?.getString(ChatBotCsatActivity.CASE_ID))
        super.onSuccessSubmit(intent)
    }

}
