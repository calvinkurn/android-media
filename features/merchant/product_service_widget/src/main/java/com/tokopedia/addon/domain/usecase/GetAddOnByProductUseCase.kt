package com.tokopedia.addon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addon.domain.model.AddOnRequest
import com.tokopedia.addon.domain.model.GetAddOnByProductRequest
import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
import com.tokopedia.addon.domain.model.Source
import com.tokopedia.common.ProductServiceWidgetConstant.SQUAD_VALUE
import com.tokopedia.common.ProductServiceWidgetConstant.USECASE_GIFTING_VALUE
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAddOnByProductUseCase @Inject constructor(
    @ApplicationContext repository: GraphqlRepository
) : GraphqlUseCase<GetAddOnByProductResponse>(repository) {

    companion object {
        private const val ADDON_LEVEL_NON_TC = "PRODUCT_ADDON"
        private const val ADDON_LEVEL_TC = "ORDER_ADDON"
        private const val PARAM_INPUT = "input"
        private val query = """ 
            query getAddOn(${'$'}input: GetAddOnByProductRequest) {
              GetAddOnByProduct(getAddOnByProductRequest: ${'$'}input) {
                Error {
                  messages
                  errorCode
                  reason
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
                      AddOnType
                      Status
                      IsEligible
                      OwnerWarehouseID
                      Metadata {
                        NotesTemplate
                        Description
                        CustomInfoJSON
                        InfoURL {
                          IconURL
                          IconDarkURL
                          EduPageURL
                          AppLinkURL
                          Title
                        }
                      }
                      Rules {
                        MaxOrder
                        CustomNotes
                        Mandatory
                        RequireCustomShipment
                        AutoSelect
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
                  PromoText
                }
              }
            }
            """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetAddOnByProductResponse::class.java)
    }

    fun setParams(
        productId: String,
        warehouseId: String,
        isTokocabang: Boolean
    ) {
        val requestParams = RequestParams.create()
        requestParams.putObject(
            PARAM_INPUT, GetAddOnByProductRequest(
                addOnRequest = listOf(
                    AddOnRequest(
                        productId = productId,
                        warehouseId = warehouseId,
                        addOnLevel = if (isTokocabang) ADDON_LEVEL_TC else ADDON_LEVEL_NON_TC
                    )
                ),
                source = Source(usecase = "testing", squad = "test")
            )
        )
        setRequestParams(requestParams.parameters)
    }
}
