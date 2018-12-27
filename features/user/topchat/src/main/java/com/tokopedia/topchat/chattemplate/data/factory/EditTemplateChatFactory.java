package com.tokopedia.topchat.chattemplate.data.factory;

import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.source.CloudEditTemplateChatDataSource;
import com.tokopedia.topchat.common.chat.ChatService;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatFactory {

    private EditTemplateChatMapper templateChatMapper;
    private ChatService chatService;

    public EditTemplateChatFactory(EditTemplateChatMapper templateChatMapper, ChatService chatService) {
        this.templateChatMapper = templateChatMapper;
        this.chatService = chatService;
    }

    public CloudEditTemplateChatDataSource createCloudEditTemplateDataSource() {
        return new CloudEditTemplateChatDataSource(templateChatMapper, chatService);
    }
}
