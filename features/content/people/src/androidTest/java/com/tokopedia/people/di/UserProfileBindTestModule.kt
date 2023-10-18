package com.tokopedia.people.di

import com.tokopedia.people.analytic.tracker.UserProfileGeneralTracker
import com.tokopedia.people.analytic.tracker.UserProfileGeneralTrackerImpl
import com.tokopedia.people.analytic.tracker.review.UserProfileReviewTracker
import com.tokopedia.people.analytic.tracker.review.UserProfileReviewTrackerImpl
import com.tokopedia.people.data.UserFollowRepository
import com.tokopedia.people.data.UserFollowRepositoryImpl
import com.tokopedia.people.data.UserProfileRepository
import com.tokopedia.people.data.UserProfileRepositoryImpl
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapper
import com.tokopedia.people.views.uimodel.mapper.UserProfileUiMapperImpl
import dagger.Binds
import dagger.Module

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
@Module
abstract class UserProfileBindTestModule {

    @Binds
    @UserProfileScope
    abstract fun bindUserFollowRepository(repository: UserFollowRepositoryImpl): UserFollowRepository

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileUiMapper(userProfileUiMapper: UserProfileUiMapperImpl): UserProfileUiMapper

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileGeneralTracker(userProfileGeneralTracker: UserProfileGeneralTrackerImpl): UserProfileGeneralTracker

    @Binds
    @UserProfileScope
    abstract fun bindUserProfileReviewTracker(userProfileReviewTracker: UserProfileReviewTrackerImpl): UserProfileReviewTracker
}

