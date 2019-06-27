package com.tokopedia.topchat.chatlist.presenter;

import com.tokopedia.topchat.chatlist.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatlist.viewmodel.BaseChatViewModel;

/**
 * Created by stevenfredian on 9/22/17.
 */
@Deprecated
//To be converted to websocket library
public interface WebSocketInterface {
    void onIncomingEvent(WebSocketResponse response);

    void onErrorWebSocket();

    void onOpenWebSocket();

    void onReceiveMessage(BaseChatViewModel message);
}
