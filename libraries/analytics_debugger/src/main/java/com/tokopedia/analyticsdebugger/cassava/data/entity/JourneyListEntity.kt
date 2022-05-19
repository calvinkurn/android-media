package com.tokopedia.analyticsdebugger.cassava.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class JourneyListEntity(
    @SuppressLint("Invalid Data Type")
    @SerializedName("id")
    @Expose
    val id: Int = 0,
    @SerializedName("journey_name")
    @Expose
    val journeyName: String = "",
    @SerializedName("tribe_name")
    @Expose
    val tribeName: String = ""
) {
    class DataResponse(
        @SerializedName("data")
        @Expose
        val data: List<JourneyListEntity> = emptyList()
    )
}