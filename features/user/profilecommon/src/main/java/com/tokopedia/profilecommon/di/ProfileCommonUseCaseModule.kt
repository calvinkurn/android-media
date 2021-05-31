package com.tokopedia.profilecommon.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecommon.domain.usecase.*
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

@Module
class ProfileCommonUseCaseModule {

    @ProfileCommonScope
    @Provides
    fun provideGetUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.QUERY_USER_PROFILE_COMPLETION) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatcher
    ): GetUserProfileCompletionUseCase = GetUserProfileCompletionUseCase(query, graphqlRepository, dispatcher)

    @ProfileCommonScope
    @Provides
    fun provideUpdateUserProfileUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_UPDATE) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatcher
    ): UpdateUserProfileUseCase = UpdateUserProfileUseCase(query, graphqlRepository, dispatcher)

    @ProfileCommonScope
    @Provides
    fun provideValidateUserProfileUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_VALIDATE) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatcher
    ): ValidateUserProfileUseCase = ValidateUserProfileUseCase(query, graphqlRepository, dispatcher)

    @ProfileCommonScope
    @Provides
    fun provideValidateUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_VALIDATE) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatcher
    ): ValidateUserProfileCompletionUseCase = ValidateUserProfileCompletionUseCase(query, graphqlRepository, dispatcher)

    @ProfileCommonScope
    @Provides
    fun provideUpdateUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_UPDATE) query: String,
            graphqlRepository: GraphqlRepository,
            dispatcher: CoroutineDispatcher
    ): UpdateUserProfileCompletionUseCase = UpdateUserProfileCompletionUseCase(query, graphqlRepository, dispatcher)
}