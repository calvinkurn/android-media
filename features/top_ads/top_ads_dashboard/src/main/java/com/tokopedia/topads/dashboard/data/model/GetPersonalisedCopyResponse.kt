package com.tokopedia.topads.dashboard.data.model


import com.google.gson.annotations.SerializedName

data class GetPersonalisedCopyResponse(
    @SerializedName("GetPersonalisedCopy")
    val getPersonalisedCopy: GetPersonalisedCopy = GetPersonalisedCopy()
) {
    data class GetPersonalisedCopy(
        @SerializedName("data")
        val getPersonalisedCopyData: GetPersonalisedCopyData = GetPersonalisedCopyData()
    ) {
        data class GetPersonalisedCopyData(
            @SerializedName("credit_performance")
            val creditPerformance: String = "",
            @SerializedName("is_auto_top_up")
            val isAutoTopUp: Boolean = false
        ) {
            var isAutoTopUpSelected: Boolean = false
        }
    }
}
