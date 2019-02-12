package com.tokopedia.topchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<InboxChatViewModel> getMessage(HashMap<String, Object> requestParams);

    Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters);

}
