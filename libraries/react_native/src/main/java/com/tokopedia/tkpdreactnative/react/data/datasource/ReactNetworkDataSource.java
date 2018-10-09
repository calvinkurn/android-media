package com.tokopedia.tkpdreactnative.react.data.datasource;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.io.IOException;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import rx.Observable;

/**
 * @author ricoharisin .
 */

public class ReactNetworkDataSource {

    private CommonService commonService;

    public ReactNetworkDataSource(CommonService commonService) {
        this.commonService = commonService;
    }

    public Observable<String> get(String url, TKPDMapParam<String, String> params) {
        if (params.size() == 0) return commonService.get(url);
        return commonService.get(url, params);
    }

    public Observable<String> getParam(String url, String params) {
        return commonService.getParam(url, params);
    }

    public Observable<String> post(String url, TKPDMapParam<String, String> params) {
        if (params.size() == 0) return commonService.post(url);
        return commonService.post(url, params);
    }

    public Observable<String> postJson(String url, String params) {
        return commonService.postJson(url, params);
    }

    public Observable<String> postParam(String url, String params) {
        return commonService.postParam(url, params);
    }

    public Observable<String> delete(String url) {
        return commonService.delete(url);
    }
}
