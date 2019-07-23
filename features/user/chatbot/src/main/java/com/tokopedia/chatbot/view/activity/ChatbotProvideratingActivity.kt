package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
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

        fun getInstance(context: Context, clickEmoji: Int, list1: List<String>): Intent {
            val i = Intent(context, ChatBotProvideRatingActivity::class.java)
            i.putExtra(CLICKED_EMOJI, clickEmoji)
            val list = ArrayList<BadCsatReasonListItem>()
            var id = MESSAGE_ID
            for(message in list1){
                val badCsatReasonListItem = BadCsatReasonListItem()
                badCsatReasonListItem.id = id
                badCsatReasonListItem.message = message
                list.add(badCsatReasonListItem)
                id++
            }
            i.putParcelableArrayListExtra(PARAM_OPTIONS_CSAT, list)
            i.putExtra(BOT_OTHER_REASON, true)
            return i
        }
    }


}
