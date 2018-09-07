package com.tokopedia.reksadana.source.api;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import rx.Observable;

public interface UploadImageApi {

    @PUT("")
    @Headers({
            "x-amz-acl:public-read"
    })
    Observable<ResponseBody> postImage(@Url String url, @Body RequestBody image);
}