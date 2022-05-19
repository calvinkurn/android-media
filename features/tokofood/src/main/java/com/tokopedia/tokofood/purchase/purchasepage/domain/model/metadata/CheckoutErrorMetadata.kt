package com.tokopedia.tokofood.purchase.purchasepage.domain.model.metadata

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CheckoutErrorMetadata(
    @SerializedName("popup_message")
    @Expose
    val popupMessage: CheckoutErrorMetadataDetail = CheckoutErrorMetadataDetail(),
    @SerializedName("popup_error_message")
    @Expose
    val popupErrorMessage: CheckoutErrorMetadataDetail = CheckoutErrorMetadataDetail()
)

data class CheckoutErrorMetadataDetail(
    @SerializedName("text")
    @Expose
    val text: String = "",
    @SerializedName("action_text")
    @Expose
    val actionText: String = "",
    @SerializedName("action")
    @Expose
    val action: Int = 0,
    @SerializedName("link")
    @Expose
    val link: String = ""
) {

    companion object {
        const val REFRESH_ACTION = 1
        const val REDIRECT_ACTION = 2
    }

}