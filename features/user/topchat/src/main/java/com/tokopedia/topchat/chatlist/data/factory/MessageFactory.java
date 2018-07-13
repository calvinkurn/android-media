package com.tokopedia.topchat.chatlist.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.source.CloudMessageDataSource;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class MessageFactory {

    private DeleteMessageMapper deleteMessageMapper;
    private GetMessageMapper getMessageMapper;
    private ChatService chatService;

    public MessageFactory(ChatService chatService, GetMessageMapper getMessageMapper, DeleteMessageMapper deleteMessageMapper){
        this.chatService = chatService;
        this.getMessageMapper = getMessageMapper;
        this.deleteMessageMapper = deleteMessageMapper;
    }

    public CloudMessageDataSource createCloudMessageDataSource() {
        return new CloudMessageDataSource(chatService, getMessageMapper, deleteMessageMapper);
    }
}
