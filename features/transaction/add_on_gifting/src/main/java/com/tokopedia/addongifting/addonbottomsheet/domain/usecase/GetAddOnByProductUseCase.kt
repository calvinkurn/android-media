package com.tokopedia.addongifting.addonbottomsheet.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductRequest
import com.tokopedia.addongifting.addonbottomsheet.data.getaddonbyproduct.GetAddOnByProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.RuntimeException
import javax.inject.Inject

@GqlQuery(GetAddOnByProductUseCase.QUERY_NAME, GetAddOnByProductUseCase.QUERY)
class GetAddOnByProductUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetAddOnByProductResponse>() {

    var mockResponse: String = ""

    private var params: Map<String, Any>? = null

    fun setParams(getAddOnByProductRequest: GetAddOnByProductRequest) {
        params = mapOf("getAddOnByProductRequest" to getAddOnByProductRequest)
    }

    override suspend fun executeOnBackground(): GetAddOnByProductResponse {
        // Todo : remove mock data before merge to release
//        if (mockResponse.isNotBlank()) {
//            return Gson().fromJson(mockResponse, GetAddOnByProductResponse::class.java)
//        }

        if (params.isNullOrEmpty()) {
            throw RuntimeException("Parameter can't be null or empty!")
        }

        val request = GraphqlRequest(GetAddOnByProductQuery.GQL_QUERY, GetAddOnByProductResponse::class.java, params)
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
                AddOnByProductResponse {
                  ProductID
                  WarehouseID
                  AddOnLevel
                  CouponText
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
                          FilePath
                          FileName
                          URL
                          URL100
                          URL200
                        }
                        NotesTemplate
                      }
                      AddOnType
                      Status
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