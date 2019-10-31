package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.net.Uri;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.common.data.interceptor.ReactNativeBearerInterceptor;
import com.tokopedia.tkpdreactnative.react.common.data.service.CommonService;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;
import com.tokopedia.user.session.UserSessionInterface;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkBearerDataSource {
    private Retrofit.Builder retrofit;
    private OkHttpClient okHttpClient;
    private UserSessionInterface sessionInterface;

    public UnifyReactNetworkBearerDataSource(Retrofit.Builder retrofit,
                                             OkHttpClient okHttpClient,
                                             UserSessionInterface sessionInterface) {
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
        this.sessionInterface = sessionInterface;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {
        String token = sessionInterface.getAccessToken();

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(okHttpClient.newBuilder().addInterceptor(new ReactNativeBearerInterceptor(configuration.getHeaders(), token)).build())
                .build().create(CommonService.class);
        return requestToNetwork(configuration, commonService);
    }

    private Observable<String> requestToNetwork(ReactNetworkingConfiguration configuration, CommonService commonService) {
        switch (configuration.getMethod()) {
            case ReactConst.GET:
                return commonService.get(configuration.getUrl(), configuration.getParams());
            case ReactConst.POST:
                return commonService.post(configuration.getUrl(), configuration.getParams());
            case ReactConst.DELETE:
                return commonService.delete(configuration.getUrl(), configuration.getParams());
            case ReactConst.PUT:
                return commonService.put(configuration.getUrl(), configuration.getParams());
            case ReactConst.HEAD:
                return commonService.head(configuration.getUrl(), configuration.getParams());
            default:
                return commonService.get(configuration.getUrl(), configuration.getParams());
        }
    }
}
