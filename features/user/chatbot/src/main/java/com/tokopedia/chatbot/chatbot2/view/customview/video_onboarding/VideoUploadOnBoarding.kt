package com.tokopedia.chatbot.chatbot2.view.customview.video_onboarding

import android.content.Context
import android.view.View
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.chatbot2.view.listener.OnboardingDismissListener
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import javax.inject.Inject

class VideoUploadOnBoarding @Inject constructor(
    private val cacheManager: com.tokopedia.chatbot.chatbot2.data.cache.ChatbotCacheManager
) {

    private var context: Context? = null
    private var anchor: View? = null
    private var coachMark: CoachMark2? = null
    var onboardingDismissListener: OnboardingDismissListener? = null

    fun showVideoBubbleOnBoarding(
        anchor: View?,
        context: Context? = null
    ) {
        initializeFields(anchor, context)
        showVideoBubbleOnBoarding()
    }

    private fun initializeFields(
        anchor: View?,
        context: Context? = null
    ) {
        this.anchor = anchor
        this.context = context
    }

    private fun showVideoBubbleOnBoarding() {
        context?.let {
            if (anchor == null) return
            val title = it.getString(R.string.chatbot_video_upload_onboarding_title)
            val description = it.getString(R.string.chatbot_video_upload_onboarding_description)
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMark = CoachMark2(it)
            coachMarkItem.add(
                CoachMark2Item(
                    anchor!!,
                    title,
                    description,
                    CoachMark2.POSITION_TOP
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            markAsShowed()
            coachMark?.setOnDismissListener {
                onboardingDismissListener?.dismissVideoUploadOnBoarding()
            }
        }
    }

    private fun markAsShowed() {
        cacheManager.saveState(CHATBOT_VIDEO_UPLOAD_ONBOARDING, true)
    }

    fun hasBeenShown(): Boolean {
        return cacheManager.loadPreviousState(CHATBOT_VIDEO_UPLOAD_ONBOARDING)
    }

    fun flush() {
        anchor = null
        context = null
    }

    fun dismiss() {
        coachMark?.dismissCoachMark()
    }

    companion object {
        const val CHATBOT_VIDEO_UPLOAD_ONBOARDING = "CHATBOT_VIDEO_UPLOAD_ONBOARDING"
    }
}
