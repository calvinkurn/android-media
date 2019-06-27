package com.tokopedia.digital.newcart.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.common_digital.cart.data.mapper.CartMapperData;
import com.tokopedia.common_digital.cart.data.mapper.ICartMapperData;
import com.tokopedia.common_digital.common.di.DigitalRestApiRetrofit;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.newcart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.digital.newcart.data.repository.CartDigitalRepository;
import com.tokopedia.digital.newcart.data.repository.CheckoutRepository;
import com.tokopedia.digital.newcart.data.repository.VoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.ICartDigitalRepository;
import com.tokopedia.digital.newcart.domain.ICheckoutRepository;
import com.tokopedia.digital.newcart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.interactor.CartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rizky on 28/08/18.
 */
@Module
public class DigitalCartModule {

    @Provides
    @DigitalCartScope
    ICartMapperData provideCommonCartMapperData() {
        return new CartMapperData();
    }

    @Provides
    @DigitalCartScope
    com.tokopedia.digital.newcart.data.mapper.ICartMapperData provideCartMapperData() {
        return new com.tokopedia.digital.newcart.data.mapper.CartMapperData();
    }

    @Provides
    @DigitalCartScope
    DigitalRestApi provideDigitalRestApi(@DigitalRestApiRetrofit Retrofit retrofit) {
        return retrofit.create(DigitalRestApi.class);
    }

    @Provides
    @DigitalCartScope
    ICartDigitalRepository provideCartDigitalRepository(DigitalRestApi digitalRestApi,
                                                        ICartMapperData cartMapperData) {
        return new CartDigitalRepository(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalCartScope
    IVoucherDigitalRepository provideVoucherDigitalRepository(DigitalRestApi digitalRestApi,
                                                              com.tokopedia.digital.newcart.data.mapper.ICartMapperData cartMapperData) {
        return new VoucherDigitalRepository(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalCartScope
    ICheckoutRepository provideCheckoutRepository(DigitalRestApi digitalRestApi,
                                                  com.tokopedia.digital.newcart.data.mapper.ICartMapperData cartMapperData) {
        return new CheckoutRepository(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalCartScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @DigitalCartScope
    ICartDigitalInteractor provideCartDigitalInteractor(CompositeSubscription compositeSubscription,
                                                        ICartDigitalRepository cartDigitalRepository,
                                                        IVoucherDigitalRepository voucherDigitalRepository) {
        return new CartDigitalInteractor(compositeSubscription, cartDigitalRepository,
                voucherDigitalRepository);
    }

    @Provides
    @DigitalCartScope
    DigitalCheckoutUseCase provideDigitalCheckoutUseCase(ICheckoutRepository checkoutRepository) {
        return new DigitalCheckoutUseCase(checkoutRepository);
    }

    @Provides
    @DigitalCartScope
    DigitalPostPaidLocalCache provideDigitalPostPaidLocalCache(@ApplicationContext Context context) {
        return DigitalPostPaidLocalCache.newInstance(context);
    }

}
