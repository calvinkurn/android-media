package com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomChangeCourier
import com.tokopedia.logisticseller.ui.confirmshipping.domain.query.ChangeCourierQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 10/05/20.
 */
class ChangeCourierUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomChangeCourier.Data>) {

    init {
        useCase.setTypeClass(SomChangeCourier.Data::class.java)
    }

    suspend fun execute(orderId: String, shippingRef: String, agencyId: Long, spId: Long): SomChangeCourier.Data {
        useCase.setRequestParams(ChangeCourierQuery.createParamChangeCourier(orderId, shippingRef, agencyId, spId))
        useCase.setGraphqlQuery(ChangeCourierQuery)
        return useCase.executeOnBackground()
    }
}
