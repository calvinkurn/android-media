package com.tokopedia.tkpd.campaign.di;

import android.content.Context;
import android.content.res.Resources;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.graphql.coroutines.data.Interactor;
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.scanner.domain.usecase.ScannerUseCase;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module
public class CampaignModule {

    public static final String IDENTIFIER = "identifier";

    @Provides
    Resources provideResources(@ApplicationContext Context context) {
        return context.getResources();
    }

    @Provides
    GraphqlRepository provideRepository() {
        return Interactor.getInstance().getGraphqlRepository();
    }

    @Provides
    ScannerUseCase provideScannerUseCase(@ApplicationContext Context context, GraphqlRepository repository) {
        return new ScannerUseCase(context.getResources(), repository);
    }

    @IdentifierWalletQualifier
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, IDENTIFIER);
    }

    @Provides
    UserSessionInterface providesUserSession(@ApplicationContext Context context) {
        return new UserSession(context);
    }

    @Provides
    RemoteConfig provideRemoteConfig(@ApplicationContext Context context) {
        return new FirebaseRemoteConfigImpl(context);
    }
}
