package com.tokopedia.chatbot.attachinvoice.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PEMBELIAN
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PENARIKAN_DANA
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.VALUE_PENJUALAN
import com.tokopedia.chatbot.attachinvoice.domain.usecase.GetFilteredInvoiceListUseCase
import com.tokopedia.chatbot.attachinvoice.view.model.EmptyTransactionInvoiceUiModel
import com.tokopedia.chatbot.attachinvoice.view.model.TransactionInvoiceUiModel
import com.tokopedia.chatbot.domain.pojo.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

private const val INVOICE_ERROR_URL = "https://images.tokopedia.net/img/android/res/singleDpi/transaction_invoice_error.png"
private const val INVOICE_EMPTY_URL = "https://images.tokopedia.net/img/android/res/singleDpi/transaction_invoice_empty.png"

class TransactionInvoiceListViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                          private val getFilteredInvoiceListUseCase: GetFilteredInvoiceListUseCase)
    : ViewModel() {

    private val filteredInvoiceLiveData: MutableLiveData<Result<List<Visitable<*>>>> = MutableLiveData()

    fun getFilteredInvoiceLiveData(): LiveData<Result<List<Visitable<*>>>> = filteredInvoiceLiveData

    fun getFilteredInvoice(filteredEvent: String, page: Int, messageId: Int) {
        viewModelScope.launchCatchError(
                block = {
                    getFilteredInvoiceListUseCase.setParams(filteredEvent, page, messageId.toString())
                    val response = getFilteredInvoiceListUseCase.executeOnBackground()
                    if (response.getInvoiceList.isNullOrEmpty()) {
                        filteredInvoiceLiveData.value = Success(mapEmptyResponse(filteredEvent))
                    } else {
                        filteredInvoiceLiveData.value = Success(mapResponse(response))
                    }

                },
                onError = {
                    it.printStackTrace()
                    filteredInvoiceLiveData.value = Success(mapErrorResponse())
                })
    }

    private fun mapErrorResponse(): List<EmptyTransactionInvoiceUiModel> {
        val result = ArrayList<EmptyTransactionInvoiceUiModel>()
        result.add(EmptyTransactionInvoiceUiModel(R.string.chatbot_error_title, getDescriptionRes(""),
                INVOICE_ERROR_URL, true))
        return result
    }

    private fun mapEmptyResponse(filteredEvent: String): ArrayList<EmptyTransactionInvoiceUiModel> {
        val result = ArrayList<EmptyTransactionInvoiceUiModel>()
        result.add(EmptyTransactionInvoiceUiModel(R.string.chatbot_empty_title, getDescriptionRes(filteredEvent),
                INVOICE_EMPTY_URL))
        return result
    }

    private fun getDescriptionRes(filteredEvent: String): Int {
        return when (filteredEvent) {
            VALUE_PEMBELIAN -> {
                R.string.chatbot_pembelian_empty_description
            }
            VALUE_PENJUALAN -> {
                R.string.chatbot_penjualan_empty_description
            }
            VALUE_PENARIKAN_DANA -> {
                R.string.chatbot_penarikan_dana_empty_description
            }
            else -> R.string.chatbot_error_description
        }
    }

    private fun mapResponse(response: GetInvoiceListPojo): ArrayList<TransactionInvoiceUiModel> {
        val result = ArrayList<TransactionInvoiceUiModel>()
        response.getInvoiceList.map {
            result.add(TransactionInvoiceUiModel(it.attributes.id, it.attributes.title,
                    it.attributes.description, it.attributes.createTime, it.attributes.statusId,
                    it.attributes.status, it.attributes.totalAmount, it.attributes.invoiceUrl,
                    it.attributes.imageUrl, userSession.userId, userSession.name, it.attributes.code,
                    it.typeId, it.type))
        }
        return result
    }
}
