package com.tokopedia.people.di

import com.tokopedia.people.analytic.tracker.UserProfileTracker
import com.tokopedia.people.analytic.tracker.UserProfileTrackerImpl
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.data.UserProfileRepositoryImpl
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapper
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapperImpl
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

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileUiMapper(userProfileUiMapper: UserProfileUiMapperImpl): UserProfileUiMapper

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileTracker(userProfileTracker: UserProfileTrackerImpl): UserProfileTracker
}
