package com.tokopedia.topchat.chatroom.data.source;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.topchat.chatroom.data.mapper.GetExistingChatMapper;
import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;

import rx.Observable;


/**
 * Created by Hendri on 07/06/18.
 */
public class CloudGetExistingChatDataSource {
    private GetExistingChatMapper getExistingChatMapper;
    private ChatService chatService;

    public CloudGetExistingChatDataSource(ChatService chatService,
                                          GetExistingChatMapper getExistingChatMapper) {
        this.getExistingChatMapper = getExistingChatMapper;
        this.chatService = chatService;
    }

    public Observable<ExistingChatPojo> getExistingChat(TKPDMapParam<String, Object> parameters){
        return chatService.getApi().getExistingChat(parameters)
                .map(getExistingChatMapper);
    }
}
