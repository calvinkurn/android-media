package com.tokopedia.qrscanner.branchio;


import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.qrscanner.branchio.api.BranchIOAPI;
import com.tokopedia.qrscanner.branchio.entity.CampaignErrorResponse;

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
    BranchIOAPI provideCampaignApi(Retrofit.Builder retrofitBuilder, HttpLoggingInterceptor httpLoggingInterceptor) {
        return retrofitBuilder.client(new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(CampaignErrorResponse.class))
                .build()).baseUrl("https://www.tokopedia.com/").build().create(BranchIOAPI.class);
    }



}
