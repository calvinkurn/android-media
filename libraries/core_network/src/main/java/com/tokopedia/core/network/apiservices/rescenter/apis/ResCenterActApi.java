package com.tokopedia.core.network.apiservices.rescenter.apis;

import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author Angga.Prasetiyo on 08/12/2015.
 */

@Deprecated
public interface ResCenterActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REJECT_ADMIN_RES_SUBMIT)
    Observable<Response<TkpdResponse>> rejectAdminResolutionSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REJECT_ADMIN_RES_VALIDATION)
    Observable<Response<TkpdResponse>> rejectAdminResolutionValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_SUBMIT)
    Observable<Response<TkpdResponse>> replyConversationSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_VALIDATION_NEW)
    Observable<Response<TkpdResponse>> replyConversationValidationNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_CANCEL_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> cancelResolution2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPORT_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> reportResolution2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_FINISH_RES_RETURN_V2)
    Observable<Response<TkpdResponse>> finishResolutionReturn2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_ACCEPT_ADMIN_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> acceptAdminResolution2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_INPUT_ADDRESS_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> inputAddressResolution2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_EDIT_ADDRESS_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> editAddressResolution2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_SUBMIT_V2)
    Observable<Response<TkpdResponse>> replyConversationSubmit2(@FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_VALIDATION_V2)
    Observable<Response<TkpdResponse>> replyConversationValidation2(@FieldMap Map<String, Object> params);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImage(@Url String url,
                                                   @PartMap Map<String, RequestBody> params,
                                                   @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadVideo(@Url String url,
                                                   @PartMap Map<String, RequestBody> params,
                                                   @Part MultipartBody.Part file);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> createImage(@Url String url,
                                                   @PartMap Map<String, RequestBody> params);


    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_GENERATE_TOKEN_HOST)
    Observable<Response<TkpdResponse>> generateTokenHost(@FieldMap TKPDMapParam<String, Object> params);
}

