package com.tokopedia.tkpd.campaign.di;


import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.core.OkHttpFactory;
import com.tokopedia.tkpd.campaign.domain.CampaignDataRepository;
import com.tokopedia.tkpd.campaign.domain.barcode.PostBarCodeDataUseCase;
import com.tokopedia.tkpd.campaign.source.CampaignData;
import com.tokopedia.tkpd.campaign.source.CampaignDataFactory;
import com.tokopedia.tkpd.campaign.source.api.CampaignAPI;
import com.tokopedia.tkpd.campaign.source.api.CampaignURL;
import com.tokopedia.tkpd.campaign.view.BarCodeScannerPresenter;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 15/12/17.
 */

@Module
public class CampaignModule {
    @Provides
    BarCodeScannerPresenter provideBarCodeScannerPresenter(PostBarCodeDataUseCase postBarCodeDataUseCase, Gson gson) {
        return new BarCodeScannerPresenter(postBarCodeDataUseCase);
    }
    @Provides
    PostBarCodeDataUseCase providePostBarCodeDataUseCase(ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread,
                                                         CampaignDataRepository bookingRideRepository) {
        return new PostBarCodeDataUseCase(threadExecutor, postExecutionThread, bookingRideRepository);
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
    CampaignAPI provideCampaignApi( Retrofit retrofit) {
        return retrofit.create(CampaignAPI.class);
    }
    @Provides
    Retrofit provideRideRetrofit( OkHttpClient okHttpClient,
                                 Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(CampaignURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClientRide() {

        return OkHttpFactory.create().buildClientCampaignAuth();
    }

}
