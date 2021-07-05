package com.tokopedia.homenav.mainnav.data.pojo.membership

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MembershipPojo(
        @SerializedName("tokopoints")
        @Expose
        val tokopoints: TokopointsPojo = TokopointsPojo()
)

data class TokopointsPojo(
        @SerializedName("status")
        @Expose
        val status: TokopointStatusPojo = TokopointStatusPojo()
)

data class TokopointStatusPojo(
        @SerializedName("tier")
        @Expose
        val tier: TierPojo = TierPojo()
)

data class TierPojo(
        @SerializedName("id")
        @Expose
        val id: Int = 0,
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("nameDesc")
        @Expose
        val nameDesc: String = "",

        @SerializedName("eggImageHomepageURL")
        @Expose
        val imageUrl: String = "",

        @SerializedName("eggImageURL")
        @Expose
        val eggImageURL: String = ""
)