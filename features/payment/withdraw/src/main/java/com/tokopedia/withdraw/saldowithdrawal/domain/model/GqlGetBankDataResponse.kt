package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GqlGetBankDataResponse(
        @SerializedName("GetBankListWDV2")
        @Expose
        var bankAccount: GqlBankListResponse
)


data class GqlBankListResponse(
        @SerializedName("status")
        @Expose
        val status: Int = 0,
        @SerializedName("message")
        @Expose
        val message: String? = null,
        @SerializedName("data")
        @Expose
        var bankAccountList: ArrayList<BankAccount> = arrayListOf(),
        @SerializedName("gopay_data")
        @Expose
        val gopayData: GopayData = GopayData()
)

data class BankAccount(
        @SerializedName("bankID")
        @Expose
        var bankID: Long = 0,

        @SerializedName("accountNo")
        @Expose
        var accountNo: String? = null,

        @SerializedName("bankName")
        @Expose
        var bankName: String? = null,

        @SerializedName("bankAccountID")
        @Expose
        var bankAccountID: Long = 0,

        @SerializedName("minAmount")
        @Expose
        var minAmount: Long = 0,

        @SerializedName("maxAmount")
        @Expose
        var maxAmount: Long = 0,

        @SerializedName("adminFee")
        @Expose
        var adminFee: Long = 0,

        @SerializedName("status")
        @Expose
        var status: Int = 0,

        @SerializedName("isVerifiedAccount")
        @Expose
        var isVerifiedAccount: Long = 0,

        @SerializedName("bankImageUrl")
        @Expose
        var bankImageUrl: String? = null,

        @SerializedName("isDefaultBank")
        @Expose
        var isDefaultBank: Int = 0,

        @SerializedName("accountName")
        @Expose
        var accountName: String? = null,
        @SerializedName("is_fraud")
        @Expose
        var isFraud: Boolean = false,

        @SerializedName("have_rp_program")
        @Expose
        var haveRPProgram: Boolean = false,

        @SerializedName("have_special_offer")
        @Expose
        var haveSpecialOffer: Boolean = false,

        @SerializedName("default_bank_account")
        @Expose
        var defaultBankAccount: Boolean = false,

        @SerializedName("warning_message")
        @Expose
        var warningMessage: String? = null,

        @SerializedName("warning_color")
        @Expose
        var warningColor: Int = 0,

        @SerializedName("notes")
        var notes: String = "",

        @SerializedName("wallet_app_data")
        var walletAppData: WalletAppData = WalletAppData(),

        var isChecked: Boolean = false,

        var gopayData: GopayData? = null
) : Parcelable {

    fun isGopay(): Boolean {
        return bankID == GOPAY_ID
    }

    fun isGopayEligible(): Boolean {
        return bankID == GOPAY_ID && walletAppData.message.isEmpty()
    }
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readLong(),
        parcel.readInt(),
        parcel.readLong(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readParcelable(WalletAppData::class.java.classLoader) ?: WalletAppData(),
        parcel.readByte() != 0.toByte(),
        parcel.readParcelable(GopayData::class.java.classLoader)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(bankID)
        parcel.writeString(accountNo)
        parcel.writeString(bankName)
        parcel.writeLong(bankAccountID)
        parcel.writeLong(minAmount)
        parcel.writeLong(maxAmount)
        parcel.writeLong(adminFee)
        parcel.writeInt(status)
        parcel.writeLong(isVerifiedAccount)
        parcel.writeString(bankImageUrl)
        parcel.writeInt(isDefaultBank)
        parcel.writeString(accountName)
        parcel.writeByte(if (isFraud) 1 else 0)
        parcel.writeByte(if (haveRPProgram) 1 else 0)
        parcel.writeByte(if (haveSpecialOffer) 1 else 0)
        parcel.writeByte(if (defaultBankAccount) 1 else 0)
        parcel.writeString(warningMessage)
        parcel.writeInt(warningColor)
        parcel.writeString(notes)
        parcel.writeParcelable(walletAppData, flags)
        parcel.writeByte(if (isChecked) 1 else 0)
        parcel.writeParcelable(gopayData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BankAccount> {
        private const val GOPAY_ID = 218L
        override fun createFromParcel(parcel: Parcel): BankAccount {
            return BankAccount(parcel)
        }

        override fun newArray(size: Int): Array<BankAccount?> {
            return arrayOfNulls(size)
        }
    }
}

data class GopayData(
    @SerializedName("limit")
    @Expose
    var limit: String = "",
    @SerializedName("limit_copy_writing")
    @Expose
    var limitCopyWriting: String = "",
    @SerializedName("image_url")
    @Expose
    var imageUrl: String = "",
    @SerializedName("widget_note")
    @Expose
    var widgetNote: String = "",
    @SerializedName("bottomsheet_data")
    @Expose
    var bottomsheetData: BottomsheetData = BottomsheetData()
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readParcelable(BottomsheetData::class.java.classLoader) ?: BottomsheetData()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(limit)
        parcel.writeString(limitCopyWriting)
        parcel.writeString(imageUrl)
        parcel.writeString(widgetNote)
        parcel.writeParcelable(bottomsheetData, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GopayData> {
        override fun createFromParcel(parcel: Parcel): GopayData {
            return GopayData(parcel)
        }

        override fun newArray(size: Int): Array<GopayData?> {
            return arrayOfNulls(size)
        }
    }

}

data class BottomsheetData(
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("balance")
    var balance: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(balance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BottomsheetData> {
        override fun createFromParcel(parcel: Parcel): BottomsheetData {
            return BottomsheetData(parcel)
        }

        override fun newArray(size: Int): Array<BottomsheetData?> {
            return arrayOfNulls(size)
        }
    }
}

data class WalletAppData(
    @SerializedName("message")
    @Expose
    var message: String = "",

    @SerializedName("cta_copy_writing")
    @Expose
    var ctaCopyWriting: String = "",

    @SerializedName("cta_link")
    @Expose
    var ctaLink: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(message)
        parcel.writeString(ctaCopyWriting)
        parcel.writeString(ctaLink)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<WalletAppData> {
        override fun createFromParcel(parcel: Parcel): WalletAppData {
            return WalletAppData(parcel)
        }

        override fun newArray(size: Int): Array<WalletAppData?> {
            return arrayOfNulls(size)
        }
    }

}
