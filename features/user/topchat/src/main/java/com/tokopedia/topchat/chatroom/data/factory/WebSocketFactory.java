package com.tokopedia.topchat.chatroom.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.topchat.chatroom.data.mapper.ChatEventMapper;
import com.tokopedia.topchat.chatroom.data.source.CloudListenWebSocketDataSource;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class WebSocketFactory {

    private ChatService chatService;
    private ChatEventMapper mapper;

    public WebSocketFactory(ChatService chatService, ChatEventMapper mapper){
        this.chatService = chatService;
        this.mapper = mapper;
    }

    public CloudListenWebSocketDataSource createCloudListWebSocketSource() {
        return new CloudListenWebSocketDataSource(chatService, mapper);
    }
}
