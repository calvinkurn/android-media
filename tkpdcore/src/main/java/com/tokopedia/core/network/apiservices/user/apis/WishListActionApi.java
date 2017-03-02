package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 04/12/2015.
 */
public interface WishListActionApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ADD_WISHLIST_PRODUCT)
    Observable<Response<TkpdResponse>> add(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_REMOVE_WISHLIST_PRODUCT)
    Observable<Response<TkpdResponse>> remove(@FieldMap Map<String, String> params);
}
