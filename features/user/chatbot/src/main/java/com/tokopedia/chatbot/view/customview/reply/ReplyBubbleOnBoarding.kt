package com.tokopedia.chatbot.view.customview.reply

import android.content.Context
import android.view.View
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.cache.ChatbotCacheManager
import com.tokopedia.chatbot.view.util.OnboardingDismissListener
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2.Companion.POSITION_TOP
import com.tokopedia.coachmark.CoachMark2Item
import javax.inject.Inject

class ReplyBubbleOnBoarding @Inject constructor(
    private val cacheManager: ChatbotCacheManager
) {

    private var context: Context? = null
    private var anchor: View? = null
    private var coachMark: CoachMark2? = null
    var onboardingDismissListener: OnboardingDismissListener? = null


    fun showReplyBubbleOnBoarding(
        anchor: View?,
        context: Context? = null
    ) {
        initializeFields(anchor, context)
        showReplyBubbleOnBoarding()
    }

    private fun initializeFields(
        anchor: View?,
        context: Context? = null
    ) {
        this.anchor = anchor
        this.context = context
    }


    private fun showReplyBubbleOnBoarding() {
        context?.let {
            if (anchor == null) return
            val title = it.getString(R.string.chatbot_reply_bubble_onboarding_title)
            val description = it.getString(R.string.chatbot_reply_bubble_onboarding_description)
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMark = CoachMark2(it)
            coachMarkItem.add(
                CoachMark2Item(
                    anchor!!, title, description, POSITION_TOP
                )
            )
            coachMark?.showCoachMark(coachMarkItem, null)
            markAsShowed()
            coachMark?.setOnDismissListener {
                onboardingDismissListener?.dismissReplyBubbleOnBoarding()
            }
        }

    }

    private fun markAsShowed() {
        cacheManager.saveState(CHATBOT_REPLY_BUBBLE_ONBOARDING, true)
    }

    fun hasBeenShown(): Boolean {
        return cacheManager.loadPreviousState(CHATBOT_REPLY_BUBBLE_ONBOARDING)
    }

    fun flush() {
        anchor = null
        context = null
    }

    fun dismiss() {
        coachMark?.dismissCoachMark()
    }

    companion object {
        const val CHATBOT_REPLY_BUBBLE_ONBOARDING = "CHATBOT_REPLY_BUBBLE_ONBOARDING"
    }
}
