package com.tokopedia.cart.view.pref

import android.content.Context
import android.content.SharedPreferences

class CartOnBoardingPreferences(val context: Context) {
    companion object {
        const val HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_DEFAULT_PRODUCT =
            "has_shown_swipe_to_delete_onboarding_default_product"
        const val HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_BUNDLING_PRODUCT =
            "has_shown_swipe_to_delete_onboarding_bundling_product"
        const val CART_PREFERENCES = "cart_preferences"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs =
            context.getSharedPreferences(CART_PREFERENCES, Context.MODE_PRIVATE)
    }

    fun setHasShownSwipeToDeleteDefaultProductOnBoarding() {
        sharedPrefs?.edit()?.putBoolean(HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_DEFAULT_PRODUCT, true)
            ?.apply()
    }

    fun setHasShownSwipeToDeleteBundlingProductOnBoarding() {
        sharedPrefs?.edit()?.putBoolean(HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_BUNDLING_PRODUCT, true)
            ?.apply()
    }

    fun getHasShownSwipeToDeleteDefaultProductOnBoarding(): Boolean {
        return sharedPrefs?.getBoolean(HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_DEFAULT_PRODUCT, false)
            ?: false
    }

    fun getHasShownSwipeToDeleteBundlingProductOnBoarding(): Boolean {
        return sharedPrefs?.getBoolean(HAS_SHOWN_SWIPE_TO_DELETE_ONBOARDING_BUNDLING_PRODUCT, false)
            ?: false
    }
}
