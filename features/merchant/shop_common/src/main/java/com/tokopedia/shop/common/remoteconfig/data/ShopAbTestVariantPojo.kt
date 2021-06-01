package com.tokopedia.shop.common.remoteconfig.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopAbTestVariantPojo (
        @SerializedName("RolloutUserVariant")
        @Expose
        var dataRollout: ShopRolloutFeatureVariants = ShopRolloutFeatureVariants()
)

data class ShopRolloutFeatureVariants (
        @SerializedName("featureVariants")
        @Expose
        var featureVariants: List<ShopFeatureVariant>? = ArrayList(),
        @SerializedName("message")
        @Expose
        var message: String = ""
)

data class ShopFeatureVariant (
        @SerializedName("feature")
        @Expose
        var feature: String = "",
        @SerializedName("variant")
        @Expose
        var variant: String = ""
)