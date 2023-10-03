package com.tokopedia.addongifting.addonbottomsheet.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(GetAddOnByProductUseCase.QUERY_NAME, GetAddOnByProductUseCase.QUERY)
class GetAddOnByProductUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetAddOnByProductResponse>() {

    private var params: Map<String, Any>? = null

    fun setParams(getAddOnByProductRequest: GetAddOnByProductRequest) {
        params = mapOf("getAddOnByProductRequest" to getAddOnByProductRequest)
    }

    override suspend fun executeOnBackground(): GetAddOnByProductResponse {
        if (params.isNullOrEmpty()) {
            throw RuntimeException("Parameter can't be null or empty!")
        }

        val request = GraphqlRequest(GetAddOnByProductQuery(), GetAddOnByProductResponse::class.java, params)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "GetAddOnByProductQuery"
        const val QUERY = """
            query GetAddOnByProduct(${'$'}getAddOnByProductRequest: GetAddOnByProductRequest) {
              GetAddOnByProduct(getAddOnByProductRequest: ${'$'}getAddOnByProductRequest) {
                Error {
                  messages
                  reason
                  errorCode
                }
                StaticInfo {
                  PromoText
                }
                AddOnByProductResponse {
                  ProductID
                  WarehouseID
                  AddOnLevel
                  Addons {
                    Basic {
                      ID
                      ShopID
                      Name
                      Rules {
                        MaxOrder
                        CustomNotes
                      }
                      Metadata {
                        Pictures {
                          URL
                        }
                        NotesTemplate
                      }
                      AddOnType
                      Status
                      AddOnKey
                    }
                    Inventory {
                      WarehouseID
                      Price
                      Stock
                      UnlimitedStock
                    }
                  }
                }
              }
            }
        """
    }
}
