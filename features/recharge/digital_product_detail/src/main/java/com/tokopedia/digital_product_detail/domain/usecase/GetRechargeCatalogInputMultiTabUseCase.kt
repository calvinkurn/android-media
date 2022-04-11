package com.tokopedia.digital_product_detail.domain.usecase

import com.tokopedia.digital_product_detail.data.model.data.DigitalCatalogProductInputMultiTab
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetRechargeCatalogInputMultiTabUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository
): GraphqlUseCase<DigitalCatalogProductInputMultiTab>(graphqlRepository) {

    private var params: RequestParams = RequestParams.EMPTY

    override suspend fun executeOnBackground(): DigitalCatalogProductInputMultiTab {
        val gqlRequest = GraphqlRequest(QUERY, DigitalCatalogProductInputMultiTab::class.java, params.parameters)
        val gqlResponse = graphqlRepository.response(listOf(gqlRequest), GraphqlCacheStrategy
            .Builder(CacheType.ALWAYS_CLOUD).build())
        val error = gqlResponse.getError(DigitalCatalogProductInputMultiTab::class.java)
        if (error == null || error.isEmpty()){
            return (gqlResponse.getData(DigitalCatalogProductInputMultiTab::class.java) as DigitalCatalogProductInputMultiTab)
        } else {
            throw MessageErrorException(error.mapNotNull { it.message }.toString())
        }
    }

    fun createProductListParams(menuID: Int, operatorId: String, clientNumber: String,
                                filterData: ArrayList<HashMap<String, Any>>?){
        params = RequestParams.create().apply {
           putInt(KEY_MENU_ID, menuID)
           putString(KEY_OPERATOR_ID, operatorId)
           putObject(KEY_CLIENT_NUMBER, arrayListOf(clientNumber))
           if (filterData != null && filterData.size > 0) {
                putObject(KEY_FILTER_DATA, filterData)
           }
        }
    }

    companion object {
        private const val KEY_MENU_ID = "menuID"
        private const val KEY_OPERATOR_ID = "operatorID"
        private const val KEY_FILTER_DATA = "filterData"
        private const val KEY_CLIENT_NUMBER = "clientNumber"

        private val QUERY = """
        query telcoProductMultiTab(${'$'}menuID: Int!,${'$'}operatorID: String!,${'$'}filterData: [RechargeCatalogFilterData], ${'$'}clientNumber: [String]) {
          rechargeCatalogProductInputMultiTab(menuID:${'$'}menuID, platformID: 5, operator:${'$'}operatorID, filterData:${'$'}filterData, clientNumber:${'$'}clientNumber) {
            productInputs {
              label
              needEnquiry
              isShowingProduct
              enquiryFields {
                id
                param_name
                name
              }
              product {
                id
                name
                text
                placeholder
                validations {
                  rule
                }
                dataCollections {
                  name
                  cluster_type
                  products {
                    id
                    attributes {
                      status  
                      product_labels
                      desc
                      detail
                      detail_url
                      detail_url_text
                      info
                      price
                      price_plain
                      status
                      detail_compact
                      category_id
                      operator_id
                      product_descriptions
                      custom_attributes {
                        id
                        name
                        value
                      }
                      promo {
                        id
                        bonus_text
                        new_price
                        new_price_plain
                        value_text
                        discount
                      }
                    }
                  }
                }
              }
              filterTagComponents {
                name
                text
                param_name
                data_collections {
                  key
                  value
                }
              }
            }
          }
        }
    """
    }
}