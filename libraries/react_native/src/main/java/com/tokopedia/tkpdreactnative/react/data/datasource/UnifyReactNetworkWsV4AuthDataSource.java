package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.net.Uri;

import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.common.data.service.CommonService;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.user.session.UserSessionInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkWsV4AuthDataSource {

    private Retrofit.Builder retrofit;
    private OkHttpClient okHttpClient;
    private UserSessionInterface userSessionInterface;

    public UnifyReactNetworkWsV4AuthDataSource(Retrofit.Builder retrofit,
                                               OkHttpClient okHttpClient,
                                               UserSessionInterface userSessionInterface) {
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
        this.userSessionInterface = userSessionInterface;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(okHttpClient)
                .build().create(CommonService.class);
        return requestToNetwork(configuration, commonService);
    }

    private Observable<String> requestToNetwork(ReactNetworkingConfiguration configuration, CommonService commonService) {
        switch (configuration.getMethod()) {
            case ReactConst.GET:
                if (configuration.getParams().size() == 0) return commonService.get(configuration.getUrl());
                else return commonService.get(
                        configuration.getUrl(),
                        AuthHelper.generateParamsNetwork(userSessionInterface.getUserId(),
                                userSessionInterface.getDeviceId(),
                                configuration.getParams()));
            case ReactConst.POST:
                if (configuration.getParams().size() == 0) return commonService.post(configuration.getUrl());
                return commonService.post(configuration.getUrl(), AuthHelper.generateParamsNetwork(
                        userSessionInterface.getUserId(),
                        userSessionInterface.getDeviceId(),
                        configuration.getParams()));
            default:
                return commonService.get(configuration.getUrl(), configuration.getParams());
        }
    }


}
