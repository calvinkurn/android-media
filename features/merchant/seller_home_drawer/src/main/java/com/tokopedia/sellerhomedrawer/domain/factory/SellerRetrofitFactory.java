package com.tokopedia.sellerhomedrawer.domain.factory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.abstraction.common.network.converter.TokopediaWsV4ResponseConverter;
import com.tokopedia.core.gcm.network.GeneratedHostConverter;
import com.tokopedia.core.gcm.network.StringResponseConverter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class SellerRetrofitFactory {

    public static Retrofit.Builder createRetrofitDefaultConfig(String baseUrl) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .setPrettyPrinting()
                .serializeNulls()
                .create();
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(new GeneratedHostConverter())
                .addConverterFactory(new TokopediaWsV4ResponseConverter())
                .addConverterFactory(new StringResponseConverter())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }
}
