package com.tokopedia.profilecompletion.profilemanagement

import com.tokopedia.applink.user.DeeplinkMapperUser
import com.tokopedia.track.builder.Tracker

object ProfileManagementTracker {

    private const val LABEL_M1 = "m1"
    private const val LABEL_M2 = "m2"
    private const val EVENT_VIEW_ACCOUNT_IRIS = "viewAccountIris"
    private const val ACTION_LOAD_SUCCESS = "success load goto profile page"
    private const val ACTION_LOAD_FAILED = "failed load goto profile page"
    private const val ACTION_CLICK_COBA_LAGI = "click on coba lagi button"
    private const val CATEGORY_GOTO_PROFILE_LOAD_PAGE = "goto profile load page"
    private const val CATEGORY_GOTO_PROFILE_LOAD_FAILED_PAGE = "failed load goto profile page"
    private const val KEY_TRACKER_ID = "trackerId"
    private const val VALUE_TRACKER_ID_45931 = "45931"
    private const val VALUE_TRACKER_ID_45932 = "45932"
    private const val VALUE_TRACKER_ID_45933 = "45933"

    private const val BUSINESS_UNIT = "user platform"
    private const val CURRENT_SITE = "tokopediamarketplace"


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4103
    // Tracker ID: 45931
    fun sendSuccessLoadGotoProfilePageEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_LOAD_SUCCESS)
            .setEventCategory(CATEGORY_GOTO_PROFILE_LOAD_PAGE)
            .setEventLabel(getLabelExperiment())
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45931)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4103
    // Tracker ID: 45932
    fun sendFailedLoadGotoProfilePageEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_LOAD_FAILED)
            .setEventCategory(CATEGORY_GOTO_PROFILE_LOAD_PAGE)
            .setEventLabel(getLabelExperiment())
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45932)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4103
    // Tracker ID: 45933
    fun sendClickOnCobaLagiButtonEvent() {
        Tracker.Builder()
            .setEvent(EVENT_VIEW_ACCOUNT_IRIS)
            .setEventAction(ACTION_CLICK_COBA_LAGI)
            .setEventCategory(CATEGORY_GOTO_PROFILE_LOAD_FAILED_PAGE)
            .setEventLabel(getLabelExperiment())
            .setCustomProperty(KEY_TRACKER_ID, VALUE_TRACKER_ID_45933)
            .setBusinessUnit(BUSINESS_UNIT)
            .setCurrentSite(CURRENT_SITE)
            .build()
            .send()
    }

    private fun getLabelExperiment(): String {
        return if (DeeplinkMapperUser.isProfileManagementM2Activated()) {
            LABEL_M2
        } else {
            LABEL_M1
        }
    }

}
