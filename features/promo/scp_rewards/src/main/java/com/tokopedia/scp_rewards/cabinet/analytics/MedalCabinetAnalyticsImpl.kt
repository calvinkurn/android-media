package com.tokopedia.scp_rewards.cabinet.analytics

import com.tokopedia.scp_rewards.common.constants.TrackerConstants
import com.tokopedia.track.builder.Tracker
import com.tokopedia.track.constant.TrackerConstant
import org.json.JSONObject

object MedalCabinetAnalyticsImpl : MedalCabinetAnalytics {

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44256
    override fun sendViewMedalCabinetPageEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44257
    override fun sendClickBackMedalCabinetPageEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44258
    override fun sendClickUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_UNLOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_UNLOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44259
    override fun sendViewUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_UNLOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_UNLOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44260
    override fun sendViewUnlockedMedalGenericEvent(badgeId: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_UNLOCKED_MEDAL_GENERIC)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_GENERIC_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44261
    override fun sendViewUnlockedMedalSectionCtaEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_UNLOCKED_MEDAL_SECTION_CTA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_CTA_SEE_MORE_UNLOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44262
    override fun sendClickUnlockedMedalSectionCtaEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_UNLOCKED_MEDAL_SECTION_CTA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_CTA_SEE_MORE_UNLOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44263
    override fun sendViewLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_PERCENTAGE,
                taskProgressPercent
            )
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_LOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_LOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44264
    override fun sendClickLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_PERCENTAGE,
                taskProgressPercent
            )
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_LOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_LOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44265
    override fun sendViewLockedMedalSectionCtaEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_LOCKED_MEDAL_SECTION_CTA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_CTA_SEE_MORE_LOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44266
    override fun sendClickLockedMedalSectionCtaEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_LOCKED_MEDAL_SECTION_CTA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_CTA_SEE_MORE_LOCKED_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44267
    override fun sendViewSeeMoreUnlockedMedalPageEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_VIEW)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44268
    override fun sendViewSeeMoreUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_UNLOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_VIEW_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44269
    override fun sendClickSeeMoreUnlockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_UNLOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_CLICK_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44270
    override fun sendClickBackSeeMoreUnlockedMedalEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44271
    override fun sendViewSeeMoreLockedMedalPageEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_VIEW)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44272
    override fun sendViewSeeMoreLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_PERCENTAGE,
                taskProgressPercent
            )
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_LOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_VIEW_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44273
    override fun sendClickSeeMoreLockedMedalEvent(
        badgeId: String,
        isLocked: Boolean,
        medalTitle: String,
        bonusText: String,
        taskProgressPercent: String
    ) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.BADGE_ID, badgeId)
            put(
                TrackerConstants.EventLabelProperties.MEDALI_STATUS,
                if (isLocked) "locked" else "unlock"
            )
            put(TrackerConstants.EventLabelProperties.MEDAL_TITLE, medalTitle)
            put(TrackerConstants.EventLabelProperties.MEDAL_BONUS_TEXT, bonusText)
            put(
                TrackerConstants.EventLabelProperties.MEDAL_PROGRESS_PERCENTAGE,
                taskProgressPercent
            )
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_LOCKED_MEDAL)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_CLICK_MEDAL)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44274
    override fun sendClickBackSeeMoreLockedMedalEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44277
    override fun sendViewBannerEvent(creativeName: String, bannerPosition: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.CREATIVE_NAME, creativeName)
            put(TrackerConstants.EventLabelProperties.BANNER_POSITION, bannerPosition)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_BANNER)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_EMPTY_BANNER)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44278
    override fun sendClickBannerEvent(creativeName: String, bannerPosition: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.CREATIVE_NAME, creativeName)
            put(TrackerConstants.EventLabelProperties.BANNER_POSITION, bannerPosition)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_BANNER)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_CLICK_EMPTY_BANNER)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44279
    override fun sendViewUnlockedMedalSectionApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_UNLOCKED_MEDAL_SECTION_ERROR)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_UNLOCKED_SECTION_API_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44280
    // Not implemented - no such UI as of now
    override fun sendClickCtaUnlockedMedalSectionApiErrorEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA_UNLOCKED_MEDAL_SECTION_ERROR)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_UNLOCKED_SECTION_API_ERROR_CLICK_COBA_LAGI)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44281
    override fun sendViewLockedMedalSectionApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.EventAction.VIEW_LOCKED_MEDAL_SECTION_ERROR)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_LOCKED_SECTION_API_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44282
    // Not implemented - no such UI as of now
    override fun sendClickCtaLockedMedalSectionApiErrorEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA_LOCKED_MEDAL_SECTION_ERROR)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_LOCKED_SECTION_API_ERROR_CLICK_COBA_LAGI)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44283
    override fun sendViewMedalCabinetPageApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_API_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44284
    override fun sendClickCobaLagiMedalCabinetPageApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA_COBA_LAGI)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_API_ERROR_CLICK_COBA_LAGI)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44285
    override fun sendClickHalamanUtamaMedalCabinetPageApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA_HALAMAN_UTAMA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_API_ERROR_CLICK_HALAMAN_UTAMA)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44286
    override fun sendClickBackMedalCabinetPageApiErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_API_ERROR_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44287
    override fun sendViewMedalCabinetSkeletonPageEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_SKELETON)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_SKELETON)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44288
    override fun sendViewMedalCabinetPageInternetErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_INTERNET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_INTERNET_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44289
    override fun sendClickCobaLagiMedalCabinetPageInternetErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA_COBA_LAGI)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_INTERNET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_INTERNET_ERROR_CLICK_COBA_LAGI)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44290
    override fun sendClickBackMedalCabinetPageInternetErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_INTERNET_ERROR)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_INTERNET_ERROR_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44326
    override fun sendViewMedalCabinetPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_VIEW_NON_WHITELISTED_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44333
    override fun sendClickCtaMedalCabinetPageNonWhitelistedErrorEvent(buttonText: String) {
        val eventLabel = JSONObject().apply {
            put(TrackerConstants.EventLabelProperties.MEDAL_BUTTON_TEXT, buttonText)
        }
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_NON_WHITELISTED)
            .setEventLabel(eventLabel.toString())
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44334
    override fun sendClickBackMedalCabinetPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.MEDAL_CABINET_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.CABINET_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44756
    override fun sendViewSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_VIEW_NON_WHITELISTED_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44757
    override fun sendClickCtaSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44758
    override fun sendClickBackSeeMoreUnlockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.UNLOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.UNLOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44759
    override fun sendViewSeeMoreLockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.VIEW_EVENT)
            .setEventAction(TrackerConstants.General.VIEW_PAGE_EVENT)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_VIEW_NON_WHITELISTED_ERROR)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44760
    override fun sendClickCtaSeeMoreLockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.EventAction.CLICK_CTA)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_CTA)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }

    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4006
    // Tracker ID: 44761
    override fun sendClickBackSeeMoreLockedMedalPageNonWhitelistedErrorEvent() {
        Tracker.Builder()
            .setEvent(TrackerConstants.Event.CLICK_EVENT)
            .setEventAction(TrackerConstants.General.BACK_BUTTON_CLICK)
            .setEventCategory(TrackerConstants.EventCategory.LOCKED_MEDAL_PAGE_NON_WHITELISTED)
            .setEventLabel("")
            .setCustomProperty(TrackerConstant.TRACKER_ID, TrackerConstants.Tracker.LOCKED_MEDAL_PAGE_NON_WHITELISTED_ERROR_CLICK_BACK)
            .setBusinessUnit(TrackerConstants.Business.BUSINESS_UNIT)
            .setCurrentSite(TrackerConstants.Business.CURRENT_SITE)
            .build()
            .send()
    }
}
