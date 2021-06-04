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
        paymentList: List<PaymentListItem>,
        onSuccess: (ArrayList<BasePaymentModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        useCaseRequestParams = RequestParams().apply {
            putObject(PARAM_PAYMENT_LIST, paymentList)
        }
        execute({ onSuccess(it) }, { onError(it) }, useCaseRequestParams)
    }

    override suspend fun executeOnBackground(): ArrayList<BasePaymentModel> {
        val list: List<PaymentListItem> =
            (useCaseRequestParams.getObject(PARAM_PAYMENT_LIST) as List<PaymentListItem>)
        return mapList(list)
    }

    /*
    * mapper to combine Virtual Account transactions having same payment code and gateway name.
    * @param paymentList: list from gql response
    * indexCount: maintain the current index of item present in BasePaymentModel
    * indexMap : maps payment code with List of indexes having same payment code in already
    * combined BasePaymentModel list. This list is use to access previous payment which needs to
    * be combined with current being processed.
    * Otherwise simply add in the returning list.
    * */
    private fun mapList(paymentList: List<PaymentListItem>): ArrayList<BasePaymentModel> {
        val paymentListModels = arrayListOf<BasePaymentModel>()
        val indexMap = HashMap<String?, ArrayList<Int>>()
        var indexCount = 0
        for (paymentModel in paymentList) {
            paymentModel.apply {

                val model: BasePaymentModel? = if (isIsVa == true) {
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
                } else if (isIsKlikbca == true) {
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
                    model.paymentCode = paymentCode ?: ""
                    model.gatewayImage = if (bankImg.isNullOrBlank()) gatewayImg ?: "" else bankImg
                    model.gatewayName = gatewayName ?: ""
                    model.productImage = productImg ?: ""
                    model.howtoPayAppLink = appLink ?: ""
                    model.invoiceDetailUrl = invoiceUrl ?: ""
                    model.shouldShowHowToPay = isShowHelpPage ?: false
                    model.actionList.addAll(getListOfAction(this))
                    paymentListModels.add(model)
                }
            }

        }
        return paymentListModels
    }

    private fun getVirtualAccountPaymentModel(
        model: PaymentListItem,
        vaItemList: ArrayList<VaTransactionItem>
    ): VirtualAccountPaymentModel {
        model.apply {
            return VirtualAccountPaymentModel(
                transactionExpireUnix ?: 0,
                transactionDate ?: "", paymentAmount ?: 0,
                vaItemList
            )
        }
    }

    private fun combinedVirtualAccountModel(
        indexMap: HashMap<String?, ArrayList<Int>>,
        PaymentListItem: PaymentListItem,
        paymentListModels: ArrayList<BasePaymentModel>
    ): VirtualAccountPaymentModel? {
        PaymentListItem.apply {
            val vaListItem = getVirtualAccountTransactionItem(this)
            return getExistingVaByProductCode(
                paymentCode ?: "",
                gatewayName ?: "",
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
        responseModel: PaymentListItem,
        indexMap: HashMap<String?, ArrayList<Int>>,
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

    private fun getVirtualAccountTransactionItem(PaymentListItem: PaymentListItem): VaTransactionItem {
        PaymentListItem.apply {
            return VaTransactionItem(
                transactionId ?: "", merchantCode ?: "",
                transactionExpireUnix ?: 0, transactionDate ?: "",
                paymentAmount ?: 0, isShowCancelButton ?: false,
                invoiceUrl ?: "", productName ?: ""
            )
        }
    }

    private fun getCreditCardPaymentModel(PaymentListItem: PaymentListItem): CreditCardPaymentModel {
        PaymentListItem.apply {
            val label = if (isShowTickerMessage == true) tickerMessage else ""
            return CreditCardPaymentModel(
                label ?: "", transactionId ?: "", merchantCode ?: "",
                transactionExpireUnix ?: 0, transactionDate ?: "",
                paymentAmount ?: 0, isShowCancelButton ?: false,
                productName ?: ""
            )
        }
    }

    private fun getKlicBCAPaymentModel(PaymentListItem: PaymentListItem): KlicBCAPaymentModel {
        PaymentListItem.apply {
            return KlicBCAPaymentModel(
                transactionId ?: "", merchantCode ?: "",
                transactionExpireUnix ?: 0, transactionDate ?: "",
                paymentAmount ?: 0, isShowCancelButton ?: false, productName ?: ""
            )
        }
    }

    private fun getStorePaymentModel(PaymentListItem: PaymentListItem): StorePaymentModel {
        PaymentListItem.apply {
            return StorePaymentModel(
                transactionId ?: "", merchantCode ?: "",
                transactionExpireUnix ?: 0, transactionDate ?: "",
                paymentAmount ?: 0, isShowCancelButton ?: false,
                productName ?: ""
            )
        }
    }

    private fun getBankTransferPaymentModel(PaymentListItem: PaymentListItem): BankTransferPaymentModel {
        PaymentListItem.apply {
            return BankTransferPaymentModel(
                transactionId ?: "", merchantCode ?: "",
                transactionExpireUnix ?: 0,
                transactionDate ?: "", paymentAmount ?: 0,
                isShowCancelButton ?: false, productName ?: "",
                BankInfo(
                    userBankAccount?.accNo,
                    userBankAccount?.accName,
                    userBankAccount?.bankId.toString()
                ),
                BankInfo(destBankAccount?.accNo, destBankAccount?.accName, null)
            )
        }
    }

    private fun isStoreTransfer(insideModel: PaymentListItem) =
        insideModel.isIsVa == false && insideModel.isIsKlikbca == false && insideModel.paymentCode?.isNotEmpty() == true

    private fun isBankTransfer(PaymentListItem: PaymentListItem) =
        PaymentListItem.destBankAccount != null && !TextUtils.isEmpty(PaymentListItem.destBankAccount.accNo)
                && PaymentListItem.userBankAccount != null && !TextUtils.isEmpty(PaymentListItem.userBankAccount.accNo)

    private fun getListOfAction(
        PaymentListItem: PaymentListItem
    ): List<TransactionActionType> {
        val listOfActions: MutableList<TransactionActionType> = ArrayList()
        if (PaymentListItem.isShowEditKlikbcaButton == true) {
            val model: TransactionActionType = EditKlicBCA(EDIT_KLIC_BCA_USER_ID)
            model.actionName = context.getString(R.string.payment_label_change_bca_user_id)
            model.actionIcon = IconUnify.UPLOAD
            listOfActions.add(model)
        }
        if (PaymentListItem.isShowEditTransferButton == true) {
            val model: TransactionActionType = EditBankTransfer(CHANGE_ACCOUNT_DETAIL)
            model.actionName = context.getString(R.string.payment_label_change_account_detail)
            model.actionIcon = IconUnify.EDIT
            listOfActions.add(model)
        }
        if (PaymentListItem.isShowUploadButton == true) {
            val model: TransactionActionType = UploadProof(UPLOAD_PAYMENT_PROOF)
            model.actionName = context.getString(R.string.payment_label_upload_proof)
            model.actionIcon = IconUnify.UPLOAD
            listOfActions.add(model)
        }
        if (PaymentListItem.isShowCancelButton == true) {
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



