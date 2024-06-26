package com.tokopedia.product_ar.usecase

import com.google.gson.Gson
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.kotlin.extensions.view.getCurrencyFormatted
import com.tokopedia.kotlin.extensions.view.ifNullOrBlank
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.percentFormatted
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ProductArResponse
import com.tokopedia.product_ar.model.ProductArUiModel
import javax.inject.Inject

class GetProductArUseCase @Inject constructor(
    graphqlRepository: GraphqlRepository,
    private val gson: Gson
) : GraphqlUseCase<ProductArResponse>(graphqlRepository) {

    companion object {
        fun createParams(
            productId: String,
            shopId: String,
            userLocationRequest: ChosenAddressRequestHelper
        ) = mutableMapOf<String, Any>().apply {
            put(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            put(ProductDetailCommonConstant.PARAM_SHOP_ID, shopId)

            val localizationChooseAddress = userLocationRequest.getChosenAddress()
            put(
                ProductDetailCommonConstant.PARAM_USER_LOCATION,
                UserLocationRequest(
                    districtID = localizationChooseAddress?.districtId ?: "",
                    addressID = localizationChooseAddress?.addressId ?: "",
                    postalCode = localizationChooseAddress?.postalCode ?: "",
                    latlon = localizationChooseAddress?.geolocation ?: ""
                )
            )
        }
    }

    init {
        setGraphqlQuery(GetARDataQuery())
        setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProductArResponse::class.java)
    }

    suspend fun executeOnBackground(requestParams: Map<String, Any>): ProductArUiModel {
        setRequestParams(requestParams)
        val data = executeOnBackground()
        return mapIntoUiModel(data)
    }

    private fun mapIntoUiModel(response: ProductArResponse): ProductArUiModel {
        return ProductArUiModel(
            optionBgImage = response.data.optionBgImage,
            provider = response.data.provider,
            options = response.data.productArs.associateBy(
                { it.productID },
                { ar ->
                    ar.copy(
                        providerDataCompiled = convertToObject(ar.providerData),
                        priceFmt = ar.priceFmt.ifNullOrBlank { ar.price.getCurrencyFormatted() },
                        slashPriceFmt = ar.slashPriceFmt.ifNullOrBlank {
                            ar.campaignInfo.originalPrice?.getCurrencyFormatted().orEmpty()
                        },
                        discPercentage = ar.discPercentage.ifNullOrBlank {
                            ar.campaignInfo.discountedPercentage.orZero().percentFormatted()
                        }
                    )
                }
            ),
            metaData = response.data.metaData
        )
    }

    private fun convertToObject(modifaceRawJson: String): ModifaceProvider {
        return gson.fromJson(modifaceRawJson, Array<ModifaceProvider>::class.java).firstOrNull()
            ?: ModifaceProvider()
    }
}
