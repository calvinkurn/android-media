package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.topchat.chatroom.domain.pojo.SendMessagePojo;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageMapper extends BaseChatApiCallMapper<SendMessagePojo,SendMessageViewModel> {
    @Override
    SendMessageViewModel mappingToDomain(SendMessagePojo data) {
        return new SendMessageViewModel(data.isSuccess());
    }
}
