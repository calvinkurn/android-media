package com.tokopedia.groupchat.chatroom.data;

/**
 * @author by nisie on 2/23/18.
 */

public class ChatroomUrl {

    static final String GET_CHANNEL_INFO = "/gcn/api/v2/channel/{channel_uuid}";
    static final String PATH_CHANNEL_UUID = "channel_uuid";
    public static final String DESKTOP_URL = "https://www.tokopedia.com/blog/chat-group";

    public static final String GROUP_CHAT_URL = "https://tokopedia.com/groupchat/{channel_url}";
    public static final String PATH_WEB_SOCKET_GROUP_CHAT_URL = "/ws/groupchat?channel_id=";
}
