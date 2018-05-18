package com.tokopedia.topchat.chatroom.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public interface ReplyRepository {

    Observable<ChatRoomViewModel> getReply(String id, TKPDMapParam<String, Object> requestParams);

    Observable<ReplyActionData> replyMessage(TKPDMapParam<String, Object> parameters);
}
