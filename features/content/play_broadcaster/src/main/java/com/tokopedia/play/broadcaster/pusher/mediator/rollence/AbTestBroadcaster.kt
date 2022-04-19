package com.tokopedia.play.broadcaster.pusher.mediator.rollence

import com.tokopedia.remoteconfig.RemoteConfigInstance

object AbTestBroadcaster {

    private const val AB_BROADCASTER_SDK_KEY = "play_media_sdk"

    fun isUseBroadcasterSdk(): Boolean {
        val abPlatform = RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(AB_BROADCASTER_SDK_KEY, "")

        return abPlatform.isNotEmpty()
    }

}