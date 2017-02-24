package com.tokopedia.sellerapp.home.di;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.coverters.GeneratedHostConverter;
import com.tokopedia.core.network.retrofit.coverters.StringResponseConverter;
import com.tokopedia.core.network.retrofit.coverters.TkpdResponseConverter;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.shopscore.data.factory.ShopScoreFactory;
import com.tokopedia.seller.shopscore.data.repository.ShopScoreRepositoryImpl;
import com.tokopedia.seller.shopscore.data.source.cloud.ShopScoreCloud;
import com.tokopedia.seller.shopscore.data.source.cloud.api.ShopScoreApi;
import com.tokopedia.seller.shopscore.domain.interactor.GetShopScoreMainDataUseCase;
import com.tokopedia.sellerapp.home.view.presenter.SellerHomePresenterImpl;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by sebastianuskh on 2/24/17.
 */
public class SellerHomeDependencyInjection {

    public static SellerHomePresenterImpl getPresenter(Context context) {
        JobExecutor threadExecutor = new JobExecutor();
        UIThread postExecutionThread = new UIThread();

        Gson gson = new Gson();


        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.readTimeout(45L, TimeUnit.SECONDS);
        clientBuilder.writeTimeout(45L, TimeUnit.SECONDS);
        TkpdAuthInterceptor authInterceptor = new TkpdAuthInterceptor();
        clientBuilder.interceptors().add(authInterceptor);
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        clientBuilder.interceptors().add(logInterceptor);
        OkHttpClient client = clientBuilder.build();

        Retrofit retrofit = createRetrofit(
                TkpdBaseURL.GOLD_MERCHANT_DOMAIN,
                client,
                new GeneratedHostConverter(),
                new TkpdResponseConverter(),
                new StringResponseConverter(),
                GsonConverterFactory.create(gson),
                RxJavaCallAdapterFactory.create()
        );
        ShopScoreApi api = retrofit.create(ShopScoreApi.class);

        SessionHandler sessionHandler = new SessionHandler(context);

        ShopScoreCloud shopScoreCloud = new ShopScoreCloud(api, sessionHandler);
        ShopScoreFactory shopScoreFactory = new ShopScoreFactory(shopScoreCloud, gson);
        ShopScoreRepositoryImpl shopScoreRepository = new ShopScoreRepositoryImpl(shopScoreFactory);

        GetShopScoreMainDataUseCase getShopScoreMainData =
                new GetShopScoreMainDataUseCase(
                        threadExecutor,
                        postExecutionThread,
                        shopScoreRepository);

        return new SellerHomePresenterImpl(getShopScoreMainData);
    }

    private static Retrofit createRetrofit(String baseUrl,
                                           OkHttpClient client,
                                           GeneratedHostConverter hostConverter,
                                           TkpdResponseConverter tkpdResponseConverter,
                                           StringResponseConverter stringResponseConverter,
                                           GsonConverterFactory gsonConverterFactory,
                                           RxJavaCallAdapterFactory rxJavaCallAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(hostConverter)
                .addConverterFactory(tkpdResponseConverter)
                .addConverterFactory(stringResponseConverter)
                .addConverterFactory(gsonConverterFactory)
                .addCallAdapterFactory(rxJavaCallAdapterFactory)
                .build();
    }
}
