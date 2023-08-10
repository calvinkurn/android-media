package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

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

@Parcelize
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

    companion object {
        private const val GOPAY_ID = 218L
    }
}

@Parcelize
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
): Parcelable

@Parcelize
data class BottomsheetData(
    @SerializedName("title")
    @Expose
    var title: String = "",
    @SerializedName("description")
    @Expose
    var description: String = "",
    @SerializedName("balance")
    var balance: String = ""
): Parcelable

@Parcelize
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
): Parcelable
