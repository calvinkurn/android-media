package com.tokopedia.sellerorder.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.sellerorder.confirmshipping.domain.query.SomChangeCourierQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class SomChangeCourierUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomChangeCourier.Data>) {

    init {
        useCase.setTypeClass(SomChangeCourier.Data::class.java)
    }

    suspend fun execute(orderId: String, shippingRef: String, agencyId: Long, spId: Long): SomChangeCourier.Data {
        useCase.setRequestParams(SomChangeCourierQuery.createParamChangeCourier(orderId, shippingRef, agencyId, spId))
        useCase.setGraphqlQuery(SomChangeCourierQuery)
        return  useCase.executeOnBackground()
    }
}