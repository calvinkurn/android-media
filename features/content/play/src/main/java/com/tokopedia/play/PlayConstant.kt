package com.tokopedia.play

/**
 * Created by mzennis on 2019-12-03.
 */

const val PLAY_KEY_CHANNEL_ID = "channelId"
const val PLAY_KEY_SOURCE_TYPE = "source_type"
const val PLAY_KEY_SOURCE_ID = "source_id"
const val PLAY_KEY_CHANNEL_RECOMMENDATION = "channel_recom"
const val PLAY_KEY_LAST_PATH_SEGMENT = "last_path_segment"
const val PLAY_KEY_WIDGET_ID = "widget_id"
const val PLAY_KEY_PAGE_SOURCE_NAME = "page_source_name"

const val HOST_INTERNAL = "tokopedia-android-internal"
const val PLAY_APP_LINK = "$HOST_INTERNAL://play/{channel_id}?source_type={source_type}&source_id={source_id}"
const val PLAY_RECOMMENDATION_APP_LINK = "$HOST_INTERNAL://play/$PLAY_KEY_CHANNEL_RECOMMENDATION?source_type={source_type}"
