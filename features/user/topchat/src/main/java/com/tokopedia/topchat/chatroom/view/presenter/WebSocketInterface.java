package com.tokopedia.topchat.chatroom.view.presenter;

import com.tokopedia.topchat.chatroom.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.topchat.chatroom.view.viewmodel.BaseChatViewModel;

/**
 * Created by stevenfredian on 9/22/17.
 */

public interface WebSocketInterface {
    void onIncomingEvent(WebSocketResponse response);

    void onErrorWebSocket();

    void onOpenWebSocket();

    void onReceiveMessage(BaseChatViewModel message);
}
