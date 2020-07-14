package com.tokopedia.sellerorder.confirmshipping.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.sellerorder.confirmshipping.data.model.SomCourierList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * Created by fwidjaja on 09/05/20.
 */
class SomGetCourierListUseCase @Inject constructor(private val useCase: GraphqlUseCase<SomCourierList.Data>) {

    suspend fun execute(query: String): Result<MutableList<SomCourierList.Data.MpLogisticGetEditShippingForm.DataShipment.Shipment>> {
        useCase.setGraphqlQuery(query)
        useCase.setTypeClass(SomCourierList.Data::class.java)

        return try {
            val resultCourierList = useCase.executeOnBackground().mpLogisticGetEditShippingForm.dataShipment.listShipment.toMutableList()
            Success(resultCourierList)
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }
}