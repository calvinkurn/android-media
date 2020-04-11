package com.tokopedia.groupchat.channel.domain.source;

import com.tokopedia.groupchat.channel.domain.mapper.ChannelMapper;
import com.tokopedia.groupchat.channel.view.model.ChannelListViewModel;
import com.tokopedia.groupchat.channel.data.ChannelApi;
import com.tokopedia.groupchat.common.di.scope.GroupChatScope;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/3/18.
 */

public class ChannelSource {

    ChannelApi chatApi;
    ChannelMapper channelMapper;

    @Inject
    public ChannelSource(@GroupChatScope ChannelApi chatApi,
                         ChannelMapper channelMapper) {
        this.chatApi = chatApi;
        this.channelMapper = channelMapper;
    }

    public Observable<ChannelListViewModel> getChannels(HashMap<String, Object> parameters) {
        return chatApi.getAllChannel(parameters)
                .map(channelMapper);
    }
}
