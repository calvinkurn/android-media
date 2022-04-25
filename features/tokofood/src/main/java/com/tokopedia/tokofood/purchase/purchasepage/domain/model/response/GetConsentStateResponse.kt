package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetConsentStateResponse(
    @SerializedName("data")
    @Expose
    val data: GetConsentStateData = GetConsentStateData()
)

data class GetConsentStateData(
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("is_agreed")
    @Expose
    val isAgreed: Boolean = false,
    @SerializedName("bottomsheet")
    @Expose
    val bottomSheet: GetConsentStateBottomsheet = GetConsentStateBottomsheet()
)

data class GetConsentStateBottomsheet(
    @SerializedName("is_show_bottomsheet")
    @Expose
    val isShowBottomsheet: Boolean = false,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("title")
    @Expose
    val title: String = "",
    @SerializedName("description")
    @Expose
    val description: String = "",
    @SerializedName("terms_and_condition")
    @Expose
    val termsAndCondition: String = ""
)