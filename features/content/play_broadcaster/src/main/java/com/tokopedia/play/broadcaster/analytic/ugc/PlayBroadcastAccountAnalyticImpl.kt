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

    override fun onClickAccountDropdown() {
        Tracker.Builder()
            .setEvent(KEY_TRACK_CLICK_EVENT_SELLER)
            .setEventAction("click - available account")
            .setEventCategory(KEY_TRACK_CATEGORY_PLAY)
            .setEventLabel("${hydraConfig.getAuthorId()} - ${hydraConfig.getAuthorTypeName()}")
            .setBusinessUnit("content")
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
            .setEventLabel("${hydraConfig.getAuthorId()} - ${hydraConfig.getAuthorTypeName()}")
            .setBusinessUnit("content")
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
            .setEventLabel("${hydraConfig.getAuthorId()} - ${hydraConfig.getAuthorTypeName()}")
            .setBusinessUnit("content")
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
            .setEventLabel("${hydraConfig.getAuthorId()} - ${hydraConfig.getAuthorTypeName()}")
            .setBusinessUnit("content")
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
            .setEventLabel("${hydraConfig.getAuthorId()} - ${hydraConfig.getAuthorTypeName()}")
            .setBusinessUnit("content")
            .setCurrentSite(currentSite)
            .setCustomProperty(KEY_SESSION_IRIS, sessionIris)
            .setUserId(userId)
            .build()
            .send()
    }

}