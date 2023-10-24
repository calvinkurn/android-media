package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.common.data.response.Error
import kotlinx.parcelize.Parcelize

/**
 * Created by hadi.putra on 23/04/18.
 */
@Parcelize
data class DataDeposit (
    @SerializedName("amount")
    @Expose
    val amount: Float = 0f,
    @SerializedName("amount_fmt")
    @Expose
    val amountFmt: String = "",
    @SerializedName("ad_usage")
    @Expose
    val isAdUsage: Boolean = false,
    @SerializedName("voucher")
    @Expose
    val voucher: VoucherShop = VoucherShop(),
    @SerializedName("free_deposit")
    @Expose
    val freeDeposit: FreeDeposit = FreeDeposit()
): Parcelable {

    data class Response(
            @SerializedName("topadsDashboardDeposits")
            @Expose
            val dataResponse: DataErrorResponse = DataErrorResponse()
    )

    data class DataErrorResponse(
            @SerializedName("data")
            @Expose
            val dataDeposit: DataDeposit = DataDeposit(),
            @SerializedName("errors")
            @Expose
            val errors: List<Error> = listOf()
    )
}
