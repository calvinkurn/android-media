package com.tokopedia.topchat.chatroom.data.source;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatroom.data.mapper.ChatEventMapper;
import com.tokopedia.topchat.chatroom.domain.pojo.websocket.ChatSocketData;

import rx.Observable;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class CloudListenWebSocketDataSource {

    private ChatEventMapper chatEventMapper;
    private ChatService chatService;

    public CloudListenWebSocketDataSource(ChatService chatService, ChatEventMapper chatEventMapper) {
        this.chatService = chatService;
        this.chatEventMapper = chatEventMapper;
    }

    public Observable<ChatSocketData> listen(TKPDMapParam<String, Object> requestParams) {
        return chatService.getApi().listenWebSocket(AuthUtil.generateParamsNetwork2(MainApplication.getAppContext(), requestParams)).map(chatEventMapper);
    }


}
