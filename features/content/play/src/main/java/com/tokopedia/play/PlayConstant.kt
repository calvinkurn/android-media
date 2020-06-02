package com.tokopedia.play

/**
 * Created by mzennis on 2019-12-03.
 */

const val PLAY_KEY_CHANNEL_ID = "channelId"

const val PLAY_GET_CHANNEL_INFO_V5 = "/gcn/api/v5/channel/{$PLAY_KEY_CHANNEL_ID}"

const val PLAY_WEB_SOCKET_GROUP_CHAT = "/ws/groupchat?channel_id="

const val KEY_GROUPCHAT_PREFERENCES = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter"
const val KEY_GROUPCHAT_DEVELOPER_OPTION_PREFERENCES = "ip_groupchat"

const val PARAM_SEND_TYPE = "type"
const val PARAM_SEND_DATA = "data"
const val PARAM_SEND_TYPE_SEND = "SEND_MESG"
const val PARAM_SEND_CHANNEL_ID = "channel_id"
const val PARAM_SEND_MESSAGE = "message"

const val ERR_SERVER_ERROR = "TOP01"
const val ERR_NOT_AVAILABLE = "TOP11"
const val ERR_NO_ACTIVE_SEGMENT = "TOP13"
const val ERR_CHANNEL_NOT_EXIST = "TOP02"
const val ERR_USER_UNAUTHORIZED = "TOP10"
const val ERR_CHANNEL_NOT_ACTIVE = "TOP03"
const val ERR_TOO_MANY_REQUEST = "TOP12"

const val ERR_STATE_SOCKET = "Socket Connection"
const val ERR_STATE_VIDEO = "Video Player"
const val ERR_STATE_GLOBAL = "Global Error"

/**
 * Performance Monitoring for Play
 * Start: PlayActivity before onCreate()
 */
const val PLAY_TRACE_PAGE = "plt_play_page"

/**
 * Monitor duration between open page until network requests
 * Start: PlayFragment onCreate()
 * Stop: onResume() getChannelInfo(channelId)
 */
const val PLAY_TRACE_PREPARE_PAGE = "plt_play_page_prepare_page"

/**
 * Monitor duration between network requests until receiving responses
 * Start: PlayFragment getChannelInfo(channelId)
 * Stop: PlayFragment observeGetChannelInfo
 */
const val PLAY_TRACE_REQUEST_NETWORK = "plt_play_page_request_network"

/**
 * Monitor duration between receive response until the UI is rendered
 * Start: PlayFragment observeGetChannelInfo()
 * Stop: PlayFragment observeVideoProperty when player is Playing
 */
const val PLAY_TRACE_RENDER_PAGE = "plt_play_page_render_page"