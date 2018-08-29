package com.tokopedia.posapp.shop.data.source.cloud;

import com.tokopedia.posapp.base.data.pojo.PosSimpleResponse;
import com.tokopedia.posapp.common.PosUrl;
import com.tokopedia.posapp.shop.data.pojo.ShopResponse;

import retrofit2.Response;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by okasurya on 8/4/17.
 */

public interface ShopApi {
    @GET(PosUrl.Shop.GET_SHOP_INFO_V1)
    Observable<Response<PosSimpleResponse<ShopResponse>>> getInfo();
}
