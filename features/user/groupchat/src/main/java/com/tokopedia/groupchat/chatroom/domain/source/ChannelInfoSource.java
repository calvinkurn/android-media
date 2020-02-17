package com.tokopedia.groupchat.chatroom.domain.source;

import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.chatroom.domain.mapper.ChannelInfoMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.DynamicButtonsMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.StickyComponentMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.VideoStreamMapper;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentsViewModel;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    private StickyComponentMapper stickyComponentMapper;
    private VideoStreamMapper videoStreamMapper;

    @Inject
    public ChannelInfoSource(ChatroomApi chatroomApi,
                             ChannelInfoMapper mapper,
                             DynamicButtonsMapper dynamicButtonsMapper,
                             StickyComponentMapper stickyComponentMapper,
                             VideoStreamMapper videoStreamMapper) {
        this.chatroomApi = chatroomApi;
        this.mapper = mapper;
        this.dynamicButtonsMapper = dynamicButtonsMapper;
        this.stickyComponentMapper = stickyComponentMapper;
        this.videoStreamMapper = videoStreamMapper;
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

    public Observable<StickyComponentsViewModel> getStickyComponent(String channelUuid,
                                                                    HashMap<String, Object> requestParam) {
        return chatroomApi.getStickyComponent(channelUuid, requestParam)
                .map(stickyComponentMapper);
    }


    public Observable<VideoStreamViewModel> getVideoStream(@Nullable String channelUuid, @NotNull HashMap<String, Object> requestParams) {
        return chatroomApi.getVideoStream(channelUuid, requestParams)
                .map(videoStreamMapper);
    }
}
