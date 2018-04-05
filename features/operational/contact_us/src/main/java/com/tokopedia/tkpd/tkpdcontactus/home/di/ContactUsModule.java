package com.tokopedia.tkpd.tkpdcontactus.home.di;

import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.tkpd.tkpdcontactus.home.data.model.ContactUsErrorResponse;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.ContactUsArticleUseCase;
import com.tokopedia.tkpd.tkpdcontactus.home.domain.IContactUsDataRepository;
import com.tokopedia.tkpd.tkpdcontactus.home.source.ContactUsArticleData;
import com.tokopedia.tkpd.tkpdcontactus.home.source.ContactUsArticleDataFactory;
import com.tokopedia.tkpd.tkpdcontactus.home.source.ContactUsArticleDataStore;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsAPI;
import com.tokopedia.tkpd.tkpdcontactus.home.source.api.ContactUsURL;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 15/12/17.
 */
@Module
public class ContactUsModule {



    @Provides
    ContactUsAPI provideCampaignApi(Retrofit retrofit) {
        return retrofit.create(ContactUsAPI.class);
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient,
                             Retrofit.Builder retrofitBuilder) {
        return retrofitBuilder.baseUrl(ContactUsURL.BASE_URL).client(okHttpClient).build();
    }

    @Provides
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new ErrorResponseInterceptor(ContactUsErrorResponse.class))
                .build();
    }

    @Provides
    ContactUsArticleDataFactory provideContactUsArticleDataFactory(ContactUsAPI campaignAPI) {
        return new ContactUsArticleDataFactory(campaignAPI);
    }
    @Provides
    IContactUsDataRepository provideContactUsArticleData(ContactUsArticleDataFactory factory) {
        return new ContactUsArticleData(factory);
    }

    @Provides
    ContactUsArticleUseCase provideArticleUseCase(IContactUsDataRepository repository) {
       return new ContactUsArticleUseCase(repository);
    }
}
