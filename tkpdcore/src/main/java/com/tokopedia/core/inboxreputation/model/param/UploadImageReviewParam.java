package com.tokopedia.core.inboxreputation.model.param;

import com.tokopedia.core.inboxreputation.model.actresult.ImageUploadResult;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by Nisie on 3/2/16.
 * <p>
 * migrate retrofit 2 by Angga.Prasetiyo
 */
public interface UploadImageReviewParam {

    @Multipart
    @POST("/upload/attachment")
    Observable<ImageUploadResult> uploadImage(
            @Header("Content-MD5") String contentMD5,
            @Header("Date") String date,
            @Header("Authorization") String authorization,
            @Header("X-Method") String xMethod,
            @Part("user_id") RequestBody userId,
            @Part("device_id") RequestBody deviceId,
            @Part("hash") RequestBody hash,
            @Part("device_time") RequestBody deviceTime,
            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile,
            @Part("id") RequestBody imageId,
            @Part("token") RequestBody token,
            @Part("web_service") RequestBody web_service

    );
}
