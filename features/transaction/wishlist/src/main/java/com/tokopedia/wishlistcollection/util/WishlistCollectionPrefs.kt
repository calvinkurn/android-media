package com.tokopedia.wishlistcollection.util

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class WishlistCollectionPrefs @Inject constructor(val context: Context) {
    companion object {
        const val WISHLIST_COLLECTION_TICKER = "wishlist_collection_first_announcement"
        const val WISHLIST_COLLECTION_TICKER_HAS_CLOSED = "wishlist_collection_first_announcement_has_closed"
    }

    private var sharedPrefs: SharedPreferences? = null

    init {
        sharedPrefs = context.getSharedPreferences(WISHLIST_COLLECTION_TICKER, Context.MODE_PRIVATE)
    }

    fun setHasClosed(hasClosed: Boolean) {
        sharedPrefs?.edit()?.putBoolean(WISHLIST_COLLECTION_TICKER_HAS_CLOSED, hasClosed)?.apply()
    }

    fun getHasClosed(): Boolean? {
        return sharedPrefs?.getBoolean(WISHLIST_COLLECTION_TICKER_HAS_CLOSED, false)
    }
}