package com.tokopedia.feedcomponent.onboarding.di

import androidx.fragment.app.Fragment
import com.tokopedia.feedcomponent.di.FragmentKey
import com.tokopedia.feedcomponent.onboarding.util.COMPLETE_STRATEGY
import com.tokopedia.feedcomponent.onboarding.util.TNC_STRATEGY
import com.tokopedia.feedcomponent.onboarding.view.FeedUGCOnboardingParentFragment
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCCompleteOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCTncOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Named

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@Module
abstract class FeedUGCOnboardingModule {

    @Binds
    @IntoMap
    @FragmentKey(FeedUGCOnboardingParentFragment::class)
    abstract fun bindFeedUGCOnboardingParentFragment(fragment: FeedUGCOnboardingParentFragment): Fragment

    /** Strategy */
    @Binds
    @Named(COMPLETE_STRATEGY)
    abstract fun bindFeedUGCCompleteOnboardingStrategy(strategy: FeedUGCCompleteOnboardingStrategy): FeedUGCOnboardingStrategy

    @Binds
    @Named(TNC_STRATEGY)
    abstract fun bindFeedUGCTncOnboardingStrategy(strategy: FeedUGCTncOnboardingStrategy): FeedUGCOnboardingStrategy
}