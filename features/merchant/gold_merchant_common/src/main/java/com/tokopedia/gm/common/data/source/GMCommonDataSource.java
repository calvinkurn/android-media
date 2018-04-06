package com.tokopedia.gm.common.data.source;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.data.source.cloud.GMCommonCloudDataSource;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;

import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 3/8/17.
 */
public class GMCommonDataSource {
    private GMCommonCloudDataSource GMInfoCloudDataSource;

    public GMCommonDataSource(GMCommonCloudDataSource GMInfoCloudDataSource) {
        this.GMInfoCloudDataSource = GMInfoCloudDataSource;
    }

    public Observable<List<GMFeaturedProduct>> getFeaturedProductList(String shopId) {
        return GMInfoCloudDataSource.getFeaturedProductList(shopId).flatMap(new Func1<Response<DataResponse<List<GMFeaturedProduct>>>, Observable<List<GMFeaturedProduct>>>() {
            @Override
            public Observable<List<GMFeaturedProduct>> call(Response<DataResponse<List<GMFeaturedProduct>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }
}
