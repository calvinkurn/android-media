package com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.domain.query.GetCourierListQuery
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class GetCourierListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomCourierList.Data>) {

    companion object {
        private const val PARAM_DELIVERY_IDENTIFIER = "deliveryIdentifier"
    }

    init {
        useCase.setTypeClass(SomCourierList.Data::class.java)
    }

    private fun createParam(deliveryId: String): Map<String, Any> {
        return RequestParams.create().apply {
            putString(PARAM_DELIVERY_IDENTIFIER, deliveryId)
        }.parameters
    }

    suspend fun execute(deliveryId: String): SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment {
        useCase.setRequestParams(createParam(deliveryId))
        useCase.setGraphqlQuery(GetCourierListQuery)
        return useCase.executeOnBackground().mpLogisticGetEditShippingForm.dataShipment
    }
}
