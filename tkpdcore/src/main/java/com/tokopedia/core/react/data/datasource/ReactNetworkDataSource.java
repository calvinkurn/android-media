package com.tokopedia.core.react.data.datasource;

import com.tokopedia.core.base.common.service.CommonService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
        return commonService.get(url, params);
    }

    public Observable<String> post(String url, TKPDMapParam<String, String> params) {
        return commonService.post(url, params);
    }

    public Observable<String> postNoParam(String url) {
        return commonService.post(url);
    }
}
