package com.tokopedia.shop.product.data.source.cloud.api;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.shop.common.constant.ShopUrl;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopOfficialStoreApi {

    @GET(ShopUrl.SHOP_PRODUCT_OS_DISCOUNT)
    Observable<Response<DataResponse<List<ShopProductCampaign>>>> getProductCampaigns(@Query("pid") String ids);
}
