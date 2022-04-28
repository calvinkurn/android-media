package com.tokopedia.chatbot.view.customview.reply

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.cache.ChatbotCacheManager
import com.tokopedia.chatbot.view.adapter.ChatbotAdapter
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import java.security.cert.TrustAnchor
import javax.inject.Inject

class ReplyBubbleOnBoarding @Inject constructor(
    private val cacheManager: ChatbotCacheManager
) {

    private var context: Context? = null
    private var anchor: View? = null
    private var coachMark: CoachMark2? = null
    private var rv: RecyclerView? = null
    private var adapter: ChatbotAdapter? = null

    fun showReplyBubbleOnBoarding(
        rv: RecyclerView,
        adapter: ChatbotAdapter,
        anchor: View?,
        context: Context? = null
    ) {
        initializeFields(rv, adapter, anchor, context)
        showReplyBubbleOnBoarding()
    }

    private fun initializeFields(
        rv: RecyclerView,
        adapter: ChatbotAdapter,
        anchor: View?,
        context: Context? = null
    ) {
        this.rv = rv
        this.adapter = adapter
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
                    anchor!!, title, description, CoachMark2.POSITION_TOP
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.setOnDismissListener {
                markAsShowed()
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
        rv = null
        adapter = null
    }

    fun dismiss() {
        coachMark?.dismissCoachMark()
    }

    companion object {
        const val CHATBOT_REPLY_BUBBLE_ONBOARDING = "CHATBOT_REPLY_BUBBLE_ONBOARDING"
    }
}