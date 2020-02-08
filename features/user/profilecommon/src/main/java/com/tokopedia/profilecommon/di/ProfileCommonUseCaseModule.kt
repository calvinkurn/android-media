package com.tokopedia.profilecommon.di

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.profilecommon.domain.usecase.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

@ProfileCommonScope
@Module
class ProfileCommonUseCaseModule {

    @ProfileCommonScope
    @Provides
    fun provideGetUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.QUERY_USER_PROFILE_COMPLETION) query: String,
            graphqlRepository: GraphqlRepository
    ): GetUserProfileCompletionUseCase = GetUserProfileCompletionUseCase(query, graphqlRepository)

    @ProfileCommonScope
    @Provides
    fun provideUpdateUserProfileUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_UPDATE) query: String,
            graphqlRepository: GraphqlRepository
    ): UpdateUserProfileUseCase = UpdateUserProfileUseCase(query, graphqlRepository)

    @ProfileCommonScope
    @Provides
    fun provideValidateUserProfileUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_VALIDATE) query: String,
            graphqlRepository: GraphqlRepository
    ): ValidateUserProfileUseCase = ValidateUserProfileUseCase(query, graphqlRepository)

    @ProfileCommonScope
    @Provides
    fun provideValidateUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_VALIDATE) query: String,
            graphqlRepository: GraphqlRepository
    ): ValidateUserProfileCompletionUseCase = ValidateUserProfileCompletionUseCase(query, graphqlRepository)

    @ProfileCommonScope
    @Provides
    fun provideUpdateUserProfileCompletionUseCase(
            @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_UPDATE) query: String,
            graphqlRepository: GraphqlRepository
    ): UpdateUserProfileCompletionUseCase = UpdateUserProfileCompletionUseCase(query, graphqlRepository)
}