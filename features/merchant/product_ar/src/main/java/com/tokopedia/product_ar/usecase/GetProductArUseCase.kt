package com.tokopedia.product_ar.usecase

import com.google.gson.Gson
import com.tokopedia.gql_query_annotation.GqlQuery
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product_ar.model.ModifaceProvider
import com.tokopedia.product_ar.model.ProductArResponse
import com.tokopedia.product_ar.model.ProductArUiModel
import javax.inject.Inject

class GetProductArUseCase @Inject constructor(graphqlRepository: GraphqlRepository,
                                              private val gson: Gson) : GraphqlUseCase<ProductArResponse>(graphqlRepository) {

    companion object {
        const val QUERY = """
            query pdpGetARData(${'$'}productID : String, ${'$'}shopID : String,  ${'$'}userLocation: pdpUserLocation) {
                  pdpGetARData(productID: ${'$'}productID, shopID: ${'$'}shopID,  userLocation: ${'$'}userLocation) {
                    provider
                    metadata {
                      shopName
                      categoryID
                      shopType
                      categoryName
                      categoryDetail {
                        id
                        name
                      }
                    }
                    options {
                      psku
                      name
                      productID
                      type
                      providerData
                      price
                      minOrder
                      campaignInfo {
                        isActive
                        campaignID
                        campaignType
                        campaignTypeName
                        discountPercentage
                        originalPrice
                        discountPrice
                        stock
                        stockSoldPercentage
                        minOrder
                      }
                      stock
                      stockCopy
                      button {
                        text
                        color
                        cart_type
                      }
                      unavailableCopy
                    }
                    optionBgImage
                  }
                }
        """

        fun createParams(productId: String,
                         shopId: String,
                         userLocationRequest: ChosenAddressRequestHelper) = mutableMapOf<String, Any>().apply {
            put(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
            put(ProductDetailCommonConstant.PARAM_SHOP_ID, shopId)

            val localizationChooseAddress = userLocationRequest.getChosenAddress()
            put(ProductDetailCommonConstant.PARAM_USER_LOCATION, UserLocationRequest(
                    districtID = localizationChooseAddress?.districtId ?: "",
                    addressID = localizationChooseAddress?.addressId ?: "",
                    postalCode = localizationChooseAddress?.postalCode ?: "",
                    latlon = localizationChooseAddress?.geolocation ?: ""
            ))
        }
    }

    init {
        setGraphqlQuery(PdpGetARData())
        setCacheStrategy(GraphqlCacheStrategy
                .Builder(CacheType.ALWAYS_CLOUD).build())
        setTypeClass(ProductArResponse::class.java)
    }

    @GqlQuery("pdpGetARData", QUERY)
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
                        {
                            it.productID
                        }, {
                    it.copy(providerDataCompiled = convertToObject(it.providerData))
                }),
                metaData = response.data.metaData
        )
    }

    private fun convertToObject(modifaceRawJson: String): ModifaceProvider {
        return gson.fromJson(modifaceRawJson, Array<ModifaceProvider>::class.java).firstOrNull()
                ?: ModifaceProvider()
    }
}