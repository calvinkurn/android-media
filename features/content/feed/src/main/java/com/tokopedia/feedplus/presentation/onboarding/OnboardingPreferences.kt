package com.tokopedia.feedplus.presentation.onboarding

/**
 * Created by kenny.hadisaputra on 06/03/23
 */
interface OnboardingPreferences {

    fun hasShownCreateContent(userId: String): Boolean

    fun hasShownProfileEntryPoint(userId: String): Boolean

    fun setHasShownCreateContent(userId: String)

    fun setHasShownProfileEntryPoint(userId: String)
}
