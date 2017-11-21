package com.tokopedia.core.network.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.network.retrofit.coverters.DigitalResponseConverter;
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


    public static Retrofit.Builder createRetrofitDefaultConfig(String baseUrl) {

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

    public static Retrofit.Builder createRetrofitDigitalConfig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new DigitalResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public static Retrofit.Builder createRetrofitTokoCashConfig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new DigitalResponseConverter())
                .addConverterFactory(new TkpdResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    public static Retrofit.Builder createDaggerRetrofitDefaultConfig(
            String baseUrl,
            Gson gson,
            GeneratedHostConverter hostConverter,
            TkpdResponseConverter tkpdConverter,
            StringResponseConverter stringConverter) {

        return new Retrofit.Builder()
                .addConverterFactory(stringConverter)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(hostConverter)
                .addConverterFactory(tkpdConverter)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseUrl);
    }

    public static Retrofit.Builder createBasicRetrofit(String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(new StringResponseConverter())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
