package com.tokopedia.chatbot.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.chatbot.databinding.ChatbotFragmentCsatBinding
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

    private var _viewBinding: ChatbotFragmentCsatBinding? = null
    private fun getBindingView() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = ChatbotFragmentCsatBinding.inflate(inflater, container, false)
        return getBindingView().root
    }

    override fun getTextHelpTitleId(): Int = getBindingView().txtHelpTitle.id
    override fun getSmilleLayoutId(): Int = getBindingView().smileLayout.id
    override fun getSmileSelectedId(): Int = getBindingView().txtSmileSelected.id
    override fun getFeedbackQuestionId(): Int = getBindingView().txtFeedbackQuestion.id
    override fun getTextFinishedId(): Int = getBindingView().txtFinished.id
    override fun getFilterReviewId(): Int = getBindingView().filterReview.id

    override fun onSuccessSubmit(intent: Intent) {
        intent.putExtra(
            ChatBotCsatActivity.CASE_CHAT_ID,
            arguments?.getString(ChatBotCsatActivity.CASE_CHAT_ID)
        )
        intent.putExtra(
            ChatBotCsatActivity.CASE_ID,
            arguments?.getString(ChatBotCsatActivity.CASE_ID)
        )
        super.onSuccessSubmit(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}
