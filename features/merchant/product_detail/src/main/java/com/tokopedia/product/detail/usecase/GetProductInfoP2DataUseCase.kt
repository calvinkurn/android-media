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
import com.tokopedia.product.detail.data.model.asUiModel
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
        fun createParams(productId: String, pdpSession: String, deviceId: String, userLocationRequest: UserLocationRequest, tokonow: TokoNowParam): RequestParams =
            RequestParams.create().apply {
                putString(ProductDetailCommonConstant.PARAM_PRODUCT_ID, productId)
                putString(ProductDetailCommonConstant.PARAM_PDP_SESSION, pdpSession)
                putString(ProductDetailCommonConstant.PARAM_DEVICE_ID, deviceId)
                putObject(ProductDetailCommonConstant.PARAM_USER_LOCATION, userLocationRequest)
                putObject(ProductDetailCommonConstant.PARAM_TOKONOW, tokonow)
            }

        val QUERY = """query GetPdpGetData(${'$'}productID: String,${'$'}deviceID: String, ${'$'}pdpSession: String, ${'$'}userLocation: pdpUserLocation, ${'$'}tokonow: pdpTokoNow) {
          pdpGetData(productID: ${'$'}productID,deviceID: ${'$'}deviceID, pdpSession: ${'$'}pdpSession, userLocation: ${'$'}userLocation, tokonow: ${'$'}tokonow) {
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
              shopMultilocation {
                warehouseCount
                eduLink {
                    appLink
                }
              }
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
                ownerID
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
              partnerLabel
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
              usedPrice
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
                override_buttons {
                  text
                  color
                  cart_type
                  onboarding_message
                  show_recommendation
                }
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
                tickers {
                  title
                  message
                  color
                  action
                  link
                }
                isScheduled
                boBadge {
                  imageURL
                  isUsingPadding
                  imageHeight
                }
              }
              boMetadata
              productMetadata {
                productID
                value
              }
              shipmentPlus {
                isShow
                logoUrl
                logoUrlDark
                bgUrl
                bgUrlDark
                text
                action
                actionLink
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
                 mediaCountTitle
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
                userLabel
                userStat {
                  formatted
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
           arInfo{
              productIDs
              applink
              message
              imageUrl
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
            navBar {
              name
              items {
                title
                componentName
              }
            }
            shopFinishRate {
              finishRate
            }
            shopAdditional {
              icon
              title
              applink
              link
              linkText
              subtitle
              label
            }
            obatKeras {
              applink
              subtitle
            }
            customInfoTitle {
               title
               status
               componentName
            }
            socialProofComponent {
                socialProofType
                socialProofID
                title
                subtitle
                icon
                applink {
                    appLink
                }
            }
            reviewList {
                title
                applink
                applinkTitle
                data {
                    userImage
                    userName
                    userTitle
                    userSubtitle
                    reviewText 
                    applink
                    reviewID
                }
            }
            bottomSheetEdu {
              isShow
              appLink
            }
          }
        }
""".trimIndent()
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
        val p2DataRequest = GraphqlRequest(
            QUERY,
            ProductInfoP2Data.Response::class.java,
            requestParams.parameters
        )
        val cacheStrategy = CacheStrategyUtil.getCacheStrategy(forceRefresh)

        try {
            val gqlResponse = graphqlRepository.response(listOf(p2DataRequest), cacheStrategy)
            val successData = gqlResponse.getData<ProductInfoP2Data.Response>(ProductInfoP2Data.Response::class.java)
            val errorData: List<GraphqlError>? = gqlResponse.getError(ProductInfoP2Data.Response::class.java)

            if (successData == null || errorData?.isNotEmpty() == true || successData.response.error.errorCode != 0) {
                throw RuntimeException()
            }

            p2UiData = successData.response.asUiModel()
        } catch (t: Throwable) {
            Timber.d(t)
            errorLogListener?.invoke(t)
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
                mCacheManager!!.delete(
                    mFingerprintManager!!.generateFingerPrint(
                        request.toString(),
                        cacheStrategy.isSessionIncluded
                    )
                )
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
