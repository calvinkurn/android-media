package com.tokopedia.feedplus.di

import com.tokopedia.feedplus.data.sharedpref.FeedTooltipPreferences
import com.tokopedia.feedplus.data.sharedpref.FeedTooltipPreferencesImpl
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManager
import com.tokopedia.feedplus.presentation.tooltip.FeedTooltipManagerImpl
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferences
import com.tokopedia.feedplus.presentation.onboarding.OnBoardingPreferencesImpl
import dagger.Binds
import dagger.Module

/**
 * Created by Jonathan Darwin on 24 April 2024
 */
@Module
abstract class FeedBindModule {

    @FeedMainScope
    @Binds
    abstract fun bindOnBoardingPreferences(preferences: OnBoardingPreferencesImpl): OnBoardingPreferences

    @FeedMainScope
    @Binds
    abstract fun bindFeedTooltipPreferences(preferences: FeedTooltipPreferencesImpl): FeedTooltipPreferences

    @FeedMainScope
    @Binds
    abstract fun bindFeedTooltipManager(manager: FeedTooltipManagerImpl): FeedTooltipManager
}
