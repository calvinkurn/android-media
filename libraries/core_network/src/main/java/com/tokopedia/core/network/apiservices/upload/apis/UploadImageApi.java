package com.tokopedia.core.network.apiservices.upload.apis;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

/**
 * @author anggaprasetiyo on 8/11/16.
 *         <p>
 *         migrate retrofit 2 by Angga.Prasetiyo
 */

@Deprecated
public interface UploadImageApi {

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImageProof(@Url String url,
                                                        @PartMap Map<String, RequestBody> params,
                                                        @Part("payment_image\"; filename=\"image.jpg") RequestBody imageFile);

}
