package com.tokopedia.topchat.chattemplate.data.factory;

import com.tokopedia.core.network.apiservices.chat.ChatService;
import com.tokopedia.topchat.chattemplate.data.mapper.TemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.source.CloudGetTemplateChatDataSource;
import com.tokopedia.topchat.chattemplate.data.source.CloudSetTemplateChatDataSource;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class TemplateChatFactory {

    private TemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public TemplateChatFactory(TemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public CloudGetTemplateChatDataSource createCloudGetTemplateDataSource() {
        return new CloudGetTemplateChatDataSource(templateChatMapper, chatService);
    }

    public CloudSetTemplateChatDataSource createCloudSetTemplateDataSource() {
        return new CloudSetTemplateChatDataSource(templateChatMapper, chatService);
    }
}
