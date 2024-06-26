package com.tokopedia.pms.paymentlist.domain.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class BasePaymentModel : Parcelable {
    var paymentCode: String = ""
    var gatewayName: String = ""
    var gatewayImage: String = ""
    var productImage: String = ""
    var howtoPayAppLink: String = ""
    var actionList = ArrayList<TransactionActionType>()
    var invoiceDetailUrl: String = ""
    var shouldShowHowToPay: Boolean = false
}

@Parcelize
data class VirtualAccountPaymentModel(
    var expiryTime: Long,
    var expiryDate: String,
    var totalAmount: Int,
    val transactionList: ArrayList<VaTransactionItem>,
) : BasePaymentModel(), Parcelable

@Parcelize
data class VaTransactionItem(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val invoiceUrl: String,
    val productName: String,
) : Parcelable

@Parcelize
data class CreditCardPaymentModel(
    val label: String,
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val productName: String,
    val paymentUrl:String?,
) : BasePaymentModel(), Parcelable

@Parcelize
data class KlicBCAPaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val productName: String,
) : BasePaymentModel(), Parcelable

@Parcelize
data class StorePaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val productName: String,
) : BasePaymentModel(), Parcelable

@Parcelize
data class BankTransferPaymentModel(
    val transactionId: String,
    val merchantCode: String,
    val expiryTime: Long,
    val expiryDate: String,
    val amount: Int,
    val showCancelButton: Boolean,
    val productName: String,
    val senderBankInfo: BankInfo,
    val destinationBankInfo: BankInfo,
) : BasePaymentModel(), Parcelable

@Parcelize
class BankInfo(
    val accountNumber: String?,
    val accountName: String?,
    val bankId: String?
): Parcelable

typealias TransactionCodes = Pair<String, String>
typealias PaymentCode = String?
typealias VaList = ArrayList<Int>
typealias CumulativeVaRecord = HashMap<PaymentCode, VaList>

fun BasePaymentModel.extractValues(): TransactionCodes {
    return when (this) {
        is CreditCardPaymentModel -> TransactionCodes(transactionId, merchantCode)
        is KlicBCAPaymentModel -> TransactionCodes(transactionId, merchantCode)
        is StorePaymentModel -> TransactionCodes(transactionId, merchantCode)
        is BankTransferPaymentModel -> TransactionCodes(transactionId, merchantCode)
        is VirtualAccountPaymentModel -> TransactionCodes(
            transactionList.getOrNull(0)?.transactionId ?: "",
            transactionList.getOrNull(0)?.merchantCode ?: ""
        )
        else -> TransactionCodes("", "")
    }
}

open class TransactionActionType(var actionName: String = "", var actionIcon: Int = -1)
data class EditKlicBCA(val actionType: Int) : TransactionActionType()
data class EditBankTransfer(val actionType: Int) : TransactionActionType()
data class UploadProof(val actionType: Int) : TransactionActionType()
data class CancelTransaction(val actionType: Int) : TransactionActionType()

