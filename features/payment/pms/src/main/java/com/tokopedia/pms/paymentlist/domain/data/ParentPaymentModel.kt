package com.tokopedia.pms.paymentlist.domain.data

open class BasePaymentModel {
    var gatewayName: String = ""
    var gatewayImage: String = ""
    var productImage: String = ""
}


data class VirtualAccountPaymentModel(
    val paymentCode: String,
    var howtoPayAppLink: String,
    var expiryTime: Long,
    var expiryDate: String,
    var totalAmount: Int,
    val transactionList: ArrayList<VaTransactionItem>,
): BasePaymentModel()

data class VaTransactionItem(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
)

data class CreditCardPaymentModel(
    val label: String,
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
): BasePaymentModel()

data class KlicBCAPaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
): BasePaymentModel()

data class StorePaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
): BasePaymentModel()

data class BankTransferPaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
    val senderBankInfo: BankInfo,
    val destinationBankInfo: BankInfo,
): BasePaymentModel()

class BankInfo(
    val accountNumber : String?,
    val accountName: String?
)
