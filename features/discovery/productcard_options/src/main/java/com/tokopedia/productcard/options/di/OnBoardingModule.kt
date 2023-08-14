package com.tokopedia.productcard.options.di

import android.content.Context
import com.tokopedia.productcard.options.onboarding.OnBoardingListenerDelegate
import dagger.Module
import dagger.Provides

@Module
object OnBoardingModule {
    @Provides
    @ProductCardOptionsScope
    @JvmStatic
    fun provideOnBoardingModule(
        @ProductCardOptionsScope
        context: Context,
    ) : OnBoardingListenerDelegate {
        return OnBoardingListenerDelegate(context)
    }
}
