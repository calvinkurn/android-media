package com.tokopedia.abstraction.processor.model

import com.tokopedia.analytic_constant.Param
import com.tokopedia.annotation.BundleThis
import com.tokopedia.annotation.Key

@BundleThis(false, true)
data class Promotion(
        @Key(Param.ITEM_ID)
        val id: String,
        @Key(Param.ITEM_NAME)
        val name: String,
        @Key(Param.CREATIVE_NAME)
        val creativeName: String,
        @Key(Param.CREATIVE_SLOT)
        val creativeSlot: String
)