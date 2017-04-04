package com.tokopedia.core.network.apiservices.upload.apis;


import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;

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
public interface UploadImageApi {

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImageProof(@Url String url,
                                                        @PartMap Map<String, RequestBody> params,
                                                        @Part("payment_image\"; filename=\"image.jpg") RequestBody imageFile);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> uploadImage(@Url String url,
                                                        @PartMap Map<String, RequestBody> params,
                                                        @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);

    @Multipart
    @POST("")
    Observable<Response<TkpdResponse>> createImage(@Url String url,
                                                   @PartMap Map<String, RequestBody> params);


}
