package com.tokopedia.topchat.chatroom.domain;

import com.tokopedia.topchat.chatroom.data.mapper.GetUserStatusMapper;
import com.tokopedia.topchat.chatroom.data.network.TopChatApi;
import com.tokopedia.topchat.chatroom.domain.pojo.getuserstatus.GetUserStatusDataPojo;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by Hendri on 31/07/18.
 */
public class GetUserStatusUseCase extends UseCase<GetUserStatusDataPojo> {
    private final TopChatApi topChatApi;
    private final GetUserStatusMapper getUserStatusMapper;
    private boolean isUser = true;

    public GetUserStatusUseCase(TopChatApi topChatApi, GetUserStatusMapper getUserStatusMapper) {
        this.topChatApi = topChatApi;
        this.getUserStatusMapper = getUserStatusMapper;
    }

    @Override
    public Observable<GetUserStatusDataPojo> createObservable(RequestParams requestParams) {
        if(isUser) {
            return topChatApi.getUserStatus(requestParams.getParamsAllValueInString()).map
                    (getUserStatusMapper);
        } else {
            return topChatApi.getShopStatus(requestParams.getParamsAllValueInString()).map
                    (getUserStatusMapper);
        }
    }

    public void setUser(boolean user) {
        isUser = user;
    }

    public static RequestParams getRequestParam(String id){

        String ID_QUERY_KEY = "id";
        String FORMAT_QUERY_KEY = "format";
        String FORMAT_VALUE_DEFAULT = "api";

        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ID_QUERY_KEY,id);
        requestParams.putString(FORMAT_QUERY_KEY,FORMAT_VALUE_DEFAULT);
        return requestParams;
    }
}
