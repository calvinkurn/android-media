package com.tokopedia.notifications.domain;

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

    private RequestParams requestParams = RequestParams.create();

    private static final String USER_ID = "userid";
    private static final String SOURCE = "source";
    private static final String FCM_TOKEN = "fcm_token";
    private static final String APP_ID = "appId";
    private static final String SDK_VERSION = "sdkVersion";
    private static final String APP_VERSION = "appVersion";
    private static final String REQUEST_TIMESTAMP = "requesttimestamp";

    private static final String SOURCE_ANDROID = "ANDROID";



    public UpdateFcmTokenUseCase() {
    }

    public void createRequestParams(String userId, String token, int sdkVersion, String appId ,int appVersion) {
        requestParams.putString(USER_ID, userId);
       // requestParams.putString("authtoken", accessToken);
        requestParams.putString(SOURCE, SOURCE_ANDROID);
        requestParams.putString(FCM_TOKEN, token);
        requestParams.putString(APP_ID, appId);
       // requestParams.putString("identifier", gAdsId);
        requestParams.putInt(SDK_VERSION, sdkVersion);
        requestParams.putInt(APP_VERSION, appVersion);
       // requestParams.putString("state", appId);

        requestParams.putString(REQUEST_TIMESTAMP, CMNotificationUtils.getCurrentLocalTimeStamp());
    }

    @Override
    protected List<RestRequest> buildRequest() {
        List<RestRequest> tempRequest = new ArrayList<>();

        RestRequest restRequest1 = new RestRequest.Builder(CMNotificationUrls.CM_TOKEN_UPDATE, String.class)
                .setBody(requestParams.getParameters())
                .setRequestType(RequestType.POST)
                .build();
        tempRequest.add(restRequest1);

        return tempRequest;
    }


}
