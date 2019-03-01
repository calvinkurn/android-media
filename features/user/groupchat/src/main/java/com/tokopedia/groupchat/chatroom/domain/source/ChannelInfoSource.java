package com.tokopedia.groupchat.chatroom.domain.source;

import android.content.Context;
import android.content.SharedPreferences;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.groupchat.chatroom.data.ChatroomApi;
import com.tokopedia.groupchat.chatroom.domain.mapper.ChannelInfoMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.DynamicButtonsMapper;
import com.tokopedia.groupchat.chatroom.domain.mapper.StickyComponentMapper;
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel;
import com.tokopedia.groupchat.common.di.qualifier.GCPQualifier;
import com.tokopedia.groupchat.room.view.activity.PlayActivity;
import com.tokopedia.groupchat.room.view.fragment.PlayFragment;
import com.tokopedia.groupchat.room.view.viewmodel.DynamicButtonsViewModel;
import com.tokopedia.groupchat.room.view.viewmodel.pinned.StickyComponentViewModel;

import java.util.HashMap;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author by nisie on 2/22/18.
 */

public class ChannelInfoSource {

    private final Context context;
    private ChatroomApi chatroomApi;
    private ChatroomApi gcpApi;
    private ChannelInfoMapper mapper;
    private DynamicButtonsMapper dynamicButtonsMapper;
    private StickyComponentMapper stickyComponentMapper;
    private SharedPreferences networkSharedPreferences;

    @Inject
    public ChannelInfoSource(@ApplicationContext Context context,
                             ChatroomApi chatroomApi,
                             @GCPQualifier ChatroomApi gcpApi,
                             ChannelInfoMapper mapper,
                             DynamicButtonsMapper dynamicButtonsMapper,
                             StickyComponentMapper stickyComponentMapper) {
        this.context = context;
        this.gcpApi = gcpApi;
        this.chatroomApi = chatroomApi;
        this.mapper = mapper;
        this.dynamicButtonsMapper = dynamicButtonsMapper;
        this.stickyComponentMapper = stickyComponentMapper;
    }

    public Observable<ChannelInfoViewModel> getChannelInfo(String channelUuid,
                                                           HashMap<String, Object> requestParam) {
        networkSharedPreferences = context.getSharedPreferences(PlayFragment.GROUP_CHAT_NETWORK_PREFERENCES,
                Context.MODE_PRIVATE);
       if(networkSharedPreferences.getBoolean(PlayActivity.EXTRA_USE_GCP, false)) {
           return gcpApi.getChannelInfo(channelUuid, requestParam)
                   .map(mapper);
       }else{
           return chatroomApi.getChannelInfo(channelUuid, requestParam)
                   .map(mapper);
       }

    }

    public Observable<DynamicButtonsViewModel> getDynamicButtons(String channelUuid,
                                                                 HashMap<String, Object> requestParam) {
        networkSharedPreferences = context.getSharedPreferences(PlayFragment.GROUP_CHAT_NETWORK_PREFERENCES,
                Context.MODE_PRIVATE);
        if(networkSharedPreferences.getBoolean(PlayActivity.EXTRA_USE_GCP, false)) {
            return gcpApi.getDynamicButtons(channelUuid, requestParam)
                    .map(dynamicButtonsMapper);
        }else{
            return chatroomApi.getDynamicButtons(channelUuid, requestParam)
                    .map(dynamicButtonsMapper);
        }
    }

    public Observable<StickyComponentViewModel> getStickyComponent(String channelUuid,
                                                                   HashMap<String, Object> requestParam) {
        networkSharedPreferences = context.getSharedPreferences(PlayFragment.GROUP_CHAT_NETWORK_PREFERENCES,
                Context.MODE_PRIVATE);
        if(networkSharedPreferences.getBoolean(PlayActivity.EXTRA_USE_GCP, false)) {
            return gcpApi.getStickyComponent(channelUuid, requestParam)
                    .map(stickyComponentMapper);
        }else{
            return chatroomApi.getStickyComponent(channelUuid, requestParam)
                    .map(stickyComponentMapper);
        }
    }
}
