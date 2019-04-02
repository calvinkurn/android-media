package com.tokopedia.imageuploader.data.source;

import android.net.Uri;

import com.tokopedia.imageuploader.data.source.api.ImageUploadApi;

import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 12/19/17.
 */

public class UploadImageDataSourceCloud {

    private Retrofit.Builder retrofitBuilder;
    private OkHttpClient okHttpClient;

    public UploadImageDataSourceCloud(Retrofit.Builder retrofitBuilder, OkHttpClient okHttpClient) {
        this.retrofitBuilder = retrofitBuilder;
        this.okHttpClient = okHttpClient;
    }

    public Observable<String> uploadImage(Map<String, RequestBody> params, String urlUploadImage) {
        Uri uri = Uri.parse(urlUploadImage);
        retrofitBuilder.baseUrl(uri.getScheme() + "://" + uri.getHost() + "/");
        Retrofit retrofit = retrofitBuilder.client(okHttpClient).build();
        return retrofit.create(ImageUploadApi.class)
                .uploadImage(urlUploadImage, params)
                .flatMap(new Func1<Response<String>, Observable<String>>() {
                    @Override
                    public Observable<String> call(Response<String> stringResponse) {
                        return Observable.just(stringResponse.body());
                    }
                });
    }
}
