package com.tokopedia.pms.paymentlist.domain.usecase

import android.content.Context
import android.text.TextUtils
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.pms.R
import com.tokopedia.pms.paymentlist.domain.data.*
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class PaymentListMapperUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
) : UseCase<ArrayList<BasePaymentModel>>() {
    private val PARAM_PAYMENT_LIST = "param_payment_list"

    fun mapResponseToRenderPaymentList(
        paymentList: List<PaymentListInside>,
        onSuccess: (ArrayList<BasePaymentModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_PAYMENT_LIST, paymentList)
        }
        execute({ onSuccess(it) }, { onError(it) }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): ArrayList<BasePaymentModel> {
        val list: List<PaymentListInside> =
            (useCaseRequestParams.getObject(PARAM_PAYMENT_LIST) as List<PaymentListInside>)
        return mapList(list)
    }

    private fun mapList(paymentList: List<PaymentListInside>): ArrayList<BasePaymentModel> {
        val paymentListModels = arrayListOf<BasePaymentModel>()
        val indexMap = HashMap<String, ArrayList<Int>>()
        var indexCount = 0
        for (paymentModel in paymentList) {
            paymentModel.apply {

                val model: BasePaymentModel? = if (isIsVa) {
                    if (indexMap.containsKey(paymentCode)) {
                        val combinedVaResult = combinedVirtualAccountModel(
                            indexMap,
                            this, paymentListModels
                        )
                        if (combinedVaResult != null && gatewayName != combinedVaResult.gatewayName) {
                            (indexMap[paymentCode] ?: arrayListOf()).add(indexCount)
                            indexCount++
                            combinedVaResult
                        } else {
                            null
                        }
                    } else {
                        val vaListItem = getVirtualAccountTransactionItem(this)
                        val vaPaymentModel =
                            getVirtualAccountPaymentModel(this, arrayListOf(vaListItem))
                        // remember VA index in result result
                        indexMap[paymentCode] = arrayListOf(indexCount)
                        indexCount++
                        vaPaymentModel
                    }
                } else if (isIsKlikbca) {
                    val klicBCA = getKlicBCAPaymentModel(this)
                    indexCount++
                    klicBCA
                } else if (isBankTransfer(this)) {
                    val bankTransferModel = getBankTransferPaymentModel(this)
                    indexCount++
                    bankTransferModel
                } else if (isStoreTransfer(this)) {
                    val storePayment = getStorePaymentModel(this)
                    indexCount++
                    storePayment
                } else {
                    // credit card
                    val creditCardModel = getCreditCardPaymentModel(this)
                    indexCount++
                    creditCardModel
                }

                // will be null in case of Combined VA which is intentional
                model?.let {
                    model.paymentCode = paymentCode
                    model.gatewayImage = gatewayImg
                    model.gatewayName = gatewayName
                    model.productImage = productImg
                    model.howtoPayAppLink = appLink
                    model.invoiceDetailUrl = invoiceUrl
                    model.shouldShowHowToPay = isShowHelpPage
                    model.actionList.addAll(getListOfAction(this))
                    paymentListModels.add(model)
                }
            }

        }
        return paymentListModels
    }

    private fun getVirtualAccountPaymentModel(
        model: PaymentListInside,
        vaItemList: ArrayList<VaTransactionItem>
    ): VirtualAccountPaymentModel {
        model.apply {
            return VirtualAccountPaymentModel(
                transactionExpireUnix,
                transactionDate, paymentAmount,
                vaItemList
            )
        }
    }

    private fun combinedVirtualAccountModel(
        indexMap: HashMap<String, ArrayList<Int>>,
        paymentListInside: PaymentListInside,
        paymentListModels: ArrayList<BasePaymentModel>
    ): VirtualAccountPaymentModel? {
        paymentListInside.apply {
            val vaListItem = getVirtualAccountTransactionItem(this)
            return getExistingVaByProductCode(
                paymentCode,
                gatewayName,
                vaListItem,
                this,
                indexMap,
                paymentListModels
            )
        }
    }

    private fun getExistingVaByProductCode(
        paymentCode: String,
        gatewayName: String,
        vaListItem: VaTransactionItem,
        responseModel: PaymentListInside,
        indexMap: HashMap<String, ArrayList<Int>>,
        paymentListModels: ArrayList<BasePaymentModel>
    ): VirtualAccountPaymentModel? {
        val indexListWithSamePaymentCode = indexMap[paymentCode] ?: listOf(0)
        var isCombineVaFound = false
        for (index in indexListWithSamePaymentCode) {
            val vaUIModel = paymentListModels[index]
            if (vaUIModel is VirtualAccountPaymentModel && vaUIModel.gatewayName == gatewayName) {
                isCombineVaFound = true
                vaUIModel.transactionList.add(vaListItem)
                vaUIModel.totalAmount += vaListItem.amount
                if (vaUIModel.expiryTime < vaListItem.expiryTime) {
                    vaUIModel.expiryDate = vaListItem.expiryDate
                    vaUIModel.expiryTime = vaListItem.expiryTime
                }
            }
        }
        return if (!isCombineVaFound) {
            getVirtualAccountPaymentModel(responseModel, arrayListOf(vaListItem))
        } else null
    }

    private fun getVirtualAccountTransactionItem(paymentListInside: PaymentListInside): VaTransactionItem {
        paymentListInside.apply {
            return VaTransactionItem(
                transactionId, merchantCode, transactionExpireUnix,
                transactionDate, paymentAmount, isShowCancelButton,
                invoiceUrl, productName
            )
        }
    }

    private fun getCreditCardPaymentModel(paymentListInside: PaymentListInside): CreditCardPaymentModel {
        paymentListInside.apply {
            // @TODO uncomment
            //val tm = if (isShowTickerMessage) tickerMessage else ""

            return CreditCardPaymentModel(
                tickerMessage, transactionId, merchantCode,
                transactionExpireUnix, transactionDate, paymentAmount, isShowCancelButton,
                productName
            )
        }

    }

    private fun getKlicBCAPaymentModel(paymentListInside: PaymentListInside): KlicBCAPaymentModel {
        paymentListInside.apply {
            return KlicBCAPaymentModel(
                transactionId, merchantCode, transactionExpireUnix,
                transactionDate, paymentAmount, isShowCancelButton, productName
            )
        }
    }

    private fun getStorePaymentModel(paymentListInside: PaymentListInside): StorePaymentModel {
        paymentListInside.apply {
            return StorePaymentModel(
                transactionId, merchantCode, transactionExpireUnix,
                transactionDate, paymentAmount, isShowCancelButton, productName
            )
        }
    }

    private fun getBankTransferPaymentModel(paymentListInside: PaymentListInside): BankTransferPaymentModel {
        paymentListInside.apply {
            return BankTransferPaymentModel(
                transactionId, merchantCode, transactionExpireUnix,
                transactionDate, paymentAmount, isShowCancelButton, productName,
                BankInfo(userBankAccount.accNo, userBankAccount.accName),
                BankInfo(destBankAccount.accNo, destBankAccount.accName)
            )
        }
    }

    private fun isStoreTransfer(insideModel: PaymentListInside) =
        insideModel.isIsVa.not() && insideModel.isIsKlikbca.not() && insideModel.paymentCode?.isNotEmpty() == true


    /*private fun getLabelDynamicViewDetailPayment(
        paymentListInside: PaymentListInside,
    ): String {
        if (paymentListInside.isIsKlikbca) {
            return context.getString(R.string.payment_label_klikbcaid)
        }
        return if (paymentListInside.isIsVa) {
            context.getString(R.string.payment_label_virtual_account_number)
        } else context.getString(R.string.payment_label_payment_code)
    }

    private fun getPaymentImage(paymentModel: PaymentListInside) =
        if (TextUtils.isEmpty(paymentModel.bankImg)) paymentModel.gatewayImg else paymentModel.bankImg
*/
    private fun isBankTransfer(paymentListInside: PaymentListInside) =
        paymentListInside.destBankAccount != null && !TextUtils.isEmpty(paymentListInside.destBankAccount.accNo)
                && paymentListInside.userBankAccount != null && !TextUtils.isEmpty(paymentListInside.userBankAccount.accNo)

    private fun getListOfAction(
        paymentListInside: PaymentListInside
    ): List<TransactionActionType> {
        val listOfActions: MutableList<TransactionActionType> = ArrayList()
        if (paymentListInside.isShowEditKlikbcaButton) {
            val model: TransactionActionType = EditKlicBCA(EDIT_KLIC_BCA_USER_ID)
            model.actionName = context.getString(R.string.payment_label_change_bca_user_id)
            model.actionIcon = IconUnify.UPLOAD
            listOfActions.add(model)
        }
        if (paymentListInside.isShowEditTransferButton) {
            val model: TransactionActionType = EditBankTransfer(CHANGE_ACCOUNT_DETAIL)
            model.actionName = context.getString(R.string.payment_label_change_account_detail)
            model.actionIcon = IconUnify.EDIT
            listOfActions.add(model)
        }
        if (paymentListInside.isShowUploadButton) {
            val model: TransactionActionType = UploadProof(UPLOAD_PAYMENT_PROOF)
            model.actionName = context.getString(R.string.payment_label_upload_proof)
            model.actionIcon = IconUnify.UPLOAD
            listOfActions.add(model)
        }
        if (paymentListInside.isShowCancelButton) {
            val model: TransactionActionType = CancelTransaction(CANCEL_TRX)
            model.actionName = context.getString(R.string.payment_label_cancel_transaction)
            model.actionIcon = IconUnify.DELETE
            listOfActions.add(model)
        }
        return listOfActions
    }

    companion object {
        const val EDIT_KLIC_BCA_USER_ID = 1
        const val CHANGE_ACCOUNT_DETAIL = 2
        const val UPLOAD_PAYMENT_PROOF = 3
        const val CANCEL_TRX = 4
    }

}



