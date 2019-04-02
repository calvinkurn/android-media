package com.tokopedia.topchat.chatlist.data.factory;

import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.data.source.CloudMessageDataSource;
import com.tokopedia.topchat.common.chat.api.ChatApi;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class MessageFactory {

    private DeleteMessageMapper deleteMessageMapper;
    private GetMessageMapper getMessageMapper;
    private ChatApi chatApi;

    public MessageFactory(ChatApi chatApi, GetMessageMapper getMessageMapper,
                          DeleteMessageMapper deleteMessageMapper){
        this.chatApi = chatApi;
        this.getMessageMapper = getMessageMapper;
        this.deleteMessageMapper = deleteMessageMapper;
    }

    public CloudMessageDataSource createCloudMessageDataSource() {
        return new CloudMessageDataSource(chatApi, getMessageMapper, deleteMessageMapper);
    }
}
