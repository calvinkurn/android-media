package com.tokopedia.updateinactivephone.data.network.api;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.updateinactivephone.common.UpdateInactivePhoneURL;
import com.tokopedia.updateinactivephone.model.response.UploadImageData;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

public interface UploadImageApi {

    @Multipart
    @POST("")
    Observable<Response<UploadImageData>> uploadImage(@Url String url,
                                                      @QueryMap Map<String, String> params,
                                                      @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);

    @FormUrlEncoded
    @POST(UpdateInactivePhoneURL.GET_UPLOAD_HOST)
    Observable<Response<TkpdResponse>> getUploadHost(@FieldMap Map<String, Object> params);
}
