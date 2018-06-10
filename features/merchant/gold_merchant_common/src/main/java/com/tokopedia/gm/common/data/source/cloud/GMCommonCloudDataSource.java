package com.tokopedia.gm.common.data.source.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.gm.common.data.source.cloud.api.GMCommonApi;
import com.tokopedia.gm.common.data.source.cloud.model.GMFeaturedProduct;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

/**
 * @author hendry on 4/4/17.
 */

public class GMCommonCloudDataSource {

    private GMCommonApi gmCommonApi;

    public GMCommonCloudDataSource(GMCommonApi gmCommonApi) {
        this.gmCommonApi = gmCommonApi;
    }

    public Observable<Response<DataResponse<List<GMFeaturedProduct>>>> getFeaturedProductList(String shopId) {
        return gmCommonApi.getFeaturedProductList(shopId);
    }
}
