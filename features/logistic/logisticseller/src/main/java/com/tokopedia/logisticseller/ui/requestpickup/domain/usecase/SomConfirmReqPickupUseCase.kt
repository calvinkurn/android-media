package com.tokopedia.logisticseller.ui.requestpickup.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomConfirmReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.domain.query.SomConfirmReqPickupQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomConfirmReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomConfirmReqPickup.Data>) {

    init {
        useCase.setTypeClass(SomConfirmReqPickup.Data::class.java)
    }

    suspend fun execute(param: SomConfirmReqPickupParam): SomConfirmReqPickup.Data {
        useCase.setGraphqlQuery(SomConfirmReqPickupQuery)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: SomConfirmReqPickupParam): Map<String, Any?> {
        return mapOf(LogisticSellerConst.PARAM_INPUT to param)
    }
}
