package com.tokopedia.buyerorder.recharge_download.domain

import com.tokopedia.buyerorder.recharge_download.data.OrderDetailRechargeDownloadWebviewEntity
import com.tokopedia.buyerorder.recharge_download.data.OrderDetailRechargeDownloadWebviewGQLConst
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 21/01/2021
 */
class OrderDetailRechargeGetInvoiceUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) {

    suspend fun executeSuspend(orderId: Int): Result<OrderDetailRechargeDownloadWebviewEntity> =
            try {
                val params = mapOf(PARAM_ORDER_ID to orderId)
                val request = GraphqlRequest(OrderDetailRechargeDownloadWebviewGQLConst.ORDER_DETAIL_RECHARGE_INVOICE,
                        OrderDetailRechargeDownloadWebviewEntity.Response::class.java,
                        params)
                val response = graphqlRepository.getReseponse(listOf(request)).getSuccessData<OrderDetailRechargeDownloadWebviewEntity.Response>()
                Success(response.rechargeEncodeInvoice)
            } catch (throwable: Throwable) {
                throwable.printStackTrace()
                Fail(throwable)
            }

    companion object {
        private const val PARAM_ORDER_ID = "orderID"
    }

}