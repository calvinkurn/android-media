package com.tokopedia.gifting.domain.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.addon.domain.model.Additional
import com.tokopedia.addon.presentation.uimodel.AddOnParam
import com.tokopedia.addon.presentation.uimodel.AddOnUIModel
import com.tokopedia.common.ProductServiceWidgetConstant.SQUAD_VALUE
import com.tokopedia.common.ProductServiceWidgetConstant.USECASE_GIFTING_VALUE
import com.tokopedia.gifting.domain.model.*
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

class GetAddOnUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
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
                }
                StaticInfo {
                  InfoURL
                  PromoText
                }
                AggregatedData {
                  Title
                  Price
                }
                AddOnByIDResponse{
                  Basic{
                    ID
                    ShopID
                    Name
                    Status
                    AddOnLevel
                    OwnerWarehouseID
                    AddOnType
                    Metadata{
                      Pictures{
                        URL100
                        URL200
                      }
                    }
                  }
                  Inventory{
                    Price
                    Stock
                  }
                  Shop{
                    Name
                    ShopTier
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

    fun setParams(
        addOnIds: List<String>,
        addOnTypes: List<String> = emptyList(),
        addOnWidgetParam: AddOnParam? = null,
        selectedAddOn: List<AddOnUIModel> = emptyList()
    ) {
        val requestParams = RequestParams.create()
        val addonRequest = if (selectedAddOn.isEmpty()) {
            addOnIds.mapToAddonRequest(addOnTypes, addOnWidgetParam, selectedAddOn)
        } else {
            selectedAddOn.mapToAddonRequest(addOnTypes, addOnWidgetParam)
        }
        requestParams.putObject(PARAM_INPUT, GetAddOnRequest(
            addOnRequest = addonRequest,
            source = Source(usecase = USECASE_GIFTING_VALUE, squad = SQUAD_VALUE)
        ))
        setRequestParams(requestParams.parameters)
    }

    fun getErrorString(throwable: Throwable): String {
        return ErrorHandler.getErrorMessage(context, throwable)
    }

    private fun List<String>.mapToAddonRequest(
        addOnTypes: List<String>,
        addOnWidgetParam: AddOnParam?,
        selectedAddOn: List<AddOnUIModel>
    ) = mapIndexed { index, addOnId ->
        val param = addOnWidgetParam ?: AddOnParam()
        val addonKey = selectedAddOn.firstOrNull()?.uniqueId.orEmpty()
        AddOnRequest(
            addOnID = addOnId,
            addOnType = addOnTypes.getOrNull(index).orEmpty(),
            addOnKey = addonKey,
            additional = Additional(
                productID = param.productId,
                categoryID = param.categoryID,
                shopID = param.shopID,
                quantity = param.quantity,
                price = param.price,
                discountedPrice = param.discountedPrice,
                condition = param.condition
            )
        )
    }

    private fun List<AddOnUIModel>.mapToAddonRequest(
        addOnTypes: List<String>,
        addOnWidgetParam: AddOnParam?
    ) = mapIndexed { index, addOn ->
        val param = addOnWidgetParam ?: AddOnParam()
        AddOnRequest(
            addOnID = addOn.id,
            addOnType = addOnTypes.getOrNull(index).orEmpty(),
            addOnKey = addOn.uniqueId,
            additional = Additional(
                productID = param.productId,
                categoryID = param.categoryID,
                shopID = param.shopID,
                quantity = param.quantity,
                price = param.price,
                discountedPrice = param.discountedPrice,
                condition = param.condition
            )
        )
    }
}
