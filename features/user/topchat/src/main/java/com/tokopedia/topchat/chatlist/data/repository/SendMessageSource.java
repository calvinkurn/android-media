package com.tokopedia.topchat.chatlist.data.repository;

import com.tokopedia.topchat.chatroom.data.mapper.SendMessageMapper;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;
import com.tokopedia.topchat.common.chat.ChatService;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageSource {
    private ChatService chatService;
    private SendMessageMapper sendMessageMapper;

    @Inject
    public SendMessageSource(ChatService chatService, SendMessageMapper sendMessageMapper) {
        this.chatService = chatService;
        this.sendMessageMapper = sendMessageMapper;
    }

    public Observable<SendMessageViewModel> sendMessage(HashMap<String, Object> requestParams) {
        return chatService.getApi()
                .sendMessage(requestParams)
                .map(sendMessageMapper);
    }
}
