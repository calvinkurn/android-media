package com.tokopedia.logisticseller.ui.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.logisticseller.ui.confirmshipping.data.model.SomCourierList
import com.tokopedia.logisticseller.ui.confirmshipping.domain.query.GetCourierListQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class GetCourierListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomCourierList.Data>) {

    init {
        useCase.setTypeClass(SomCourierList.Data::class.java)
    }

    suspend fun execute(): MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment> {
        useCase.setGraphqlQuery(GetCourierListQuery)
        return useCase.executeOnBackground().mpLogisticGetEditShippingForm.dataShipment.listShipment.toMutableList()
    }
}
