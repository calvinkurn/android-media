package com.tokopedia.notifications.domain;

import com.google.gson.Gson;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestUseCase;
import com.tokopedia.notifications.data.source.CMNotificationUrls;
import com.tokopedia.notifications.common.CMNotificationUtils;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ashwani Tyagi on 23/10/18.
 */
public class UpdateFcmTokenUseCase extends RestRequestUseCase {


    private static final String USER_ID = "userId";
    private static final String SOURCE = "source";
    private static final String FCM_TOKEN = "token";
    private static final String APP_ID = "appId";
    private static final String SDK_VERSION = "sdkVersion";
    private static final String APP_VERSION = "appVersion";
    private static final String REQUEST_TIMESTAMP = "requestTimeStamp";

    private static final String SOURCE_ANDROID = "android";
    private static final String USER_STATE = "state";


    public UpdateFcmTokenUseCase() {
    }

    public RequestParams createRequestParams(String userId, String token, int sdkVersion, String appId, String appVersionName,
                                             String loginStatus, long timeStamp) {
        RequestParams requestParams = RequestParams.create();

        if (userId != null && userId.length() > 0)
            requestParams.putInt(USER_ID, Integer.parseInt(userId));
        requestParams.putString(SOURCE, SOURCE_ANDROID);
        requestParams.putString(FCM_TOKEN, token);
        requestParams.putString(APP_ID, appId);
        requestParams.putString(SDK_VERSION, String.valueOf(sdkVersion));
        requestParams.putString(APP_VERSION, appVersionName);
        requestParams.putString(USER_STATE, loginStatus);
        requestParams.putLong(REQUEST_TIMESTAMP, timeStamp);
        return requestParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(CMNotificationUrls.CM_TOKEN_UPDATE, String.class)
                .setBody(new Gson().toJson(requestParams.getParameters()))
                .setRequestType(RequestType.POST)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }
}
