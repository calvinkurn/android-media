package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.net.Uri;

import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.common.data.interceptor.ReactNativeInterceptor;
import com.tokopedia.tkpdreactnative.react.common.data.service.CommonService;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkDataSource {
    private Retrofit.Builder retrofit;
    private OkHttpClient okHttpClient;

    public UnifyReactNetworkDataSource(Retrofit.Builder retrofit, OkHttpClient okHttpClient) {
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(okHttpClient.newBuilder().addInterceptor(new ReactNativeInterceptor(configuration.getHeaders())).build())
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
