package com.tokopedia.mediauploader.common.data.entity

import com.google.gson.annotations.SerializedName

data class DataSourcePolicy(
    @SerializedName(
        "uploadpedia_policy",
        alternate = ["uploadpedia_secure_policy"]
    )
    val dataPolicy: UploaderPolicy = UploaderPolicy()
) {

    // simplify sub-class access of sourcePolicy
    fun sourcePolicy() = dataPolicy.sourcePolicy

}
