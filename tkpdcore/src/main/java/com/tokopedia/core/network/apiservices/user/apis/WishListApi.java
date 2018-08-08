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
 * @author Angga.Prasetiyo on 03/12/2015.
 */
public interface WishListApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_IS_ALREADY_WISHLIST_PRODUCT)
    Observable<Response<TkpdResponse>> check(@FieldMap Map<String, String> params);
}
