package com.tokopedia.topchat.chatroom.view.onboarding

import android.content.Context
import android.view.View
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.TopChatRoomAdapter
import com.tokopedia.topchat.chatroom.view.custom.message.TopchatMessageRecyclerView
import com.tokopedia.topchat.common.network.TopchatCacheManager
import javax.inject.Inject

class ReplyBubbleOnBoarding @Inject constructor(
    private val cacheManager: TopchatCacheManager
) {
    private var rv: TopchatMessageRecyclerView? = null
    private var adapter: TopChatRoomAdapter? = null
    private var anchor: View? = null
    private var context: Context? = null
    private var coachMark: CoachMark2? = null

    fun showReplyBubbleOnBoarding(
        rv: TopchatMessageRecyclerView?,
        adapter: TopChatRoomAdapter?,
        anchor: View?,
        context: Context? = null
    ) {
        assignField(rv, adapter, anchor, context)
        showReplyBubbleOnBoardingAt()
    }

    private fun showReplyBubbleOnBoardingAt() {
        context?.let {
            if (anchor == null) return
            val coachMarkItem = ArrayList<CoachMark2Item>()
            val title = it.getString(R.string.title_topchat_reply_bubble_onboarding)
            val desc = it.getString(R.string.desc_topchat_reply_bubble_onboarding)
            coachMark = CoachMark2(it)
            coachMarkItem.add(
                CoachMark2Item(
                    anchor!!,
                    title,
                    desc
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.setOnDismissListener {
                markAsShown()
            }
        }
    }

    private fun assignField(
        rv: TopchatMessageRecyclerView?,
        adapter: TopChatRoomAdapter?,
        anchor: View?,
        context: Context? = null
    ) {
        this.rv = rv
        this.adapter = adapter
        this.anchor = anchor
        this.context = context
    }

    private fun markAsShown() {
        cacheManager.saveState(KEY_REPLY_BUBBLE_ONBOARDING, true)
    }

    fun hasBeenShown(): Boolean {
        return cacheManager.getPreviousState(KEY_REPLY_BUBBLE_ONBOARDING, false)
    }

    fun flush() {
        rv = null
        anchor = null
        context = null
        adapter = null
    }

    fun dismiss() {
        coachMark?.dismissCoachMark()
    }

    companion object {
        const val KEY_REPLY_BUBBLE_ONBOARDING = "KEY_REPLY_BUBBLE_ONBOARDING_NEW"
    }
}
