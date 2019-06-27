package com.tokopedia.topchat.chattemplate.data.source;

import com.google.gson.JsonObject;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudSetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatApi chatApi;

    public CloudSetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatApi chatApi) {
        this.templateChatMapper = templateChatMapper;
        this.chatApi = chatApi;
    }

    public Observable<GetTemplateViewModel> setTemplate(JsonObject parameters) {
        return chatApi.setTemplate(parameters).map(templateChatMapper);
    }
}
