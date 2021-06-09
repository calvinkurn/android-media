package com.tkpd.atc_variant.usecase

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
        query pdpGetVariantComponent(${'$'}productID : String, ${'$'}source : String, ${'$'}whID : String, ${'$'}pdpSession : String , ${'$'}userLocation: pdpUserLocation) {
            pdpGetVariantComponent(productID: ${'$'}productID, source: ${'$'}source, whID: ${'$'}whID, pdpSession: ${'$'}pdpSession, userLocation: ${'$'}userLocation) {
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
                            warehouseId: String? = null,
                            pdpSession: String? = null): Map<String, Any?> = mapOf(
            ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
            ProductDetailCommonConstant.PARAM_PDP_SESSION to pdpSession,
            ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId,
            ProductDetailCommonConstant.PARAM_TEASER_SOURCE to source,
            ChosenAddressRequestHelper.KEY_CHOSEN_ADDRESS to chosenAddressRequestHelper.getChosenAddress()
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
                data.variantData,
                data.cardRedirection.data.associateBy({ it.productId }, { it }),
                data.nearestWarehouse.associateBy({ it.productId }, { it.warehouseInfo })
        )
    }
}