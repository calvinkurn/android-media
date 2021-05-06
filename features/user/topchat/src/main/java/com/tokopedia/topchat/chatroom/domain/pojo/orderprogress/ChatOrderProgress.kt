package com.tokopedia.topchat.chatroom.domain.pojo.orderprogress


import com.google.gson.annotations.SerializedName

data class ChatOrderProgress(
        @SerializedName("button")
        val button: Button = Button(),
        @SerializedName("enable")
        val enable: Boolean = false,
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("invoiceId")
        val invoiceId: String = "",
        @SerializedName("label")
        val label: Label = Label(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("orderId")
        val orderId: String = "",
        @SerializedName("shipment")
        val shipment: Shipment = Shipment(),
        @SerializedName("state")
        val state: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("uri")
        val uri: String = ""
) {

    val isCtaTrack: Boolean get() = button.key == ctaTrack
    val isCtaFinish: Boolean get() = button.key == ctaFinishOrder
    val hasActionButton: Boolean get() = button.key.isNotEmpty()
    val ctaType: String get() {
        return when (button.key) {
            ctaTrack -> "lacak"
            ctaFinishOrder -> "selesai"
            ctaReview -> "review"
            else -> "unknown cta type"
        }
    }

    fun isNotEmpty(): Boolean = state != stateEmpty
    fun isStateFinished(): Boolean = state == stateFinish

    companion object {
        const val stateEmpty = "empty"
        const val stateNew_order = "new_order"
        const val stateOngoing = "on_going"
        const val stateFinish = "finish"

        const val ctaBuyAgain = "buy_again"
        const val ctaFinishOrder = "finish_order"
        const val ctaTrack = "track"
        const val ctaReview = "review"

        const val DEFAULT_STATE = stateEmpty
    }
}