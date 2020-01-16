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
import com.tokopedia.tkpd.campaign.data.model.CampaignErrorResponse;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.tkpd.campaign.domain.shake.ShakeUseCase;
import com.tokopedia.tkpd.campaign.network.CampaignAuthInterceptor;
import com.tokopedia.tkpd.campaign.source.CampaignData;
import com.tokopedia.tkpd.campaign.source.CampaignDataFactory;
import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;
import com.tokopedia.tkpd.campaign.source.api.CampaignURL;
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
    ShakeUseCase provideShakeUseCase(CampaignDataRepository bookingRideRepository) {
        return new ShakeUseCase(bookingRideRepository);
    }

    @Provides
    GraphqlRepository provideRepository() {
        return Interactor.getInstance().getGraphqlRepository();
    }

    @Provides
    ScannerUseCase provideScannerUseCase(@ApplicationContext Context context, GraphqlRepository repository) {
        return new ScannerUseCase(context.getResources(), repository);
    }

    @Provides
    CampaignDataRepository provideCampaignRideRepository(CampaignDataFactory campaignDataFactory) {
        return new CampaignData(campaignDataFactory);
    }

    @Provides
    CampaignDataFactory provideCampaignDataFactory(CampaignAPI campaignAPI) {
        return new CampaignDataFactory(campaignAPI);
    }

    @Provides
    CampaignAPI provideCampaignApi(Retrofit retrofit) {
        return retrofit.create(CampaignAPI.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(CampaignURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(CampaignAuthInterceptor tkpdAuthInterceptor, HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(tkpdAuthInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(CampaignErrorResponse.class))
                .build();
    }

    @IdentifierWalletQualifier
    @Provides
    LocalCacheHandler provideLocalCacheHandler(@ApplicationContext Context context) {
        return new LocalCacheHandler(context, IDENTIFIER);
    }

    @Provides
    CampaignAuthInterceptor provideContactUsAuthInterceptor(@ApplicationContext Context context,
                                                            AbstractionRouter abstractionRouter) {
        return new CampaignAuthInterceptor(context, abstractionRouter);

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
