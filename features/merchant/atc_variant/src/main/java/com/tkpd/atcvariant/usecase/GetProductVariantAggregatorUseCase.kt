package com.tkpd.atcvariant.usecase

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregator
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorResponse
import com.tokopedia.product.detail.common.data.model.aggregator.ProductVariantAggregatorUiData
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * Created by Yehezkiel on 05/05/21
 */
class GetProductVariantAggregatorUseCase @Inject constructor(
    private val graphqlRepository: GraphqlRepository,
    @ApplicationContext
    private val context: Context
) : UseCase<ProductVariantAggregatorUiData>() {

    companion object {
        val QUERY = """
        query pdpGetVariantComponent(${'$'}productID : String, ${'$'}source : String, ${'$'}shopID : String, ${'$'}whID : String, ${'$'}pdpSession : String , ${'$'}userLocation: pdpUserLocation, ${'$'}isTokoNow: Boolean, ${'$'}tokonow: pdpTokoNow , ${'$'}extParams: String) {
            pdpGetVariantComponent(productID: ${'$'}productID, source: ${'$'}source, shopID: ${'$'}shopID, whID: ${'$'}whID, pdpSession: ${'$'}pdpSession, userLocation: ${'$'}userLocation, isTokoNow: ${'$'}isTokoNow, tokonow: ${'$'}tokonow, extParams: ${'$'}extParams) {
                    isCashback{
                        percentage
                    }
                    basicInfo {
                          shopID
                          shopName
                          defaultMediaURL
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
                    uniqueSellingPoint{
                       bebasOngkirExtra{
                          icon
                       }
                    }
                    bebasOngkir {
                          products{
                            productID
                            boType
                          }
                          images{
                            boType
                            imageURL
                            tokoCabangImageURL
                          }
                    }
                    shopInfo {
                        shopType
                    }
                    restrictionInfo {
                      message
                      restrictionData {
                        productID
                        isEligible
                        action {
                          actionType
                          title
                          description
                          attributeName
                          badgeURL
                          buttonLink
                          buttonText
                        }
                      }
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
                        optionName
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
                          stockFmt
                          stockCopy
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
                ratesEstimate {
                  warehouseID
                  products
                  data {
                    totalService
                    isSupportInstantCourier
                    destination
                    icon
                    title
                    subtitle
                    eTAText
                    errors {
                      Code
                      Message
                      DevMessage
                    }
                    courierLabel
                    cheapestShippingPrice
                  }
                  bottomsheet {
                    title
                    iconURL
                    subtitle
                    buttonCopy
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

    fun createRequestParams(
        productId: String,
        source: String,
        isTokoNow: Boolean,
        shopId: String,
        extParams: String,
        warehouseId: String? = null,
        pdpSession: String? = null
    ): Map<String, Any?> {
        val chooseAddress = ChooseAddressUtils.getLocalizingAddressData(context)
        return mapOf(
            ProductDetailCommonConstant.PARAM_PRODUCT_ID to productId,
            ProductDetailCommonConstant.PARAM_PDP_SESSION to pdpSession,
            ProductDetailCommonConstant.PARAM_WAREHOUSE_ID to warehouseId,
            ProductDetailCommonConstant.PARAM_TEASER_SOURCE to source,
            ProductDetailCommonConstant.PARAM_TOKO_NOW to isTokoNow,
            ProductDetailCommonConstant.PARAM_SHOP_ID to shopId,
            ProductDetailCommonConstant.PARAM_EXT_PARAMS to extParams,
            ProductDetailCommonConstant.PARAM_USER_LOCATION to UserLocationRequest(
                chooseAddress.district_id,
                chooseAddress.address_id,
                chooseAddress.postal_code,
                chooseAddress.latLong,
                chooseAddress.city_id
            ),
            ProductDetailCommonConstant.PARAM_TOKONOW to generateTokoNow(chooseAddress)
        )
    }

    private var requestParams: Map<String, Any?> = mapOf()

    suspend fun executeOnBackground(requestParams: Map<String, Any?>): ProductVariantAggregatorUiData {
        this.requestParams = requestParams
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductVariantAggregatorUiData {
        val request =
            GraphqlRequest(QUERY, ProductVariantAggregatorResponse::class.java, requestParams)
        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD)
            .build()

        val response = graphqlRepository.response(listOf(request), cacheStrategy)
        val error: List<GraphqlError>? =
            response.getError(ProductVariantAggregatorResponse::class.java)
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
            nearestWarehouse = data.nearestWarehouse.associateBy(
                { it.productId },
                { it.warehouseInfo }),
            alternateCopy = data.cardRedirection.alternateCopy,
            simpleBasicInfo = data.basicInfo,
            shopType = data.shopInfo.shopType,
            boData = data.bebasOngkir,
            rates = data.ratesEstimate,
            reData = data.restrictionInfo,
            uspImageUrl = data.uniqueSellingPoint.uspBoe.uspIcon,
            cashBackPercentage = data.isCashback.percentage,
            isCod = data.isCod
        )
    }

    private fun generateTokoNow(chooseAddress: LocalCacheModel): TokoNowParam {
        return TokoNowParam(
            chooseAddress.shop_id,
            chooseAddress.warehouse_id,
            chooseAddress.service_type
        )
    }
}