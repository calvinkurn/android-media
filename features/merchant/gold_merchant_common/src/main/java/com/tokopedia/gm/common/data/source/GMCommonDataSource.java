package com.tokopedia.gm.common.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.network.mapper.DataResponseMapper;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;
import com.tokopedia.gm.common.data.source.cloud.model.RequestCashbackModel;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class GMCommonDataSource {
    private GMCommonCloudDataSource gmCommonCloudDataSource;

    @Inject
    public GMCommonDataSource(GMCommonCloudDataSource gmCommonCloudDataSource) {
        this.gmCommonCloudDataSource = gmCommonCloudDataSource;
    }

    public Observable<List<GMFeaturedProduct>> getFeaturedProductList(String shopId) {
        return gmCommonCloudDataSource.getFeaturedProductList(shopId).flatMap(new Func1<Response<DataResponse<List<GMFeaturedProduct>>>, Observable<List<GMFeaturedProduct>>>() {
            @Override
            public Observable<List<GMFeaturedProduct>> call(Response<DataResponse<List<GMFeaturedProduct>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    public Observable<Boolean> setCashback(String productId, int cashback) {
        return gmCommonCloudDataSource.setCashback(new RequestCashbackModel(Long.parseLong(productId), cashback))
                .map(new DataResponseMapper<String>())
                .map(new Func1<String, Boolean>() {
                    @Override
                    public Boolean call(String s) {
                        return s != null;
                    }
                });
    }
}
