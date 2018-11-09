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

    public UpdateFcmTokenUseCase() {
    }

    public void createRequestParams(String userId, String token, int sdkVersion, String appId ,int appVersion) {
        requestParams.putString("userid", userId);
       // requestParams.putString("authtoken", accessToken);
        requestParams.putString("source", "ANDROID");
        requestParams.putString("fcm_token", token);
        requestParams.putString("appId", appId);
       // requestParams.putString("identifier", gAdsId);
        requestParams.putInt("sdkVersion", sdkVersion);
        requestParams.putInt("appVersion", appVersion);
       // requestParams.putString("state", appId);

        requestParams.putString("requesttimestamp", CMNotificationUtils.getCurrentLocalTimeStamp());
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
