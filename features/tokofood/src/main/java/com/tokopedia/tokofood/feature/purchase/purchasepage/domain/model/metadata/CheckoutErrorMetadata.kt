package com.tokopedia.tokofood.feature.purchase.purchasepage.domain.model.metadata

import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class CheckoutErrorMetadata(
    @SerializedName("popup_message")
    val popupMessage: CheckoutErrorMetadataDetail = CheckoutErrorMetadataDetail(),
    @SerializedName("popup_error_message")
    val popupErrorMessage: CheckoutErrorMetadataDetail = CheckoutErrorMetadataDetail()
)

data class CheckoutErrorMetadataDetail(
    @SerializedName("text")
    val text: String = "",
    @SerializedName("action_text")
    val actionText: String = "",
    @SerializedName("action")
    val action: Int = Int.ZERO,
    @SerializedName("link")
    val link: String = ""
) {

    companion object {
        const val REFRESH_ACTION = 1
        const val REDIRECT_ACTION = 2
    }

}