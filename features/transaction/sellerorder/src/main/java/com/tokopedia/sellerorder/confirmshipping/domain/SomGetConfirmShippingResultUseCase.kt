package com.tokopedia.sellerorder.confirmshipping.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomConfirmShipping
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetConfirmShippingResultUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmShipping.Data>) {

    suspend fun execute(query: String): Result<SomConfirmShipping.Data.MpLogisticConfirmShipping> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomConfirmShipping.Data::class.java)

        return try {
            val resultConfirmShipping = useCase.executeOnBackground().mpLogisticConfirmShipping
            Success(resultConfirmShipping)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}