package com.tokopedia.posapp.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.di.qualifier.AccountsQualifier;
import com.tokopedia.posapp.data.factory.AccountFactory;
import com.tokopedia.posapp.data.mapper.ValidatePasswordMapper;
import com.tokopedia.posapp.data.repository.AccountRepository;
import com.tokopedia.posapp.data.repository.AccountRepositoryImpl;
import com.tokopedia.posapp.data.source.cloud.api.AccountApi;
import com.tokopedia.posapp.di.scope.ValidatePasswordScope;
import com.tokopedia.posapp.domain.usecase.ValidatePasswordUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by okasurya on 9/27/17.
 */

@Module
public class ValidatePasswordModule {
    @ValidatePasswordScope
    @Provides
    ValidatePasswordMapper provideValidatePasswordMapper() {
        return new ValidatePasswordMapper();
    }

    @ValidatePasswordScope
    @Provides
    AccountApi provideAccountApi(@AccountsQualifier Retrofit retrofit) {
        return retrofit.create(AccountApi.class);
    }

    @ValidatePasswordScope
    @Provides
    AccountFactory provideAccountFactory(AccountApi accountApi,
                                         ValidatePasswordMapper validatePasswordMapper) {
        return new AccountFactory(accountApi, validatePasswordMapper);
    }

    @ValidatePasswordScope
    @Provides
    AccountRepository provideAccountRepository(AccountFactory accountFactory) {
        return new AccountRepositoryImpl(accountFactory);
    }

    @ValidatePasswordScope
    @Provides
    ValidatePasswordUseCase provideCheckPasswordUseCase(ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread,
                                                        AccountRepository accountRepository) {
        return new ValidatePasswordUseCase(threadExecutor, postExecutionThread, accountRepository);
    }
}
