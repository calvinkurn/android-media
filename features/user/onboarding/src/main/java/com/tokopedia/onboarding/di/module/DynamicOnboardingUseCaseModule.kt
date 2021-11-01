package com.tokopedia.onboarding.di.module

import com.tokopedia.graphql.coroutines.data.GraphqlInteractor
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.onboarding.di.OnboardingScope
import com.tokopedia.onboarding.domain.model.DynamicOnboardingResponseDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
class DynamicOnboardingUseCaseModule {

    @OnboardingScope
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @OnboardingScope
    @Provides
    fun provideGraphQlRepository(): GraphqlRepository = GraphqlInteractor.getInstance().graphqlRepository
}