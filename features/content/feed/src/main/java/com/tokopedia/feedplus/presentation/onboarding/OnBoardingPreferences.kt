package com.tokopedia.feedplus.presentation.onboarding

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
interface OnBoardingPreferences {

    fun hasShownCreateContent(): Boolean

    fun hasShownProfileEntryPoint(): Boolean

    fun hasShownSwipeOnBoarding(): Boolean

    fun setHasShownCreateContent()

    fun setHasShownProfileEntryPoint()

    fun setHasShownSwipeOnBoarding()
}
