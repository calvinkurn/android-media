package com.tokopedia.remoteconfig.abtest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AbTestUserVariantPojo (
        @SerializedName("RolloutUserVariant")
        @Expose
        var dataRollout: RolloutUserVariants = RolloutUserVariants()
)

data class RolloutUserVariants (
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
