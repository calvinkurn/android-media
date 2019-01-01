package com.tokopedia.topchat.chatroom.data.repository;


import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface ReplyRepository {

    Observable<ChatRoomViewModel> getReply(String id, HashMap<String, Object> requestParams);

    Observable<ReplyActionData> replyMessage(HashMap<String, Object> parameters);

    Observable<ExistingChatPojo> getExistingChat(HashMap<String, Object> parameters);
}
