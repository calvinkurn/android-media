package com.tokopedia.groupchat.chatroom.data;

import com.tokopedia.network.constant.TkpdBaseURL;

/**
 * @author by nisie on 2/23/18.
 */

public class ChatroomUrl {

    static final String GET_CHANNEL_INFO = "/gcn/api/v3/channel/{channel_uuid}";
    static final String GET_DYNAMIC_BUTTONS = "/gcn/api/v3/channel/{channel_uuid}/buttons";
    static final String GET_STICKY_COMPONENTS = "/gcn/api/v3/channel/{channel_uuid}/sticky_components";

    static final String PATH_CHANNEL_UUID = "channel_uuid";
    public static final String DESKTOP_URL = "https://tokopedia.link/playblog";

    public static final String GROUP_CHAT_URL = "https://tokopedia.com/groupchat/{channel_url}";
    public static final String PATH_WEB_SOCKET_GROUP_CHAT_URL = "/ws/groupchat?channel_id=";
    public static String GROUP_CHAT_WEBSOCKET_DOMAIN = TkpdBaseURL.GROUP_CHAT_WEBSOCKET_DOMAIN;
}
