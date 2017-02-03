package com.tokopedia.core.base.common.service;

import com.tokopedia.core.base.common.response.GetListFaveShopIdResponse;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Kulomady on 12/9/16.
 */

public interface ServiceV4 {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_LIST_FAVE_SHOP_ID)
    Observable<Response<GetListFaveShopIdResponse>> getListFaveShopId(
            @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_WISHLIST)
    Observable<Response<String>> getWishlist(@FieldMap TKPDMapParam<String, String> param);


    @FormUrlEncoded
    @POST(TkpdBaseURL.Etc.PATH_GET_FAVORITE_SHOP)
    Observable<Response<String>> getFavoriteShop(@FieldMap Map<String, String> params);
}
