package com.tokopedia.people.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.content.common.onboarding.di.UGCOnboardingModule
import com.tokopedia.feedcomponent.di.FeedFloatingButtonManagerModule
import com.tokopedia.feedcomponent.di.FeedFragmentFactoryModule
import com.tokopedia.feedcomponent.people.di.PeopleModule
import com.tokopedia.feedcomponent.shoprecom.di.ShopRecomModule
import dagger.Component

/**
 * Created By : Jonathan Darwin on June 14, 2023
 */
@Component(
    modules = [
        UserProfileTestModule::class,
        UserProfileBindTestModule::class,

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
@UserProfileScope
interface UserProfileTestComponent : UserProfileComponent
