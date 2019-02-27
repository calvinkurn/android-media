package com.tokopedia.groupchat.chatroom.data;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.groupchat.chatroom.domain.pojo.ButtonsPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.StickyComponentPojo;
import com.tokopedia.groupchat.chatroom.domain.pojo.channelinfo.ChannelInfoPojo;

import java.util.HashMap;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author by nisie on 2/23/18.
 */

public interface ChatroomApi {

    @GET(ChatroomUrl.GET_CHANNEL_INFO)
    Observable<Response<DataResponse<ChannelInfoPojo>>> getChannelInfo(
            @Path(ChatroomUrl.PATH_CHANNEL_UUID) String channelUuid,
            @QueryMap HashMap<String, Object> requestParam);

    @GET(ChatroomUrl.GET_DYNAMIC_BUTTONS)
    Observable<Response<com.tokopedia.network.data.model.response.DataResponse<ButtonsPojo>>> getDynamicButtons(
            @Path(ChatroomUrl.PATH_CHANNEL_UUID) String channelUuid,
            @QueryMap HashMap<String, Object> requestParam);

    @GET(ChatroomUrl.GET_STICKY_COMPONENTS)
    Observable<Response<com.tokopedia.network.data.model.response.DataResponse<StickyComponentPojo>>> getStickyComponent(
            @Path(ChatroomUrl.PATH_CHANNEL_UUID) String channelUuid,
            @QueryMap HashMap<String, Object> requestParam);

}
