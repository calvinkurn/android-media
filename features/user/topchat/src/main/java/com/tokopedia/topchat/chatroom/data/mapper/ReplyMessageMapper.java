package com.tokopedia.topchat.chatroom.data.mapper;

import com.tokopedia.topchat.chatroom.domain.pojo.replyaction.ReplyActionData;

/**
 * Created by stevenfredian on 8/31/17.as
 */

public class ReplyMessageMapper extends BaseChatApiCallMapper<ReplyActionData,ReplyActionData> {
    @Override
    ReplyActionData mappingToDomain(ReplyActionData data) {
        return data;
    }
}