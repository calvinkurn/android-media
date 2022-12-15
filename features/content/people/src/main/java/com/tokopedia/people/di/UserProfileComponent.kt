package com.tokopedia.people.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.feedcomponent.di.FeedFloatingButtonManagerModule
import com.tokopedia.feedcomponent.di.FeedFragmentFactoryModule
import com.tokopedia.feedcomponent.people.di.PeopleModule
import com.tokopedia.feedcomponent.shoprecom.di.ShopRecomModule
import com.tokopedia.people.views.activity.FollowerFollowingListingActivity
import com.tokopedia.people.views.activity.UserProfileActivity
import dagger.Component

@UserProfileScope
@Component(
    modules = [
        UserProfileModule::class,
        UserProfileBindModule::class,
        UserProfileViewModelModule::class,
        UserProfileFragmentModule::class,
        UGCOnboardingModule::class,
        FeedFragmentFactoryModule::class,
        FeedFloatingButtonManagerModule::class,
        ShopRecomModule::class,
        PeopleModule::class,
    ],
    dependencies = [BaseAppComponent::class],
)
interface UserProfileComponent {

    fun inject(activity: UserProfileActivity)
    fun inject(activity: FollowerFollowingListingActivity)
}
