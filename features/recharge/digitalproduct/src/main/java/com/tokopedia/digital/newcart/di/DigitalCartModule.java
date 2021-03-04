package com.tokopedia.digital.newcart.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.abstraction.common.network.interceptor.ErrorResponseInterceptor;
import com.tokopedia.common_digital.common.data.api.DigitalInterceptor;
import com.tokopedia.common_digital.product.data.response.TkpdDigitalResponse;
import com.tokopedia.digital.common.data.apiservice.DigitalRestApi;
import com.tokopedia.digital.common.di.DigitalRestApiRetrofit;
import com.tokopedia.digital.newcart.data.repository.CartDigitalRepository;
import com.tokopedia.digital.newcart.data.repository.CheckoutRepository;
import com.tokopedia.digital.newcart.data.repository.VoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.ICartDigitalRepository;
import com.tokopedia.digital.newcart.domain.ICheckoutRepository;
import com.tokopedia.digital.newcart.domain.IVoucherDigitalRepository;
import com.tokopedia.digital.newcart.domain.interactor.CartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.interactor.ICartDigitalInteractor;
import com.tokopedia.digital.newcart.domain.mapper.CartMapperData;
import com.tokopedia.digital.newcart.domain.mapper.ICartMapperData;
import com.tokopedia.digital.newcart.domain.usecase.DigitalCheckoutUseCase;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalAddToCartUseCase;
import com.tokopedia.digital.newcart.presentation.usecase.DigitalGetCartUseCase;
import com.tokopedia.network.NetworkRouter;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import retrofit2.Retrofit;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Rizky on 28/08/18.
 */
@Module
public class DigitalCartModule {

    @Provides
    @DigitalCartScope
    DigitalAddToCartUseCase provideDigitalAddToCartUseCase(ArrayList<Interceptor> listInterceptor, @ApplicationContext Context context) {
        return new DigitalAddToCartUseCase(listInterceptor, context);
    }

    @Provides
    @DigitalCartScope
    DigitalGetCartUseCase provideDigitalGetCartUseCase(ArrayList<Interceptor> listInterceptor, @ApplicationContext Context context) {
        return new DigitalGetCartUseCase(listInterceptor, context);
    }

    @Provides
    @DigitalCartScope
    ArrayList<Interceptor> provideDigitalInterceptorNew(DigitalInterceptor digitalInterceptor) {
        ArrayList<Interceptor> listInterceptor = new ArrayList<Interceptor>();
        listInterceptor.add(digitalInterceptor);
        listInterceptor.add(new ErrorResponseInterceptor(TkpdDigitalResponse.DigitalErrorResponse.class));
        return listInterceptor;
    }

    @Provides
    @DigitalCartScope
    DigitalInterceptor provideDigitalInterceptor(@ApplicationContext Context context,
                                                 NetworkRouter networkRouter,
                                                 UserSessionInterface userSession) {
        return new DigitalInterceptor(context, networkRouter, userSession);
    }

    @DigitalCartScope
    @Provides
    NetworkRouter provideNetworkRouter(@ApplicationContext Context context) {
        if (context instanceof NetworkRouter) {
            return (NetworkRouter) context;
        }
        throw new RuntimeException("Application must implement " + NetworkRouter.class.getCanonicalName());
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
                                                              ICartMapperData cartMapperData) {
        return new VoucherDigitalRepository(digitalRestApi, cartMapperData);
    }

    @Provides
    @DigitalCartScope
    ICheckoutRepository provideCheckoutRepository(DigitalRestApi digitalRestApi,
                                                  ICartMapperData cartMapperData) {
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

}
