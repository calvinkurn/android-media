package com.tokopedia.tkpdreactnative.react.data.datasource;

import android.content.Context;
import android.net.Uri;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpdreactnative.react.ReactConst;
import com.tokopedia.tkpdreactnative.react.domain.ReactNetworkingConfiguration;

import retrofit2.Retrofit;
import rx.Observable;

/**
 * Created by alvarisi on 10/9/17.
 */

public class UnifyReactNetworkWsV4AuthDataSource {
    private Retrofit.Builder retrofit;
    private Context context;

    public UnifyReactNetworkWsV4AuthDataSource(Retrofit.Builder retrofit, Context context) {
        this.retrofit = retrofit;
        this.context = context;
    }

    public Observable<String> request(ReactNetworkingConfiguration configuration) {

        Uri uri = Uri.parse(configuration.getUrl());
        CommonService commonService = retrofit.baseUrl(uri.getScheme() + "://" + uri.getHost())
                .client(OkHttpFactory.create().buildClientDefaultAuth())
                .build().create(CommonService.class);
        return requestToNetwork(configuration, commonService);
    }

    private Observable<String> requestToNetwork(ReactNetworkingConfiguration configuration, CommonService commonService) {
        switch (configuration.getMethod()) {
            case ReactConst.GET:
                if (configuration.getParams().size() == 0) return commonService.get(configuration.getUrl());
                else return commonService.get(configuration.getUrl(), AuthUtil.generateParamsNetwork(context, configuration.getParams()));
            case ReactConst.POST:
                if (configuration.getParams().size() == 0) return commonService.post(configuration.getUrl());
                return commonService.post(configuration.getUrl(), AuthUtil.generateParamsNetwork(context, configuration.getParams()));
            default:
                return commonService.get(configuration.getUrl(), configuration.getParams());
        }
    }


}
