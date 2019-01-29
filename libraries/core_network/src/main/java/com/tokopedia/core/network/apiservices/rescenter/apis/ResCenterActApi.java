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
    @POST(TkpdBaseURL.ResCenter.PATH_ACCEPT_ADMIN_RESOLUTION)
    Observable<Response<TkpdResponse>> acceptAdminResolution(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_ACCEPT_RESOLUTION)
    Observable<Response<TkpdResponse>> acceptResolution(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_CANCEL_RESOLUTION)
    Observable<Response<TkpdResponse>> cancelResolution(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_CREATE_RES_SUBMIT)
    Observable<Response<TkpdResponse>> createResolutionSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_CREATE_RES_VALIDATION)
    Observable<Response<TkpdResponse>> createResolutionValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_EDIT_RESI_RESOLUTION_VALIDATION)
    Observable<Response<TkpdResponse>> editResiResolutionValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_EDIT_RESI_RESOLUTION_SUBMIT)
    Observable<Response<TkpdResponse>> editResiResolutionSubmit(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_FINISH_RES_RETURN)
    Observable<Response<TkpdResponse>> finishResolutionReturn(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_INPUT_RESI_RESOLUTION_VALIDATION)
    Observable<Response<TkpdResponse>> inputResiResolutionValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_INPUT_RESI_RESOLUTION_SUBMIT)
    Observable<Response<TkpdResponse>> inputResiResolutionSubmit(@FieldMap Map<String, String> params);

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
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_VALIDATION)
    Observable<Response<TkpdResponse>> replyConversationValidation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPLY_CONVERSATION_VALIDATION_NEW)
    Observable<Response<TkpdResponse>> replyConversationValidationNew(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_REPORT_REOLUTION)
    Observable<Response<TkpdResponse>> reportResolution(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_INPUT_ADDRESS_RESOLUTION)
    Observable<Response<TkpdResponse>> inputAddressResolution(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_EDIT_ADDRESS_RESOLUTION)
    Observable<Response<TkpdResponse>> editAddressResolution(@FieldMap Map<String, String> params);

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
    @POST(TkpdBaseURL.ResCenter.PATH_ACCEPT_RESOLUTION_V2)
    Observable<Response<TkpdResponse>> acceptResolution2(@FieldMap Map<String, Object> params);

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

    @FormUrlEncoded
    @POST(TkpdBaseURL.ResCenter.PATH_GENERATE_TOKEN_HOST_WITHOUT_HEADER)
    Observable<Response<TkpdResponse>> generateTokenHostWithoutHeader(@FieldMap TKPDMapParam<String, Object> params);

}

