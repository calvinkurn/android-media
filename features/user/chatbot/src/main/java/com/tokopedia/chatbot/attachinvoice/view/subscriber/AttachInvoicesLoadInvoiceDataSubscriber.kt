package com.tokopedia.chatbot.attachinvoice.view.subscriber


import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.chatbot.attachinvoice.domain.model.InvoiceConstants.DEFAULT_LIMIT
import com.tokopedia.chatbot.attachinvoice.view.AttachInvoiceContract
import com.tokopedia.chatbot.attachinvoice.view.model.InvoiceViewModel
import com.tokopedia.chatbot.domain.pojo.invoicelist.api.GetInvoiceListPojo
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import rx.Subscriber

/**
 * Created by Hendri on 22/03/18.
 */

class AttachInvoicesLoadInvoiceDataSubscriber(private val view: AttachInvoiceContract.View)
    : Subscriber<GraphqlResponse>() {

    override fun onCompleted() {

    }

    override fun onError(e: Throwable) {
        view.showErrorMessage(e)
    }

    override fun onNext(response: GraphqlResponse) {

        val graphqlErrorList = response.getError(GetInvoiceListPojo::class.java)
        if (graphqlErrorList == null || graphqlErrorList.isEmpty()) {
            routingOnNext(response)
        } else if (!graphqlErrorList.isEmpty()
                && graphqlErrorList[0] != null
                && graphqlErrorList[0].message != null) {
            onError(MessageErrorException(graphqlErrorList[0].message))
        }

    }

    private fun routingOnNext(graphqlResponse: GraphqlResponse) {
        val pojo = graphqlResponse.getData<GetInvoiceListPojo>(GetInvoiceListPojo::class.java)
        view.addInvoicesToList(mapToInvoiceList(pojo),
                pojo.getInvoiceList.size >= DEFAULT_LIMIT)
    }

    private fun mapToInvoiceList(pojo: GetInvoiceListPojo?): List<InvoiceViewModel> {
        val list = ArrayList<InvoiceViewModel>()
        pojo?.run {
            for (pojoItem in getInvoiceList) {
                list.add(InvoiceViewModel(
                        pojoItem.attributes.id.toLongOrZero(),
                        pojoItem.typeId,
                        pojoItem.attributes.statusId,
                        pojoItem.attributes.code,
                        pojoItem.attributes.title,
                        pojoItem.attributes.imageUrl,
                        pojoItem.attributes.status,
                        pojoItem.attributes.createTime,
                        pojoItem.attributes.totalAmount,
                        pojoItem.type,
                        pojoItem.attributes.description,
                        pojoItem.attributes.invoiceUrl

                ))
            }
        }
        return list
    }
}
