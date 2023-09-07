package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.editshipping.domain.param.OngkirShippingEditorPopupInput
import com.tokopedia.editshipping.domain.param.ValidateShippingEditorParam
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.shippingeditor.ValidateShippingEditorResponse
import javax.inject.Inject

class ValidateShippingEditorUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<OngkirShippingEditorPopupInput, ValidateShippingEditorResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: OngkirShippingEditorPopupInput): ValidateShippingEditorResponse {
        return gql.request(graphqlQuery(), ValidateShippingEditorParam(params))
    }

    companion object {
        private const val QUERY =
            """query ongkirShippingEditorPoup (${'$'}input: OngkirShippingEditorPopupInput!) {
          ongkirShippingEditorPopup (input:${'$'}input) {
            data {
              ui_content {
                warehouses {
                 	warehouse_id
                  shop_id {
                    int64
                    valid
                  }
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
                }
                warehouse_ids
                header
                header_location
                body
                ticker {
                  text_link
                  body
                  header
                  url_link
                }
              }
              state
              feature_id
            }
            status
            message
          }
        }"""
    }
}
