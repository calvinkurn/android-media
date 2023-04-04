package com.tokopedia.kol.feature.postdetail.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.EventName.ADD_TO_CART
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.EventName.CLICKPG
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.EventName.PROMO_VIEW
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.EventName.VIEW_PG_IRIS
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.Promotions.ITEM_PRODUCT_SGC
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by shruti agarwal on 29/07/22
 */


class ContentDetailNewPageAnalytics @Inject constructor(
    private val trackingQueue: TrackingQueue,
    private val userSession: UserSessionInterface
) {
    companion object {
        const val PROMOTIONS = "promotions"
        const val BUSINESS_UNIT = "businessUnit"
        const val CURRENT_SITE = "currentSite"
        const val USER_ID = "userId"
        const val CONTENT = "content"
        const val PRODUCTS = "products"
        const val LIST = "list"
        const val TOKOPEDIA_MARKET_PLACE = "tokopediamarketplace"
        const val SCREEN_NAME = "screenName"
        const val IS_LOGGED_IN = "isLoggedInStatus"
        const val TRACKER_ID = "trackerId"
        private const val TYPE_VIDEO = "video"
        private const val TYPE_LONG_VIDEO = "long-video"
        const val ACTION_FIELD = "actionField"


        const val FORMAT_THREE_PARAM = "%s - %s - %s"

        private const val SGC_IMAGE = "sgc image"
        private const val LONG_VIDEO_SGC = "sgc long video"
        private const val LONG_VIDEO_SGC_RECOM = "sgc long video recom"
        private const val SGC_VOD_PLAY = "sgc play long video"
        private const val SGC_VOD_PLAY_RECOM = "sgc play long video recom"
        private const val UGC_VOD_PLAY = "ugc play long video"
        private const val UGC_VOD_PLAY_RECOM = "ugc play long video recom"
        private const val SGC_IMAGE_RECOM = "sgc image recom"
        private const val ASGC = "asgc"
        private const val VIDEO = "sgc video"
        private const val ASGC_RECOM = "asgc recom"
        private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT = "FeedXCardProductsHighlight"
        private const val TYPE_FEED_X_CARD_PLAY = "FeedXCardPlay"
        private const val TYPE_FEED_X_CARD_POST = "FeedXCardPost"

        private const val UGC_AUTHOR_TYPE = "3"

    }


    fun sendClickShopSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - shop - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickFollowAsgcRecomEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - follow - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendAsgcMoreProductClicked(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - more product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickThreeDotsSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - three dots - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickLihatProdukSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lihat produk - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickImageSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - image - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendImpressionImageSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        val eventAction: String
        when (contentDetailPageAnalyticsDataModel.mediaType) {
            TYPE_VIDEO -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(
                        contentDetailPageAnalyticsDataModel.type,
                        contentDetailPageAnalyticsDataModel.isFollowed,
                        TYPE_VIDEO,
                        contentDetailPageAnalyticsDataModel.authorType
                    )
                )
            }
            TYPE_LONG_VIDEO -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(
                        contentDetailPageAnalyticsDataModel.type,
                        contentDetailPageAnalyticsDataModel.isFollowed,
                        TYPE_LONG_VIDEO,
                        contentDetailPageAnalyticsDataModel.authorType
                    )
                )
            }
            else -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "image",
                    getPostType(
                        contentDetailPageAnalyticsDataModel.type,
                        contentDetailPageAnalyticsDataModel.isFollowed,
                        contentDetailPageAnalyticsDataModel.mediaType,
                        contentDetailPageAnalyticsDataModel.authorType
                    )
                )

            }
        }
        trackEnhancedEcommerceEventWithMap(
            eventName = PROMO_VIEW,
            eventAction = eventAction,
            eventLabel = EventLabel.getPostLabel(
                contentDetailPageAnalyticsDataModel
            ),
            eCommerceData = getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPost(
                            contentDetailPageAnalyticsDataModel,
                            if (contentDetailPageAnalyticsDataModel.isFollowed) Promotions.ITEM_NAME_IMAGE_SGC else Promotions.ITEM_NAME_POST_SGC,
                        )
                    )
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickLikeSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - like - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickDoubleTapLikeUnlikeSgcImageEvent(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        isLiked: Boolean
    ) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = if (isLiked) "click - double tap like - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }" else "click - double tap unlike - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickCommentSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - comment - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33262
        )
    }

    fun sendClickLehatSemuaCommentClick(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lihat semua - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33262
        )
    }

    fun sendClickShareSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - share - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33263
        )
    }

    fun sendClickShopNameBelowSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - shop name below - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33264
        )
    }

    fun sendClickLihatSelengkapnyaSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lihat selengkapnya - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33265
        )
    }

    fun sendClickXSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - x - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33266
        )
    }

    fun sendClickGreyAreaProductBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - grey area - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33266
        )
    }

    fun sendImpressionProductSgcImageEvent(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        feedXProducts: List<FeedXProduct>
    ) {
        trackEnhancedEcommerceEventWithMap(
            eventName = EventName.PRODUCT_VIEW,
            eventAction = "impression - product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getProductLabel(
                contentDetailPageAnalyticsDataModel
            ),
            eventCategory = if (contentDetailPageAnalyticsDataModel.isTypeSGCVideo) EventCategory.CONTENT_DETAIL_PAGE else EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eCommerceData = DataLayer.mapOf(
                Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR,
                "impressions", getProductItems(feedXProducts)
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickProductSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = EventName.PRODUCT_CLICK,
            eventAction = "click - product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getProductLabel(
                contentDetailPageAnalyticsDataModel
            ),
            eventCategory = if (contentDetailPageAnalyticsDataModel.isTypeSGCVideo) EventCategory.CONTENT_DETAIL_PAGE else EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eCommerceData = DataLayer.mapOf(
                EventName.CLICK, mapOf(
                    "actionField" to mapOf(
                        "list" to ITEM_PRODUCT_SGC
                    ),
                    "products" to getSingleProductList(contentDetailPageAnalyticsDataModel)
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )

    }

    fun sendClickThreeDotsSgcImageEventForBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - three dots - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33269
        )
    }

    fun sendClickThreeDotsSgcRecomm(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - three dots product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = "34279"
        )
    }

    fun sendClickWishlistProductEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - wishlist - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = if (contentDetailPageAnalyticsDataModel.isTypeSGCVideo) EventCategory.CONTENT_DETAIL_PAGE else EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )

    }

    fun sendClickWishlistProductSgcRecommEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - wishlist product - sgc image recom",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            trackerID = "34280"
        )

    }

    fun sendClickLihatWishlistSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lihat wishlist - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }


    fun sendClickShareSgcImageBottomSheet(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel
    ) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - share - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = if (contentDetailPageAnalyticsDataModel.isTypeASGC || contentDetailPageAnalyticsDataModel.isTypeVOD)
                EventLabel.getPostLabel(
                    contentDetailPageAnalyticsDataModel
                ) else EventLabel.getProductShareLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickGreyAreaSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = EventAction.CLICK_GREY_AREA_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendImpressionPost(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = PROMO_VIEW,
            eventAction = "impression - post - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            eCommerceData = getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPost(
                            contentDetailPageAnalyticsDataModel, Promotions.ITEM_NAME_POST_SGC
                        )
                    )
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendImpressionPostVOD(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = PROMO_VIEW,
            eventAction = "impression - video - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            eCommerceData = getPromoViewData(
                getPromotionsData(
                    listOf(
                        getImpressionPost(
                            contentDetailPageAnalyticsDataModel, Promotions.ITEM_NAME_POST_SGC
                        )
                    )
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickReportReasonSgcImageEvent(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        reportReason: String
    ) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - report reason - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_REPORT,
            eventLabel = EventLabel.getPostReportLabel(
                contentDetailPageAnalyticsDataModel,
                reportReason
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33288
        )
    }

    fun sendClickGreyAreaReportBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - grey area - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_REPORT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33289
        )
    }

    fun sendClickXThreeDotsBottomSHeet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - x - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickXThreeDotsReportSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - x - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_REPORT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33290
        )
    }

    fun sendClickThreeDotsMenuSgcImageEvent(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        selectedOption: String
    ) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - three dots menu - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getThreeDotsSheetLabel(
                contentDetailPageAnalyticsDataModel,
                selectedOption
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33291
        )
    }

    fun sendClickGreyAreaThreeDotsMenu(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - grey area - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33292
        )
    }

    fun sendClickReportSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - report - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = if (contentDetailPageAnalyticsDataModel.isTypeSGCVideo) EventCategory.CONTENT_DETAIL_PAGE else EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33293
        )
    }

    fun sendClickCancelReportSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - cancel report - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33294
        )
    }

    fun sendClickAddToCartAsgcEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {

        val product = contentDetailPageAnalyticsDataModel.feedXProduct
        trackEnhancedEcommerceEventWithMap(
            eventName = ADD_TO_CART,
            eventAction = "click - add to cart - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            eCommerceData = DataLayer.mapOf(Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR) +
                getAddData(
                    getActionFieldData(
                        getListData(
                            "/cdp - ${
                                getPostType(
                                    contentDetailPageAnalyticsDataModel.type,
                                    contentDetailPageAnalyticsDataModel.isFollowed,
                                    contentDetailPageAnalyticsDataModel.mediaType,
                                    contentDetailPageAnalyticsDataModel.authorType
                                )
                            }"
                        )
                    ) +
                        getProductsData(
                            listOf(
                                getProductData(
                                    product.id,
                                    product.name,
                                    product.priceFmt.getDigits().toZeroIfNull(),
                                    1,
                                    contentDetailPageAnalyticsDataModel.shopId,
                                    contentDetailPageAnalyticsDataModel.shopName
                                )
                            )
                        )
                ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickHashtagSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = EventAction.CLICK_HASHTAG_SGC_IMAGE,
            eventLabel = EventLabel.getPostHashtagLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }


    fun sendClickSoundSgcPlayLongVideoEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - sound - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //"34159"
        )
    }

    fun sendClickLanjutMenontonVod(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lanjut menonton - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "34160" else "34178"
        )
    }

    fun sendClickLanjutMenontonLongVideo(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - lanjut menonton - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "34511" else "34529"
        )
    }

    fun eventWatchVideo(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = VIEW_PG_IRIS,
            eventAction = "watch - video - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getWatchVideoLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickVideoSgcVideoEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - video - sgc video",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = "34608"
        )
    }

    fun sendClickFullScreenVOD(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - full screen - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "34186" else "34188"

        )
    }

    fun sendClickFullScreenLongVideo(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - full screen - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "34537" else "34539"

        )
    }


    fun sendClickXShareDetailPage(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_SHARE,
            eventAction = "click - x - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33285" else "34296"

        )
    }

    fun sendClickShareOptionInShareBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_SHARE,
            eventAction = "click - share - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getProductShareLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33286" else "34297"

        )
    }

    fun sendClickShareProductSgcRecommEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - share product - sgc image recom",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = "34281"
        )

    }

    fun sendClickGreyAreaShareBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_SHARE,
            eventAction = "click - grey area - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33287" else "34298"

        )
    }

    fun sendClickBackOnContentDetailpage(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - back - sgc video",
            eventCategory = "content detail page - post detail",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = "34606"
        )
    }

    fun sendClickProductTagAsgcEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = EventName.PRODUCT_CLICK,
            eventAction = "click - product tag - asgc",
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            eCommerceData =
            DataLayer.mapOf(
                EventName.CLICK, mapOf(
                    "actionField" to mapOf(
                        "list" to "/cdp - product bottomsheet"
                    ),
                    "products" to getSingleProductList(contentDetailPageAnalyticsDataModel)
                )
            ),
            trackerID = "34114"

        )
    }

    fun sendClickThreeDotsMenuLaporkanAsgcEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - three dots menu laporkan - asgc",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostReportLabel(
                contentDetailPageAnalyticsDataModel,
                "laporkan"
            ),
            trackerID = "34115"
        )
    }

    /**
    Comment Page Analytics when opened from Content Detail Page
     ***/

    fun openCommentPageAnalytics() {
        createAnalyticsForOpenScreen(
            eventName = EventName.OPEN_SCREEN,
            isLoggedInStatus = userSession.isLoggedIn,
            screenName = "content detail page - comment detail",
            trackerID = "33275"
        )
    }


    fun sendClickHashtagEventCommentPage(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - hashtag - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostHashtagLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33276" else "34287"
        )
    }


    fun sendClickBackOnCommentPage(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - back - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33277" else "34288"
        )
    }


    fun sendClickShopOnConmmentPage(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - shop name - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33278" else "34289"
        )
    }


    fun sendClickCommentCreator(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - comment creator - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33279" else "34290"
        )
    }


    fun sendClickReportOnComment(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - report - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33280" else "34291"
        )
    }

    fun sendClickDeleteComment(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - delete - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33281" else "34292"
        )
    }

    /**
    can be used when like button developed on comment
     ***/
    fun sendClickLikeComment(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - like - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33282" else "34293"
        )
    }

    fun sendClickSendComment(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - send - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33283" else "34294"
        )
    }

    fun sendClickKembalikanToUndoDeleteSgcImageEvent(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = CLICKPG,
            eventAction = "click - kembalikan to undo delete - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType,
                    contentDetailPageAnalyticsDataModel.authorType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_COMMENT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = if (contentDetailPageAnalyticsDataModel.isFollowed) "33284" else "34295"
        )
    }

    //end of comment trackers here

    private fun getSingleProductList(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        val map = createItemMap(
            contentDetailPageAnalyticsDataModel.feedXProduct,
            contentDetailPageAnalyticsDataModel.rowNumber.toString()
        )
        list.add(map)
        return list
    }

    private fun getProductItems(
        feedXProduct: List<FeedXProduct>
    ): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        for (i in feedXProduct) {
            val map = createItemMap(i, (feedXProduct.indexOf(i) + 1).toString(), ITEM_PRODUCT_SGC)
            list.add(map)
        }
        return list
    }

    private fun getProductsData(
        productDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PRODUCTS, productDataList)

    private fun getProductData(
        id: String,
        name: String,
        price: Int,
        quantity: Int = 1,
        shopId: String,
        shopName: String
    ): Map<String, Any> = DataLayer.mapOf(
        Product.ID, id,
        Product.CATEGORY_ID, id,
        Product.NAME, name,
        Product.PRICE, price,
        Product.QTY, quantity,
        Product.SHOP_ID, shopId,
        Product.SHOP_NAME, shopName,
        Product.SHOP_TYPE, "",
        Product.VARIANT, "",
        Product.BRAND, "",
        Product.CATEGORY, "",
    )

    private fun createItemMap(
        feedXProduct: FeedXProduct,
        index: String,
        list: String = ""
    ): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, feedXProduct.id,
            Product.NAME, feedXProduct.name,
            Product.VARIANT, "",
            Product.LIST, list,
            Product.PRICE,
            if (feedXProduct.isDiscount) feedXProduct.priceDiscount.toString() else feedXProduct.price.toString(),
        )

    private fun getPromoViewData(data: Any): Map<String, Any> =
        DataLayer.mapOf(PROMO_VIEW, data)

    private fun getPromotionsData(
        promotionDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getEcommerceData(data: Any): Map<String, Any> =
        DataLayer.mapOf(Constant.ECOMMERCE, data)

    private fun getAddData(data: Any): Map<String, Any> = DataLayer.mapOf(EventName.ADD, data)

    private fun getActionFieldData(data: Any): Map<String, Any> =
        DataLayer.mapOf(ACTION_FIELD, data)

    private fun getListData(data: Any): Map<String, Any> = DataLayer.mapOf(LIST, data)

    private fun getImpressionPost(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        itemName: String = ""
    ): Map<String, Any> = DataLayer.mapOf(
        Promotions.CREATIVE, contentDetailPageAnalyticsDataModel.mediaUrl,
        Promotions.ID, contentDetailPageAnalyticsDataModel.activityId,
        Promotions.NAME,
        if (itemName.isBlank()) contentDetailPageAnalyticsDataModel.itemName else itemName,
        Promotions.POSITION, contentDetailPageAnalyticsDataModel.rowNumber + 1,
    )

    private fun createAnalyticsData(
        eventName: String,
        eventAction: String = "",
        eventCategory: String = EventCategory.CONTENT_DETAIL_PAGE,
        eventLabel: String = "",
        trackerID: String = ""

    ) {
        val generalData = DataLayer.mapOf(
            TrackAppUtils.EVENT, eventName,
            TrackAppUtils.EVENT_ACTION, eventAction,
            TrackAppUtils.EVENT_CATEGORY, eventCategory,
            TrackAppUtils.EVENT_LABEL, eventLabel,
            BUSINESS_UNIT, CONTENT,
            CURRENT_SITE, TOKOPEDIA_MARKET_PLACE,
            USER_ID, userSession.userId,
            TRACKER_ID, trackerID,
        )

        if (trackerID.isNotEmpty())
            TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }

    private fun getGeneralDataNew(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        trackerID: String = ""
    ): Map<String, Any> = DataLayer.mapOf(
        TrackAppUtils.EVENT, eventName,
        TrackAppUtils.EVENT_CATEGORY, eventCategory,
        TrackAppUtils.EVENT_ACTION, eventAction,
        TrackAppUtils.EVENT_LABEL, eventLabel,
        USER_ID, userSession.userId,
        BUSINESS_UNIT, CONTENT,
        CURRENT_SITE, TOKOPEDIA_MARKET_PLACE,
        TRACKER_ID, trackerID
    )

    private fun createAnalyticsForOpenScreen(
        eventName: String,
        isLoggedInStatus: Boolean? = null,
        screenName: String = "",
        trackerID: String = ""
    ) {
        val generalData = mapOf(
            TrackAppUtils.EVENT to eventName,
            BUSINESS_UNIT to CONTENT,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to userSession.userId.toString(),
            IS_LOGGED_IN to isLoggedInStatus.toString(),
            SCREEN_NAME to screenName,
            TRACKER_ID to trackerID
        )
        if (trackerID.isNotEmpty())
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, generalData)

    }

    private fun trackEnhancedEcommerceEventWithMap(
        eventName: String,
        eventAction: String,
        eventCategory: String = EventCategory.CONTENT_DETAIL_PAGE,
        eventLabel: String,
        eCommerceData: Map<String, Any>,
        trackerID: String = ""
    ) {
        if (trackerID.isNotEmpty()) {
            trackingQueue.putEETracking(
                getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel, trackerID)
                    .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
            )
        }
    }

    /**
     * Send all pending analytics in trackingQueue
     */
    fun sendPendingAnalytics() {
        trackingQueue.sendAll()
    }


    private fun getPostType(
        type: String,
        isFollowed: Boolean,
        mediaType: String = "image",
        authorType: String
    ): String {
        return if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed)
            ASGC_RECOM
        else if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && isFollowed)
            ASGC
        else if (type == TYPE_FEED_X_CARD_PLAY && !isFollowed && authorType == UGC_AUTHOR_TYPE)
            UGC_VOD_PLAY_RECOM
        else if (type == TYPE_FEED_X_CARD_PLAY && isFollowed && authorType == UGC_AUTHOR_TYPE)
            UGC_VOD_PLAY
        else if (type == TYPE_FEED_X_CARD_PLAY && !isFollowed)
            SGC_VOD_PLAY_RECOM
        else if (type == TYPE_FEED_X_CARD_PLAY && isFollowed)
            SGC_VOD_PLAY
        else if (type == TYPE_FEED_X_CARD_POST && isFollowed && mediaType == TYPE_LONG_VIDEO)
            LONG_VIDEO_SGC
        else if (type == TYPE_FEED_X_CARD_POST && !isFollowed && mediaType == TYPE_LONG_VIDEO)
            LONG_VIDEO_SGC_RECOM
        else if (type == TYPE_FEED_X_CARD_POST && mediaType == TYPE_VIDEO)
            VIDEO
        else if (type != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed)
            SGC_IMAGE_RECOM
        else
            SGC_IMAGE
    }


    private object EventName {
        const val ADD = "add"
        const val VIEW_PG_IRIS = "viewPGIris"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val ADD_TO_CART = "addToCart"
        const val OPEN_SCREEN = "openScreen"
        const val CLICK = "click"
        const val CLICKPG = "clickPG"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
    }

    private object EventCategory {
        const val CONTENT_DETAIL_PAGE = "content detail page"
        const val CONTENT_DETAIL_PAGE_BOTTOM_SHEET = "content detail page - bottom sheet"
        const val CONTENT_DETAIL_PAGE_REPORT = "content detail page - report"
        const val CONTENT_DETAIL_PAGE_THREE_DOTS = "content detail page - three dots page"
        const val CONTENT_DETAIL_PAGE_SHARE = "content detail page - share"
        const val CONTENT_DETAIL_PAGE_COMMENT = "content detail page - comment"
    }

    private object EventAction {
        const val CLICK_GREY_AREA_SGC_IMAGE = "click - grey area - sgc image"
        const val CLICK_HASHTAG_SGC_IMAGE = "click - hashtag - sgc image"
    }

    private object EventLabel {

        fun getPostLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getProductLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.productId} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getProductShareLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.shareMedia} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getPostReportLabel(
            contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
            reportReason: String
        ) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${reportReason} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getPostHashtagLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.hashtag} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getThreeDotsSheetLabel(
            contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
            selectedOption: String
        ) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${selectedOption} - ${contentDetailPageAnalyticsDataModel.source}"

        fun getWatchVideoLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.duration} - ${contentDetailPageAnalyticsDataModel.source}"
    }

    private object Promotions {
        const val CREATIVE = "creative"
        const val ID = "id"
        const val NAME = "name"
        const val POSITION = "position"
        const val ITEM_NAME_IMAGE_SGC = "/cdp - image"
        const val ITEM_NAME_POST_SGC = "/cdp - post"
        const val ITEM_PRODUCT_SGC = "/cdp - product bottomsheet"
    }

    private object Product {
        const val ID = "id"
        const val NAME = "name"
        const val PRICE = "price"
        const val QTY = "quantity"
        const val LIST = "list"
        const val SHOP_ID = "shop_id"
        const val CATEGORY_ID = "category_id"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val VARIANT = "variant"
        const val INDEX = "index"
    }

}
