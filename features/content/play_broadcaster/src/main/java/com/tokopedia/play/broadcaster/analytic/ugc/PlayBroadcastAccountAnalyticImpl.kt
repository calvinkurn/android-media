package com.tokopedia.play.broadcaster.analytic.ugc

import com.tokopedia.content.analytic.BusinessUnit
import com.tokopedia.content.analytic.Event
import com.tokopedia.content.analytic.Key
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
    private val hydraConfig: HydraConfigStore
) : PlayBroadcastAccountAnalytic {

    private val userId = userSession.userId
    private val authorId: String
        get() = hydraConfig.getAuthorId()
    private val authorTypeName: String
        get() = hydraConfig.getAuthorTypeName()

    override fun onClickAccountDropdown() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - available account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(Key.trackerId, "35372")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickAccount() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - pilih akun")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(Key.trackerId, "35382")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickAccountAndHaveDraft() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - available account after draft")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(Key.trackerId, "35494")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCancelSwitchAccount() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - batal switch account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(Key.trackerId, "35508")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickConfirmSwitchAccount() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - confirm switch account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(Key.trackerId, "35509")
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCloseOnboardingUGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - x username")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40052")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickUsernameFieldCompleteOnboardingUGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - username field")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40053")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCheckBoxCompleteOnboardingUGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - tnc box")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40054")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickNextOnboardingUGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - lanjut")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40055")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickCloseTNCSGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - x ineligible")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40057")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun onClickOkButtonTNCSGC() {
        Tracker.Builder()
            .setEvent(Event.clickContent)
            .setEventAction("click - oke mengerti")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("$authorId - $authorTypeName")
            .setCustomProperty(Key.trackerId, "40058")
            .setBusinessUnit(BusinessUnit.content)
            .setCurrentSite(currentSite)
            .setCustomProperty(Key.sessionIris, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }
}
