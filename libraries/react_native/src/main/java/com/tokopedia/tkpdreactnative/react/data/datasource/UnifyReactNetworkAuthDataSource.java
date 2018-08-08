package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.net.Uri;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkAuthDataSource {
    private Retrofit.Builder retrofit;
    private String token;

    public UnifyReactNetworkAuthDataSource(Retrofit.Builder retrofit) {
        this.retrofit = retrofit;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(getOkHttpClient(configuration))
                .build().create(CommonService.class);
        return requestToNetwork(configuration, commonService);
    }

    private OkHttpClient getOkHttpClient(ReactNetworkingConfiguration configuration) {
        if(configuration.getUrl().contains("pulsa-api")) {
            return OkHttpFactory.create().buildClientDigitalAuth(TkpdBaseURL.DigitalApi.HMAC_KEY).newBuilder().build();
        }
        return OkHttpFactory.create().buildClientReactNativeAuth(configuration.getHeaders()).newBuilder().build();
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
