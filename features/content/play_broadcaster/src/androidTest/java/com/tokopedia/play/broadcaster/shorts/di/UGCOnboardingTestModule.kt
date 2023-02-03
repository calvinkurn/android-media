package com.tokopedia.play.broadcaster.shorts.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.content.common.onboarding.data.UGCOnboardingRepositoryImpl
import com.tokopedia.content.common.onboarding.di.qualifier.CompleteStrategy
import com.tokopedia.content.common.onboarding.di.qualifier.TncStrategy
import com.tokopedia.content.common.onboarding.domain.repository.UGCOnboardingRepository
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserCompleteOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.bottomsheet.UserTnCOnboardingBottomSheet
import com.tokopedia.content.common.onboarding.view.strategy.UGCCompleteOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.strategy.UGCTncOnboardingStrategy
import com.tokopedia.content.common.onboarding.view.strategy.base.UGCOnboardingStrategy
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created By : Jonathan Darwin on December 12, 2022
 */
@Module
abstract class UGCOnboardingTestModule {

    /** Bottom Sheet */
    @Binds
    @IntoMap
    @FragmentKey(UserCompleteOnboardingBottomSheet::class)
    abstract fun bindFeedUserCompleteOnboardingBottomSheet(fragment: UserCompleteOnboardingBottomSheet): Fragment

    @Binds
    @IntoMap
    @FragmentKey(UserTnCOnboardingBottomSheet::class)
    abstract fun bindFeedUserTnCOnboardingBottomSheet(fragment: UserTnCOnboardingBottomSheet): Fragment

    /** Strategy */
    @Binds
    @CompleteStrategy
    abstract fun bindFeedUGCCompleteOnboardingStrategy(strategy: UGCCompleteOnboardingStrategy): UGCOnboardingStrategy

    @Binds
    @TncStrategy
    abstract fun bindFeedUGCTncOnboardingStrategy(strategy: UGCTncOnboardingStrategy): UGCOnboardingStrategy
}
