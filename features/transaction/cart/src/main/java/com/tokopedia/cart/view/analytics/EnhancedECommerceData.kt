package com.tokopedia.cart.view.analytics

import java.util.*

/**
 * Created by Irfan Khoirul on 2019-09-03.
 */

class EnhancedECommerceData {
    private val data = HashMap<String, Any>()

    init {
        this.data.put(KEY_CURRENCY_CODE, DATA_CURRENCY_IDR)
    }

    fun setClickData(clickData: Map<String, Any>) {
        this.data.put(KEY_CLICK, clickData)
    }

    fun setImpressionData(impressionDataList: List<Map<String, Any>>) {
        this.data.put(KEY_IMPRESSION, impressionDataList)
    }

    fun getData(): Map<String, Any> {
        return this.data
    }

    companion object {
        private val KEY_CLICK = "click"
        private val KEY_IMPRESSION = "impressions"
        private val KEY_CURRENCY_CODE = "currencyCode"
        private val DATA_CURRENCY_IDR = "IDR"
    }
}
