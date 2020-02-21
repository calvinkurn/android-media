package com.tokopedia.topchat.chatlist.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.data.mapper.DeleteMessageMapper;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudMessageDataSource {

    private ChatApi chatApi;
    private DeleteMessageMapper deleteMessageMapper;

    public CloudMessageDataSource(ChatApi chatApi,
                                  DeleteMessageMapper deleteMessageMapper) {
        this.chatApi = chatApi;
        this.deleteMessageMapper = deleteMessageMapper;
    }

    public Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters) {
        return chatApi.deleteMessage(parameters).map(deleteMessageMapper);
    }
}
