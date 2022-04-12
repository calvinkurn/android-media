package com.tokopedia.product.detail.usecase

import com.tokopedia.graphql.FingerprintManager
import com.tokopedia.graphql.GraphqlCacheManager
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlError
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.product.detail.common.ProductDetailCommonConstant
import com.tokopedia.product.detail.common.data.model.rates.TokoNowParam
import com.tokopedia.product.detail.common.data.model.rates.UserLocationRequest
import com.tokopedia.product.detail.data.model.ProductInfoP2Data
import com.tokopedia.product.detail.data.model.ProductInfoP2UiData
import com.tokopedia.product.detail.data.model.affiliate.AffiliateUIIDRequest
import com.tokopedia.product.detail.data.util.DynamicProductDetailMapper
import com.tokopedia.product.detail.data.util.OnErrorLog
import com.tokopedia.product.detail.view.util.CacheStrategyUtil
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by Yehezkiel on 20/07/20
 */
class GetProductInfoP2DataUseCase @Inject constructor(private val graphqlRepository: GraphqlRepository) : UseCase<ProductInfoP2UiData>(), CoroutineScope {

    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + SupervisorJob()

    companion object {
        fun createParams(productId: String, pdpSession: String, deviceId: String, userLocationRequest: UserLocationRequest, affiliateUUIDRequest : AffiliateUIIDRequest?, tokonow: TokoNowParam): RequestParams =
                RequestParams.create().apply {
                    putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                    putString(ProductDetailCommonConstant.PARAM_PDP_SESSION, pdpSession)
                    putString(ProductDetailCommonConstant.PARAM_DEVICE_ID, deviceId)
                    putObject(ProductDetailCommonConstant.PARAM_USER_LOCATION, userLocationRequest)
                    if(affiliateUUIDRequest!=null)
                        putObject(ProductDetailCommonConstant.PARAM_AFFILIATE_UUID, affiliateUUIDRequest)
                    putObject(ProductDetailCommonConstant.PARAM_TOKONOW, tokonow)
                }

        val QUERY = """query GetPdpGetData(${'$'}productID: String,${'$'}deviceID: String, ${'$'}pdpSession: String, ${'$'}userLocation: pdpUserLocation, ${'$'}affiliate: pdpAffiliate, ${'$'}tokonow: pdpTokoNow) {
          pdpGetData(productID: ${'$'}productID,deviceID: ${'$'}deviceID, pdpSession: ${'$'}pdpSession, userLocation: ${'$'}userLocation, affiliate: ${'$'}affiliate, tokonow: ${'$'}tokonow) {
            error {
              Code
              Message
              DevMessage
            }
            ppGetItemDetailPage{
              program{
                protectionAvailable
                title
                subTitle
                titlePDP
                subTitlePDP
                iconURL
                partnerText
                partnerLogo
                linkURL
                isAppLink
              }
            }
            productView
            wishlistCount
            shopCommitment {
              result {
                isNowActive
                staticMessages {
                  pdpMessage
                }
              }
              error {
                message
              }
            }
            reputationShop {
              badge
              badgeHD
              score
              scoreMap
            }
            shopInfo {
              closedInfo {
                closedNote
                reason
                until
                detail {
                  openDateUTC
                }
              }
              shipmentInfo {
                code
                isAvailable
                shipmentID
                image
                name
                isPickup
                maxAddFee
                awbStatus
                product {
                  isAvailable
                  productName
                  shipProdID
                  additionalFee
                  minimumWeight
                  uiHidden
                }
              }
              activeProduct
              createInfo {
                shopCreated
                epochShopCreated
                openSince
                epochShopCreatedUTC
              }
              shopTerms
              shopAssets {
                avatar
                cover
              }
              shopCore {
                description
                domain
                shopID
                name
                tagLine
                url
              }
              shopLastActive
              location
              statusInfo {
                shopStatus
                statusMessage
                statusTitle
                isIdle
              }
              isAllowManage
              isFreeReturns
              shippingLoc {
                districtID
                districtName
                postalCode
                addressStreet
                latitude
                longitude
                provinceID
                cityID
                cityName
                countryName
                provinceName
              }
              shopStats {
                productSold
                totalTx
                totalShowcase
              }
              shopType
              badgeURL
              shopTier
              tickerData {
                title
                message
                color
                link
                action
                actionLink
                tickerType
                actionBottomSheet {
                  title
                  message
                  reason
                  buttonText
                  buttonLink
                }
              }
            }
            nearestWarehouse {
              product_id
              warehouse_info {
                warehouse_id
                is_fulfillment
                district_id
                postal_code
                geolocation
              }
            }
            installmentRecommendation {
              data {
                monthly_price
                os_monthly_price
                partner_code
                subtitle
              }
            }
            installmentCalculation {
              message
              data {
                credit_card {
                  partner_code
                  partner_name
                  partner_icon
                  partner_url
                  tnc_id
                  installment_list {
                    term
                    mdr_value
                    mdr_type
                    interest_rate
                    minimum_amount
                    maximum_amount
                    monthly_price
                    os_monthly_price
                    partner_code
                    partner_name
                    partner_icon
                  }
                  instruction_list {
                    order
                    description
                    ins_image_url
                  }
                }
                non_credit_card {
                  partner_code
                  partner_name
                  partner_icon
                  partner_url
                  tnc_id
                  installment_list {
                    term
                    mdr_value
                    mdr_type
                    interest_rate
                    minimum_amount
                    maximum_amount
                    monthly_price
                    os_monthly_price
                    partner_code
                    partner_name
                    partner_icon
                  }
                  instruction_list {
                    order
                    description
                    ins_image_url
                  }
                }
                tnc {
                  tnc_id
                  tnc_list {
                    order
                    description
                  }
                }
              }
            }
            validateTradeIn {
              isEligible
              isDiagnosed
              useKyc
              usedPrice
              remainingPrice
              message
              widgetString
            }
            cartRedirection {
              status
              error_message
              alternate_copy {
                 text
                 cart_type
                 color
              }
              data{
                product_id
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
            upcomingCampaigns {
               campaignID
               campaignType
               campaignTypeName
               startDate
               endDate
               notifyMe
               ribbonCopy
               upcomingType
               productID
               bgColor
             }
            shopTopChatSpeed {
              messageResponseTime
            }
            shopRatingsQuery {
              ratingScore
            }
            shopPackSpeed {
              hour
              speedFmt
            }
            shopFeature{
              IsGoApotik
            }
            restrictionInfo{
                message
                restrictionData{
                    productID
                    isEligible
                    action{
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
            uniqueSellingPoint{
              bebasOngkirExtra{
                icon
              }
            }
            bebasOngkir{
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
            ratesEstimate{
              warehouseID
              products
              bottomsheet {
                title
                iconURL
                subtitle
                buttonCopy
              }
              data {
                totalService
                courierLabel
                cheapestShippingPrice
                destination
                icon
                title
                subtitle
                eTAText
                errors{
                  Code
                  Message
                  DevMessage       
                }
                shippingCtxDesc
                originalShippingRate
                fulfillmentData{
                  icon
                  prefix
                  description
                }
                chipsLabel
                hasUsedBenefit
              }
            }
            merchantVoucherSummary{
                animatedInfo{
                    title
                    subTitle
                    iconURL
                }
                isShown
            }
            reviewImage{
              list{
                imageID
                videoID
                reviewID
                imageSibling
              }
              detail{
                 reviews {
                   reviewId
                   message
                   ratingDescription
                   rating
                   time_format{
                     date_time_fmt1
                   }
                   updateTime
                   isAnonymous
                   isReportable
                   isUpdated
                   reviewer{
                     userID
                     fullName
                     profilePicture
                     url
                   }
                 }
                 images {
                   imageAttachmentID
                   description
                   uriThumbnail
                   uriLarge
                   reviewID
                 }
                 videos {
                   attachmentID
                   url
                   feedbackID
                 }
                 mediaCountFmt
                 mediaCount
               }
              hasNext
              hasPrev
            }
            mostHelpFulReviewData{
              list{
                reviewId
                message
                productRating
                reviewCreateTime
                user {
                  userId
                  fullName
                  image
                }
                imageAttachments{
                  attachmentId
                  imageUrl
                  imageThumbnailUrl
                }
                likeDislike{
                  TotalLike
                  TotalDislike
                  isShowable
                }
                variant{
                  name
                }
              }
            }
            rating {
                ratingScore
                totalRating
                totalReviewTextAndImage
            }
            bundleInfo {
              productID
              bundleID
              groupID
              name
              type
              status
              titleComponent
              finalPriceBundling
              originalPriceBundling
              savingPriceBundling
              preorderString
              bundleItems {
                productID
                name
                picURL
                status
                quantity
                originalPrice
                bundlePrice
                discountPercentage
                stock
              }
  	        }
            ticker {
              tickerInfo {
                productIDs
                tickerData {
                  title
                  message
                  color
                  link
                  action
                  actionLink
                  tickerType
                  actionBottomSheet {
                    title
                    message
                    reason
                    buttonText
                    buttonLink
                  }
                }
              }
            }
          }
    }""".trimIndent()
    }

