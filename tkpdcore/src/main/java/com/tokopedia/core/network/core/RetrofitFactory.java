package com.tokopedia.core.network.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ricoharisin on 2/28/17.
 */

public class RetrofitFactory {

    private String baseUrl;

    public RetrofitFactory (String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public Retrofit.Builder createRetrofitDefaultConfig() {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        return new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(new GeneratedHostConverter())
                        .addConverterFactory(new TkpdResponseConverter())
                        .addConverterFactory(new StringResponseConverter())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
