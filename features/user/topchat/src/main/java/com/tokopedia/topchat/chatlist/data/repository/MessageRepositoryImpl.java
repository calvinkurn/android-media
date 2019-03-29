package com.tokopedia.topchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.factory.MessageFactory;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import java.util.HashMap;

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
    public Observable<InboxChatViewModel> getMessage(HashMap<String, Object> mapParam) {
        return messageFactory.createCloudMessageDataSource().getMessage(mapParam);
    }

    @Override
    public Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters) {
        return messageFactory.createCloudMessageDataSource().deleteMessage(parameters);
    }

}
