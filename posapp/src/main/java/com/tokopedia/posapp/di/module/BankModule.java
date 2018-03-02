package com.tokopedia.posapp.di.module;

import com.google.gson.Gson;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.PosGatewayAuth;
import com.tokopedia.posapp.bank.data.source.cloud.api.BankApi;
import com.tokopedia.posapp.bank.data.factory.BankFactory;
import com.tokopedia.posapp.bank.data.mapper.GetBankInstallmentMapper;
import com.tokopedia.posapp.cache.data.repository.BankRepository;
import com.tokopedia.posapp.cache.data.repository.BankRepositoryImpl;
import com.tokopedia.posapp.bank.data.source.local.BankDbManager;
import com.tokopedia.posapp.di.scope.BankScope;
import com.tokopedia.posapp.bank.domain.usecase.GetBankUseCase;
import com.tokopedia.posapp.bank.domain.usecase.StoreBankUsecase;

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
    BankApi provideGatewayBankApi(@PosGatewayAuth Retrofit retrofit) {
        return retrofit.create(BankApi.class);
    }

    @Provides
    BankDbManager provideBankDbManager() {
        return new BankDbManager();
    }

    @Provides
    BankFactory provideBankFactory(BankApi bankApi,
                                   GetBankInstallmentMapper getBankInstallmentMapper) {
        return new BankFactory(bankApi, getBankInstallmentMapper);
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
