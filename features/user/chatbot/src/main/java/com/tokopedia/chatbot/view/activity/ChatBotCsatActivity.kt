package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.chatbot.data.csatoptionlist.CsatOptionsUiModel
import com.tokopedia.csat_rating.activity.BottomSheetProvideRatingActivity
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.kotlin.extensions.view.orZero

class ChatBotCsatActivity : BottomSheetProvideRatingActivity() {

    override fun getNewFragment(): Fragment? {
        return null
    }

    override fun onSuccessSubmit(intent: Intent) {
        intent.putExtra(
            CASE_CHAT_ID,
            intent.getStringExtra(CASE_CHAT_ID)
        )
        intent.putExtra(
            CASE_ID,
            intent.getStringExtra(CASE_ID)
        )
        super.onSuccessSubmit(intent)
    }

    companion object {

        val CSAT_TITLE = "csatTitle"
        val CAPTION_LIST = "captionList"
        val QUESTION_LIST = "questionList"
        val CASE_CHAT_ID = "caseChatID"
        val CASE_ID = "caseID"
        const val CLICKED_EMOJI = "clicked_emoji"
        const val PARAM_OPTIONS_CSAT = "options_csat"

        fun getInstance(context: Context, clickEmoji: Int, model: CsatOptionsUiModel?): Intent {
            val intent = Intent(context, ChatBotCsatActivity::class.java)
            intent.putExtra(CLICKED_EMOJI, clickEmoji)
            intent.putExtra(CASE_CHAT_ID, model?.csat?.caseChatId)
            intent.putExtra(CASE_ID, model?.csat?.caseId)
            val list = ArrayList<BadCsatReasonListItem>()
            model?.csat?.reasons?.let {
                for (reason in it) {
                    val badCsatReasonListItem = BadCsatReasonListItem()
                    badCsatReasonListItem.id = reason.code.orZero().toLong()
                    badCsatReasonListItem.message = reason.text
                    list.add(badCsatReasonListItem)
                }
            }

            intent.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, list)

            val captionList = ArrayList<String>()
            val questionList = ArrayList<String>()
            model?.csat?.points?.forEach {
                questionList.add(it.description ?: "")
                captionList.add(it.caption ?: "")
            }

            questionList.reverse()
            captionList.reverse()

            intent.putExtra(CSAT_TITLE, model?.csat?.title)
            intent.putStringArrayListExtra(CAPTION_LIST, captionList)
            intent.putStringArrayListExtra(QUESTION_LIST, questionList)
            return intent
        }
    }
}
