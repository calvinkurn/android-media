package com.tokopedia.stories.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * @author by astidhiyaa on 05/09/23
 */
class StoriesSharingAnalyticsImpl @AssistedInject constructor(
    @Assisted val shopId: String,
    private val userSession: UserSessionInterface
) : StoriesSharingAnalytics {

    @AssistedFactory
    interface Factory : StoriesSharingAnalytics.Factory {
        override fun create(
            shopId: String,
            ): StoriesSharingAnalyticsImpl
    }


    private val role: String
        get() {
            val isOwner = userSession.shopId == shopId
            return if (isOwner) "creator" else "viewer"
        }

    private val userId: String
        get() = userSession.userId

    override fun onClickShareIcon(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf()
        )
    }

    override fun onClickShareOptions(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf()
        )
    }

    override fun onImpressShareSheet(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf()
        )
    }

    override fun onCloseShareSheet(storyId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            mapOf()
        )
    }
}
