package com.tokopedia.feedplus.view.coachmark

import android.content.Context
import android.view.View
import com.tokopedia.affiliatecommon.data.util.AffiliatePreference
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.content.common.util.coachmark.ContentCoachMarkSharedPref
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class FeedOnboardingCoachmark @Inject constructor(
    private val context: Context,
    private val userSession: UserSessionInterface,
    private val coachMarkSharedPref: ContentCoachMarkSharedPref

) {
    private var anchorMap: Map<String, View>? = null

    @Inject
    internal lateinit var affiliatePreference: AffiliatePreference

    private var mListener: Listener? = null
    private var mShouldShowShortVideoCoachMark: Boolean = false
    private var mShouldShowUserProfileCoachMark: Boolean = false
    private val coachMark = CoachMark2(context)

    fun showFeedOnboardingCoachmark(
        anchorMap: Map<String, View>,
        listener: Listener?,
        shouldShowShortVideoCoachMark: Boolean,
        shouldShowUserProfileCoachMark: Boolean
    ) {
        initializeFields(anchorMap)
        mListener = listener
        mShouldShowShortVideoCoachMark = shouldShowShortVideoCoachMark
        mShouldShowUserProfileCoachMark = shouldShowUserProfileCoachMark
        setUpFeedOnboardingCoachmark()
    }

    private fun setUpFeedOnboardingCoachmark() {
        if (anchorMap == null) return

        val isUserProfileEntryShown =
            affiliatePreference.isUserProfileEntryPointCoachMarkShown(userSession.userId)

        val isShortVideoCoachmarkShown =
            coachMarkSharedPref.hasBeenShown(ContentCoachMarkSharedPref.Key.PlayShortsEntryPoint, userSession.userId)

        val isVideoTabCoachmarkShown =
            affiliatePreference.isVideoTabEntryPointCoachMarkShown(userSession.userId)

        // If no coahmark is to be shown then screen should not be freezed
        if (shouldNotShowAnyCoachMark(
                isVideoTabCoachmarkShown,
                isShortVideoCoachmarkShown,
                isUserProfileEntryShown
            )
        ) {
            mListener?.onCoachmarkFinish()
        }

        val coachMarkItem = ArrayList<CoachMark2Item>()
        coachMark.elevation = 16f
        coachMark.onFinishListener = {
            mListener?.onCoachmarkFinish()
            markAsShowed()
        }
        coachMark.onDismissListener = {
            mListener?.onCoachmarkFinish()
            markAsShowed()
        }

        if (!isUserProfileEntryShown && mShouldShowUserProfileCoachMark) {
            addUserProfileCoachmarkItem(coachMarkItem)
        }

        if (!isVideoTabCoachmarkShown) {
            addVideoTabCoachmarkItem(coachMarkItem)
        }

        if (!isShortVideoCoachmarkShown && mShouldShowShortVideoCoachMark) {
            addShortVideoCoachmarkItem(coachMarkItem)
        }

        coachMark.showCoachMark(coachMarkItem)
    }
    private fun shouldNotShowAnyCoachMark(
        isVideoTabCoachmarkShown: Boolean,
        isShortVideoCoachmarkShown: Boolean,
        isUserProfileEntryShown: Boolean
    ) =
        isVideoTabCoachmarkShown && (isShortVideoCoachmarkShown || !mShouldShowShortVideoCoachMark) && (isUserProfileEntryShown || mShouldShowUserProfileCoachMark)

    private fun markAsShowed() {
        affiliatePreference.setUserProfileEntryPointCoachMarkShown(userSession.userId)
        coachMarkSharedPref.setHasBeenShown(ContentCoachMarkSharedPref.Key.PlayShortsPreparation, userSession.userId)
        affiliatePreference.setVideoTabEntryPointCoachMarkShown(userSession.userId)
    }

    private fun addUserProfileCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_user_profile_coachmark_title)
        val description =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_user_profile_coachmark_description)
        val view: View? = anchorMap?.get(USER_PROFILE_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view,
                    title,
                    description,
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
    }

    private fun addVideoTabCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_video_tab_coachmark_title)
        val description =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_video_tab_coachmark_description)
        val view: View? = anchorMap?.get(VIDEO_TAB_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view,
                    title,
                    description,
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
    }

    private fun addShortVideoCoachmarkItem(coachMarkItem: ArrayList<CoachMark2Item>) {
        val title =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_short_video_coachmark_title)
        val description =
            context.getString(com.tokopedia.feedplus.R.string.feed_onboarding_short_video_coachmark_description)
        val view: View? = anchorMap?.get(SHORT_VIDEO_COACH_MARK_ANCHOR)
        if (view != null) {
            coachMarkItem.add(
                CoachMark2Item(
                    view,
                    title,
                    description,
                    CoachMark2.POSITION_TOP
                )
            )
        }
    }

    fun dismissCoachmark() {
        coachMark.dismissCoachMark()
    }

    private fun initializeFields(
        anchorMap: Map<String, View>
    ) {
        this.anchorMap = anchorMap
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
