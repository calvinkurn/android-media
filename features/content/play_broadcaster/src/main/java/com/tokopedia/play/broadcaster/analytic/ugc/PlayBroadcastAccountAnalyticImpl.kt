package com.tokopedia.play.broadcaster.analytic.ugc

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by fachrizalmrsln on 22/09/22
 */
class PlayBroadcastAccountAnalyticImpl @Inject constructor(
    userSession: UserSessionInterface,
    private val hydraConfig: HydraConfigStore,
) : PlayBroadcastAccountAnalytic {

    private val userId = userSession.userId
    private val authorId: String
        get() = hydraConfig.getAuthorId()
    private val authorTypeName: String
        get() = hydraConfig.getAuthorTypeName()

    override fun onClickAccountDropdown() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - available account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "35372")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickAccount() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - pilih akun")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "35382")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickAccountAndHaveDraft() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - available account after draft")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "35494")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCancelSwitchAccount() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - batal switch account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "35508")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickConfirmSwitchAccount() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - confirm switch account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, "35509")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCloseOnboardingUGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - x username")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40052")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickUsernameFieldCompleteOnboardingUGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - username field")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40053")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCheckBoxCompleteOnboardingUGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - tnc box")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40054")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickNextOnboardingUGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - lanjut")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40055")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCloseTNCSGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - x ineligible")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40057")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickOkButtonTNCSGC() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - oke mengerti")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(KEY_TRACKER_ID, "40058")
            .setBusinessUnit(VAL_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}