    private var mCacheManager: GraphqlCacheManager? = null
    private var mFingerprintManager: FingerprintManager? = null
    private var cacheStrategy: GraphqlCacheStrategy = GraphqlCacheStrategy.Builder(CacheType.NONE).build()

    private var requestParams: RequestParams = RequestParams.EMPTY
    private var forceRefresh: Boolean = false

    private var errorLogListener: OnErrorLog? = null

    suspend fun executeOnBackground(requestParams: RequestParams, forceRefresh: Boolean): ProductInfoP2UiData {
        this.requestParams = requestParams
        this.forceRefresh = forceRefresh
        return executeOnBackground()
    }

    override suspend fun executeOnBackground(): ProductInfoP2UiData {
        var p2UiData = ProductInfoP2UiData()
        val p2DataRequest = GraphqlRequest(QUERY,
                ProductInfoP2Data.Response::class.java, requestParams.parameters)
        val cacheStrategy = CacheStrategyUtil.getCacheStrategy(forceRefresh)

        try {
            val gqlResponse = graphqlRepository.response(listOf(p2DataRequest), cacheStrategy)
            val successData = gqlResponse.getData<ProductInfoP2Data.Response>(ProductInfoP2Data.Response::class.java)
            val errorData: List<GraphqlError>? = gqlResponse.getError(ProductInfoP2Data.Response::class.java)

            if (successData == null || errorData?.isNotEmpty() == true || successData.response.error.errorCode != 0) {
                throw RuntimeException()
            }

            p2UiData = mapIntoUiData(successData.response)
        } catch (t: Throwable) {
            Timber.d(t)
            errorLogListener?.invoke(t)
        }
        return p2UiData
    }

