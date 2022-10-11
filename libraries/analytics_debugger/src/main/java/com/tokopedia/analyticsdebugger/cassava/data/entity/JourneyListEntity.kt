package com.tokopedia.analyticsdebugger.cassava.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JourneyListEntity(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("journey_name")
    val journeyName: String = "",
    @SerializedName("tribe_name")
    val tribeName: String = ""
) {
    class DataResponse(
        @SerializedName("data")
        val data: List<JourneyListEntity> = emptyList()
    )
}