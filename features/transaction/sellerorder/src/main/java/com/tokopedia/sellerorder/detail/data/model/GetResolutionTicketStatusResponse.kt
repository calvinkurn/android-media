package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.SerializedName

data class GetResolutionTicketStatusResponse(
    @SerializedName("resolutionGetTicketStatus")
    val resolutionGetTicketStatus: ResolutionGetTicketStatus? = ResolutionGetTicketStatus()
) {
    data class ResolutionGetTicketStatus(
        @SerializedName("data")
        val data: ResolutionData? = ResolutionData(),
        @SerializedName("messageError")
        val messageError: List<String>? = listOf(),
        @SerializedName("status")
        val status: String? = ""
    ) {
        data class ResolutionData(
            @SerializedName("card_title")
            val cardTitle: String? = "",
            @SerializedName("deadline")
            val deadline: Deadline? = Deadline(),
            @SerializedName("description")
            val description: String? = "",
            @SerializedName("profile_picture")
            val profilePicture: String? = "",
            @SerializedName("redirect_path")
            val redirectPath: RedirectPath? = RedirectPath(),
            @SerializedName("resolution_status")
            val resolutionStatus: ResolutionStatus? = ResolutionStatus()
        ) {

            fun shouldShow(): Boolean {
                val hasValidDeadlineData = (deadline?.showDeadline == true
                        && !deadline.datetime.isNullOrBlank()
                        && !deadline.backgroundColor.isNullOrBlank())
                return !cardTitle.isNullOrBlank()
                        && !description.isNullOrBlank()
                        && !profilePicture.isNullOrBlank()
                        && !redirectPath?.android.isNullOrBlank()
                        && !resolutionStatus?.text.isNullOrBlank()
                        && !resolutionStatus?.fontColor.isNullOrBlank()
                        && (deadline?.showDeadline != true || hasValidDeadlineData)
            }

            data class Deadline(
                @SerializedName("background_color")
                val backgroundColor: String? = "",
                @SerializedName("background_color_unify")
                val backgroundColorUnify: String? = "",
                @SerializedName("datetime")
                val datetime: String? = "",
                @SerializedName("show_deadline")
                val showDeadline: Boolean? = false
            )

            data class RedirectPath(
                @SerializedName("android")
                val android: String? = ""
            )

            data class ResolutionStatus(
                @SerializedName("status")
                val status: Int? = -1,
                @SerializedName("text")
                val text: String? = "",
                @SerializedName("font_color")
                val fontColor: String? = ""
            )
        }
    }
}