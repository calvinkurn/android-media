package com.tokopedia.feedplus.presentation.adapter.util

import android.os.Bundle

/**
 * Created By : Jonathan Darwin on July 24, 2023
 */
object FeedFollowProfilePayloadHelper {

    private const val PAYLOAD_IS_PLAY_VIDEO = "IS_PLAY_VIDEO"

    fun createPayload(
        isPlayVideo: Boolean
    ) = Bundle().apply {
        putBoolean(PAYLOAD_IS_PLAY_VIDEO, isPlayVideo)
    }

    fun isPlayVideo(
        payloads: Bundle
    ): Boolean {
        return payloads.getBoolean(PAYLOAD_IS_PLAY_VIDEO)
    }
}
