package com.tokopedia.gamification.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseCrackResultEntity(
    @SerializedName("crackResult")
    @Expose
    var crackResultEntity: CrackResultEntity = CrackResultEntity(),
)
