package com.tokopedia.logisticseller.ui.requestpickup.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.common.LogisticSellerConst
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickup
import com.tokopedia.logisticseller.ui.requestpickup.data.model.SomProcessReqPickupParam
import com.tokopedia.logisticseller.ui.requestpickup.domain.query.SomProcessReqPickupQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 12/05/20.
 */
class SomProcessReqPickupUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomProcessReqPickup.Data>) {

    init {
        useCase.setTypeClass(SomProcessReqPickup.Data::class.java)
    }

    suspend fun execute(param: SomProcessReqPickupParam): SomProcessReqPickup.Data {
        useCase.setGraphqlQuery(SomProcessReqPickupQuery)
        useCase.setRequestParams(generateParam(param))
        return useCase.executeOnBackground()
    }

    private fun generateParam(param: SomProcessReqPickupParam): Map<String, Any?> {
        return mapOf(LogisticSellerConst.PARAM_INPUT to param)
    }
}
