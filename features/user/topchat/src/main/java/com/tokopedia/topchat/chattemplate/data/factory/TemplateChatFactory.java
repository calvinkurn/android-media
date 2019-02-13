package com.tokopedia.topchat.chattemplate.data.factory;

import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.source.CloudGetTemplateChatDataSource;
import com.tokopedia.topchat.chattemplate.data.source.CloudSetTemplateChatDataSource;
import com.tokopedia.topchat.common.chat.api.ChatApi;

import javax.inject.Inject;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateChatFactory {

    private TemplateChatMapper templateChatMapper;
    private ChatApi chatApi;

    @Inject
    public TemplateChatFactory(TemplateChatMapper templateChatMapper, ChatApi chatApi) {
        this.templateChatMapper = templateChatMapper;
        this.chatApi = chatApi;
    }

    public CloudGetTemplateChatDataSource createCloudGetTemplateDataSource() {
        return new CloudGetTemplateChatDataSource(templateChatMapper, chatApi);
    }

    public CloudSetTemplateChatDataSource createCloudSetTemplateDataSource() {
        return new CloudSetTemplateChatDataSource(templateChatMapper, chatApi);
    }
}
