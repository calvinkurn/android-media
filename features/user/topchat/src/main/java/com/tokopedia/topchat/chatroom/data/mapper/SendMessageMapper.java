package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.topchat.chatroom.domain.pojo.SendMessagePojo;
import com.tokopedia.topchat.chatroom.view.viewmodel.SendMessageViewModel;

import javax.inject.Inject;

/**
 * @author by nisie on 10/25/17.
 */

public class SendMessageMapper extends BaseChatApiCallMapper<SendMessagePojo,SendMessageViewModel> {

    @Inject
    public SendMessageMapper() {
    }

    @Override
    SendMessageViewModel mappingToDomain(SendMessagePojo data) {
        return new SendMessageViewModel(data.isSuccess());
    }
}
