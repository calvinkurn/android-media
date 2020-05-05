package com.tokopedia.topchat.chatlist.data.repository;


import com.google.gson.JsonObject;
import com.tokopedia.topchat.chatlist.viewmodel.DeleteChatListUiModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface MessageRepository {

    Observable<DeleteChatListUiModel> deleteMessage(JsonObject parameters);

}
