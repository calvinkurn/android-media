package com.tokopedia.topchat.chattemplate.data.source;

import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.common.chat.ChatService;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudGetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudGetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<GetTemplateViewModel> getTemplate(HashMap<String, Object> parameters) {
        return chatService.getApi().getTemplate(parameters).map(templateChatMapper);
    }
}
