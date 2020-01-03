package com.tokopedia.play

/**
 * Created by mzennis on 2019-12-03.
 */

const val PLAY_KEY_CHANNEL_ID = "channelId"

const val PLAY_GET_CHANNEL_INFO_V3 = "/gcn/api/v3/channel/{$PLAY_KEY_CHANNEL_ID}"
const val PLAY_GET_CHANNEL_INFO_V4 = "/gcn/api/v4/channel/{$PLAY_KEY_CHANNEL_ID}"

const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

const val KEY_GROUPCHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"

const val PLAY_GET_VIDEO_STREAM = "${PLAY_GET_CHANNEL_INFO_V3}/video_stream"
const val PLAY_GET_STICKY_COMPONENTS = "${PLAY_GET_CHANNEL_INFO_V3}/sticky_components"
const val PLAY_GET_TOTAL_LIKES = "${PLAY_GET_CHANNEL_INFO_V3}/lope_lope"

const val PLAY_POST_LIKE = "${PLAY_GET_CHANNEL_INFO_V3}/click_lope_lope"

const val PARAM_SEND_TYPE = "type"
const val PARAM_SEND_DATA = "data"
const val PARAM_SEND_TYPE_SEND = "SEND_MESG"
const val PARAM_SEND_CHANNEL_ID = "channel_id"
const val PARAM_SEND_MESSAGE = "message"