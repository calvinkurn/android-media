package com.tokopedia.feedcomponent.onboarding.di

import androidx.fragment.app.Fragment
import com.tokopedia.feedcomponent.di.FragmentKey
import com.tokopedia.feedcomponent.onboarding.view.FeedUGCOnboardingParentFragment
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@Module
abstract class FeedUGCOnboardingModule {

    @Binds
    @IntoMap
    @FragmentKey(FeedUGCOnboardingParentFragment::class)
    abstract fun bindFeedUGCOnboardingParentFragment(fragment: FeedUGCOnboardingParentFragment): Fragment

}