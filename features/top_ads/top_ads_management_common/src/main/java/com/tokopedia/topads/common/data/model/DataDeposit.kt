package com.tokopedia.topads.common.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.DataResponse
import com.tokopedia.topads.common.data.response.Error

/**
 * Created by hadi.putra on 23/04/18.
 */

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
    constructor(parcel: Parcel) : this(
            parcel.readFloat(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readParcelable(VoucherShop::class.java.classLoader),
            parcel.readParcelable(FreeDeposit::class.java.classLoader)) {
    }

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

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(amount)
        parcel.writeString(amountFmt)
        parcel.writeByte(if (isAdUsage) 1 else 0)
        parcel.writeParcelable(voucher, flags)
        parcel.writeParcelable(freeDeposit, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DataDeposit> {
        override fun createFromParcel(parcel: Parcel): DataDeposit {
            return DataDeposit(parcel)
        }

        override fun newArray(size: Int): Array<DataDeposit?> {
            return arrayOfNulls(size)
        }
    }
}
