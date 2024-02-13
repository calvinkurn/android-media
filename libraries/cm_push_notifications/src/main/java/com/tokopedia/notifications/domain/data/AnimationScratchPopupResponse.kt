package com.tokopedia.notifications.domain.data

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@SuppressLint("Invalid Data Type")
data class AnimationScratchPopupResponse(
    @SerializedName("gamiScratchCardPreEvaluate")
    @Expose
    val gamiScratchCardPreEvaluate: GamiScratchCardPreEvaluate = GamiScratchCardPreEvaluate()
)

data class GamiScratchCardPreEvaluate(
    @SerializedName("resultStatus")
    @Expose
    val resultStatus: ResultStatus? = ResultStatus(),
    @SerializedName("scratchCard")
    @Expose
    val scratchCard: ScratchCard = ScratchCard(),
    @SerializedName("popUpContent")
    @Expose
    val popUpContent: PopUpContent? = PopUpContent(),
)

data class ResultStatus(
    @SerializedName("code")
    @Expose
    val code: String? = "",
    @SerializedName("reason")
    @Expose
    val reason: String? = "",
    @SerializedName("message")
    @Expose
    val message: List<String>? = emptyList()
)

data class ScratchCard(
    @SerializedName("id")
    @Expose
    val id: Long? = 0,
    @SerializedName("name")
    @Expose
    val name: String? = "",
    @SerializedName("description")
    @Expose
    val description: String? = "",
    @SerializedName("slug")
    @Expose
    val slug: String? = "",
    @SerializedName("startTime")
    @Expose
    val startTime: String? = "",
    @SerializedName("endTime")
    @Expose
    val endTime: String? = ""
)

data class PopUpContent(
    @SerializedName("isShown")
    @Expose
    val isShown: Boolean? = true,
    @SerializedName("assets")
    @Expose
    val assets: List<Assets>? = emptyList()
)

data class Assets(
    @SerializedName("key")
    @Expose
    val key: Boolean? = true,
    @SerializedName("value")
    @Expose
    val value: String? = ""
)

