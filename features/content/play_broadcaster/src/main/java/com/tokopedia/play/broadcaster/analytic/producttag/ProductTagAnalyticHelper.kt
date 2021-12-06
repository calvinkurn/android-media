package com.tokopedia.play.broadcaster.analytic.producttag

import android.util.Log
import com.tokopedia.play.broadcaster.analytic.PlayBroadcastAnalytic
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel

/**
 * Created By : Jonathan Darwin on December 06, 2021
 */
class ProductTagAnalyticHelper(
    val analytic: PlayBroadcastAnalytic
) {

    private var channelId: String = ""
    private var product: ProductContentUiModel? = null
    private var position: Int = -1

    fun trackScrollProduct(channelId: String, product: ProductContentUiModel, position: Int) {
        if(position > this.position) {
            this.channelId = channelId
            this.product = product
            this.position = position
        }
    }

    fun sendTrackingProduct() {
        product?.let {
            Log.d("<LOG>", "Send Impression : $channelId | $product | $position")
//            analytic.scrollProductTag(channelId, it, position)
            clearData()
        }
    }

    private fun clearData() {
        product = null
    }
}