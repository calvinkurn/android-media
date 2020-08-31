package com.tokopedia.topchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class MessageRepositoryImpl implements MessageRepository {

    private MessageFactory messageFactory;

    public MessageRepositoryImpl(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    @Override
    public Observable<DeleteChatListUiModel> deleteMessage(JsonObject parameters) {
        return messageFactory.createCloudMessageDataSource().deleteMessage(parameters);
    }

}
