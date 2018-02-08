package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.posapp.data.factory.BankFactory;
import com.tokopedia.posapp.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.data.repository.BankRepository;
import com.tokopedia.posapp.data.repository.BankRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.GatewayBankApi;
import com.tokopedia.posapp.database.manager.BankDbManager;
import com.tokopedia.posapp.di.scope.BankScope;
import com.tokopedia.posapp.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.domain.usecase.StoreBankUsecase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/5/17.
 */

@BankScope
@Module
public class BankModule {
    @Provides
    GetBankInstallmentMapper provideGetBankInstallmentMapper(Gson gson) {
        return new GetBankInstallmentMapper(gson);
    }

    @Provides
    GatewayBankApi provideGatewayBankApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(GatewayBankApi.class);
    }

    @Provides
    BankDbManager provideBankDbManager() {
        return new BankDbManager();
    }

    @Provides
    BankFactory provideBankFactory(GatewayBankApi gatewayBankApi,
                                   GetBankInstallmentMapper getBankInstallmentMapper) {
        return new BankFactory(gatewayBankApi, getBankInstallmentMapper);
    }

    @Provides
    BankRepository provideBankRepository(BankFactory bankFactory) {
        return new BankRepositoryImpl(bankFactory);
    }

    @Provides
    GetBankUseCase provideGetBankInstallmentUseCase(ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutionThread,
                                                    BankRepository bankRepository) {
        return new GetBankUseCase(threadExecutor, postExecutionThread, bankRepository);
    }

    @Provides
    StoreBankUsecase provideStoreBankInstallmentUseCase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        BankRepository bankRepository) {
        return new StoreBankUsecase(threadExecutor, postExecutionThread, bankRepository);
    }
}
