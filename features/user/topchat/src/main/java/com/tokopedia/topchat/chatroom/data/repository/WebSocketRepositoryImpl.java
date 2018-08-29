package com.tokopedia.topchat.chatroom.data.repository;


import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatroom.data.factory.ReplyFactory;
import com.tokopedia.topchat.chatroom.data.factory.WebSocketFactory;
import com.tokopedia.topchat.chatroom.domain.pojo.websocket.ChatSocketData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class WebSocketRepositoryImpl implements WebSocketRepository{

    private ReplyFactory replyFactory;
    private WebSocketFactory webSocketFactory;

    public WebSocketRepositoryImpl(WebSocketFactory webSocketFactory){
        this.webSocketFactory = webSocketFactory;
    }

    @Override
    public Observable<ChatSocketData> listen(TKPDMapParam<String, Object> requestParams) {
        return webSocketFactory.createCloudListWebSocketSource().listen(requestParams);
    }
}
