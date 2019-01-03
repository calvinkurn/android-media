package com.tokopedia.topchat.chattemplate.data.source;

import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.view.viewmodel.GetTemplateViewModel;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class CloudGetTemplateChatDataSource {

    private TemplateChatMapper templateChatMapper;
    private ChatApi chatApi;

    public CloudGetTemplateChatDataSource(TemplateChatMapper templateChatMapper, ChatApi
            chatApi) {
        this.templateChatMapper = templateChatMapper;
        this.chatApi = chatApi;
    }

    public Observable<GetTemplateViewModel> getTemplate(HashMap<String, Object> parameters) {
        return chatApi.getTemplate(parameters).map(templateChatMapper);
    }
}
