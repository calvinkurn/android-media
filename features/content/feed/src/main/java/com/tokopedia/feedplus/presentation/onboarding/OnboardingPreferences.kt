package com.tokopedia.feedplus.presentation.onboarding

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
interface OnboardingPreferences {

    fun hasShownCreateContent(): Boolean

    fun hasShownProfileEntryPoint(): Boolean

    fun setHasShownCreateContent()

    fun setHasShownProfileEntryPoint()
}
