package com.tokopedia.editshipping.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.shippingeditor.GetShipperDetailResponse
import javax.inject.Inject

class GetShipperDetailUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<Unit, GetShipperDetailResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return QUERY
    }

    override suspend fun execute(params: Unit): GetShipperDetailResponse {
        return gql.request(graphqlQuery(), params)
    }

    companion object {
        private const val QUERY =
            """query ongkirShippingEditorGetShipperDetail {
          ongkirShippingEditorGetShipperDetail() {
            status
           	message
            data {
              shipper_details {
                name
                description
                image
                shipper_product {
                  name
                  description
                }
              }
              feature_details {
        		header
                description
              }
              service_details {
              	header
                description
              }
            }
          }
        }"""
    }
}
