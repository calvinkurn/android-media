package com.tokopedia.kol.common.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.di.scope.ApplicationScope;
import com.tokopedia.abstraction.common.network.OkHttpRetryPolicy;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.kol.R;
import com.tokopedia.kol.common.data.source.KolAuthInterceptor;
import com.tokopedia.kol.common.data.source.api.KolApi;
import com.tokopedia.kol.common.network.KolUrl;
import com.tokopedia.kol.common.util.KolConstant;
import com.tokopedia.kol.feature.video.view.listener.VideoDetailContract;
import com.tokopedia.kol.feature.video.view.presenter.VideoDetailPresenter;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.network.interceptor.FingerprintInterceptor;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.wishlist.common.domain.interactor.GetProductIsWishlistedUseCase;
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase;
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by milhamj on 06/02/18.
 */

@Module
public class KolModule {
    private static final int NET_READ_TIMEOUT = 60;
    private static final int NET_WRITE_TIMEOUT = 60;
    private static final int NET_CONNECT_TIMEOUT = 60;
    private static final int NET_RETRY = 1;

    @KolScope
    @Provides
    VideoDetailContract.Presenter provideVideoDetailPresenter(VideoDetailPresenter presenter) {
        return presenter;
    }

    @KolScope
    @Provides
    public UserSessionInterface provideUserSessionInterface(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @KolScope
    @Provides
    public OkHttpClient provideOkHttpClient(@ApplicationScope HttpLoggingInterceptor
                                                    httpLoggingInterceptor,
                                            KolAuthInterceptor kolAuthInterceptor,
                                            @KolQualifier OkHttpRetryPolicy retryPolicy,
                                            @KolChuckQualifier Interceptor chuckInterceptor,
                                            @ApplicationContext Context context) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(retryPolicy.connectTimeout, TimeUnit.SECONDS)
                .readTimeout(retryPolicy.readTimeout, TimeUnit.SECONDS)
                .writeTimeout(retryPolicy.writeTimeout, TimeUnit.SECONDS)
                .addInterceptor(kolAuthInterceptor)
                .addInterceptor(new FingerprintInterceptor((NetworkRouter) context,
                        new UserSession(context)));

        if (GlobalConfig.isAllowDebuggingTools()) {
            clientBuilder.addInterceptor(httpLoggingInterceptor);
            clientBuilder.addInterceptor(chuckInterceptor);
        }

        return clientBuilder.build();
    }

    @KolScope
    @Provides
    @KolQualifier
    public Retrofit provideKolRetrofit(OkHttpClient okHttpClient,
                                       Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(KolUrl.BASE_URL).client(okHttpClient).build();
    }

    @KolScope
    @Provides
    public KolApi provideKolApi(@KolQualifier Retrofit retrofit) {
        return retrofit.create(KolApi.class);
    }

    @KolScope
    @KolQualifier
    @Provides
    public OkHttpRetryPolicy provideOkHttpRetryPolicy() {
        return new OkHttpRetryPolicy(NET_READ_TIMEOUT,
                NET_WRITE_TIMEOUT,
                NET_CONNECT_TIMEOUT,
                NET_RETRY);
    }

    @KolScope
    @Provides
    @KolChuckQualifier
    public Interceptor provideChuckInterceptory(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @KolScope
    @Provides
    public CoroutineDispatcher provideDispatcher(){
        return Dispatchers.getMain();
    }

    @KolScope
    @Provides
    public AddWishListUseCase provideAddWishListUseCase(@ApplicationContext Context context){
        return new AddWishListUseCase(context);
    }

    @KolScope
    @Provides
    public RemoveWishListUseCase provideRemoveWishListUseCase(@ApplicationContext Context context){
        return new RemoveWishListUseCase(context);
    }

    @KolScope
    @Provides
    @Named(KolConstant.KEY_QUERY_IS_WISHLISTED)
    public String getQueryProductIsWishlisted(@ApplicationContext Context context){
        return GraphqlHelper.loadRawString(context.getResources(), R.raw.gql_get_is_wishlisted);
    }

    @KolScope
    @Provides
    public GetProductIsWishlistedUseCase provideGetProductIsWishlistedUseCase(@Named(KolConstant.KEY_QUERY_IS_WISHLISTED)
                                                                              String rawQuery,
                                                                              GraphqlUseCase gqlUseCase){
        return new GetProductIsWishlistedUseCase(rawQuery, gqlUseCase);
    }

}
