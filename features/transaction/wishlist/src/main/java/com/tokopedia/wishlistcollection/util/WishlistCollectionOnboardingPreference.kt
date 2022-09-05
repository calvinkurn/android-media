package com.tokopedia.wishlistcollection.util

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_GRID
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST
import com.tokopedia.wishlist.util.WishlistV2Consts.TYPE_LIST_INT
import javax.inject.Inject

class WishlistCollectionOnboardingPreference  @Inject constructor(val context: Context) {
    companion object {
        const val HAS_ONBOARDING_WISHLIST_COLLECTION_SHOWN = "has_onboarding_wishlist_collection_shown"
        const val BOOLEAN_HAS_ONBOARDING_SHOWN = "boolean_has_onboarding_shown"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(HAS_ONBOARDING_WISHLIST_COLLECTION_SHOWN, Context.MODE_PRIVATE)
    }

    fun setShown(hasShown: Boolean) {
        sharedPrefs?.edit()?.putBoolean(BOOLEAN_HAS_ONBOARDING_SHOWN, hasShown)?.apply()
    }

    fun hasOnboardingShown(): Boolean {
        return sharedPrefs?.getBoolean(BOOLEAN_HAS_ONBOARDING_SHOWN, false) == true
    }
}