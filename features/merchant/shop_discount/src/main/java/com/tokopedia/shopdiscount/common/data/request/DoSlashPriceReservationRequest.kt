package com.tokopedia.shopdiscount.common.data.request

import androidx.annotation.StringDef
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.DELETE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.RESERVE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationAction.Companion.STOPDELETE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationState.Companion.CREATE
import com.tokopedia.shopdiscount.common.data.request.DoSlashPriceReservationRequest.DoSlashPriceReservationState.Companion.EDIT

data class DoSlashPriceReservationRequest(
    @SerializedName("request_header")
    @Expose
    var requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("action")
    @Expose
    var action: String = "",
    @SerializedName("request_id")
    @Expose
    var requestId: String = "",
    @SerializedName("state")
    @Expose
    var state: String = "",
    @SerializedName("product_data")
    @Expose
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

    @Retention(AnnotationRetention.SOURCE)
    @StringDef(CREATE, EDIT)
    annotation class DoSlashPriceReservationState {
        companion object {
            const val CREATE = "CREATE"
            const val EDIT = "EDIT"
        }
    }

    data class SlashPriceReservationProduct(
        @SerializedName("product_id")
        @Expose
        var productId: String = "",
        @SerializedName("position")
        @Expose
        var position: String = "",
        @SerializedName("warehouse")
        @Expose
        var warehouseId: String = ""
    )
}