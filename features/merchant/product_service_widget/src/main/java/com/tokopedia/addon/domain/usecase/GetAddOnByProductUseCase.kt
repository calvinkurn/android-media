package com.tokopedia.addon.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addon.domain.model.AddOnRequest
import com.tokopedia.addon.domain.model.Additional
import com.tokopedia.addon.domain.model.GetAddOnByProductRequest
import com.tokopedia.addon.domain.model.GetAddOnByProductResponse
import com.tokopedia.addon.domain.model.Source
import com.tokopedia.addon.domain.model.TypeFilters
import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.common.ProductServiceWidgetConstant.ADDON_PAGE_SOURCE_PDP
import com.tokopedia.common.ProductServiceWidgetConstant.SQUAD_VALUE_ADDON
import com.tokopedia.common.ProductServiceWidgetConstant.SQUAD_VALUE_ADDON_PDP
import com.tokopedia.common.ProductServiceWidgetConstant.USECASE_ADDON_VALUE
import com.tokopedia.gifting.presentation.uimodel.AddOnType
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
        private const val ADDON_FILTER_QUANTITY = 3
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
                      AddOnKey
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
                      DiscountedPrice
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
        param: AddOnParam,
        typeFilters: List<AddOnType>
    ) {
        val requestParams = RequestParams.create()
        requestParams.putObject(
            PARAM_INPUT, GetAddOnByProductRequest(
                addOnRequest = listOf(
                    AddOnRequest(
                        productId = param.productId,
                        warehouseId = param.warehouseId,
                        addOnLevel = ADDON_LEVEL_NON_TC,
                        typeFilters = typeFilters.map {
                            TypeFilters(
                                type = it.name,
                                quantity = ADDON_FILTER_QUANTITY
                            )
                        },
                        additional = Additional(
                            categoryID = param.categoryID,
                            shopID = param.shopID,
                            quantity = param.quantity,
                            price = param.price,
                            discountedPrice = param.discountedPrice,
                            condition = param.condition,
                        ),
                    )
                ),
                source = Source(usecase = USECASE_ADDON_VALUE, squad =
                    if (param.pageSource == ADDON_PAGE_SOURCE_PDP) SQUAD_VALUE_ADDON_PDP
                    else SQUAD_VALUE_ADDON)
            )
        )
        setRequestParams(requestParams.parameters)
    }
}
