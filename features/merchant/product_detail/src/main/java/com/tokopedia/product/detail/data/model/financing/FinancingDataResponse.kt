package com.tokopedia.product.detail.data.model.financing

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class FinancingDataResponse(
        @SerializedName("ft_installment_calculation")
        val ftInstallmentCalculation: FtInstallmentCalculationDataResponse = FtInstallmentCalculationDataResponse()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readParcelable<FtInstallmentCalculationDataResponse>(FtInstallmentCalculationDataResponse::class.java.classLoader)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(ftInstallmentCalculation, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FinancingDataResponse> {
        override fun createFromParcel(parcel: Parcel): FinancingDataResponse {
            return FinancingDataResponse(parcel)
        }

        override fun newArray(size: Int): Array<FinancingDataResponse?> {
            return arrayOfNulls(size)
        }
    }
}

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

data class FtInstallmentCalcualtionData(
        @SerializedName("credit_card")
        val creditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList(),

        @SerializedName("non_credit_card")
        val nonCreditCardInstallmentData: ArrayList<FtCalculationPartnerData> = ArrayList(),

        @SerializedName("tnc")
        val tncDataList: ArrayList<FtInstallmentTnc> = ArrayList()
) : Parcelable {

    constructor(parcel: Parcel) : this(
            arrayListOf<FtCalculationPartnerData>().apply {
                parcel.readList(this, FtCalculationPartnerData::class.java.classLoader)
            },
            arrayListOf<FtCalculationPartnerData>().apply {
                parcel.readList(this, FtCalculationPartnerData::class.java.classLoader)
            },

            arrayListOf<FtInstallmentTnc>().apply {
                parcel.readList(this, FtInstallmentTnc::class.java.classLoader)
            }
    )

    override fun writeToParcel(dest: Parcel, flags: Int) {

        dest.writeList(creditCardInstallmentData)
        dest.writeList(nonCreditCardInstallmentData)
        dest.writeList(tncDataList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FtInstallmentCalcualtionData> {
        override fun createFromParcel(source: Parcel): FtInstallmentCalcualtionData {

            return FtInstallmentCalcualtionData(source)
        }

        override fun newArray(size: Int): Array<FtInstallmentCalcualtionData?> {

            return arrayOfNulls(size)
        }
    }
}

data class FtInstallmentTnc(

        @SerializedName("tnc_id")
        val tncId: Int,

        @SerializedName("tnc_list")
        val tncList: ArrayList<FtTncData> = ArrayList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            arrayListOf<FtTncData>().apply {
                parcel.readList(this, FtTncData::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tncId)
        parcel.writeList(tncList)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FtTncData> {
        override fun createFromParcel(parcel: Parcel): FtTncData {
            return FtTncData(parcel)
        }

        override fun newArray(size: Int): Array<FtTncData?> {
            return arrayOfNulls(size)
        }
    }
}

data class FtTncData(

        @SerializedName("order")
        val tncOrder: Int,

        @SerializedName("description")
        val tncDescription: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(tncOrder)
        parcel.writeString(tncDescription)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FtTncData> {
        override fun createFromParcel(parcel: Parcel): FtTncData {
            return FtTncData(parcel)
        }

        override fun newArray(size: Int): Array<FtTncData?> {
            return arrayOfNulls(size)
        }
    }
}


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
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt(),
            arrayListOf<CalculationInstallmentData>().apply {
                parcel.readList(this, CalculationInstallmentData::class.java.classLoader)
            },
            arrayListOf<PaymentPartnerInstructionData>().apply {
                parcel.readList(this, PaymentPartnerInstructionData::class.java.classLoader)
            },
            parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(partnerCode)
        parcel.writeString(partnerName)
        parcel.writeString(partnerIcon)
        parcel.writeInt(tncId)
        parcel.writeList(creditCardInstallmentList)
        parcel.writeList(creditCardInstructionList)
        parcel.writeByte(if(expandLayout) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FtCalculationPartnerData> {
        override fun createFromParcel(parcel: Parcel): FtCalculationPartnerData {
            return FtCalculationPartnerData(parcel)
        }

        override fun newArray(size: Int): Array<FtCalculationPartnerData?> {
            return arrayOfNulls(size)
        }
    }

}

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

data class PaymentPartnerInstructionData(
        @SerializedName("order")
        val order: Int,

        @SerializedName("description")
        val description: String,

        @SerializedName("ins_image_url")
        val insImageUrl: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(order)
        parcel.writeString(description)
        parcel.writeString(insImageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PaymentPartnerInstructionData> {
        override fun createFromParcel(parcel: Parcel): PaymentPartnerInstructionData {
            return PaymentPartnerInstructionData(parcel)
        }

        override fun newArray(size: Int): Array<PaymentPartnerInstructionData?> {
            return arrayOfNulls(size)
        }
    }

}