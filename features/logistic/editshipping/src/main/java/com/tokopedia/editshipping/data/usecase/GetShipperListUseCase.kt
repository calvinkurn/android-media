package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editshipping.domain.param.ShippingEditorParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperListResponse
import javax.inject.Inject

class GetShipperListUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<ShippingEditorParam, GetShipperListResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: ShippingEditorParam): GetShipperListResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY =
            """query ongkirShippingEditor (${'$'}input: ShippingEditorShopMultiLocInput!){
          ongkirShippingEditor(input: ${'$'}input) {
            status
            message
            data {
              drop_off_maps_url
              shippers {
                ondemand {
                  shipper_id
                  shipper_name
                  is_active
                  text_promo
                  image
                  feature_info {
                    header
                    body
                  }
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    shipper_product_desc
                    is_active
                  }
                }
                conventional {
                  shipper_id
                  shipper_name
                  is_active
                  text_promo
                  image
                  feature_info {
                    header
                    body
                  }
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    shipper_product_desc
                    is_active
                  }
                }
              }
              ticker {
                header
                body
                text_link
                url_link
              }
            }
          }
        }"""
    }
}
