package com.tokopedia.people.di

import com.tokopedia.people.data.UserProfileRepositoryImpl
import com.tokopedia.people.domains.repository.UserProfileRepository
import dagger.Binds
import dagger.Module

@Module
abstract class UserProfileModule {

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileRepository(userProfileRepositoryImpl: UserProfileRepositoryImpl): UserProfileRepository
}
