package com.tokopedia.sellerorder.confirmshipping.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetConfirmShippingResultUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmShipping.Data>) {

    init {
        useCase.setTypeClass(SomConfirmShipping.Data::class.java)
    }

    suspend fun execute(query: String): SomConfirmShipping.Data.MpLogisticConfirmShipping {
        useCase.setGraphqlQuery(query)
        return useCase.executeOnBackground().mpLogisticConfirmShipping
    }
}