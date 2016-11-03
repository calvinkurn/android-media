package com.tokopedia.tkpd.manage.people.profile.interactor;

import com.tokopedia.tkpd.manage.people.profile.model.UploadProfileImageData;
import com.tokopedia.tkpd.network.constants.TkpdBaseURL;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created on 6/29/16.
 * <p>
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface UploadImageProfile {

    @Multipart
    @POST(TkpdBaseURL.Upload.PATH_PROFILE_IMAGE)
    Observable<UploadProfileImageData> uploadImage(
            @Header("Content-MD5") String contentMD5,// 1
            @Header("Date") String date,// 2
            @Header("Authorization") String authorization, // 3
            @Header("X-Method") String xMethod,// 4
            @Part("user_id") RequestBody userId,// 5
            @Part("device_id") RequestBody deviceId, // 6
            @Part("hash") RequestBody hash,// 7
            @Part("device_time") RequestBody deviceTime,// 8
            @Part("profile_img\"; filename=\"image.jpg") RequestBody imageFile, // "; filename="image.jpg"
            @Part("server_id") RequestBody serverId,
            @Part("new_add") RequestBody serverLanguage
    );
}
