package com.tokopedia.sellerorder.confirmshipping.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.sellerorder.confirmshipping.domain.query.SomGetCourierListQuery
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetCourierListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomCourierList.Data>) {

    init {
        useCase.setTypeClass(SomCourierList.Data::class.java)
    }

    suspend fun execute(): MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment> {
        useCase.setGraphqlQuery(SomGetCourierListQuery)
        return useCase.executeOnBackground().mpLogisticGetEditShippingForm.dataShipment.listShipment.toMutableList()
    }
}