package com.tokopedia.logisticCommon.data.repository

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.response.customproductlogistic.OngkirGetCPLQGLResponse
import com.tokopedia.logisticCommon.domain.param.CPLParam
import javax.inject.Inject

class CustomProductLogisticUseCase @Inject constructor(
    @ApplicationContext private val gql: GraphqlRepository,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<CPLParam, OngkirGetCPLQGLResponse>(dispatcher.io) {

    fun getParam(
        shopId: Long,
        productId: Long?,
        cplParam: List<Long>?
    ): CPLParam {
        return CPLParam(
            cplDataParam = CPLParam.CPLDataParam(
                shopId = shopId,
                productId = productId,
                productCpls = cplParam?.joinToString(separator = ",")
            )
        )
    }

    override fun graphqlQuery(): String {
        return ONGKIR_GET_CPL_EDITOR_QUERY
    }

    @GqlQuery(QUERY_GET_CPL, ONGKIR_GET_CPL_EDITOR_QUERY)
    override suspend fun execute(params: CPLParam): OngkirGetCPLQGLResponse {
        return gql.request(OngkirGetCPLEditor(), params)
    }

    companion object {
        private const val ONGKIR_GET_CPL_EDITOR_QUERY = """
        query ongkirGetCPLEditor(${'$'}input: OngkirGetCPLEditorInput!){
          ongkirGetCPLEditor (input:${'$'}input) {
            data {
              shipper_list {
                header
                description
                whitelabels {
                  title
                  description
                  shipper_product_ids
                  is_active
                }
                shippers {
                  shipper_id
                  shipper_name
                  logo
                  shipper_product {
                    shipper_product_id
                    shipper_product_name
                    shipper_service_name
                    ui_hidden
                    is_active
                  }
                }
              }
            } 
            errors {
              id
              title
              status
            }
          }
        }
    """

        private const val QUERY_GET_CPL = "OngkirGetCPLEditor"
    }
}
