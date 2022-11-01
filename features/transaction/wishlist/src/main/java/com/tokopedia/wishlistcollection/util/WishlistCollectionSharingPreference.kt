package com.tokopedia.wishlistcollection.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class WishlistCollectionSharingPreference  @Inject constructor(val context: Context) {
    companion object {
        const val HAS_COACHMARK_WISHLIST_SHARING_SHOWN = "has_coachmark_wishlist_sharing_shown"
        const val BOOLEAN_HAS_COACHMARK_SHARING_SHOWN = "boolean_has_coachmark_sharing_shown"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(HAS_COACHMARK_WISHLIST_SHARING_SHOWN, Context.MODE_PRIVATE)
    }

    fun setShown(hasShown: Boolean) {
        sharedPrefs?.edit()?.putBoolean(BOOLEAN_HAS_COACHMARK_SHARING_SHOWN, hasShown)?.apply()
    }

    fun hasCoachmarkSharingShown(): Boolean {
        return sharedPrefs?.getBoolean(BOOLEAN_HAS_COACHMARK_SHARING_SHOWN, false) == true
    }
}
