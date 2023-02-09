package com.tokopedia.play.broadcaster.analytic.setup.cover

import com.tokopedia.play.broadcaster.analytic.*
import com.tokopedia.play.broadcaster.analytic.KEY_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_CURRENT_SITE
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_ACTION
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_EVENT_LABEL
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_BUSINESS_UNIT
import com.tokopedia.play.broadcaster.analytic.KEY_TRACK_CATEGORY
import com.tokopedia.play.broadcaster.analytic.KEY_USER_ID
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.track.TrackApp
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on February 04, 2022
 */
class PlayBroSetupCoverAnalyticImpl @Inject constructor(
    private val userSession: UserSessionInterface,
    private val hydraConfig: HydraConfigStore,
) : PlayBroSetupCoverAnalytic {

    private  val userId = userSession.userId
    private val authorId: String
        get() = hydraConfig.getAuthorId()
    private val authorTypeName: String
        get() = hydraConfig.getAuthorTypeName()

    override fun clickAddNewCover() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT)
            .setEventAction("click - cover thumbnail to add cover")
            .setEventCategory(KEY_TRACK_CATEGORY)
            .setEventLabel(userId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickEditCover() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT)
            .setEventAction("click - edit cover")
            .setEventCategory(KEY_TRACK_CATEGORY)
            .setEventLabel(userSession.userId)
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCloseTemplateTabCoachMark() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - x template tab")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40737","40703"))
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCoverTab(tabName: String) {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - cover tab")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40738", "40704"))
            .setEventLabel("$authorId - $authorTypeName - $tabName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickTemplateCoverAddProduct() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - template tambah produk")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40739", "40705"))
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickCloseCoverBottomSheet() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - x cover bottomsheet")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40740", "40706"))
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickTryAgainTemplateCoverBottomSheet() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - coba lagi template cover")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40741", "40708"))
            .setEventLabel("$authorId - $authorTypeName")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSaveGeneratedCover() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - save generated cover")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40742", "40709"))
            .setEventLabel("$authorId - $authorTypeName - coverId - 1 - coverColor")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickDeleteGeneratedCover() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - delete generated cover")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40743", "40710"))
            .setEventLabel("$authorId - $authorTypeName - coverId - 1 - coverColor")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSelectGeneratedCover() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - generated cover")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40744", "40711"))
            .setEventLabel("$authorId - $authorTypeName - coverId - 1 - coverColor")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

    override fun clickSaveGeneratedCoverOption() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - save generated cover option")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setCustomProperty(KEY_TRACKER_ID, getTrackerId("40745", "40712"))
            .setEventLabel("$authorId - $authorTypeName - coverId - 1 - coverColor")
            .setBusinessUnit(KEY_TRACK_BUSINESS_UNIT)
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}
