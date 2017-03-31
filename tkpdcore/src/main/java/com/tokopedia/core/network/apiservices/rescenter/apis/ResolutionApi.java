package com.tokopedia.core.network.apiservices.rescenter.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResolutionApi {

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_DETAIL)
    Observable<Response<TkpdResponse>> getResCenterDetail(@Path("resolution_id") String resolutionID,
                                                          @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION)
    Observable<Response<TkpdResponse>> getResCenterConversation(@Path("resolution_id") String resolutionID,
                                                                @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION_MORE)
    Observable<Response<TkpdResponse>> getResCenterConversationMore(@Path("resolution_id") String resolutionID,
                                                                    @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_AWB)
    Observable<Response<TkpdResponse>> getHistoryAwb(@Path("resolution_id") String resolutionID,
                                                     @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_ADDRESS)
    Observable<Response<TkpdResponse>> getHistoryAddress(@Path("resolution_id") String resolutionID,
                                                     @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_ACTION)
    Observable<Response<TkpdResponse>> getHistoryAction(@Path("resolution_id") String resolutionID,
                                                     @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_LIST_PRODUCT)
    Observable<Response<TkpdResponse>> getListProduct(@Path("resolution_id") String resolutionID,
                                                     @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_PRODUCT_DETAIL)
    Observable<Response<TkpdResponse>> getProductDetail(@Path("resolution_id") String resolutionID,
                                                     @Path("trouble_id") String troubleID,
                                                     @QueryMap TKPDMapParam<String, Object> params);
}
