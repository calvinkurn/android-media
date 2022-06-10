package com.tokopedia.shopdiscount.common.data.request

import androidx.annotation.StringDef
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.DELETE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.RESERVE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.STOPDELETE

data class DoSlashPriceReservationRequest(
    @SerializedName("request_header")
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("action")
    var action: String = "",
    @SerializedName("request_id")
    var requestId: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("product_data")
    var listProductData: List<SlashPriceReservationProduct> = listOf()
) {

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(RESERVE, DELETE, STOPDELETE)
    annotation class DoSlashPriceReservationAction {
        companion object {
            const val RESERVE = "RESERVE"
            const val DELETE = "DELETE"
            const val STOPDELETE = "STOPDELETE"
        }
    }

    enum class DoSlashPriceReservationState {
        CREATE,
        EDIT
    }

    data class SlashPriceReservationProduct(
        @SerializedName("product_id")
        var productId: String = "",
        @SerializedName("position")
        var position: String = "",
        @SerializedName("warehouse")
        var warehouseId: String = ""
    )
}