package com.tokopedia.people.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.feedcomponent.di.FeedFloatingButtonManagerModule
import com.tokopedia.people.views.fragment.FollowerFollowingListingFragment
import com.tokopedia.people.views.fragment.FollowerListingFragment
import com.tokopedia.people.views.fragment.FollowingListingFragment
import com.tokopedia.people.views.fragment.UserProfileFragment
import com.tokopedia.feedcomponent.di.FeedFragmentFactoryModule
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity
import com.tokopedia.people.views.activity.UserProfileActivity
import dagger.Component

@UserProfileScope
@Component(modules = [
    UserProfileModule::class,
    UserProfileBindModule::class,
    UserProfileViewModelModule::class,
    UserProfileFragmentModule::class,
    FeedFragmentFactoryModule::class,
    FeedFloatingButtonManagerModule::class,
], dependencies = [BaseAppComponent::class])
interface UserProfileComponent {
    fun inject(activity: UserProfileActivity)
    fun inject(activity: FollowerFollowingListingActivity)

    fun inject(fragment: UserProfileFragment)
    fun inject(fragment: FollowerFollowingListingFragment)
    fun inject(fragment: FollowingListingFragment)
    fun inject(fragment: FollowerListingFragment)
}
