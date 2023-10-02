package com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.logisticseller.ui.confirmshipping.domain.query.GetConfirmShippingQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class GetConfirmShippingResultUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmShipping.Data>) {

    init {
        useCase.setTypeClass(SomConfirmShipping.Data::class.java)
    }

    suspend fun execute(orderId: String, shippingRef: String): SomConfirmShipping.Data.MpLogisticConfirmShipping {
        useCase.setRequestParams(GetConfirmShippingQuery.createParamGetConfirmShipping(orderId, shippingRef))
        useCase.setGraphqlQuery(GetConfirmShippingQuery)
        return useCase.executeOnBackground().mpLogisticConfirmShipping
    }
}
