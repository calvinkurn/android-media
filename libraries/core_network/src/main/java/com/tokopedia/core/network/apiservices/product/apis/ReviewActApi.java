package com.tokopedia.core.network.apiservices.product.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public interface ReviewActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_LIKE_DISLIKE_REVIEW)
    Observable<Response<TkpdResponse>> likeDislikeReview(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.Product.PATH_REPORT_REVIEW)
    Observable<Response<TkpdResponse>> reportReview(@FieldMap Map<String, String> params);
}
