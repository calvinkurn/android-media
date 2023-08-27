package com.tokopedia.feedplus.presentation.adapter

import android.os.Bundle

/**
 * Created By : Jonathan Darwin on August 08, 2023
 */
class FeedFollowRecommendationPayload {

    var isSelectedChanged: Boolean = false

    fun create(): Bundle {
        return Bundle().apply {
            putBoolean(PAYLOAD_IS_SELECTED_CHANGED, isSelectedChanged)
        }
    }

    companion object {
        private const val PAYLOAD_IS_SELECTED_CHANGED = "PAYLOAD_IS_SELECTED_CHANGED"

        fun isSelectedChanged(bundle: Bundle): Boolean {
            return bundle.getBoolean(PAYLOAD_IS_SELECTED_CHANGED)
        }
    }
}
