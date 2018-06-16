package com.tokopedia.reksadana.domain;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.reksadana.source.UploadImageDataStore;
import com.tokopedia.reksadana.source.api.UploadImageApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class UploadImageFactory {
    UploadImageApi uploadImageApi;

    public UploadImageFactory(String url) {



        CommonUtils.dumper(url);
        CommonUtils.dumper(url.substring(0 , url.indexOf("?")));

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .followRedirects(true)
                .followSslRedirects(true)
                .build();

        uploadImageApi = new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .baseUrl(url.substring(0 , url.indexOf("?")) +"/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(client)
                .build().create(UploadImageApi.class);
    }



    public UploadImageDataStore createDataSource() {
        return new UploadImageDataStore(uploadImageApi);
    }
}
