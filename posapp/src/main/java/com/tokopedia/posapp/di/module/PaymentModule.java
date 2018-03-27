package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.posapp.cart.di.CartModule;
import com.tokopedia.posapp.payment.otp.data.factory.PaymentFactory;
import com.tokopedia.posapp.payment.otp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.payment.otp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepository;
import com.tokopedia.posapp.payment.otp.data.repository.PaymentRepositoryImpl;
import com.tokopedia.posapp.payment.otp.data.source.PaymentApi;
import com.tokopedia.posapp.di.scope.PaymentScope;
import com.tokopedia.posapp.payment.otp.domain.usecase.CheckPaymentStatusUseCase;
import com.tokopedia.posapp.payment.otp.domain.usecase.CreateOrderUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */
@PaymentScope
@Module(includes = CartModule.class)
public class PaymentModule {
    @Provides
    PaymentApi provideGatewayPaymentApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(PaymentApi.class);
    }

    @Provides
    CreateOrderMapper provideCreateOrderMapper(Gson gson) {
        return new CreateOrderMapper(gson);
    }

    @Provides
    PaymentStatusMapper providePaymentStatusMapper(Gson gson) {
        return new PaymentStatusMapper(gson);
    }

    @Provides
    PaymentFactory providePaymentFactory(PaymentApi paymentApi,
                                         PaymentStatusMapper paymentStatusmapper,
                                         CreateOrderMapper createOrderMapper) {
        return new PaymentFactory(paymentApi, paymentStatusmapper, createOrderMapper);
    }

    @Provides
    PaymentRepository providePaymentRepository(PaymentFactory paymentFactory) {
        return new PaymentRepositoryImpl(paymentFactory);
    }

    @Provides
    CheckPaymentStatusUseCase provideCheckPaymentStatusUseCase(ThreadExecutor threadExecutor,
                                                               PostExecutionThread postExecutionThread,
                                                               PaymentRepository paymentRepository) {
        return new CheckPaymentStatusUseCase(
                threadExecutor,
                postExecutionThread,
                paymentRepository);
    }

    @Provides
    CreateOrderUseCase provideCreateOrderUseCase(ThreadExecutor threadExecutor,
                                                 PostExecutionThread postExecutionThread,
                                                 PaymentRepository paymentRepository) {
        return new CreateOrderUseCase(
                threadExecutor,
                postExecutionThread,
                paymentRepository
        );
    }
}
