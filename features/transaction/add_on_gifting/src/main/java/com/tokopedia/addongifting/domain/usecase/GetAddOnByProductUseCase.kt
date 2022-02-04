package com.tokopedia.addongifting.domain.usecase

import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.addongifting.data.response.GetAddOnByProductResponse
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

@GqlQuery(GetAddOnByProductUseCase.QUERY_NAME, GetAddOnByProductUseCase.QUERY)
class GetAddOnByProductUseCase @Inject constructor(@ApplicationContext private val graphqlRepository: GraphqlRepository) : UseCase<GetAddOnByProductResponse>() {

    var mockResponse: String = ""

    override suspend fun executeOnBackground(): GetAddOnByProductResponse {
        if (mockResponse.isNotBlank()) {
            return Gson().fromJson(mockResponse, GetAddOnByProductResponse::class.java)
        }

        val request = GraphqlRequest(GetAddOnByProductQuery(), GetAddOnByProductResponse::class.java)
        return graphqlRepository.response(listOf(request)).getSuccessData()
    }

    companion object {
        const val QUERY_NAME = "GetAddOnByProductQuery"
        const val QUERY = """
            {
              GetAddOnByProduct(input: getAddOnByProductRequest) {
                error {
                  messages
                  reason
                  errorCode
                }
                StaticInfo {
                  InfoURL
                }
                GetAddOnByProductResponse {
                  AddOnByProductResponse {
                    ProductID
                    WarehouseID
                    AddOnType
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
                        ProductAddOnType
                        Status
                      }
                      Pictures {
                        FilePath
                        FileName
                        URL
                        URL100
                        URL200
                      }
                      Inventory {
                        WarehouseID
                        Price
                        Stock
                        UnlimitedStock
                      }
                      Warehouse {
                        WarehouseName
                        CityName
                      }
                      Shop {
                        Name
                        ShopTier
                        ShopType
                      }
                    }
                  }
                }
              }
            }
        """
    }
}