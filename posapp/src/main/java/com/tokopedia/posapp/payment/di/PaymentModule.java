package com.tokopedia.posapp.payment.di;

import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.common.PosApiModule;
import com.tokopedia.posapp.payment.otp.data.source.PaymentApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */
@PaymentScope
@Module(includes = {PosApiModule.class, CartModule.class})
public class PaymentModule {
    @Provides
    PaymentApi provideGatewayPaymentApi(Retrofit retrofit) {
        return retrofit.create(PaymentApi.class);
    }
}
