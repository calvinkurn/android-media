package com.tokopedia.pms.paymentlist.domain.data

import com.google.gson.annotations.SerializedName

class PaymentListResponse(
    @SerializedName("paymentList")
    val paymentList: PaymentList
)

class PaymentList(
    @SerializedName("has_next_page")
    val isHasNextPage: Boolean,
    @SerializedName("last_cursor")
    val lastCursor: String?,
    @SerializedName("payment_list")
    val paymentList: List<PaymentListItem>
)

data class PaymentListItem(
    @SerializedName("transaction_id")
    val transactionId: String?,

    @SerializedName("transaction_date")
    val transactionDate: String?,

    @SerializedName("transaction_expire")
    val transactionExpire: String?,

    @SerializedName("transaction_expire_unix")
    val transactionExpireUnix: Long?,

    @SerializedName("merchant_code")
    val merchantCode: String?,

    @SerializedName("payment_amount")
    val paymentAmount: Int?,

    @SerializedName("invoice_url")
    val invoiceUrl: String?,

    @SerializedName("product_name")
    val productName: String?,

    @SerializedName("product_img")
    val productImg: String?,

    @SerializedName("gateway_name")
    val gatewayName: String?,

    @SerializedName("gateway_img")
    val gatewayImg: String?,

    @SerializedName("payment_code")
    val paymentCode: String?,

    @SerializedName("is_va")
    val isIsVa: Boolean?,

    @SerializedName("is_klikbca")
    val isIsKlikbca: Boolean?,

    @SerializedName("bank_img")
    val bankImg: String?,

    @SerializedName("user_bank_account")
    val userBankAccount: UserBankAccount?,

    @SerializedName("dest_bank_account")
    val destBankAccount: DestBankAccount?,

    @SerializedName("show_upload_button")
    val isShowUploadButton: Boolean?,

    @SerializedName("show_edit_transfer_button")
    val isShowEditTransferButton: Boolean?,

    @SerializedName("show_edit_klikbca_button")
    val isShowEditKlikbcaButton: Boolean?,

    @SerializedName("show_cancel_button")
    val isShowCancelButton: Boolean?,

    @SerializedName("show_expire_ticker")
    val isShowExpireTicker: Boolean?,

    @SerializedName("show_help_page")
    val isShowHelpPage: Boolean?,

    @SerializedName("show_ticker_message")
    val isShowTickerMessage: Boolean?,

    @SerializedName("ticker_message")
    val tickerMessage: String?,

    @SerializedName("app_link")
    val appLink: String?,
)

data class DestBankAccount(
    @SerializedName("acc_no")
    val accNo: String?,

    @SerializedName("acc_name")
    val accName: String?
)

data class UserBankAccount(
    @SerializedName("acc_no")
    val accNo: String?,

    @SerializedName("acc_name")
    val accName: String?,

    @SerializedName("bank_id")
    val bankId: Int?
)