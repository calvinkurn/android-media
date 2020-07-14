package com.tokopedia.remoteconfig.abtest.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AbTestVariantPojo (
        @SerializedName("RolloutFeatureVariants")
        @Expose
        var dataRollout: RolloutFeatureVariants = RolloutFeatureVariants()
)

data class RolloutFeatureVariants (
        @SerializedName("featureVariants")
        @Expose
        var featureVariants: List<FeatureVariant>? = ArrayList(),
        @SerializedName("globalRev")
        @Expose
        var globalRev: Int = 0,
        @SerializedName("status")
        @Expose
        var status: Boolean = false
)

data class FeatureVariant (
        @SerializedName("feature")
        @Expose
        var feature: String = "",
        @SerializedName("variant")
        @Expose
        var variant: String = ""
)

data class FeatureVariantAnalytics (
    @SerializedName("name")
    @Expose
    val name: String,
    @SerializedName("variant")
    @Expose
    var variant: String)