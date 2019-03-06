package com.tokopedia.digital.product.additionalfeature.etoll.di;

import com.tokopedia.common_digital.common.di.DigitalRestApiRetrofit;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.product.additionalfeature.etoll.data.mapper.SmartcardMapper;
import com.tokopedia.digital.product.additionalfeature.etoll.data.repository.ETollRepository;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardCommandDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.data.source.SmartcardInquiryDataSource;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardCommandUseCase;
import com.tokopedia.digital.product.additionalfeature.etoll.domain.interactor.SmartcardInquiryUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class DigitalETollModule {
    @Provides
    @DigitalETollScope
    SmartcardInquiryDataSource provideSmartcardInquiryDataSource(DigitalRestApi digitalRestApi, SmartcardMapper mapper) {
        return new SmartcardInquiryDataSource(digitalRestApi, mapper);
    }

    @Provides
    @DigitalETollScope
    SmartcardCommandDataSource provideSmartcardCommandDataSource(DigitalRestApi digitalRestApi, SmartcardMapper mapper){
        return new SmartcardCommandDataSource(digitalRestApi, mapper);
    }

    @Provides
    @DigitalETollScope
    ETollRepository provideETollRepository(SmartcardInquiryDataSource  smartcardInquiryDataSource,SmartcardCommandDataSource smartcardCommandDataSource ){
        return new ETollRepository(smartcardInquiryDataSource, smartcardCommandDataSource);
    }

    @Provides
    @DigitalETollScope
    SmartcardInquiryUseCase provideSmartcardInquiryUseCase(ETollRepository repository){
        return new SmartcardInquiryUseCase(repository);
    }


    @Provides
    @DigitalETollScope
    SmartcardCommandUseCase provideSmartcardCommandUseCase(ETollRepository repository){
        return new SmartcardCommandUseCase(repository);
    }


    @Provides
    @DigitalETollScope
    DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalRestApi.class);
    }
}
