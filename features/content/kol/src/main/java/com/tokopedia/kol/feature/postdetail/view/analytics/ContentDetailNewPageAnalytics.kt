package com.tokopedia.kol.feature.postdetail.view.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.feedcomponent.data.feedrevamp.FeedXProduct
import com.tokopedia.kol.feature.postdetail.view.analytics.ContentDetailNewPageAnalytics.EventName.PROMO_VIEW
import com.tokopedia.kol.feature.postdetail.view.datamodel.ContentDetailPageAnalyticsDataModel
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.trackingoptimizer.constant.Constant
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
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
        private const val TYPE_IMAGE = "image"
        private const val TYPE_VIDEO = "video"
        private const val TYPE_LONG_VIDEO = "long-video"
        const val ACTION_FIELD = "actionField"


        const val FORMAT_TWO_PARAM = "%s - %s"
        const val FORMAT_THREE_PARAM = "%s - %s - %s"

        private const val SGC_IMAGE = "sgc image"
        private const val LONG_VIDEO_SGC = "sgc long video"
        private const val LONG_VIDEO_SGC_RECOM = "sgc long video recom"
        private const val SGC_VOD_PLAY = "sgc play long video"
        private const val SGC_VOD_PLAY_RECOM = "sgc play long video recom"
        private const val SGC_IMAGE_RECOM = "sgc image recom"
        private const val ASGC = "asgc"
        private const val VIDEO = "sgc video"
        private const val ASGC_RECOM = "asgc recom"
        private const val TOPADS = "topads"
        private const val TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT= "FeedXCardProductsHighlight"
        private const val TYPE_FEED_X_CARD_PLAY= "FeedXCardPlay"
        private const val TYPE_FEED_X_CARD_POST= "FeedXCardPost"
        private const val TYPE_FEED_X_CARD_PRODUCT_TOPADS= "topads_headline_new"

    }


    //asgc recom = 34085
    fun sendClickShopSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - shop - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    //asgc recom = 34086
    fun sendClickFollowAsgcRecomEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - follow - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }
    //asgc recom = 34087
    fun sendAsgcMoreProductClicked(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - more product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickThreeDotsSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - three dots - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickLihatProdukSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - lihat produk - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickImageSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_IMAGE_SGC,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendImpressionImageSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        var eventAction = ""
        when(contentDetailPageAnalyticsDataModel.mediaType) {
            TYPE_VIDEO -> {
                eventAction =  String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, TYPE_VIDEO)
                )
            }
            TYPE_LONG_VIDEO -> {
                eventAction =  String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "video",
                    getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, TYPE_LONG_VIDEO)
                )
            }
            else -> {
                eventAction = String.format(
                    FORMAT_THREE_PARAM,
                    "impression",
                    "image",
                    getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, contentDetailPageAnalyticsDataModel.mediaType)
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
                        getImpressionPost(contentDetailPageAnalyticsDataModel, Promotions.ITEM_NAME_IMAGE_SGC)
                    )
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    //34096
    fun sendClickLikeSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - like - ${getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, contentDetailPageAnalyticsDataModel.mediaType)}",
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickDoubleTapLikeUnlikeSgcImageEvent(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        isLiked: Boolean
    ) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = if (isLiked) EventAction.CLICK_DOUBLE_TAP_LIKE_SGC else EventAction.CLICK_DOUBLE_TAP_UNLIKE_SGC,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickCommentSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_COMMENT_SGC ,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33262
        )
    }

    //34097
    fun sendClickShareSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - share - ${getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, contentDetailPageAnalyticsDataModel.mediaType)}" ,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33263
        )
    }

    fun sendClickShopNameBelowSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_SHOP_NAME_BELOW_SGC ,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33264
        )
    }

    fun sendClickLihatSelengkapnyaSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_SELENGKAPNYA ,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33265
        )
    }

    //34088
    fun sendClickXSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - x  - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33266
        )
    }

    //34095
    fun sendClickGreyAreaProductBottomSheet (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - grey area - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33266
        )
    }

    fun sendImpressionProductSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, feedXProducts: List<FeedXProduct>) {

        trackEnhancedEcommerceEventWithMap(
            eventName = EventName.PRODUCT_VIEW,
            eventAction = "impression - product - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            }",
            eventLabel = EventLabel.getProductLabel(
                contentDetailPageAnalyticsDataModel
            ),
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eCommerceData = DataLayer.mapOf(
                Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR,
                "impressions",
                getProductItems(
                    feedXProducts,
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }

    fun sendClickProductSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = EventName.PRODUCT_CLICK,
            eventAction = "click - product - ${
            getPostType(
                contentDetailPageAnalyticsDataModel.type,
                contentDetailPageAnalyticsDataModel.isFollowed,
                contentDetailPageAnalyticsDataModel.mediaType
            )}",
            eventLabel = EventLabel.getProductLabel(
                contentDetailPageAnalyticsDataModel
            ),
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET ,
            eCommerceData = DataLayer.mapOf(
                EventName.CLICK, mapOf(
                "actionField" to mapOf(
                    "list" to Promotions.ITEM_PRODUCT_SGC
                ),
                "products" to getSingleProductList(contentDetailPageAnalyticsDataModel)
            )
            ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )

    }
 //asgc recom 34091
    fun sendClickThreeDotsSgcImageEventForBottomSheet(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - three dots - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )}",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33269
        )
    }

    //"34092"
    fun sendClickWishlistSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - wishlist - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )}",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33270
        )

    }
   //34093
    fun sendClickLihatWishlistSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - lihat wishlist - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )}",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33271
        )
    }


    //34094
    fun sendClickShareSgcImageBottomSheet(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel,
        shareMedia: String = ""
    ) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - share - ${
                getPostType(
                    contentDetailPageAnalyticsDataModel.type,
                    contentDetailPageAnalyticsDataModel.isFollowed,
                    contentDetailPageAnalyticsDataModel.mediaType
                )}",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductShareLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33272
        )
    }

    fun sendClickGreyAreaSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_GREY_AREA_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33273
        )
    }

    //sgc image asgc recom = 34084

    fun sendImpressionPost(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        trackEnhancedEcommerceEventWithMap(
            eventName = PROMO_VIEW,
            eventAction = "impression - post - ${getPostType(contentDetailPageAnalyticsDataModel.type,contentDetailPageAnalyticsDataModel.isFollowed,contentDetailPageAnalyticsDataModel.mediaType)}",
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

    fun sendClickReportReasonSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, reportReason: String) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = "click - $reportReason - sgc image",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_REPORT,
            eventLabel = EventLabel.getPostReportLabel(contentDetailPageAnalyticsDataModel, reportReason),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33288
        )
    }

    fun sendClickGreyAreaReportBottomSheet (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_GREY_AREA_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_REPORT,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33289
        )
    }

    fun sendClickXThreeDotsBottomSHeet (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_X_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33290
        )
    }

    fun sendClickThreeDotsMenuSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, selectedOption: String) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_THREE_DOTS_MENU_BOTTOM_SHEET,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getThreeDotsSheetLabel(contentDetailPageAnalyticsDataModel, selectedOption),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33291
        )
    }

    fun sendClickGreyAreaThreeDotsMenu (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_GREY_AREA_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33292
        )
    }

    fun sendClickReportSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_REPORT_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33293
        )
    }

    fun sendClickCancelReportSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_CANCEL_REPORT_SGC_IMAGE,
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_THREE_DOTS,
            eventLabel = EventLabel.getPostLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33294
        )
    }

    //34112
    fun sendClickAddToCartAsgcEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {

        val product = contentDetailPageAnalyticsDataModel.feedXProduct
        trackEnhancedEcommerceEventWithMap(
            eventName = PROMO_VIEW,
            eventAction = "click - add to cart - ${getPostType(contentDetailPageAnalyticsDataModel.type,contentDetailPageAnalyticsDataModel.isFollowed,contentDetailPageAnalyticsDataModel.mediaType)}",
            eventCategory = EventCategory.CONTENT_DETAIL_PAGE_BOTTOM_SHEET,
            eventLabel = EventLabel.getProductLabel(contentDetailPageAnalyticsDataModel),
            eCommerceData = DataLayer.mapOf(Product.CURRENCY_CODE, Product.CURRENCY_CODE_IDR) +
                    getAddData(
                        getActionFieldData(getListData("/cdp - ${getPostType(contentDetailPageAnalyticsDataModel.type, contentDetailPageAnalyticsDataModel.isFollowed, contentDetailPageAnalyticsDataModel.mediaType)}")) +
                                getProductsData(
                                    listOf(
                                        getProductData(
                                            product.id,
                                            product.name,
                                            product.priceFmt.getDigits().toZeroIfNull(),
                                            1,
                                            contentDetailPageAnalyticsDataModel.shopId,
                                            contentDetailPageAnalyticsDataModel.shopName,
                                            contentDetailPageAnalyticsDataModel.type,
                                            contentDetailPageAnalyticsDataModel.isFollowed,
                                            contentDetailPageAnalyticsDataModel.mediaType
                                        )
                                    )
                                )
                    ),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId
        )
    }


    fun sendClickHashtagSgcImageEvent (contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) {
        createAnalyticsData(
            eventName = EventName.CLICKPG,
            eventAction = EventAction.CLICK_HASHTAG_SGC_IMAGE,
            eventLabel = EventLabel.getPostHashtagLabel(contentDetailPageAnalyticsDataModel),
            trackerID = contentDetailPageAnalyticsDataModel.trackerId //33295
        )
    }


    private fun getPromotionsClickData(
        id: String, name: String, position: Int, creative: List<String>
    ): Map<String, Any> {
        val listPromo: MutableList<Map<String, Any>> = mutableListOf()
        creative.forEach {
            listPromo.add(
                DataLayer.mapOf(
                    Promotions.CREATIVE, it,
                    Promotions.ID, id,
                    Promotions.NAME, name,
                    Promotions.POSITION, (position + 1).toString()
                )
            )
        }

        return getPromoClickData(
            getPromotionsData(
                listPromo
            )
        )

    }
    private fun getSingleProductList(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        val map = createItemMap(contentDetailPageAnalyticsDataModel.feedXProduct, contentDetailPageAnalyticsDataModel.rowNumber.toString())
        list.add(map)
        return list
    }
    private fun getProductItems(feedXProduct: List<FeedXProduct>, type: String, isFollowed: Boolean, mediaType: String): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()
        for (i in feedXProduct) {
            val map = createItemMap(i, (feedXProduct.indexOf(i)+1).toString())
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
        shopName: String,
        type: String = "",
        isFollowed: Boolean = false,
        mediaType: String =""
    ): Map<String, Any> = DataLayer.mapOf(
        Product.ID, id,
        Product.CATEGORY_ID,id,
        Product.NAME, name,
        Product.PRICE, price,
        Product.QTY, quantity,
        Product.SHOP_ID, shopId,
        Product.SHOP_NAME, shopName,
        Product.SHOP_TYPE, "",
        Product.VARIANT, "",
        Product.BRAND, "",
        Product.CATEGORY, "",
        Product.DIMENSION_39, "/feed - ${getPostType(type, isFollowed, mediaType)}"
    )
    private fun createItemMap(feedXProduct: FeedXProduct, index: String): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, feedXProduct.id,
            Product.NAME, feedXProduct.name,
            Product.VARIANT, "",
            Product.PRICE,
            if (feedXProduct.isDiscount) feedXProduct.priceDiscount.toString() else feedXProduct.price.toString(),
        )

    private fun getPromoClickData(data: Any): Map<String, Any> =
        DataLayer.mapOf(EventName.PROMO_CLICK, data)

    private fun getPromoViewData(data: Any): Map<String, Any> =
        DataLayer.mapOf(EventName.PROMO_VIEW, data)

    private fun getPromotionsData(
        promotionDataList: List<Any>,
    ): Map<String, Any> = DataLayer.mapOf(PROMOTIONS, promotionDataList)

    private fun getEcommerceData(data: Any): Map<String, Any> = DataLayer.mapOf(Constant.ECOMMERCE, data)

    private fun getAddData(data: Any): Map<String, Any> = DataLayer.mapOf(EventName.ADD, data)

    private fun getActionFieldData(data: Any): Map<String, Any> =
        DataLayer.mapOf(ACTION_FIELD, data)

    private fun getListData(data: Any): Map<String, Any> = DataLayer.mapOf(LIST, data)




    private fun getPromotionsViewData(
        channelId: String, name: String, position: Int, creative: List<String>
    ): Map<String, Any> {
        val listPromo: MutableList<Map<String, Any>> = mutableListOf()
        creative.forEach {
            listPromo.add(
                DataLayer.mapOf(
                    Promotions.ID, channelId,
                    Promotions.NAME, name,
                    Promotions.CREATIVE, it,
                    Promotions.POSITION, (position + 1).toString()
                )
            )
        }

        return getPromoViewData(
            getPromotionsData(
                listPromo
            )
        )
    }
    private fun getImpressionPost(
        contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, itemName: String
    ): Map<String, Any> = DataLayer.mapOf(
        Promotions.CREATIVE, contentDetailPageAnalyticsDataModel.mediaUrl,
        Promotions.ID, contentDetailPageAnalyticsDataModel.activityId,
        Promotions.NAME, itemName,
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
        screenName: String = ""
    ) {
        val generalData = mapOf(
            TrackAppUtils.EVENT to eventName,
            BUSINESS_UNIT to CONTENT,
            CURRENT_SITE to TOKOPEDIA_MARKET_PLACE,
            USER_ID to userSession.userId.toString(),
            IS_LOGGED_IN to isLoggedInStatus.toString(),
            SCREEN_NAME to screenName
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(generalData)

    }

    private fun trackEnhancedEcommerceEvent(
        eventName: String,
        eventAction: String,
        eventCategory: String,
        eventLabel: String,
        activityId: String, name: String, position: Int, creative: List<String>
    ) {
        val eCommerceData =
            if (eventName == EventName.PROMO_VIEW) {
                getPromotionsViewData(activityId, name, position, creative)
            } else {
                getPromotionsClickData(activityId, name, position, creative)
            }
        trackingQueue.putEETracking(
            getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel)
                .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
        )
    }
    private fun trackEnhancedEcommerceEventWithMap(
        eventName: String,
        eventAction: String,
        eventCategory: String = EventCategory.CONTENT_DETAIL_PAGE ,
        eventLabel: String,
        eCommerceData: Map<String, Any>,
        trackerID: String = ""
    ) {
        trackingQueue.putEETracking(
            getGeneralDataNew(eventName, eventCategory, eventAction, eventLabel, trackerID)
                .plus(getEcommerceData(eCommerceData)) as HashMap<String, Any>
        )
    }
    private fun getPostType(
        type: String,
        isFollowed: Boolean,
        mediaType: String = "image"
    ): String {
        return if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed)
            ASGC_RECOM
        else if (type == TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && isFollowed)
            ASGC
        else if (type == TYPE_FEED_X_CARD_PLAY && !isFollowed)
            SGC_VOD_PLAY_RECOM
        else if (type == TYPE_FEED_X_CARD_PLAY && isFollowed)
            SGC_VOD_PLAY
        else if (type == TYPE_FEED_X_CARD_PRODUCT_TOPADS)
            TOPADS
        else if (type == TYPE_FEED_X_CARD_POST && isFollowed && mediaType == TYPE_LONG_VIDEO)
            LONG_VIDEO_SGC
        else if (type == TYPE_FEED_X_CARD_POST && !isFollowed && mediaType == TYPE_LONG_VIDEO)
            LONG_VIDEO_SGC_RECOM
        else if (type == TYPE_FEED_X_CARD_POST && mediaType == VIDEO)
            VIDEO
        else if (type != TYPE_FEED_X_CARD_PRODUCT_HIGHLIGHT && !isFollowed)
            SGC_IMAGE_RECOM
        else
            SGC_IMAGE
    }



    private object EventName {
        const val VIEW_ITEM = "view_item"
        const val ADD = "add"
        const val SELECT_CONTENT = "select_content"
        const val CLICK_HOMEPAGE = "clickHomepage"
        const val VIEW_HOMEPAGE_IRIS = "viewHomepageIris"
        const val PROMO_CLICK = "promoClick"
        const val PROMO_VIEW = "promoView"
        const val OPEN_SCREEN = "openScreen"
        const val CLICK_FEED = "clickFeed"
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
    }
    private object EventAction {
        const val POST_IMPRESSION = "impression - post - cdp"
        const val WATCH_VIDEO_CDP = "watch - video - cdp"
        const val CLICK_LANJUT_MENONTON = "click - lanjut menonton - cdp"
        const val CLICK_PRODUCT_TAG_CDP = "click - product tag - cdp"
        const val CLICK_MUTE_UNMUTE = "click - mute - cdp"
        const val CLICK_FULL_SCREEN = "click - full screen - cdp"
        const val CLICK_DOUBLE_TAP_LIKE_SGC = "click - double tap like - sgc image"
        const val CLICK_DOUBLE_TAP_UNLIKE_SGC = "click - double tap unlike - sgc image"
        const val CLICK_LIKE_SGC = "click - like - sgc image"
        const val CLICK_COMMENT_SGC = "click - comment - sgc image"
        const val CLICK_SHARE_SGC = "click - share - sgc image"
        const val CLICK_SHOP_IMAGE_SGC = "click - shop - sgc image"
        const val CLICK_SHOP_NAME_BELOW_SGC = "click - shop name below - sgc image"
        const val CLICK_THREE_DOTS_IMAGE_SGC = "click - three dots - sgc image"
        const val CLICK_LIHAT_PRODUK_IMAGE_SGC = "click - lihat produk - sgc image"
        const val CLICK_IMAGE_SGC = "click - image - sgc image"
        const val CLICK_SELENGKAPNYA = "click - lihat selengkapnya - sgc image"
        const val CLICK_X_SGC_IMAGE = "click - x - sgc image"
        const val CLICK_PRODUCT_SGC_IMAGE = "click - product - sgc image"
        const val IMPRESS_PRODUCT_SGC_IMAGE = "impression - product - sgc image"
        const val CLICK_THREE_DOTS_BOTTOM_SHEET = "click - three dots - sgc image"
        const val CLICK_THREE_DOTS_MENU_BOTTOM_SHEET = "click - three dots menu - sgc image"
        const val CLICK_WISHLIST_SGC_IMAGE = "click - wishlist - sgc image"
        const val CLICK_LIHAT_WISHLIST_SGC_IMAGE = "click - lihat wishlist - sgc image"
        const val CLICK_GREY_AREA_SGC_IMAGE = "click - grey area - sgc image"
        const val IMPRESS_POST_SGC_IMAGE = "impression - post - sgc image"
        const val CLICK_REPORT_SGC_IMAGE = "click - report - sgc image"
        const val CLICK_CANCEL_REPORT_SGC_IMAGE = "click - cancel report - sgc image"
        const val CLICK_HASHTAG_SGC_IMAGE = "click - hashtag - sgc image"





    }
    private object EventLabel {

        fun getPostLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getProductLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.productId} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getProductShareLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.shareMedia} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getPostReportLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, reportReason: String) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${reportReason} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getPostHashtagLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${contentDetailPageAnalyticsDataModel.hashtag} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getThreeDotsSheetLabel(contentDetailPageAnalyticsDataModel: ContentDetailPageAnalyticsDataModel, selectedOption: String) =
            "${contentDetailPageAnalyticsDataModel.activityId} - ${contentDetailPageAnalyticsDataModel.shopId} - ${selectedOption} - ${contentDetailPageAnalyticsDataModel.source}"
        fun getProductTagLabel(shopId: String, productId: String, mediaType: String) =
            "$shopId - $productId - $mediaType"
    }
    
    private object Promotions{
        const val CREATIVE ="creative"
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
        const val SHOP_ID = "shop_id"
        const val CATEGORY_ID = "category_id"
        const val DIMENSION_40 = "dimension40"
        const val DIMENSION_39 = "dimension39"
        const val SHOP_NAME = "shop_name"
        const val SHOP_TYPE = "shop_type"
        const val MEDIA_PREVIEW = "/feed media preview - {role} post"
        const val MEDIA_PREVIEW_TAG = "{role}"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val VARIANT = "variant"
        const val LIST = "list"
        const val POSITION = "position"
        const val ACTION_FIELD = "actionField"
        const val CLICK = "click"
        const val IMPRESSIONS = "impressions"
        const val ITEMS = "items"
        const val INDEX = "index"
    }

}