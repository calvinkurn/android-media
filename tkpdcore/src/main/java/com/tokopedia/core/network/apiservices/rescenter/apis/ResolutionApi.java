package com.tokopedia.core.network.apiservices.rescenter.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by hangnadi on 3/9/17.
 */

public interface ResolutionApi {

    String PATH_RESOLUTION_ID = "resolution_id";
    String PATH_ORDER_ID = "order_id";
    String PATH_TROUBLE_ID = "trouble_id";
    String PATH_CONVERSATION_ID = "conversation_id";
    //Version 2

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_STEP_1)
    Observable<Response<TkpdResponse>> getProductProblemList(@Path(PATH_ORDER_ID) String orderId,
                                                             @QueryMap TKPDMapParam<String, Object> params);

    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_STEP_2_3)
    Observable<Response<TkpdResponse>> getSolution(@Path(PATH_ORDER_ID) String orderId,
                                                   @Body String object);

    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_CREATE)
    Observable<Response<TkpdResponse>> postCreateResolution(@Path(PATH_ORDER_ID) String orderId,
                                                   @Body String object);


    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_VALIDATE)
    Observable<Response<TkpdResponse>> postCreateValidateResolution(@Path(PATH_ORDER_ID) String orderId,
                                                            @Body String object);

    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_SUBMIT)
    Observable<Response<TkpdResponse>> postCreateSubmitResolution(@Path(PATH_ORDER_ID) String orderId,
                                                                    @Body String object);

    @POST(TkpdBaseURL.ResCenterV2.BASE_RESOLUTION_CREATE)
    Observable<Response<TkpdResponse>> postCreateResolutionCache(@Path(PATH_ORDER_ID) String orderId,
                                                            @Body String object);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_EDIT)
    Observable<Response<TkpdResponse>> getEditSolution(@Path(PATH_RESOLUTION_ID) String resoId);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_EDIT)
    Observable<Response<TkpdResponse>> postEditSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                                                        @FieldMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_APPEAL)
    Observable<Response<TkpdResponse>> getAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_APPEAL)
    Observable<Response<TkpdResponse>> postAppealSolution(@Path(PATH_RESOLUTION_ID) String resoId,
                                                        @FieldMap TKPDMapParam<String, Object> params);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImage(@Url String url,
                                                   @PartMap Map<String, RequestBody> params,
                                                   @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);


    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION_V2)
    Observable<Response<TkpdResponse>> getConversation(@Path(PATH_RESOLUTION_ID) String resoId, @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION_MORE_V2)
    Observable<Response<TkpdResponse>> getConversationMore(
            @Path("resolution_id") String resoId,
            @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.BASE_DETAIL_NEXT_ACTION_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> getNextAction(@Path(PATH_RESOLUTION_ID) String resoId);


    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_DETAIL_V2)
    Observable<Response<TkpdResponse>> getResCenterDetailV2(@Path(PATH_RESOLUTION_ID) String resoId);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_CANCEL_RESOLUTION)
    Observable<Response<TkpdResponse>> cancelResolution(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                        @FieldMap TKPDMapParam<String, Object> params);


    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_REPLY_RESOLUTION)
    Observable<Response<TkpdResponse>> replyResolution(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                       @FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_REPLY_RESOLUTION)
    Observable<Response<TkpdResponse>> replyResolutionSubmit(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                             @FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_FINISH_RESOLUTION)
    Observable<Response<TkpdResponse>> finishResolution(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                        @FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.ACTION_ASK_HELP_RESOLUTION)
    Observable<Response<TkpdResponse>> askHelpResolution(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                         @FieldMap TKPDMapParam<String, Object> params);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadVideo(@Url String url,
                                                   @PartMap Map<String, RequestBody> params,
                                                   @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_CONVERSATION_ADDRESS)
    Observable<Response<TkpdResponse>> inputAddress(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                         @FieldMap TKPDMapParam<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenterV2.POST_RESOLUTION_CONVERSATION_ADDRESS_EDIT)
    Observable<Response<TkpdResponse>> editAddress(@Path(PATH_RESOLUTION_ID) String resolutionID, @Path(PATH_CONVERSATION_ID) String conversationID,
                                                    @FieldMap TKPDMapParam<String, Object> params);


    //Version 1
    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_DETAIL)
    Observable<Response<TkpdResponse>> getResCenterDetail(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                          @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION)
    Observable<Response<TkpdResponse>> getResCenterConversation(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                                @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_CONVERSATION_MORE)
    Observable<Response<TkpdResponse>> getResCenterConversationMore(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                                    @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_AWB)
    Observable<Response<TkpdResponse>> getHistoryAwb(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                     @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_ADDRESS)
    Observable<Response<TkpdResponse>> getHistoryAddress(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                         @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_ACTION)
    Observable<Response<TkpdResponse>> getHistoryAction(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                        @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_HISTORY_ACTION_V2)
    Observable<Response<TkpdResponse>> getHistoryActionV2(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                        @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_LIST_PRODUCT)
    Observable<Response<TkpdResponse>> getListProduct(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                      @QueryMap TKPDMapParam<String, Object> params);

    @GET(TkpdBaseURL.ResCenterV2.GET_RESOLUTION_PRODUCT_DETAIL)
    Observable<Response<TkpdResponse>> getProductDetail(@Path(PATH_RESOLUTION_ID) String resolutionID,
                                                        @Path(PATH_TROUBLE_ID) String troubleID,
                                                        @QueryMap TKPDMapParam<String, Object> params);


}
