package com.tokopedia.chatbot.view.customview.reply

import android.content.Context
import android.view.View
import com.tokopedia.chatbot.data.cache.ChatbotCacheManager
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import javax.inject.Inject

class ReplyBubbleOnBoarding @Inject constructor(
    private val cacheManager: ChatbotCacheManager
){

    // TODO need adapter and recycler view

    private var context: Context? = null
    private var anchor: View? = null
    private var coachMark: CoachMark2? =null

    private fun showReplyBubbleOnBoarding(){
        context?.let {
            if (anchor == null) return
            val title = ""
            val description = ""
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMark = CoachMark2(it)
            coachMarkItem.add(
                CoachMark2Item(
                    anchor!!, title, description
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.setOnDismissListener {
                markAsShowed()
            }
        }

    }

    private fun markAsShowed(){
        cacheManager.saveState(CHATBOT_REPLY_BUBBLE_ONBOARDING,true)
    }

    fun hasBeenShown(): Boolean {
        return cacheManager.loadPreviousState(CHATBOT_REPLY_BUBBLE_ONBOARDING)
    }

    fun flush(){
        //Flush the rv and adapter
        anchor = null
        context = null

    }

    fun dismiss(){
        coachMark?.dismissCoachMark()
    }

    companion object {
        const val CHATBOT_REPLY_BUBBLE_ONBOARDING = "CHATBOT_REPLY_BUBBLE_ONBOARDING"
    }
}