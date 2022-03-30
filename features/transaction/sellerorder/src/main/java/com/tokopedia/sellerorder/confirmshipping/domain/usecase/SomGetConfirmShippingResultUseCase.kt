package com.tokopedia.sellerorder.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.sellerorder.confirmshipping.domain.query.SomGetConfirmShippingQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetConfirmShippingResultUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmShipping.Data>) {

    init {
        useCase.setTypeClass(SomConfirmShipping.Data::class.java)
    }

    suspend fun execute(orderId: String, shippingRef: String): SomConfirmShipping.Data.MpLogisticConfirmShipping {
        useCase.setGraphqlQuery(SomGetConfirmShippingQuery)
        useCase.setRequestParams(SomGetConfirmShippingQuery.createParamGetConfirmShipping(orderId, shippingRef))
        return useCase.executeOnBackground().mpLogisticConfirmShipping
    }
}