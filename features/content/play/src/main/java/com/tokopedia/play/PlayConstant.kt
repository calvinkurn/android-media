package com.tokopedia.play


/**
 * Created by mzennis on 2019-12-03.
 */

const val PLAY_KEY_CHANNEL_ID = "channelId"

const val PLAY_GET_CHANNEL_INFO_V3 = "/gcn/api/v3/channel/{$PLAY_KEY_CHANNEL_ID}"
const val PLAY_GET_CHANNEL_INFO_V4 = "/gcn/api/v4/channel/{$PLAY_KEY_CHANNEL_ID}"

const val PLAY_GET_VIDEO_STREAM = "${PLAY_GET_CHANNEL_INFO_V3}/video_stream"
const val PLAY_GET_STICKY_COMPONENTS = "${PLAY_GET_CHANNEL_INFO_V3}/sticky_components"
const val PLAY_GET_TOTAL_LIKES = "${PLAY_GET_CHANNEL_INFO_V3}/lope_lope"

const val PLAY_POST_LIKE = "${PLAY_GET_CHANNEL_INFO_V3}/click_lope_lope"