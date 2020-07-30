package com.tokopedia.tkpd.tkpdreputation.network.uploadimage;

import com.tokopedia.abstraction.common.network.response.TokopediaWsV4Response;
import java.util.Map;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;
import rx.Observable;

public interface UploadImageApi {

    @Multipart
    @POST("")
    Observable<Response<TokopediaWsV4Response>> uploadImage(@Url String url,
                                                            @PartMap Map<String, RequestBody> params,
                                                            @Part("fileToUpload\"; filename=\"image.jpg") RequestBody imageFile);

}
