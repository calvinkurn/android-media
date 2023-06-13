package com.tokopedia.search.result.product.samesessionrecommendation

import android.content.Context
import com.tokopedia.kotlin.extensions.long

class SameSessionRecommendationPreferenceImpl(
    context: Context
) : SameSessionRecommendationPreference {
    private val sharedPref = context.getSharedPreferences(
        SEARCH_SAME_SESSION_RECOMMENDATION_PREFERENCE,
        Context.MODE_PRIVATE
    )

    override var hideRecommendationTimestamp: Long by sharedPref.long(
        0L,
        HIDE_RECOMMENDATION_TIMESTAMP
    )

    companion object {
        private const val SEARCH_SAME_SESSION_RECOMMENDATION_PREFERENCE =
            "search_same_session_recommendation_preference"

        private const val HIDE_RECOMMENDATION_TIMESTAMP = "hide_recommendation_timestamp"
    }
}
