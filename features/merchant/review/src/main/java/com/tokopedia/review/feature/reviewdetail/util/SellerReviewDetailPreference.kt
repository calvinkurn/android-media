package com.tokopedia.review.feature.reviewdetail.util

import android.content.Context
import android.content.SharedPreferences

class SellerReviewDetailPreference(context: Context?) {

    private var sharedPrefs: SharedPreferences? = null

    companion object {
        private const val SELLER_REVIEW_PREF = "seller_review.pref"
        private const val SELLER_REVIEW_RATING_DISCLAIMER_KEY = "seller_review_disclaimer: %s"
        const val TICKER_INITIAL_VALUE = true
    }

    init {
        this.sharedPrefs = context?.getSharedPreferences(SELLER_REVIEW_PREF, Context.MODE_PRIVATE)
    }

    fun updateSharedPrefs(productId: String) {
        sharedPrefs?.run {
            edit().putBoolean(getKey(productId), false).apply()
        }
    }

    fun shouldShowTicker(productId: String): Boolean {
        return sharedPrefs?.getBoolean(getKey(productId), TICKER_INITIAL_VALUE) ?: false
    }

    private fun getKey(productId: String): String {
        return String.format(SELLER_REVIEW_RATING_DISCLAIMER_KEY, productId)
    }
}