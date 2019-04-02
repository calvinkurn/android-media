package com.tokopedia.home.account.domain;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common.network.data.model.RequestType;
import com.tokopedia.common.network.data.model.RestRequest;
import com.tokopedia.common.network.domain.RestRequestSupportInterceptorUseCase;
import com.tokopedia.home.account.constant.SettingConstant;
import com.tokopedia.home.account.data.model.SettingEditResponse;
import com.tokopedia.network.utils.AuthUtil;
import com.tokopedia.network.utils.TKPDMapParam;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSessionInterface;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import okhttp3.Interceptor;

public class SaveEmailNotifUseCase extends RestRequestSupportInterceptorUseCase{
    private HashMap<String, Integer> bodyParams = new HashMap<>();
    private UserSessionInterface userSession;

    @Inject public SaveEmailNotifUseCase(UserSessionInterface userSession, List<Interceptor> interceptors,
                                         @ApplicationContext Context context) {
        super(interceptors, context);
        this.userSession = userSession;
    }

    public void setBodyParams(HashMap<String, Integer> bodyParams) {
        this.bodyParams = bodyParams;
    }

    @Override
    protected List<RestRequest> buildRequest(RequestParams requestParams) {
        List<RestRequest> tempRequest = new ArrayList<>();
        String url = SettingConstant.PeopleUrl.BASE_URL+SettingConstant.PeopleUrl.PATH_EDIT_NOTIF_SETTING;

        Type token = new TypeToken<SettingEditResponse>() {}.getType();
        RestRequest restRequest1 = new RestRequest.Builder(url, token)
                .setRequestType(RequestType.POST)
                .setBody(getBodyParams())
                .build();
        tempRequest.add(restRequest1);
        return tempRequest;
    }

    private Map<String,String> getBodyParams(){
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        for (String key: bodyParams.keySet()){
            param.put(key, String.valueOf(bodyParams.get(key)));
        }

        return AuthUtil.generateParamsNetwork(userSession.getUserId(),
                userSession.getDeviceId(), param);
    }
}
