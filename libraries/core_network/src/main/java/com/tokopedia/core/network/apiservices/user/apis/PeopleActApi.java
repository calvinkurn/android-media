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
 * @author Angga.Prasetiyo on 07/12/2015.
 */

@Deprecated
public interface PeopleActApi {

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ADD_ADDRESS)
    Observable<Response<TkpdResponse>> addAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_ADD_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> addBankAccount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_CONFIRM_NEW_EMAIL)
    Observable<Response<TkpdResponse>> confirmNewEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_ADDRESS)
    Observable<Response<TkpdResponse>> deleteAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_DELETE_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> deleteBankAccount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_ADDRESS)
    Observable<Response<TkpdResponse>> editAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> editBankAccount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_BIODATA)
    Observable<Response<TkpdResponse>> editBiodata(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_CONTACT)
    Observable<Response<TkpdResponse>> editContact(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_DEFAULT_ADDRESS)
    Observable<Response<TkpdResponse>> editDefaultAddress(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_DEFAULT_BANK_ACCOUNT)
    Observable<Response<TkpdResponse>> editDefaultBankAccount(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_EMAIL)
    Observable<Response<TkpdResponse>> editEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_NOTIFICATION)
    Observable<Response<TkpdResponse>> editNotification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_PASSWORD)
    Observable<Response<TkpdResponse>> editPassword(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_PRIVACY)
    Observable<Response<TkpdResponse>> editPrivacy(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_EDIT_PROFILE)
    Observable<Response<TkpdResponse>> editProfile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_SEND_OTP_EDIT_EMAIL)
    Observable<Response<TkpdResponse>> sendOTPEditEmail(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TkpdBaseURL.User.PATH_UPLOAD_PROFILE_PICTURE)
    Observable<Response<TkpdResponse>> uploadProfilePic(@FieldMap Map<String, String> params);


}
