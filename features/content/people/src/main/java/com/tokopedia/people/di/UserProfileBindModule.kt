package com.tokopedia.people.di

import com.tokopedia.people.data.UserProfileRepositoryImpl
import com.tokopedia.people.domains.repository.UserProfileRepository
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 28, 2022
 */
@Module
abstract class UserProfileBindModule {

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileRepository(userProfileRepositoryImpl: UserProfileRepositoryImpl): UserProfileRepository
}
