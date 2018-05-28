package com.tokopedia.contactus.home.di;

import com.tokopedia.contactus.home.domain.ContactUsArticleUseCase;
import com.tokopedia.contactus.home.domain.ContactUsPurchaseListUseCase;
import com.tokopedia.contactus.home.domain.ContactUsTopBotUseCase;
import com.tokopedia.contactus.home.domain.IContactUsDataRepository;
import com.tokopedia.contactus.home.source.ContactUsArticleData;
import com.tokopedia.contactus.home.source.ContactUsArticleDataFactory;
import com.tokopedia.contactus.home.source.api.ContactUsAPI;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by sandeepgoyal on 12/04/18.
 */

@Module
public class ContactUsHomeModule {

    @Provides
    ContactUsAPI provideCampaignApi(Retrofit retrofit) {
        return retrofit.create(ContactUsAPI.class);
    }
    @Provides
    ContactUsArticleDataFactory provideContactUsArticleDataFactory(ContactUsAPI contactUsAPI) {
        return new ContactUsArticleDataFactory(contactUsAPI);
    }
    @Provides
    IContactUsDataRepository provideContactUsArticleData(ContactUsArticleDataFactory factory) {
        return new ContactUsArticleData(factory);
    }


    @Provides
    ContactUsArticleUseCase provideArticleUseCase(IContactUsDataRepository repository) {
        return new ContactUsArticleUseCase(repository);
    }

    @Provides
    ContactUsPurchaseListUseCase providePurchaseListUseCase(IContactUsDataRepository repository) {
        return new ContactUsPurchaseListUseCase(repository);
    }

    @Provides
    ContactUsTopBotUseCase provideTopBotUseCase(IContactUsDataRepository repository) {
        return new ContactUsTopBotUseCase(repository);
    }
}
