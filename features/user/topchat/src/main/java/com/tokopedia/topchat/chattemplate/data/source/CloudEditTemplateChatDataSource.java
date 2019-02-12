package com.tokopedia.topchat.chattemplate.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.EditTemplateViewModel;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 12/27/17.
 */

public class CloudEditTemplateChatDataSource {

    private EditTemplateChatMapper templateChatMapper;
    private ChatApi chatApi;

    public CloudEditTemplateChatDataSource(EditTemplateChatMapper templateChatMapper, ChatApi
            chatApi) {
        this.templateChatMapper = templateChatMapper;
        this.chatApi = chatApi;
    }

    public Observable<EditTemplateViewModel> editTemplate(int index, HashMap<String, Object> parameters) {
        return chatApi.editTemplate(index, parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> createTemplate(HashMap<String, Object> parameters) {
        return chatApi.createTemplate(parameters).map(templateChatMapper);
    }

    public Observable<EditTemplateViewModel> deleteTemplate(int index) {
        return chatApi.deleteTemplate(index, new JsonObject()).map(templateChatMapper);
    }
}