    private fun mapIntoUiData(responseData: ProductInfoP2Data): ProductInfoP2UiData {
        val p2UiData = ProductInfoP2UiData()
        responseData.run {
            p2UiData.shopInfo = responseData.shopInfo
            p2UiData.shopSpeed = shopSpeed.hour
            p2UiData.shopChatSpeed = shopChatSpeed.messageResponseTime
            p2UiData.shopRating = shopRating.ratingScore
            p2UiData.productView = productView
            p2UiData.wishlistCount = wishlistCount
            p2UiData.isGoApotik = shopFeature.isGoApotik
            p2UiData.shopBadge = shopBadge.badge
            p2UiData.shopCommitment = shopCommitment.shopCommitment
            p2UiData.productPurchaseProtectionInfo = productPurchaseProtectionInfo
            p2UiData.validateTradeIn = validateTradeIn
            p2UiData.cartRedirection = cartRedirection.data.associateBy({ it.productId }, { it })
            p2UiData.nearestWarehouseInfo = nearestWarehouseInfo.associateBy({ it.productId }, { it.warehouseInfo })
            p2UiData.upcomingCampaigns = upcomingCampaigns.associateBy { it.productId ?: "" }
            p2UiData.productFinancingRecommendationData = productFinancingRecommendationData
            p2UiData.productFinancingCalculationData = productFinancingCalculationData
            p2UiData.ratesEstimate = ratesEstimate
            p2UiData.restrictionInfo = restrictionInfo
            p2UiData.bebasOngkir = bebasOngkir
            p2UiData.uspImageUrl = uspTokoCabangData.uspBoe.uspIcon
            p2UiData.merchantVoucherSummary = merchantVoucherSummary
            p2UiData.helpfulReviews = mostHelpFulReviewData.list
            p2UiData.reviewMediaThumbnails = DynamicProductDetailMapper.generateReviewMediaThumbnails(reviewImage)
            p2UiData.detailedMediaResult = DynamicProductDetailMapper.generateDetailedMediaResult(reviewImage)
            p2UiData.alternateCopy = cartRedirection.alternateCopy
            p2UiData.bundleInfoMap = bundleInfoList.associateBy { it.productId }
            p2UiData.rating = rating
            p2UiData.ticker = ticker
        }
        return p2UiData
    }

    fun setErrorLogListener(setErrorLogListener: OnErrorLog) {
        this.errorLogListener = setErrorLogListener
    }

    fun clearCache() {
        launchCatchError(Dispatchers.IO, block = {
            initCacheManager()
            if (mCacheManager != null && mFingerprintManager != null) {
                val request = GraphqlRequest(QUERY, ProductInfoP2Data.Response::class.java, requestParams.parameters)
                mCacheManager!!.delete(mFingerprintManager!!.generateFingerPrint(
                        request.toString(),
                        cacheStrategy.isSessionIncluded))

            }
        }) {
            it.printStackTrace()
        }
    }

    private fun initCacheManager() {
        if (mCacheManager == null) {
            mCacheManager = GraphqlCacheManager()
        }
        if (mFingerprintManager == null) {
            mFingerprintManager = GraphqlClient.getFingerPrintManager()
        }
    }
}