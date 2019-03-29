package com.tokopedia.gm.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.data.source.cloud.model.GMGetCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;
import com.tokopedia.gm.common.data.source.cloud.model.RequestGetCashbackModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class GMCommonCloudDataSource {

    private GMCommonApi gmCommonApi;

    @Inject
    public GMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        this.gmCommonApi = gmCommonApi;
    }

    public Observable<Response<DataResponse<List<GMFeaturedProduct>>>> getFeaturedProductList(String shopId) {
        return gmCommonApi.getFeaturedProductList(shopId);
    }

    public Observable<Response<DataResponse<String>>> setCashback(RequestCashbackModel requestCashbackModel) {
        return gmCommonApi.setCashback(requestCashbackModel);
    }

    public Observable<Response<DataResponse<List<GMGetCashbackModel>>>> getCashbackList(RequestGetCashbackModel requestGetCashbackModel) {
        return gmCommonApi.getCashbackList(requestGetCashbackModel);
    }
}
