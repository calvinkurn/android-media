package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.topchat.chatroom.domain.pojo.existingchat.ExistingChatPojo;

/**
 * Created by Hendri on 07/06/18.
 */
public class GetExistingChatMapper extends BaseChatApiCallMapper<ExistingChatPojo,ExistingChatPojo> {

    @Override
    ExistingChatPojo mappingToDomain(ExistingChatPojo data) {
        return data;
    }
}
