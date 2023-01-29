package com.tokopedia.feedplus.view.coachmark

import android.content.Context
import android.view.View
import android.widget.Toast
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FeedOnboardingCoachmark  @Inject constructor(
    private val userSession: UserSessionInterface,
){
    private var anchorMap: Map<String, View>? = null
    private var context: Context? = null
    private var affiliatePreference: AffiliatePreference? = null
    private var mListener: FeedOnboardingCoachmark.Listener? = null
    private var mShouldShowShortVideoCoachMark: Boolean = false
    private var mShouldShowUserProfileCoachMark: Boolean = false

    fun showFeedOnboardingCoachmark(
        anchorMap: Map<String, View>,
        context: Context? = null,
        affiliatePreference: AffiliatePreference,
        listener: FeedOnboardingCoachmark.Listener?,
        showShortVideoCoachMark :Boolean,
        showUserProfileCoachMark :Boolean
    ) {
        initializeFields(anchorMap, context, affiliatePreference)
        mListener = listener
        mShouldShowShortVideoCoachMark = showShortVideoCoachMark
        mShouldShowUserProfileCoachMark = showUserProfileCoachMark
        showFeedOnboardingCoachmark()

    }

    private fun showFeedOnboardingCoachmark() {
        context?.let {
            if(anchorMap == null) return

            val isUserProfileEntryShown =
                affiliatePreference?.isUserProfileEntryPointCoachMarkShown(userSession.userId)
                    ?: false
            val isUserEligibleForShortVideo =
                affiliatePreference?.isShortVideoEntryPointCoachMarkShown(userSession.userId)
                    ?: false
            val isUserEligibleForVideoTab =
                affiliatePreference?.isVideoTabEntryPointCoachMarkShown(userSession.userId) ?: false


            val coachMarkItem = ArrayList<CoachMark2Item>()
            val coachMark = CoachMark2(it)
            coachMark.elevation = 16f
            coachMark.onFinishListener = {
                mListener?.onCoachmarkFinish()
                markAsShowed()

            }
            coachMark.onDismissListener = {
                mListener?.onCoachmarkFinish()
                markAsShowed()
            }

            if (!isUserProfileEntryShown && mShouldShowUserProfileCoachMark)
                addUserProfileCoachmarkItem(coachMarkItem)

            if (!isUserEligibleForVideoTab)
             addVideoTabCoachmarkItem(coachMarkItem)

            if (!isUserEligibleForShortVideo && mShouldShowShortVideoCoachMark)
                addShortVideoCoachmarkItem(coachMarkItem)

            coachMark.showCoachMark(coachMarkItem)

        }
    }

    private fun markAsShowed() {
            affiliatePreference?.setUserProfileEntryPointCoachMarkShown(userSession.userId)
            affiliatePreference?.setShortVideoEntryPointCoachMarkShown(userSession.userId)
            affiliatePreference?.setVideoTabEntryPointCoachMarkShown(userSession.userId)
    }

    private fun addUserProfileCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_user_profile_coachmark_title)
                ?: ""
        val description =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_user_profile_coachmark_description)
                ?: ""
        val view : View? =  anchorMap?.get(USER_PROFILE_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view , title, description, CoachMark2.POSITION_BOTTOM
                )
            )
        }
    }

    private fun addVideoTabCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_video_tab_coachmark_title)
                ?: ""
        val description =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_video_tab_coachmark_description)
                ?: ""
        val view : View? =  anchorMap?.get(VIDEO_TAB_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view , title, description, CoachMark2.POSITION_BOTTOM
                )
            )
        }
    }

    private fun addShortVideoCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_short_video_coachmark_title)
                ?: ""
        val description =
            context?.getString(com.tokopedia.feedplus.R.string.feed_onboarding_short_video_coachmark_description)
                ?: ""
        val view : View? = anchorMap?.get(SHORT_VIDEO_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view , title, description, CoachMark2.POSITION_TOP
                )
            )
        }
    }

    private fun initializeFields(
        anchorMap: Map<String, View>,
        context: Context? = null,
        affiliatePreference: AffiliatePreference
    ) {
        this.anchorMap = anchorMap
        this.context = context
        this.affiliatePreference = affiliatePreference
    }

    companion object {
        const val USER_PROFILE_COACH_MARK_ANCHOR = "USER_PROFILE_COACH_MARK_ANCHOR_POSITION"
        const val VIDEO_TAB_COACH_MARK_ANCHOR = "VIDEO_TAB_COACH_MARK_ANCHOR_POSITION"
        const val SHORT_VIDEO_COACH_MARK_ANCHOR = "SHORT_VIDEO_COACH_MARK_ANCHOR_POSITION"
    }

    interface Listener {
        fun onCoachmarkFinish()
    }
}
