package com.tokopedia.topchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListViewModel;
import com.tokopedia.topchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<InboxChatViewModel> getMessage(TKPDMapParam<String, Object> requestParams);

    Observable<DeleteChatListViewModel> deleteMessage(JsonObject parameters);

    Observable<SendMessageViewModel> sendMessage(TKPDMapParam<String, Object> requestParams);
}
