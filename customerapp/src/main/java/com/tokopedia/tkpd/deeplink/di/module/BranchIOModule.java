package com.tokopedia.tkpd.deeplink.di.module;


import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.tkpd.campaign.data.model.CampaignErrorResponse;
import com.tokopedia.tkpd.campaign.source.api.CampaignURL;
import com.tokopedia.tkpd.deeplink.domain.branchio.BranchIODeeplinkUseCase;
import com.tokopedia.tkpd.deeplink.source.BranchIOData;
import com.tokopedia.tkpd.deeplink.source.BranchIODataFactory;
import com.tokopedia.tkpd.deeplink.source.BranchIODataRepository;
import com.tokopedia.tkpd.deeplink.source.api.BranchIOAPI;
import com.tokopedia.tkpd.deeplink.source.api.BranchIOURL;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 19/03/18.
 */
@Module
public class BranchIOModule {
    @Provides
    BranchIODeeplinkUseCase provideShakeUseCase(BranchIODataRepository branchIODataRepository) {
        return new BranchIODeeplinkUseCase(branchIODataRepository);
    }

    @Provides
    BranchIODataRepository provideCampaignRideRepository(BranchIODataFactory branchIODataFactory) {
        return new BranchIOData(branchIODataFactory);
    }


    @Provides
    BranchIODataFactory provideCampaignDataFactory(BranchIOAPI branchIOAPI) {
        return new BranchIODataFactory(branchIOAPI);
    }

    @Provides
    BranchIOAPI provideCampaignApi(Retrofit retrofit) {
        return retrofit.create(BranchIOAPI.class);
    }



}
