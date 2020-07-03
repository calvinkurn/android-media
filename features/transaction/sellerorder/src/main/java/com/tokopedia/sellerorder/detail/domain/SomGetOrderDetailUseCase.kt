package com.tokopedia.sellerorder.detail.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.common.util.SomConsts.PARAM_LANG_ID
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_LANG
import com.tokopedia.sellerorder.common.util.SomConsts.VAR_PARAM_ORDERID
import com.tokopedia.sellerorder.detail.data.model.SomDetailOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrder
import com.tokopedia.sellerorder.list.data.model.SomListOrderParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomGetOrderDetailUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomDetailOrder.Data>) {

    suspend fun execute(query: String, orderId: String): Result<SomDetailOrder.Data.GetSomDetail> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomDetailOrder.Data::class.java)
        useCase.setRequestParams(generateParam(orderId))

        return try {
            val orderDetail = useCase.executeOnBackground().getSomDetail
            Success(orderDetail)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun generateParam(orderId: String): HashMap<String, String> {
        return hashMapOf(VAR_PARAM_ORDERID to orderId, VAR_PARAM_LANG to PARAM_LANG_ID)
    }
}