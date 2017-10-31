package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.posapp.data.factory.PaymentFactory;
import com.tokopedia.posapp.data.mapper.CreateOrderMapper;
import com.tokopedia.posapp.data.mapper.PaymentStatusMapper;
import com.tokopedia.posapp.data.repository.PaymentRepository;
import com.tokopedia.posapp.data.repository.PaymentRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.GatewayPaymentApi;
import com.tokopedia.posapp.di.scope.PaymentScope;
import com.tokopedia.posapp.domain.usecase.CheckPaymentStatusUseCase;
import com.tokopedia.posapp.domain.usecase.CreateOrderUseCase;

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
    GatewayPaymentApi provideGatewayPaymentApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(GatewayPaymentApi.class);
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
    PaymentFactory providePaymentFactory(GatewayPaymentApi gatewayPaymentApi,
                                         PaymentStatusMapper paymentStatusmapper,
                                         CreateOrderMapper createOrderMapper) {
        return new PaymentFactory(gatewayPaymentApi, paymentStatusmapper, createOrderMapper);
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
