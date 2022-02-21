package com.tokopedia.gifting.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.gifting.domain.model.AddOnRequest
import com.tokopedia.gifting.domain.model.GetAddOnRequest
import com.tokopedia.gifting.domain.model.GetAddOnResponse
import com.tokopedia.gifting.domain.model.Source
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAddOnUseCase @Inject constructor(
    @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetAddOnResponse>(repository) {

    companion object {
        private const val PARAM_INPUT = "input"
        private val query =
            """ 
            query getAddOnByProduct(${'$'}input: GetAddOnByProductRequest) {
              GetAddOnByProduct(getAddOnByProductRequest: ${'$'}input) {
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
                      AddOnType
                      Status
                      IsEligible
                      Rules {
                        MaxOrder
                        CustomNotes
                      }
                      OwnerWarehouseID
                      Metadata {
                        NotesTemplate
                        Pictures {
                          FilePath
                          FileName
                          URL
                          URL100
                          URL200
                        }
                      }
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
                      ShopGrade
                    }
                  }
                }
                StaticInfo {
                  InfoURL
                }
              }
            }
            """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetAddOnResponse::class.java)
    }

    fun setParams(productId: String, warehouseId: String) {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_INPUT, GetAddOnRequest(
            addOnRequest = AddOnRequest(productID = productId, warehouseID = warehouseId, addOnLevel = "ORDER_ADDON"),
            source = Source(usecase = "sss", squad = "sss")
        ))
        setRequestParams(requestParams.parameters)
    }
}