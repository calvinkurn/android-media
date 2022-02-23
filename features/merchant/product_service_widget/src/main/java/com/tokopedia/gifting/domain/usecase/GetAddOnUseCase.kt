package com.tokopedia.gifting.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.common.ProductServiceWidgetConstant.SQUAD_VALUE
import com.tokopedia.common.ProductServiceWidgetConstant.USECASE_GIFTING_VALUE
import com.tokopedia.gifting.domain.model.*
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
            query getAddOn(${'$'}input: GetAddOnByIDRequest) {
              GetAddOnByID(req: ${'$'}input){
                Error{
                  errorCode
                  messages
                  reason
                },
                AddOnByIDResponse{
                  Basic{
                    ID
                    ShopID
                    Name
                    Status
                    AddOnLevel
                    OwnerWarehouseID
                    AddOnType
                    Rules{
                      MaxOrder
                      CustomNotes
                    }
                    Metadata{
                      Pictures{
                        FilePath
                        FileName
                        URL
                        URL100
                        URL200
                      }
                      NotesTemplate
                    }
                  }
                  Inventory{
                    WarehouseID
                    Price
                    Stock
                    UnlimitedStock
                  }
                  Warehouse{
                    WarehouseName
                    CityName
                  }
                  Shop{
                    Name
                    ShopTier
                    ShopGrade
                  }
                }
              }
            }
            """.trimIndent()
    }

    init {
        setGraphqlQuery(query)
        setTypeClass(GetAddOnResponse::class.java)
    }

    fun setParams(addOnId: String) {
        val requestParams = RequestParams.create()
        requestParams.putObject(PARAM_INPUT, GetAddOnRequest(
            addOnRequest = AddOnRequest(addOnId),
            source = Source(usecase = USECASE_GIFTING_VALUE, squad = SQUAD_VALUE)
        ))
        setRequestParams(requestParams.parameters)
    }
}