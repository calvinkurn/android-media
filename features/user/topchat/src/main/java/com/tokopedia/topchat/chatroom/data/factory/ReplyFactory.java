package com.tokopedia.topchat.chatroom.data.factory;

import com.tokopedia.topchat.chatroom.data.mapper.GetExistingChatMapper;
import com.tokopedia.topchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.topchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.topchat.chatroom.data.source.CloudReplyActionDataSource;
import com.tokopedia.topchat.chatroom.data.source.CloudReplyDataSource;
import com.tokopedia.topchat.chatroom.data.mapper.GetReplyMapper;
import com.tokopedia.topchat.chatroom.data.mapper.ReplyMessageMapper;
import com.tokopedia.topchat.chatroom.data.source.CloudReplyActionDataSource;
import com.tokopedia.topchat.chatroom.data.source.CloudReplyDataSource;
import com.tokopedia.topchat.chatroom.data.source.CloudGetExistingChatDataSource;
import com.tokopedia.topchat.common.chat.ChatService;

/**
 * Created by stevenfredian on 8/31/17.
 */

public class ReplyFactory {

    private GetReplyMapper getReplyMapper;
    private ReplyMessageMapper replyMessageMapper;
    private GetExistingChatMapper getExistingChatMapper;
    private ChatService chatService;

    public ReplyFactory(ChatService chatService, GetReplyMapper getReplyMapper,
                        ReplyMessageMapper replyMessageMapper, GetExistingChatMapper getExistingChatMapper){
        this.chatService = chatService;
        this.getReplyMapper = getReplyMapper;
        this.replyMessageMapper = replyMessageMapper;
        this.getExistingChatMapper = getExistingChatMapper;
    }

    public CloudReplyDataSource createCloudReplyDataSource() {
        return new CloudReplyDataSource(chatService, getReplyMapper);
    }

    public CloudReplyActionDataSource createCloudReplyActionDataSource(){
        return new CloudReplyActionDataSource(chatService, replyMessageMapper);
    }

    public CloudGetExistingChatDataSource createCloudGetExistingChatDataSource(){
        return new CloudGetExistingChatDataSource(chatService,getExistingChatMapper);
    }
}
