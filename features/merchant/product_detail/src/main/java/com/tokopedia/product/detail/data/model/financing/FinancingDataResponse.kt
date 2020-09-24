package com.tokopedia.product.detail.data.model.financing

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FtInstallmentCalculationDataResponse(
        @SerializedName("data")
        val data: FtInstallmentCalcualtionData = FtInstallmentCalcualtionData()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable<FtInstallmentCalcualtionData>(FtInstallmentCalcualtionData::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FtInstallmentCalculationDataResponse> {
        override fun createFromParcel(parcel: Parcel): FtInstallmentCalculationDataResponse {
            return FtInstallmentCalculationDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<FtInstallmentCalculationDataResponse?> {
            return arrayOfNulls(size)
        }
    }
}
@Parcelize
data class FtInstallmentCalcualtionData(
        @SerializedName("credit_card")
        val creditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList(),

        @SerializedName("non_credit_card")
        val nonCreditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList(),

        @SerializedName("tnc")
        val tncDataList: ArrayList<FtInstallmentTnc> = ArrayList()
) : Parcelable

@Parcelize
data class FtInstallmentTnc(

        @SerializedName("tnc_id")
        val tncId: Int,

        @SerializedName("tnc_list")
        val tncList: ArrayList<FtTncData> = ArrayList()
) : Parcelable

@Parcelize
data class FtTncData(

        @SerializedName("order")
        val tncOrder: Int,

        @SerializedName("description")
        val tncDescription: String
) : Parcelable

@Parcelize
data class FtCalculationPartnerData(
        @SerializedName("partner_code")
        val partnerCode: String,

        @SerializedName("partner_name")
        val partnerName: String,

        @SerializedName("partner_icon")
        val partnerIcon: String,

        @SerializedName("tnc_id")
        val tncId: Int,

        @SerializedName("installment_list")
        val creditCardInstallmentList: ArrayList<CalculationInstallmentData> = ArrayList(),

        @SerializedName("instruction_list")
        val creditCardInstructionList: ArrayList<PaymentPartnerInstructionData> = ArrayList(),

        var expandLayout: Boolean = false
) : Parcelable

data class CalculationInstallmentData(
        @SerializedName("term")
        val creditCardInstallmentTerm: Int,

        @SerializedName("mdr_value")
        val mdrValue: Float,

        @SerializedName("mdr_type")
        val mdrType: String,

        @SerializedName("interest_rate")
        val interestRate: Float,

        @SerializedName("minimum_amount")
        val minimumAmount: Int,

        @SerializedName("maximum_amount")
        val maximumAmount: Int,

        @SerializedName("monthly_price")
        val monthlyPrice: Float,

        @SerializedName("os_monthly_price")
        val osMonthlyPrice: Float
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readString() ?: "",
            parcel.readFloat(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readFloat(),
            parcel.readFloat())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(creditCardInstallmentTerm)
        parcel.writeFloat(mdrValue)
        parcel.writeString(mdrType)
        parcel.writeFloat(interestRate)
        parcel.writeInt(minimumAmount)
        parcel.writeInt(maximumAmount)
        parcel.writeFloat(monthlyPrice)
        parcel.writeFloat(osMonthlyPrice)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CalculationInstallmentData> {
        override fun createFromParcel(parcel: Parcel): CalculationInstallmentData {
            return CalculationInstallmentData(parcel)
        }

        override fun newArray(size: Int): Array<CalculationInstallmentData?> {
            return arrayOfNulls(size)
        }
    }
}

@Parcelize
data class PaymentPartnerInstructionData(
        @SerializedName("order")
        val order: Int,

        @SerializedName("description")
        val description: String,

        @SerializedName("ins_image_url")
        val insImageUrl: String
) : Parcelable