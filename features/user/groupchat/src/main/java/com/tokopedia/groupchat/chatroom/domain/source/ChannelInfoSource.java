package com.tokopedia.groupchat.chatroom.domain.source;

import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.chatroom.domain.mapper.ChannelInfoMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.DynamicButtonsMapper;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoSource {

    private ChatroomApi chatroomApi;
    private ChannelInfoMapper mapper;
    private DynamicButtonsMapper dynamicButtonsMapper;

    @Inject
    public ChannelInfoSource(ChatroomApi chatroomApi, ChannelInfoMapper mapper,
                             DynamicButtonsMapper dynamicButtonsMapper) {
        this.chatroomApi = chatroomApi;
        this.mapper = mapper;
        this.dynamicButtonsMapper = dynamicButtonsMapper;
    }

    public Observable<ChannelInfoViewModel> getChannelInfo(String channelUuid,
                                                           HashMap<String, Object> requestParam) {
        return chatroomApi.getChannelInfo(channelUuid, requestParam)
                .map(mapper);
    }

    public Observable<DynamicButtonsViewModel> getDynamicButtons(String channelUuid,
                                                                 HashMap<String, Object> requestParam) {
        return chatroomApi.getDynamicButtons(channelUuid, requestParam)
                .map(dynamicButtonsMapper);
    }
}
