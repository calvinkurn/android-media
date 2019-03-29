package com.tokopedia.core.network.apiservices.user.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface InboxResCenterApi {

    @GET(TkpdBaseURL.User.PATH_GET_CREATE_RESOLUTION_FORM)
    Observable<Response<TkpdResponse>> getCreateResForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_CREATE_RESOLUTION_FORM_NEW)
    Observable<Response<TkpdResponse>> getCreateResFormNew(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_RES_CENTER_PRODUCT_LIST)
    Observable<Response<TkpdResponse>> getResCenterProductList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_SOLUTION)
    Observable<Response<TkpdResponse>> getSolutionList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_KURIR_LIST)
    Observable<Response<TkpdResponse>> getCourierList(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_RESOLUTION_CENTER)
    Observable<Response<TkpdResponse>> getResCenter(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_RESOLUTION_CENTER_DETAIL)
    Observable<Response<TkpdResponse>> getResCenterDetail(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_RESOLUTION_CENTER_SHOW_MORE)
    Observable<Response<TkpdResponse>> getResCenterMore(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_TRACK_SHIPPING_REF)
    Observable<Response<TkpdResponse>> trackShippingRef(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_EDIT_RESOLUTION_FORM)
    Observable<Response<TkpdResponse>> getEditResolutionForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_GET_APPEAL_RESOLUTION_FORM)
    Observable<Response<TkpdResponse>> getAppealResolutionForm(@QueryMap Map<String, String> params);

    @GET(TkpdBaseURL.User.PATH_TRACK_SHIPPING_REF_V2)
    Observable<Response<TkpdResponse>> trackShippingRefv2(@QueryMap Map<String, Object> params);


}
