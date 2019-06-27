package com.tokopedia.home.account.domain;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.network.data.model.CacheType;
import com.tokopedia.common.network.data.model.RestCacheStrategy;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.common.network.util.RestConstant;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.data.model.AppNotificationSettingModel;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class GetEmailNotifUseCase extends RestRequestSupportInterceptorUseCase {
    private static final String PARAM_SESSION_ID = "profile_user_id";
    private static final String PARAM_DEVICE_TIME = "device_time";

    private UserSessionInterface userSession;
    private boolean forceRequest = false;

    @Inject public GetEmailNotifUseCase(UserSessionInterface userSession, List<Interceptor> interceptors,
                                        @ApplicationContext Context context) {
        super(interceptors, context);
        this.userSession = userSession;
    }

    public void setForceRequest(boolean forceRequest) {
        this.forceRequest = forceRequest;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        String url = SettingConstant.PeopleUrl.BASE_URL+SettingConstant.PeopleUrl.PATH_GET_NOTIF_SETTING;

        Type token = new TypeToken<DataResponse<AppNotificationSettingModel.Response>>() {}.getType();

        RestCacheStrategy cacheStrategy = new RestCacheStrategy.Builder(forceRequest ? CacheType.ALWAYS_CLOUD : CacheType.CACHE_FIRST)
                .setExpiryTime(RestConstant.HOUR_MS)
                .setSessionIncluded(true).build();

        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setQueryParams(getQueryParams())
                .setCacheStrategy(cacheStrategy)
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }

    private Map<String,Object> getQueryParams(){
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_SESSION_ID, userSession.getUserId());
        Map<String, String> tmp = AuthUtil.generateParamsNetwork(userSession.getUserId(),
                userSession.getDeviceId(), param);
        tmp.remove(PARAM_DEVICE_TIME);

        Map<String, Object> result = new HashMap<>();

        for (String key: tmp.keySet()){
            result.put(key, tmp.get(key));
        }

        return result;
    }


}
