package com.tokopedia.groupchat.chatroom.domain.source;

import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.chatroom.domain.mapper.ChannelInfoMapper;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoSource {

    private ChatroomApi chatroomApi;
    private ChannelInfoMapper mapper;

    @Inject
    public ChannelInfoSource(ChatroomApi chatroomApi, ChannelInfoMapper mapper) {
        this.chatroomApi = chatroomApi;
        this.mapper = mapper;
    }

    public Observable<ChannelInfoViewModel> getChannelInfo(String channelUuid,
                                                           HashMap<String, Object> requestParam) {
        return chatroomApi.getChannelInfo(channelUuid, requestParam)
                .map(mapper);
    }
}
