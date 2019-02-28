package com.tokopedia.topchat.chattemplate.data.factory;

import com.tokopedia.topchat.chattemplate.data.mapper.EditTemplateChatMapper;
import com.tokopedia.topchat.chattemplate.data.source.CloudEditTemplateChatDataSource;
import com.tokopedia.topchat.common.chat.api.ChatApi;

/**
 * Created by stevenfredian on 11/27/17.
 */

public class EditTemplateChatFactory {

    private EditTemplateChatMapper templateChatMapper;
    private ChatApi chatApi;

    public EditTemplateChatFactory(EditTemplateChatMapper templateChatMapper, ChatApi chatApi) {
        this.templateChatMapper = templateChatMapper;
        this.chatApi = chatApi;
    }

    public CloudEditTemplateChatDataSource createCloudEditTemplateDataSource() {
        return new CloudEditTemplateChatDataSource(templateChatMapper, chatApi);
    }
}
