package com.tokopedia.onboarding.di

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.onboarding.domain.model.DynamicOnboardingDataModel
import com.tokopedia.onboarding.domain.usecase.DynamicOnbaordingUseCase
import dagger.Module
import dagger.Provides

@OnboardingScope
@Module
class DynamicOnbaordingUseCaseModule {

    @OnboardingScope
    @Provides
    fun provideGrapqlUseCase(repository: GraphqlRepository): GraphqlUseCase<DynamicOnboardingDataModel> {
        return GraphqlUseCase(repository)
    }

    @OnboardingScope
    @Provides
    fun provideDynamicOnboardingUseCase(rawQueries: Map<String, String>, useCase: GraphqlUseCase<DynamicOnboardingDataModel>): DynamicOnbaordingUseCase {
        return DynamicOnbaordingUseCase(rawQueries, useCase)
    }
}