package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import com.tokopedia.chatbot.domain.pojo.csatRating.websocketCsatRatingResponse.WebSocketCsatResponse
import com.tokopedia.chatbot.view.fragment.ChatBotProvideRatingFragment
import com.tokopedia.csat_rating.data.BadCsatReasonListItem
import com.tokopedia.csat_rating.activity.BaseProvideRatingActivity
import java.util.ArrayList

class ChatBotProvideRatingActivity: BaseProvideRatingActivity() {


    override fun getNewFragment(): Fragment {
        return ChatBotProvideRatingFragment.newInstance(intent.extras)
    }

    companion object {

        val MESSAGE_ID= 0
        val BOT_OTHER_REASON = "bot_other_reason"
        val CSAT_TITLE = "csatTitle"
        val OTHER_REASON_TITLE = "otherReasonTitle"
        val CAPTION_LIST = "captionList"
        val QUESTION_LIST = "questionList"

        fun getInstance(context: Context, clickEmoji: Int, mCsatResponse: WebSocketCsatResponse): Intent {
            var webSocketCsatattribute = mCsatResponse.attachment?.attributes
            val intent = Intent(context, ChatBotProvideRatingActivity::class.java)
            intent.putExtra(CLICKED_EMOJI, clickEmoji)
            val list = ArrayList<BadCsatReasonListItem>()
            var id = MESSAGE_ID
            for(message in webSocketCsatattribute?.reasons!!){
                val badCsatReasonListItem = BadCsatReasonListItem()
                badCsatReasonListItem.id = id
                badCsatReasonListItem.message = message
                list.add(badCsatReasonListItem)
                id++
            }
            intent.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, list)
            intent.putExtra(BOT_OTHER_REASON, true)
            val captionList = ArrayList<String>()
            for(point in webSocketCsatattribute.points!!){
                captionList.add(point?.caption!!)
            }

            val questionList = ArrayList<String>()
            for(point in webSocketCsatattribute.points!!){
                questionList.add(point?.description!!)
            }

            intent.putExtra(CSAT_TITLE,webSocketCsatattribute.title)
            intent.putExtra(OTHER_REASON_TITLE,webSocketCsatattribute.reasonTitle)
            intent.putStringArrayListExtra(CAPTION_LIST,captionList)
            intent.putStringArrayListExtra(QUESTION_LIST,questionList)
            return intent
        } }
}
