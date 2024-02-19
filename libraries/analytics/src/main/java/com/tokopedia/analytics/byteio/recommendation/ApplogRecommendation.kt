package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogAnalytics.addPage
import com.tokopedia.analytics.byteio.EventName
import org.json.JSONObject

/**
 * Byte.io tracking model
 */
data class TrackRecommendationModel (
    val productId: String = "",
    val tabName: String? = null,
    val tabPosition: Int? = null,
    val pageName: String = "",
    val sourceModule: String = "",
    val trackId: String = "",
    val isAd: Boolean = false,
    val isUseCache: Boolean = false,
    val recParams: String = "",
    val requestId: String = "",
    val shopId: String = "",
)

/**
 * Byte.io tracking
 */
object RecommendationApplog {
    fun sendViewApplog(model: TrackRecommendationModel) {
        AppLogAnalytics.send(
            EventName.PRODUCT_SHOW,
            buildViewApplog(model)
        )
    }

    private fun buildViewApplog(model: TrackRecommendationModel): JSONObject {
        return JSONObject().apply {
            // other params
            addPage()
        }
    }
}
