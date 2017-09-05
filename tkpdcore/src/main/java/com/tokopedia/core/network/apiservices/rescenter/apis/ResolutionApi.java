package com.tokopedia.core.network.apiservices.rescenter.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResolutionApi {

    //Version 2
    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_STEP_1)
    Observable<Response<TkpdResponse>> getProductProblemList(@Path("order_id") String orderId,
                                                             @QueryMap TKPDMapParam<String, Object> params);

    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_STEP_2_3)
    Observable<Response<TkpdResponse>> getSolution(@Path("order_id") String orderId,
                                                   @Body String object);

    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_CREATE)
    Observable<Response<TkpdResponse>> postCreateResolution(@Path("order_id") String orderId,
                                                   @Body String object);

    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_CREATE)
    Observable<Response<TkpdResponse>> postCreateResolutionCache(@Path("order_id") String orderId,
                                                            @Body String object);

    //Version 1
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

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_REPLY_RESOLUTION)
    Observable<Response<TkpdResponse>> replyResolution(@Path("resolution_id") String resolutionID,
                                                       @FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_REPLY_RESOLUTION)
    Observable<Response<TkpdResponse>> replyResolutionSubmit(@Path("resolution_id") String resolutionID,
                                                             @FieldMap TKPDMapParam<String, Object> params);




}
