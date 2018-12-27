package com.tokopedia.topchat.chatlist.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.data.mapper.GetMessageMapper;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.common.chat.ChatService;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudMessageDataSource {

    private ChatService chatService;
    private GetMessageMapper getMessageMapper;
    private DeleteMessageMapper deleteMessageMapper;

    public CloudMessageDataSource(ChatService chatService, GetMessageMapper getMessageMapper, DeleteMessageMapper deleteMessageMapper) {
        this.chatService = chatService;
        this.getMessageMapper = getMessageMapper;
        this.deleteMessageMapper = deleteMessageMapper;
    }

    public Observable<InboxChatViewModel> getMessage(HashMap<String, Object> requestParams) {
        return chatService.getApi().getMessage(requestParams).map(getMessageMapper);
    }

    public Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters) {
        return chatService.getApi().deleteMessage(parameters).map(deleteMessageMapper);
    }
}
