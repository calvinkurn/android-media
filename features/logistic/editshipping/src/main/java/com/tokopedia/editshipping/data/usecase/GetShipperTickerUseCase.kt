package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editshipping.domain.param.ShippingEditorParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperTickerResponse
import javax.inject.Inject

class GetShipperTickerUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<ShippingEditorParam, GetShipperTickerResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return ongkirShippingEditorTicker
    }

    override suspend fun execute(params: ShippingEditorParam): GetShipperTickerResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val ongkirShippingEditorTicker = """
        query ongkirShippingEditorGetShipperTicker (${'$'}input: ShippingEditorShopMultiLocInput!){
          ongkirShippingEditorGetShipperTicker(input: ${'$'}input) {
            status
            message
            data {
            	header_ticker {
            	  header
            	  body
            	  text_link
            	  url_link
            	  is_active
                  warehouse_ids
            	}
              courier_ticker {
                shipper_id
                warehouse_ids
                ticker_state
                is_available
                shipper_product {
                  shipper_product_id
                  is_available
                }
              }
              warehouses {
                warehouse_id
                warehouse_name
                district_id
                district_name
                city_id
                city_name
                province_id
                province_name
                status
                postal_code
                is_default
                latlon
                latitude
                longitude
                address_detail
                country
                is_fulfillment
                warehouse_type
                email
                shop_id {
                  int64
                  valid
                }
                partner_id {
                  int64
                  valid
                }
              }
            }
          }
        }
    """
    }
}
