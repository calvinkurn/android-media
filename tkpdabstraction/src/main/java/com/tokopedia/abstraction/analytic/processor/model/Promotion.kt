package com.example.googletagmanagerwithannotation.enhancedecommerce.models.ecommerce

import com.example.annotation.BundleThis
import com.example.annotation.Key
import com.tokopedia.analytic_constant.Param

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