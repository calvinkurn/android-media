package com.tokopedia.posapp.data.source.cloud.api.pos;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by okasurya on 10/13/17.
 */

public interface PosProductApi {

    String SHOP_ID = "shopId";
    String START_OFFSET = "startOffset";
    String ROW_OFFSET = "rowOffset";

    @GET(TkpdBaseURL.Pos.GET_PRODUCT_LIST)
    Observable<Response<TkpdResponse>> getProductList(@Path(SHOP_ID) String shopId,
                                                      @Path(START_OFFSET) int start,
                                                      @Path(ROW_OFFSET) int offset);

    @GET(TkpdBaseURL.Pos.GET_ETALASE)
    Observable<Response<TkpdResponse>> getEtalase(@Path(SHOP_ID) String shopId);

}
