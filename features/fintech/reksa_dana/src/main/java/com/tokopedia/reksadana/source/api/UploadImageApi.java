package com.tokopedia.reksadana.source.api;

import com.tokopedia.abstraction.common.data.model.response.GraphqlResponse;
import com.tokopedia.reksadana.view.data.signimageurl.Data;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;
import rx.Observable;

public interface UploadImageApi {

    @PUT("")
    @Headers({
            "x-amz-acl:public-read"
    })
    Observable<ResponseBody> postImage(@Url String url, @Body RequestBody image);
}