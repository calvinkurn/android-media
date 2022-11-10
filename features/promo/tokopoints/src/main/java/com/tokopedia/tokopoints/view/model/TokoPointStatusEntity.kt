package com.tokopedia.tokopoints.view.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokoPointStatusEntity(
    @Expose @SerializedName("tier")
    var tier: TokoPointStatusTierEntity = TokoPointStatusTierEntity(),
    @Expose
    @SerializedName("points")
    var points: TokoPointStatusPointsEntity = TokoPointStatusPointsEntity(),
    @SerializedName("fullName")
    var userName: String = "",
    @SerializedName("emptyMessage")
    var emptyMessage: EmptyMessage = EmptyMessage(),
    @SerializedName("cta")
    var cta: TokopointCta = TokopointCta(),
)
