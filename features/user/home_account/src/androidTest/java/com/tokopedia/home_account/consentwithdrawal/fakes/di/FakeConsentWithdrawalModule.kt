package com.tokopedia.home_account.consentwithdrawal.fakes.di

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ApplicationScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentGroupListUseCase
import com.tokopedia.home_account.consentWithdrawal.domain.GetConsentPurposeByGroupUseCase
import com.tokopedia.home_account.consentWithdrawal.domain.SubmitConsentPreferenceUseCase
import com.tokopedia.home_account.consentwithdrawal.fakes.ConsentWithdrawalRepositoryStub
import dagger.Module
import dagger.Provides

@Module
class FakeConsentWithdrawalModule {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideConsentWithdrawalRepositoryStub(): GraphqlRepository {
        return ConsentWithdrawalRepositoryStub()
    }

    @Provides
    @ApplicationScope
    fun provideGetConsentGroupListUseCase(
        @ApplicationContext repositoryStub: ConsentWithdrawalRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetConsentGroupListUseCase {
        return GetConsentGroupListUseCase(repositoryStub, dispatchers)
    }

    @Provides
    @ApplicationScope
    fun provideGetConsentPurposeByGroupUseCase(
        @ApplicationContext repositoryStub: ConsentWithdrawalRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): GetConsentPurposeByGroupUseCase {
        return GetConsentPurposeByGroupUseCase(repositoryStub, dispatchers)
    }

    @Provides
    @ApplicationScope
    fun provideSubmitConsentPreferenceUseCase(
        @ApplicationContext repositoryStub: ConsentWithdrawalRepositoryStub,
        dispatchers: CoroutineDispatchers
    ): SubmitConsentPreferenceUseCase {
        return SubmitConsentPreferenceUseCase(repositoryStub, dispatchers)
    }
}
