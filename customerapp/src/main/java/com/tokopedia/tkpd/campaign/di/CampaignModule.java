package com.tokopedia.tkpd.campaign.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;
import com.tokopedia.tkpd.campaign.source.CampaignData;
import com.tokopedia.tkpd.campaign.source.CampaignDataFactory;
import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;
import com.tokopedia.tkpd.campaign.source.api.CampaignURL;
import com.tokopedia.tokocash.di.TokoCashModule;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module(includes = TokoCashModule.class)
public class CampaignModule {

    @Provides
    PostBarCodeDataUseCase providePostBarCodeDataUseCase(CampaignDataRepository bookingRideRepository) {
        return new PostBarCodeDataUseCase(bookingRideRepository);
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
    Retrofit provideRideRetrofit(OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(CampaignURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClientRide() {
        return OkHttpFactory.create().buildClientCampaignAuth();
    }

}
