package com.tkpd.atcvariant.usecase

import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.common.ChosenAddressRequestHelper
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorResponse
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/05/21
 */
class GetProductVariantAggregatorUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository,
                                                             private val chosenAddressRequestHelper: ChosenAddressRequestHelper)
    : UseCase<ProductVariantAggregatorUiData>() {

    companion object {
        val QUERY = """
        query pdpGetVariantComponent(${'$'}productID : String, ${'$'}source : String, ${'$'}shopID : String, ${'$'}whID : String, ${'$'}pdpSession : String , ${'$'}userLocation: pdpUserLocation, ${'$'}isTokoNow: Boolean) {
            pdpGetVariantComponent(productID: ${'$'}productID, source: ${'$'}source, shopID: ${'$'}shopID, whID: ${'$'}whID, pdpSession: ${'$'}pdpSession, userLocation: ${'$'}userLocation, isTokoNow: ${'$'}isTokoNow) {
                    basicInfo {
                          shopID
                          shopName
                          category {
                            id
                            name
                            title
                            breadcrumbURL
                            isAdult
                            lastUpdateCategory
                            detail {
                              id
                              name
                              breadcrumbURL
                              isAdult
                            }
                          }
                    }
                    bebasOngkir {
                      products {
                        productID
                        boType
                      }
                    }
                    shopInfo {
                        shopType
                    }
                    variantData { 
                      errorCode
                      parentID
                      defaultChild
                      variants {
                        productVariantID
                        variantID
                        name
                        identifier
                        option {
                          picture {
                            url
                            url200
                            url100
                          }
                          productVariantOptionID
                          variantUnitValueID
                          value
                          hex
                        }
                      }
                      children {
                        productID
                        price
                        priceFmt
                        sku
                        optionID
                        productName
                        productURL
                        picture {
                          url
                          url200
                          url100
                        }
                        stock {
                          stock
                          isBuyable
                          stockWording
                          stockWordingHTML
                          minimumOrder
                          maximumOrder
                        }
                        isCOD
                        isWishlist
                        campaignInfo {
                          campaignID
                          campaignType
                          campaignTypeName
                          discountPercentage
                          originalPrice
                          discountPrice
                          stock
                          stockSoldPercentage
                          threshold
                          startDate
                          endDate
                          endDateUnix
                          appLinks
                          isAppsOnly
                          isActive
                          hideGimmick
                          isCheckImei
                          isUsingOvo
                          minOrder
                          background
                          campaignIdentifier
                          paymentInfoWording
                        }
                      }
                      sizeChart
                }
                cartRedirection {
                  status
                  error_message
                  alternate_copy {
                     text
                     cart_type
                     color
                  }
                  data {
                    product_id
                    config_name
                    available_buttons {
                      text
                      color
                      cart_type
                      onboarding_message
                      show_recommendation
                    }
                    unavailable_buttons
                    hide_floating_button
                  }
                }
                warehouseInfo{
                  product_id
                  warehouse_info {
                    warehouse_id
                    is_fulfillment
                    district_id
                    postal_code
                    geolocation
                  }
                }
                callsError{
                  cartRedirection{
                    Code
                    Message
                  }
                }
            }
        }
        """.trimIndent()
    }

    fun createRequestParams(productId: String,
                            source: String,
                            isTokoNow:Boolean,
                            shopId: String,
                            warehouseId: String? = null,
                            pdpSession: String? = null): Map<String, Any?> = mapOf(
            ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
            ProductDetailCommonConstant.PARAM_PDP_SESSION to pdpSession,
            ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId,
            ProductDetailCommonConstant.PARAM_TEASER_SOURCE to source,
            ProductDetailCommonConstant.PARAM_TOKO_NOW to isTokoNow,
            ProductDetailCommonConstant.PARAM_SHOP_ID to shopId,
            ProductDetailCommonConstant.PARAM_USER_LOCATION to UserLocationRequest(
                    chosenAddressRequestHelper.getChosenAddress()?.districtId ?: "",
                    chosenAddressRequestHelper.getChosenAddress()?.addressId ?: "",
                    chosenAddressRequestHelper.getChosenAddress()?.postalCode ?: "",
                    chosenAddressRequestHelper.getChosenAddress()?.geolocation ?: ""
            )

    )

    private var requestParams: Map<String, Any?> = mapOf()

    suspend fun executeOnBackground(requestParams: Map<String, Any?>): ProductVariantAggregatorUiData {
        this.requestParams = requestParams
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductVariantAggregatorUiData {
        val request = GraphqlRequest(QUERY, ProductVariantAggregatorResponse::class.java, requestParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
                .build()

        val response = graphqlRepository.getReseponse(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? = response.getError(ProductVariantAggregatorResponse::class.java)
        val data = response.getSuccessData<ProductVariantAggregatorResponse>()

        if (error != null && error.isNotEmpty()) {
            throw MessageErrorException(error.firstOrNull()?.message ?: "")
        }

        if (!data.response.variantData.hasVariant && !data.response.variantData.hasChildren) {
            throw NullPointerException("variant empty")
        }

        return mapToUiData(data.response)
    }

    private fun mapToUiData(data: ProductVariantAggregator): ProductVariantAggregatorUiData {
        return ProductVariantAggregatorUiData(
                variantData = data.variantData,
                cardRedirection = data.cardRedirection.data.associateBy({ it.productId }, { it }),
                nearestWarehouse = data.nearestWarehouse.associateBy({ it.productId }, { it.warehouseInfo }),
                alternateCopy = data.cardRedirection.alternateCopy,
                simpleBasicInfo = data.basicInfo,
                shopType = data.shopInfo.shopType,
                boData = data.bebasOngkir.boProduct
        )
    }
}