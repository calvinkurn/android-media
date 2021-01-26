package com.tokopedia.profilecommon.di

import com.tokopedia.profilecommon.domain.query.*
import dagger.Module
import dagger.Provides
import javax.inject.Named

/**
 * Created by Ade Fulki on 2019-12-12.
 * ade.hadian@tokopedia.com
 */

@Module
class ProfileCommonQueryModule {

    @ProfileCommonScope
    @Provides
    @Named(ProfileCommonQueryConstant.QUERY_USER_PROFILE_COMPLETION)
    fun provideQueryUserProfileCompletion(): String = QueryUserProfileCompletion.getQuery()

    @ProfileCommonScope
    @Provides
    @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_UPDATE)
    fun provideMutationUserProfileUpdate(): String = MutationUserProfileUpdate.getQuery()

    @ProfileCommonScope
    @Provides
    @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_VALIDATE)
    fun provideMutationUserProfileValidate(): String = MutationUserProfileValidate.getQuery()

    @ProfileCommonScope
    @Provides
    @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_VALIDATE)
    fun provideMutationUserProfileCompletionValidate(): String = MutationUserProfileCompletionValidate.getQuery()

    @ProfileCommonScope
    @Provides
    @Named(ProfileCommonQueryConstant.MUTATION_USER_PROFILE_COMPLETION_UPDATE)
    fun provideMutationUserProfileCompletionUpdate(): String = MutationUserProfileCompletionUpdate.getQuery()
}