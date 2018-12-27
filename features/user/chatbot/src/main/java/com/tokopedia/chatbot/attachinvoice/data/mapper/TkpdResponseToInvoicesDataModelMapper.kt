package com.tokopedia.chatbot.attachinvoice.data.mapper

import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicesPayloadWrapper
import com.tokopedia.chatbot.attachinvoice.data.model.GetInvoicesResponsePojo
import com.tokopedia.chatbot.attachinvoice.data.model.InvoicesDataModel
import com.tokopedia.chatbot.attachinvoice.domain.model.Invoice
import retrofit2.Response
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * Created by Hendri on 21/03/18.
 */

class TkpdResponseToInvoicesDataModelMapper @Inject constructor() : Func1<Response<GetInvoicesResponsePojo>, List<Invoice>> {

    override fun call(getInvoicesResponseWrapperResponse: Response<GetInvoicesResponsePojo>): List<Invoice> {
        val responseWrapper = getInvoicesResponseWrapperResponse.body()
        var domainModel: List<Invoice> = ArrayList()
        if (responseWrapper.dataWrapper != null
                && responseWrapper.dataWrapper?.payloadWrapper != null) {
            val payloadWrapper = responseWrapper.dataWrapper?.payloadWrapper

            if (payloadWrapper != null) {
                domainModel = convertPayloadDataToInvoiceList(payloadWrapper)
            }

        }
        return domainModel
    }

    private fun convertPayloadDataToInvoiceList(payloadWrapper: GetInvoicesPayloadWrapper): List<Invoice> {
        val domainModel = ArrayList<Invoice>()
        for (invoicesDataModel in payloadWrapper.invoices) {
            domainModel.add(convertInvoicefromInvoiceDataModel(invoicesDataModel))
        }

        return domainModel
    }

    private fun convertInvoicefromInvoiceDataModel(invoicesDataModel: InvoicesDataModel): Invoice {
        val invoiceAttributesDataModel = invoicesDataModel
                .attribute
        val invoiceBuilder = Invoice.InvoiceBuilder.instance
        if (invoiceAttributesDataModel != null) {
            invoiceBuilder.setStatusInt(invoiceAttributesDataModel.statusId)
                    .setNumber(invoiceAttributesDataModel.invoiceNo)
                    .setType(invoicesDataModel.type)
                    .setUrl(invoiceAttributesDataModel.url)
                    .setTitle(invoiceAttributesDataModel.title)
                    .setDesc(invoiceAttributesDataModel.description)
                    .setDate(invoiceAttributesDataModel.invoiceDate)
                    .setStatus(invoiceAttributesDataModel.status)
                    .setTotal(invoiceAttributesDataModel.amount)
                    .setImageUrl(invoiceAttributesDataModel.imageUrl)
                    .setInvoiceTypeInt(invoicesDataModel.typeId)
                    .setInvoiceId(invoiceAttributesDataModel.invoiceId)
        }
        return invoiceBuilder.createInvoice()
    }
}
