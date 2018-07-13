package com.tokopedia.topchat.chattemplate.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudSetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudSetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<GetTemplateViewModel> setTemplate(JsonObject parameters) {
        return chatService.getApi().setTemplate(parameters).map(templateChatMapper);
    }
}
