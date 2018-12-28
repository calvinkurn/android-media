package com.tokopedia.topchat.chattemplate.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.common.chat.ChatService;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class CloudEditTemplateChatDataSource {

    private EditTemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public CloudEditTemplateChatDataSource(EditTemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public Observable<EditTemplateViewModel> editTemplate(int index, HashMap<String, Object> parameters) {
        return chatService.getApi().editTemplate(index, parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> createTemplate(HashMap<String, Object> parameters) {
        return chatService.getApi().createTemplate(parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> deleteTemplate(int index) {
        return chatService.getApi().deleteTemplate(index, new JsonObject()).map(templateChatMapper);
    }
}
