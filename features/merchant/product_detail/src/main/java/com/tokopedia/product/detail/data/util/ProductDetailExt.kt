package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.Basic
import com.tokopedia.product.detail.common.data.model.Campaign
import com.tokopedia.product.detail.common.data.model.TxStats
import com.tokopedia.product.detail.common.data.model.Video

const val MAX_PERCENT = 100
const val KILO = 1000

val Video.thumbnailUrl: String
    get() = "http://img.youtube.com/vi/$url/1.jpg"

val TxStats.successRate: Float
    get() = if (txSuccess == 0 && txReject == 0) 0f
            else 100f * txSuccess.toFloat()/(txSuccess + txReject).toFloat()

val Basic.weightInKg: Float
    get() = if (weightUnit == 2) weight.toFloat() else weight.toFloat()/ KILO