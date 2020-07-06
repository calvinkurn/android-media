package com.tokopedia.oneclickcheckout.preference.edit.domain.shipping

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.oneclickcheckout.preference.edit.data.shipping.ShippingNoPriceResponse
import com.tokopedia.oneclickcheckout.preference.edit.domain.shipping.mapper.ShippingDurationModelMapper
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.model.ShippingListModel
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetShippingDurationUseCase @Inject constructor(private val graphqlUseCase: GraphqlUseCase<ShippingNoPriceResponse>, private val mapper: ShippingDurationModelMapper) : UseCase<ShippingListModel>() {

    override suspend fun executeOnBackground(): ShippingListModel {
        graphqlUseCase.setGraphqlQuery(QUERY)
        graphqlUseCase.setTypeClass(ShippingNoPriceResponse::class.java)
        val result = graphqlUseCase.executeOnBackground()
        return mapper.convertToDomainModel(result)
    }

    private val QUERY = """
        query ongkir_shipper_service {
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
