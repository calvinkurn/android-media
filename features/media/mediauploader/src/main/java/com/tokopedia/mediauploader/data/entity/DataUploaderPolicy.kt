package com.tokopedia.mediauploader.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataUploaderPolicy(
        @Expose @SerializedName("uploadpedia_policy") val dataPolicy: UploaderPolicy = UploaderPolicy()
)