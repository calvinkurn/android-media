package com.tokopedia.people.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedcomponent.di.FeedFloatingButtonManagerModule
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment
import com.tokopedia.people.views.fragment.FollowerListingFragment
import com.tokopedia.people.views.fragment.FollowingListingFragment
import com.tokopedia.people.views.fragment.UserProfileFragment
import dagger.Component

@UserProfileScope
@Component(modules = [
    UserProfileModule::class,
    UserProfileBindModule::class,
    UserProfileViewModelModule::class,
    FeedFloatingButtonManagerModule::class,
], dependencies = [BaseAppComponent::class])
interface UserProfileComponent {
    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: FollowerFollowingListingFragment)
    fun inject(fragment: FollowingListingFragment)
    fun inject(fragment: FollowerListingFragment)
}
