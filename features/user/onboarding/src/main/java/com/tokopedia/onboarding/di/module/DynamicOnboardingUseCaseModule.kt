package com.tokopedia.onboarding.di.module

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.onboarding.di.OnboardingScope
import com.tokopedia.onboarding.domain.model.DynamicOnboardingResponseDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnboardingUseCase
import dagger.Module
import dagger.Provides

@Module
class DynamicOnboardingUseCaseModule {

    @OnboardingScope
    @Provides
    fun provideGrapqlUseCase(repository: GraphqlRepository): GraphqlUseCase<DynamicOnboardingResponseDataModel> {
        return GraphqlUseCase(repository)
    }

    @OnboardingScope
    @Provides
    fun provideDynamicOnboardingUseCase(rawQueries: Map<String, String>, useCase: GraphqlUseCase<DynamicOnboardingResponseDataModel>): DynamicOnboardingUseCase {
        return DynamicOnboardingUseCase(rawQueries, useCase)
    }
}