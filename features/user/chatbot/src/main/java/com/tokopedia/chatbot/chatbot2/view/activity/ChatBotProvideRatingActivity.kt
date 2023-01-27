package com.tokopedia.chatbot.chatbot2.view.activity

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.chatbot.chatbot2.data.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.chatbot2.view.fragment.ChatBotProvideRatingFragment
import com.tokopedia.chatbot.chatbot2.view.fragment.ChatBotProvideRatingFragment.Companion.TIME_STAMP
import com.tokopedia.csat_rating.activity.BaseProvideRatingActivity
import com.tokopedia.csat_rating.data.BadCsatReasonListItem

class ChatBotProvideRatingActivity : BaseProvideRatingActivity() {

    override fun getNewFragment(): Fragment {
        return ChatBotProvideRatingFragment.newInstance(intent.extras)
    }

    companion object {

        val MESSAGE_ID: Long = 0
        val BOT_OTHER_REASON = "bot_other_reason"
        val CSAT_TITLE = "csatTitle"
        val OTHER_REASON_TITLE = "otherReasonTitle"
        val CAPTION_LIST = "captionList"
        val QUESTION_LIST = "questionList"
        val IS_SHOW_OTHER_REASON = "is_show_other_reason"

        fun getInstance(context: Context, clickEmoji: Int, mCsatResponse: WebSocketCsatResponse): Intent {
            val webSocketCsatattribute = mCsatResponse.attachment?.attributes
            val intent = Intent(context, ChatBotProvideRatingActivity::class.java)
            intent.putExtra(CLICKED_EMOJI, clickEmoji)
            val list = ArrayList<BadCsatReasonListItem>()
            var id = MESSAGE_ID
            webSocketCsatattribute?.reasons?.let {
                for (message in it) {
                    val badCsatReasonListItem = BadCsatReasonListItem()
                    badCsatReasonListItem.id = id
                    badCsatReasonListItem.message = message
                    list.add(badCsatReasonListItem)
                    id++
                }
            }

            intent.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, list)
            intent.putExtra(BOT_OTHER_REASON, true)

            val captionList = ArrayList<String>()
            val questionList = ArrayList<String>()
            webSocketCsatattribute?.points?.let {
                for (point in it) {
                    point?.caption?.let {
                        captionList.add(it)
                    }
                    point?.description?.let {
                        questionList.add(it)
                    }
                }
            }

            intent.putExtra(IS_SHOW_OTHER_REASON, webSocketCsatattribute?.showOtherReason)
            intent.putExtra(CSAT_TITLE, webSocketCsatattribute?.title)
            intent.putExtra(OTHER_REASON_TITLE, webSocketCsatattribute?.reasonTitle)
            intent.putStringArrayListExtra(CAPTION_LIST, captionList)
            intent.putStringArrayListExtra(QUESTION_LIST, questionList)
            intent.putExtra(TIME_STAMP, mCsatResponse.message?.timestampUnix.toString())
            return intent
        }
    }
}
