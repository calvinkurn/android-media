package com.tokopedia.feedcomponent.onboarding.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.feedcomponent.onboarding.data.FeedUGCOnboardingRepositoryImpl
import com.tokopedia.feedcomponent.onboarding.di.qualifier.CompleteStrategy
import com.tokopedia.feedcomponent.onboarding.di.qualifier.TncStrategy
import com.tokopedia.feedcomponent.onboarding.domain.repository.FeedUGCOnboardingRepository
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.FeedUserCompleteOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.bottomsheet.FeedUserTnCOnboardingBottomSheet
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCCompleteOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.strategy.FeedUGCTncOnboardingStrategy
import com.tokopedia.feedcomponent.onboarding.view.strategy.base.FeedUGCOnboardingStrategy
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on July 04, 2022
 */
@Module
abstract class FeedUGCOnboardingModule {

    /** Bottom Sheet */
    @Binds
    @IntoMap
    @FragmentKey(FeedUserCompleteOnboardingBottomSheet::class)
    abstract fun bindFeedUserCompleteOnboardingBottomSheet(fragment: FeedUserCompleteOnboardingBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(FeedUserTnCOnboardingBottomSheet::class)
    abstract fun bindFeedUserTnCOnboardingBottomSheet(fragment: FeedUserTnCOnboardingBottomSheet): Fragment

    /** Repository */
    @Binds
    abstract fun bindFeedUGCOnboardingRepositoryImpl(repo: FeedUGCOnboardingRepositoryImpl): FeedUGCOnboardingRepository

    /** Strategy */
    @Binds
    @CompleteStrategy
    abstract fun bindFeedUGCCompleteOnboardingStrategy(strategy: FeedUGCCompleteOnboardingStrategy): FeedUGCOnboardingStrategy

    @Binds
    @TncStrategy
    abstract fun bindFeedUGCTncOnboardingStrategy(strategy: FeedUGCTncOnboardingStrategy): FeedUGCOnboardingStrategy


}