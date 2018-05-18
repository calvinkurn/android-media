package com.tokopedia.topchat.chatroom.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatroom.data.factory.ReplyFactory;
import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.topchat.chatroom.view.viewmodel.ChatRoomViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ReplyRepositoryImpl implements ReplyRepository{

    private ReplyFactory replyFactory;

    public ReplyRepositoryImpl(ReplyFactory replyFactory){
        this.replyFactory = replyFactory;
    }

    @Override
    public Observable<ChatRoomViewModel> getReply(String id, TKPDMapParam<String, Object> requestParams) {
        return replyFactory.createCloudReplyDataSource().getReply(id, requestParams);
    }

    @Override
    public Observable<ReplyActionData> replyMessage(TKPDMapParam<String, Object> parameters) {
        return replyFactory.createCloudReplyActionDataSource().replyMessage(parameters);
    }
}
