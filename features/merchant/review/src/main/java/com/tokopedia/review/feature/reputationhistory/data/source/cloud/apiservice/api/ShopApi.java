package com.tokopedia.review.feature.reputationhistory.data.source.cloud.apiservice.api;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.network.data.model.response.DataResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Hendry on 4/20/2017.
 */

public interface ShopApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Shop.PATH_SHOP + TkpdBaseURL.Shop.PATH_GET_SHOP_INFO)
    Observable<Response<DataResponse<ShopModel>>> getShopInfo(@FieldMap Map<String, String> params);
}
