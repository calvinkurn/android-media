package com.tokopedia.product.detail.data.util

import com.tokopedia.product.detail.common.data.model.Basic
import com.tokopedia.product.detail.common.data.model.TxStats
import com.tokopedia.product.detail.common.data.model.Video

const val KG = "kilogram"
const val KILO = 1000

const val LABEL_KG = "Kg"
const val LABEL_GRAM = "gram"

val Video.thumbnailUrl: String
    get() = "https://img.youtube.com/vi/$url/1.jpg"

val TxStats.successRate: Float
    get() = if (txSuccess == 0 && txReject == 0) 0f
            else 100f * txSuccess.toFloat()/(txSuccess + txReject).toFloat()

val Basic.weightInKg: Float
    get() = if (weightUnit.toLowerCase() == KG) weight else weight/ KILO