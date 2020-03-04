package com.tokopedia.purchase_platform.features.one_click_checkout.common.domain

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.model.response.ShippingNoPriceResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.mapper.ShippingDurationModelMapper
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.shippingnoprice.ShippingListModel
import javax.inject.Inject

class GetShippingDurationUseCase @Inject constructor(val graphqlUseCase: GraphqlUseCase<ShippingNoPriceResponse>) {

    fun execute(onSuccess: (ShippingNoPriceResponse) -> Unit, onError: (Throwable) -> Unit){
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setTypeClass(ShippingNoPriceResponse::class.java)
        return graphqlUseCase.execute(
                { shippingNoPriceResponse: ShippingNoPriceResponse ->
                    print(shippingNoPriceResponse)
                    onSuccess(shippingNoPriceResponse) },
                { throwable ->
                    print(throwable)
                    onError(throwable) })
    }



    companion object {
        val QUERY = """
            query ongkir_shipper_service{
            ongkir_shipper_service {
            services { 
                service_code
                service_id
                service_duration
                shipper_ids
                spids
            }
            }
            }
            """.trimIndent()
    }
}


