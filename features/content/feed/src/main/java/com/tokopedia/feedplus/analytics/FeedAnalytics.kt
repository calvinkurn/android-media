package com.tokopedia.feedplus.analytics

import android.os.Bundle
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.content.analytic.Key
import com.tokopedia.feedplus.analytics.FeedAnalytics.EnhanceEcommerce.ITEM_LIST_PRODUCT_HIGHLIGHT
import com.tokopedia.feedplus.analytics.FeedAnalytics.EnhanceEcommerce.KEY_ITEMS
import com.tokopedia.feedplus.analytics.FeedAnalytics.EnhanceEcommerce.KEY_ITEM_LIST
import com.tokopedia.feedplus.data.FeedXCard
import com.tokopedia.feedplus.domain.mapper.MapperFeedModelToTrackerDataModel
import com.tokopedia.feedplus.presentation.fragment.FeedBaseFragment
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.feedplus.presentation.model.FeedProductActionModel
import com.tokopedia.feedplus.presentation.model.FeedTrackerDataModel
import com.tokopedia.mvcwidget.AnimatedInfos
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.EVENT
import com.tokopedia.track.TrackAppUtils.EVENT_ACTION
import com.tokopedia.track.TrackAppUtils.EVENT_CATEGORY
import com.tokopedia.track.TrackAppUtils.EVENT_LABEL
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

/**
 * Created By : Muhammad Furqan on 14/04/23
 */
class FeedAnalytics @AssistedInject constructor(
    @Assisted private val userSession: UserSessionInterface,
    @Assisted private val entrySource: MapperFeedModelToTrackerDataModel.FeedEntrySource
) {

    @AssistedFactory
    interface Factory {
        fun create(
            userSession: UserSessionInterface,
            entrySource: MapperFeedModelToTrackerDataModel.FeedEntrySource
        ): FeedAnalytics
    }

    private val userId = userSession.userId

    private val pageSource get() = "${entrySource.entryPoint}.0.0.${entrySource.widgetId}"

    private object Event {
        const val SELECT_CONTENT = "select_content"
        const val VIEW_ITEM = "view_item"
        const val VIEW_ITEM_LIST = "view_item_list"
        const val VIEW_CONTENT_IRIS = "viewContentIris"
        const val CLICK_CONTENT = "clickContent"
        const val OPEN_SCREEN = "openScreen"
        const val ADD_TO_CART = "add_to_cart"
    }

    private object Action {
        const val VIEW_POST = "view - post"
        const val CLICK_PAUSE_VIDEO = "click - screen pause video"
        const val VIEW_PRODUCT_LIST_BOTTOMSHEET = "view - product list bottomsheet"
        const val CLICK_HOLD_SEEKER_BAR_VIDEO = "click - tap hold seeker bar video"
        const val WATCH_VIDEO_POST = "watch video post"
        const val SWIPE_UP_DOWN_CONTENT = "swipe - up down content"
        const val SWIPE_RIGHT_LEFT_MULTIPLE_POST = "swipe - right left multiple post"
        const val CLICK_CTA_BUTTON_CAMPAIGN = "click - cta button campaign"
        const val CLICK_REMIND_ME_BUTTON = "click - ingatkan saya button"
        const val CLICK_ACTIVE_REMIND_ME_BUTTON = "click - pengingat aktif button"
        const val CLICK_LIKE_BUTTON = "click - like button"
        const val CLICK_DOUBLE_LIKE_BUTTON = "click - double click like"
        const val CLICK_THREE_DOTS_BUTTON = "click - three dots button"
        const val CLICK_WATCH_MODE = "click - mode nonton"
        const val CLICK_REPORT_CONTENT = "click - laporkan content"
        const val CLICK_REASON_REPORT_CONTENT = "click - reason laporkan content"
        const val VIEW_SUCCESS_REPORT_CONTENT = "view - success report content"
        const val CLICK_PRODUCT_TAG = "click - product tag"
        const val CLICK_PRODUCT_LABEL_PDP = "click - product label pdp"
        const val VIEW_VOUCHER_BOTTOMSHEET = "view - voucher bottomsheet"
        const val CLICK_PRODUCT = "click - product"
        const val CLICK_BUY_BUTTON = "click - beli button"
        const val CLICK_CART_BUTTON = "click - keranjang button"
        const val CLICK_VIEW_CART = "click - lihat keranjang button"
        const val CLICK_CLOSE_PRODUCT_LIST = "click - close product list bottomsheet"
        const val CLICK_FOLLOW_BUTTON = "click - follow button"
        const val CLICK_CREATOR_NAME = "click - creator name"
        const val CLICK_CREATOR_PROFILE_PICTURE = "click - creator profile picture"
        const val CLICK_CONTENT_CAPTION = "click - content caption"
        const val CLICK_CONTENT_PRODUCT_LABEL = "click - product label bottomsheet"
        const val CLICK_BUTTON_COMMENT = "click - comment button"
        const val CLICK_LIVE_PREVIEW = "click - live preview"
    }

    private object EnhanceEcommerce {
        const val KEY_ITEM_LIST = "item_list"
        const val KEY_ITEMS = "items"
        const val KEY_PROMOTIONS = "promotions"
        const val KEY_DIMENSION40 = "dimension40"
        const val KEY_DIMENSION90 = "dimension90"
        const val KEY_DIMENSION45 = "dimension45"
        const val KEY_INDEX = "index"
        const val KEY_ITEM_BRAND = "item_brand"
        const val KEY_CATEGORY_ID = "category_id"
        const val KEY_ITEM_CATEGORY = "item_category"
        const val KEY_ITEM_ID = "item_id"
        const val KEY_ITEM_NAME = "item_name"
        const val KEY_ITEM_VARIANT = "item_variant"
        const val KEY_PRICE = "price"
        const val KEY_QUANTITY = "quantity"
        const val KEY_SHOP_ID = "shop_id"
        const val KEY_SHOP_NAME = "shop_name"
        const val KEY_SHOP_TYPE = "shop_type"

        const val KEY_CREATIVE_NAME = "creative_name"
        const val KEY_CREATIVE_SLOT = "creative_slot"

        const val ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET = "/unified feed - product list bottomsheet"
        const val ITEM_LIST_PRODUCT_LABEL = "/unified feed - product label"
        const val UNIFIED_FEED_CONTENT = "unified-feed-content"
        const val CONTENT_IN_UNIFIED_FEED = "content in unified feed"
        const val CAMPAIGN_POST_IN_UNIFIED_FEED = "campaign post in unified feed"
        const val ITEM_LIST_PRODUCT_HIGHLIGHT = "/unified feed - product highlight"
    }

    fun eventPostImpression(
        trackerData: FeedTrackerDataModel,
        activityId: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.VIEW_ITEM,
            generateGeneralTrackerBundleData(
                Event.VIEW_ITEM,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_POST,
                "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
                getPostType(
                    trackerData.typename,
                    trackerData.type,
                    trackerData.authorType.value,
                    trackerData.isFollowing
                )
                } - ${
                getContentType(
                    trackerData.typename,
                    trackerData.type,
                    trackerData.mediaType
                )
                } - ${trackerData.contentScore} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${entrySource.entryPoint}",
                "41567",
                pageSource = pageSource
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_PROMOTIONS,
                    arrayListOf(
                        Bundle().apply {
                            putString(
                                EnhanceEcommerce.KEY_CREATIVE_NAME,
                                EnhanceEcommerce.UNIFIED_FEED_CONTENT
                            )
                            putInt(EnhanceEcommerce.KEY_CREATIVE_SLOT, index + 1)
                            putString(EnhanceEcommerce.KEY_ITEM_ID, activityId)
                            putString(
                                EnhanceEcommerce.KEY_ITEM_NAME,
                                EnhanceEcommerce.CONTENT_IN_UNIFIED_FEED
                            )
                        }
                    )
                )
            }
        )
    }

    fun eventClickPauseVideo(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PAUSE_VIDEO,
                getEventLabel(trackerData),
                "41568"
            )
        )
    }

    fun eventHoldSeekBarVideo(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_HOLD_SEEKER_BAR_VIDEO,
                getEventLabel(trackerData),
                "41569"
            )
        )
    }

    fun eventWatchVideoPost(
        trackerData: FeedTrackerDataModel
    ) {
        val trackerDataMap =
            generateGeneralTrackerData(
                Event.OPEN_SCREEN,
                CATEGORY_UNIFIED_FEED,
                Action.WATCH_VIDEO_POST,
                getEventLabel(trackerData),
                "41570",
                pageSource = pageSource
            ).toMutableMap()
        trackerDataMap[KEY_IS_LOGGED_IN_STATUS] = userSession.isLoggedIn.toString()
        trackerDataMap[KEY_SCREEN_NAME] =
            UNIFIED_FEED_WATCH_VIDEO_POST.format(trackerData.activityId)

        sendEventTracker(trackerDataMap)
    }

    fun eventSwipeUpDownContent(
        tabType: String,
        entryPoint: String,
        widgetId: String
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.SWIPE_UP_DOWN_CONTENT,
                "${getPrefix(tabType)} - $entryPoint",
                "41571",
                pageSource = pageSource
            )
        )
    }

    fun eventSwipeLeftRightMultiplePost(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.SWIPE_RIGHT_LEFT_MULTIPLE_POST,
                "${getPrefix(trackerData.tabType)} - ${entrySource.entryPoint}",
                "41572"
            )
        )
    }

    fun eventClickCampaignRibbon(
        trackerData: FeedTrackerDataModel,
        campaignName: String,
        positionInFeed: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            generateGeneralTrackerBundleData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CTA_BUTTON_CAMPAIGN,
                getEventLabel(trackerData),
                "41574"
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_PROMOTIONS,
                    arrayListOf(
                        Bundle().apply {
                            putString(
                                EnhanceEcommerce.KEY_CREATIVE_NAME,
                                EnhanceEcommerce.CAMPAIGN_POST_IN_UNIFIED_FEED
                            )
                            putString(EnhanceEcommerce.KEY_CREATIVE_SLOT, "${positionInFeed + 1}")
                            putString(
                                EnhanceEcommerce.KEY_ITEM_ID,
                                trackerData.activityId
                            )
                            putString(EnhanceEcommerce.KEY_ITEM_NAME, campaignName)
                        }
                    )
                )
            }
        )
    }

    fun eventClickRemindMe(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REMIND_ME_BUTTON,
                getEventLabel(trackerData),
                "41575"
            )
        )
    }

    fun eventClickActiveRemindMe(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_ACTIVE_REMIND_ME_BUTTON,
                getEventLabel(trackerData),
                "41576"
            )
        )
    }

    fun eventClickLikeButton(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_LIKE_BUTTON,
                getEventLabel(trackerData),
                "41577",
                pageSource = pageSource
            )
        )
    }

    fun eventDoubleClickLikeButton(
        trackerData: FeedTrackerDataModel
    ) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_DOUBLE_LIKE_BUTTON,
                getEventLabel(trackerData),
                "41578",
                pageSource = pageSource
            )
        )
    }

    fun eventClickThreeDots(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_THREE_DOTS_BUTTON,
                getEventLabel(trackerData),
                "41598"
            )
        )
    }

    fun eventClickWatchMode(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_WATCH_MODE,
                getEventLabel(trackerData),
                "41599"
            )
        )
    }

    fun eventClickReportContent(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REPORT_CONTENT,
                getEventLabel(trackerData),
                "41600"
            )
        )
    }

    fun eventClickReasonReportContent(trackerData: FeedTrackerDataModel, reportType: String) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_REASON_REPORT_CONTENT,
                "${getEventLabel(trackerData)} - $reportType",
                "41601"
            )
        )
    }

    fun eventViewSuccessReportContent(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.VIEW_CONTENT_IRIS,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_SUCCESS_REPORT_CONTENT,
                getEventLabel(trackerData),
                "41602"
            )
        )
    }

    fun eventClickProductTag(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT_TAG,
                getEventLabel(trackerData),
                "41603",
                pageSource = pageSource
            )
        )
    }

    fun eventClickProductLabel(
        trackerData: FeedTrackerDataModel,
        productList: List<FeedCardProductModel>
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            generateGeneralTrackerBundleData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT_LABEL_PDP,
                "${getEventLabel(trackerData)} - ${productList.firstOrNull()?.id ?: ""}",
                "41604",
                pageSource = pageSource
            ).also {
                it.putString(
                    EnhanceEcommerce.KEY_ITEM_LIST,
                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LABEL
                )
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    ArrayList(
                        productList.mapIndexed { index, feedCardProductModel ->
                            Bundle().apply {
                                putString(
                                    EnhanceEcommerce.KEY_DIMENSION40,
                                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LABEL
                                )
                                putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
                                putString(
                                    EnhanceEcommerce.KEY_ITEM_BRAND,
                                    feedCardProductModel.shopName
                                )
                                putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
                                putString(EnhanceEcommerce.KEY_ITEM_ID, feedCardProductModel.id)
                                putString(EnhanceEcommerce.KEY_ITEM_NAME, feedCardProductModel.name)
                                putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
                                putDouble(EnhanceEcommerce.KEY_PRICE, feedCardProductModel.price)
                                putString(EnhanceEcommerce.KEY_DIMENSION90, pageSource)
                            }
                        }
                    )
                )
            }
        )
    }

    fun eventViewProductListBottomSheets(
        trackerData: FeedTrackerDataModel,
        productList: List<FeedCardProductModel>
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.VIEW_ITEM_LIST,
            generateGeneralTrackerBundleData(
                Event.VIEW_ITEM_LIST,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_PRODUCT_LIST_BOTTOMSHEET,
                "${getEventLabel(trackerData)} - ${productList.firstOrNull()?.id ?: ""}",
                "41605",
                pageSource = pageSource
            ).also {
                it.putString(
                    EnhanceEcommerce.KEY_ITEM_LIST,
                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                )
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    ArrayList(
                        productList.mapIndexed { index, feedCardProductModel ->
                            Bundle().apply {
                                putString(
                                    EnhanceEcommerce.KEY_DIMENSION40,
                                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                                )
                                putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
                                putString(
                                    EnhanceEcommerce.KEY_ITEM_BRAND,
                                    feedCardProductModel.shopName
                                )
                                putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
                                putString(EnhanceEcommerce.KEY_ITEM_ID, feedCardProductModel.id)
                                putString(EnhanceEcommerce.KEY_ITEM_NAME, feedCardProductModel.name)
                                putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
                                putDouble(EnhanceEcommerce.KEY_PRICE, feedCardProductModel.price)
                                putString(EnhanceEcommerce.KEY_DIMENSION90, pageSource)
                            }
                        }
                    )
                )
            }
        )
    }

    fun eventMvcWidgetImpression(trackerData: FeedTrackerDataModel, mvcData: List<AnimatedInfos?>) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.VIEW_ITEM,
            generateGeneralTrackerBundleData(
                Event.VIEW_ITEM,
                CATEGORY_UNIFIED_FEED,
                Action.VIEW_VOUCHER_BOTTOMSHEET,
                getEventLabel(trackerData),
                "41606",
                pageSource = pageSource
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_PROMOTIONS,
                    ArrayList(
                        mvcData.filterNotNull().mapIndexed { index, animatedInfos ->
                            val name = MethodChecker.fromHtml(animatedInfos.title).toString()
                            Bundle().apply {
                                putString(EnhanceEcommerce.KEY_CREATIVE_NAME, name)
                                putString(EnhanceEcommerce.KEY_CREATIVE_SLOT, "${index + 1}")
                                putString(EnhanceEcommerce.KEY_ITEM_ID, "")
                                putString(EnhanceEcommerce.KEY_ITEM_NAME, name)
                            }
                        }
                    )
                )
            }
        )
    }

    fun eventClickProductInProductListBottomSheet(
        trackerData: FeedTrackerDataModel,
        productName: String,
        productId: String,
        productPrice: Double,
        shopName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            generateGeneralTrackerBundleData(
                Event.SELECT_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_PRODUCT,
                "${getEventLabel(trackerData)} - $productId",
                "41608",
                pageSource = pageSource
            ).also {
                it.putString(
                    EnhanceEcommerce.KEY_ITEM_LIST,
                    EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                )
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        Bundle().apply {
                            putString(
                                EnhanceEcommerce.KEY_DIMENSION40,
                                EnhanceEcommerce.ITEM_LIST_PRODUCT_LIST_BOTTOMSHEET
                            )
                            putString(EnhanceEcommerce.KEY_INDEX, "${index + 1}")
                            putString(
                                EnhanceEcommerce.KEY_ITEM_BRAND,
                                shopName
                            )
                            putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
                            putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
                            putString(EnhanceEcommerce.KEY_ITEM_NAME, productName)
                            putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
                            putDouble(EnhanceEcommerce.KEY_PRICE, productPrice)
                            putString(EnhanceEcommerce.KEY_DIMENSION90, pageSource)
                        }
                    )
                )
            }
        )
    }

    fun eventClickBuyButton(
        trackerData: FeedTrackerDataModel,
        productInfo: FeedProductActionModel
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.ADD_TO_CART,
            generateGeneralTrackerBundleData(
                Event.ADD_TO_CART,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_BUY_BUTTON,
                "${getEventLabel(trackerData)} - ${productInfo.product.id}",
                "41609",
                pageSource = pageSource
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        getProductTrackerBundle(
                            productInfo.product.shop.name,
                            productInfo.product.id,
                            productInfo.product.title,
                            productInfo.product.finalPrice,
                            productInfo.product.shop.id,
                            productInfo.cartId
                        )
                    )
                )
            }
        )
    }

    fun eventClickCartButton(
        trackerData: FeedTrackerDataModel,
        productInfo: FeedProductActionModel
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.ADD_TO_CART,
            generateGeneralTrackerBundleData(
                Event.ADD_TO_CART,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CART_BUTTON,
                "${getEventLabel(trackerData)} - ${productInfo.product.id}",
                "41610",
                pageSource = pageSource
            ).also {
                it.putParcelableArrayList(
                    EnhanceEcommerce.KEY_ITEMS,
                    arrayListOf(
                        getProductTrackerBundle(
                            productInfo.product.shop.name,
                            productInfo.product.id,
                            productInfo.product.title,
                            productInfo.product.finalPrice,
                            productInfo.product.shop.id,
                            productInfo.cartId
                        )
                    )
                )
            }
        )
    }

    fun eventClickViewCart(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_VIEW_CART,
                getEventLabel(trackerData),
                "41611"
            )
        )
    }

    fun eventClickCloseProductListBottomSheet(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CLOSE_PRODUCT_LIST,
                getEventLabel(trackerData),
                "41612"
            )
        )
    }

    fun eventClickFollowButton(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_FOLLOW_BUTTON,
                getEventLabel(trackerData),
                "41613",
                pageSource = pageSource
            )
        )
    }

    fun eventClickAuthorName(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CREATOR_NAME,
                getEventLabel(trackerData),
                "41614"
            )
        )
    }

    fun eventClickAuthorProfilePicture(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CREATOR_PROFILE_PICTURE,
                getEventLabel(trackerData),
                "41615"
            )
        )
    }

    fun eventClickContentCaption(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CONTENT_CAPTION,
                getEventLabel(trackerData),
                "41616"
            )
        )
    }

    fun eventClickContentProductLabel(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_CONTENT_PRODUCT_LABEL,
                getEventLabel(trackerData),
                "41688",
                pageSource = pageSource
            )
        )
    }

    fun eventClickComment(trackerData: FeedTrackerDataModel) {
        sendEventTracker(
            generateGeneralTrackerData(
                Event.CLICK_CONTENT,
                CATEGORY_UNIFIED_FEED,
                Action.CLICK_BUTTON_COMMENT,
                getEventLabel(trackerData),
                "41579",
                pageSource = pageSource
            )
        )
    }

    fun eventClickLivePreview(
        trackerData: FeedTrackerDataModel,
        productId: String,
        authorName: String,
        index: Int
    ) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
            Event.SELECT_CONTENT,
            addPromotionForLivePreview(
                generateGeneralTrackerBundleData(
                    Event.SELECT_CONTENT,
                    CATEGORY_UNIFIED_FEED,
                    Action.CLICK_LIVE_PREVIEW,
                    getEventLabel(trackerData),
                    "41689"
                ),
                productId,
                authorName,
                index
            )
        )
    }

    private fun addPromotionForLivePreview(
        bundle: Bundle,
        productId: String,
        authorName: String,
        index: Int
    ): Bundle {
        bundle.let {
            it.putParcelableArrayList(
                EnhanceEcommerce.KEY_PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(
                            EnhanceEcommerce.KEY_CREATIVE_NAME,
                            "live preview in feed unified"
                        )
                        putInt(EnhanceEcommerce.KEY_CREATIVE_SLOT, index + 1)
                        putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
                        putString(
                            EnhanceEcommerce.KEY_ITEM_NAME,
                            authorName
                        )
                    }
                )
            )
        }
        return bundle
    }

    fun getEventLabel(trackerData: FeedTrackerDataModel) =
        "${trackerData.activityId} - ${trackerData.authorId} - ${getPrefix(trackerData.tabType)} - ${
        getPostType(
            trackerData.typename,
            trackerData.type,
            trackerData.authorType.value,
            trackerData.isFollowing
        )
        } - ${
        getContentType(
            trackerData.typename,
            trackerData.type,
            trackerData.mediaType
        )
        } - ${trackerData.contentScore.ifBlank { "0" }} - ${trackerData.hasVoucher} - ${trackerData.campaignStatus} - ${entrySource.entryPoint}"

    private fun getProductTrackerBundle(
        shopName: String,
        productId: String,
        productName: String,
        productPrice: Double,
        shopId: String,
        cartId: String
    ) = Bundle().apply {
        putString(EnhanceEcommerce.KEY_CATEGORY_ID, "")
        putString(EnhanceEcommerce.KEY_DIMENSION40, "")
        putString(
            EnhanceEcommerce.KEY_ITEM_BRAND,
            shopName
        )
        putString(EnhanceEcommerce.KEY_ITEM_CATEGORY, "")
        putString(EnhanceEcommerce.KEY_ITEM_ID, productId)
        putString(EnhanceEcommerce.KEY_ITEM_NAME, productName)
        putString(EnhanceEcommerce.KEY_ITEM_VARIANT, "")
        putDouble(EnhanceEcommerce.KEY_PRICE, productPrice)
        putString(EnhanceEcommerce.KEY_QUANTITY, "1")
        putString(EnhanceEcommerce.KEY_SHOP_ID, shopId)
        putString(EnhanceEcommerce.KEY_SHOP_NAME, shopName)
        putString(EnhanceEcommerce.KEY_SHOP_TYPE, "")
        if (cartId.isNotBlank()) putString(EnhanceEcommerce.KEY_DIMENSION45, cartId)
        putString(EnhanceEcommerce.KEY_DIMENSION90, pageSource)
    }

    private fun sendEventTracker(params: Map<String, Any>) {
        TrackApp.getInstance().gtm.sendGeneralEvent(params)
    }

    private fun generateGeneralTrackerData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String,
        pageSource: String = ""
    ): Map<String, Any> = mutableMapOf(
        EVENT to eventName,
        EVENT_CATEGORY to eventCategory,
        EVENT_ACTION to eventAction,
        EVENT_LABEL to eventLabel,
        KEY_EVENT_USER_ID to userId,
        KEY_BUSINESS_UNIT_EVENT to BUSINESS_UNIT_CONTENT,
        KEY_CURRENT_SITE_EVENT to CURRENT_SITE_MARKETPLACE,
        KEY_TRACKER_ID to trackerId
    ).apply {
        if (pageSource.isNotEmpty()) put(Key.pageSource, pageSource)
    }

    private fun generateGeneralTrackerBundleData(
        eventName: String,
        eventCategory: String,
        eventAction: String,
        eventLabel: String,
        trackerId: String,
        pageSource: String = ""
    ): Bundle = Bundle().apply {
        putString(EVENT, eventName)
        putString(EVENT_ACTION, eventAction)
        putString(EVENT_CATEGORY, eventCategory)
        putString(EVENT_LABEL, eventLabel)
        putString(KEY_EVENT_USER_ID, userId)
        putString(KEY_BUSINESS_UNIT_EVENT, BUSINESS_UNIT_CONTENT)
        putString(KEY_CURRENT_SITE_EVENT, CURRENT_SITE_MARKETPLACE)
        putString(KEY_TRACKER_ID, trackerId)
        if (pageSource.isNotEmpty()) putString(Key.pageSource, pageSource)
    }

    fun sendClickShareButtonEvent(trackerData: FeedTrackerDataModel) {
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - share button")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(getEventLabel(trackerData))
            .setCustomProperty("trackerId", "10488")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickCloseButtonOnShareBottomSheetEvent(trackerData: FeedTrackerDataModel) {
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - close button on share bottom sheet")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(getEventLabel(trackerData))
            .setCustomProperty("trackerId", "10489")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickSharingChannelEvent(shareOption: String, trackerData: FeedTrackerDataModel) {
        Tracker.Builder()
            .setEvent("clickCommunication")
            .setEventAction("click - sharing channel")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel("$shareOption - ${getEventLabel(trackerData)}")
            .setCustomProperty("trackerId", "10490")
            .setBusinessUnit("sharingexperience")
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun sendClickOkeShareToasterEvent(trackerData: FeedTrackerDataModel) {
        Tracker.Builder()
            .setEvent("clickContent")
            .setEventAction("click - oke share toaster")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(getEventLabel(trackerData))
            .setCustomProperty("trackerId", "41597")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .build()
            .send()
    }

    fun impressProductHighlight(product: FeedCardProductModel, trackerModel: FeedTrackerDataModel) {
        val eventLabel = getEventLabel(trackerModel) + "- ${product.id}"
        Tracker.Builder()
            .setEvent(Event.VIEW_ITEM_LIST)
            .setEventAction("view - product card highlight")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, "49770")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .setCustomProperty(KEY_PAGE_SOURCE, pageSource)
            .setCustomProperty(
                KEY_ITEMS, getProductTrackerBundle(
                shopId = product.shopId,
                shopName = product.shopName,
                cartId = "",
                productId = product.id,
                productName = product.name,
                productPrice = product.price
            ))
            .setCustomProperty(KEY_ITEM_LIST, ITEM_LIST_PRODUCT_HIGHLIGHT)
            .build()
            .send()
    }

    fun sendClickProductHighlight(product: FeedCardProductModel, trackerModel: FeedTrackerDataModel) {
        val eventLabel = getEventLabel(trackerModel) + "- ${product.id}"
        Tracker.Builder()
            .setEvent(Event.SELECT_CONTENT)
            .setEventAction("click - product on card highlight")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, "49771")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .setCustomProperty(KEY_PAGE_SOURCE, pageSource)
            .setCustomProperty(KEY_ITEMS, getProductTrackerBundle(
                shopId = product.shopId,
                shopName = product.shopName,
                cartId = "",
                productId = product.id,
                productName = product.name,
                productPrice = product.price
            ))
            .setCustomProperty(KEY_ITEM_LIST, ITEM_LIST_PRODUCT_HIGHLIGHT)
            .build()
            .send()
    }

    fun atcFromProductHighlight(product: FeedProductActionModel, trackerModel: FeedTrackerDataModel) {
        val eventLabel = getEventLabel(trackerModel) + "- ${product.product.id}"
        Tracker.Builder()
            .setEvent(Event.ADD_TO_CART)
            .setEventAction("click - keranjang button on card highlight")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, "49772")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .setCustomProperty(KEY_PAGE_SOURCE, pageSource)
            .setCustomProperty(
                KEY_ITEMS, getProductTrackerBundle(
                shopId = product.product.shop.id,
                shopName = product.product.shop.name,
                cartId = "",
                productId = product.product.id,
                productName = product.product.title,
                productPrice = product.product.finalPrice
            ))
            .setCustomProperty(KEY_ITEM_LIST, ITEM_LIST_PRODUCT_HIGHLIGHT)
            .build()
            .send()
    }
    fun closeProductHighlight(trackerModel: FeedTrackerDataModel) {
        val eventLabel =" ${getPrefix(trackerModel.tabType)} - ${entrySource.entryPoint} - ${trackerModel.authorType.name.lowercase()} - ${trackerModel.authorId}"
        Tracker.Builder()
            .setEvent(Event.CLICK_CONTENT)
            .setEventAction("click - x - product card highlight")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(eventLabel)
            .setCustomProperty(KEY_TRACKER_ID, "49773")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setUserId(userId)
            .setCustomProperty(KEY_PAGE_SOURCE, pageSource)
            .build()
            .send()
    }

    fun atcFromProductHighlightWithVariant(product: FeedProductActionModel, trackerModel: FeedTrackerDataModel) {
        Tracker.Builder()
            .setEvent(Event.CLICK_CONTENT)
            .setEventAction("click - keranjang button on card highlight bottomsheet")
            .setEventCategory(CATEGORY_UNIFIED_FEED)
            .setEventLabel(getEventLabel(trackerModel))
            .setCustomProperty(KEY_TRACKER_ID, "49891")
            .setBusinessUnit(BUSINESS_UNIT_CONTENT)
            .setCurrentSite(CURRENT_SITE_MARKETPLACE)
            .setCustomProperty(KEY_PAGE_SOURCE, pageSource)
            .setUserId(userId)
            .setCustomProperty(
                KEY_ITEMS, getProductTrackerBundle(
                shopId = product.product.shop.id,
                shopName = product.product.shop.name,
                cartId = "",
                productId = product.product.id,
                productName = product.product.title,
                productPrice = product.product.finalPrice
            ))
            .setCustomProperty(KEY_ITEM_LIST, ITEM_LIST_PRODUCT_HIGHLIGHT)
            .build()
            .send()
    }

    companion object {
        const val KEY_EVENT_USER_ID = "userId"
        const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        const val KEY_CURRENT_SITE_EVENT = "currentSite"
        const val KEY_TRACKER_ID = "trackerId"
        const val KEY_IS_LOGGED_IN_STATUS = "isLoggedInStatus"
        const val KEY_SCREEN_NAME = "screenName"
        const val KEY_PAGE_SOURCE = "pageSource"

        const val BUSINESS_UNIT_CONTENT = "content"
        const val CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
        const val CATEGORY_UNIFIED_FEED = "unified feed"

        private const val POST_TYPE_ASGC = "asgc"
        private const val POST_TYPE_SGC = "sgc"
        private const val POST_TYPE_UGC = "ugc"
        private const val POST_TYPE_ASGC_RECOM = "asgc recom"
        private const val POST_TYPE_TOPADS = "topads"
        private const val POST_TYPE_UGC_RECOM = "ugc recom"
        private const val POST_TYPE_SGC_RECOM = "sgc recom"

        private const val CONTENT_TYPE_SHORT = "play short video"
        private const val CONTENT_TYPE_LIVE_PREVIEW = "play live preview"
        private const val CONTENT_TYPE_LIVE_VOD = "play vod after live"

        private const val AUTHOR_TOKOPEDIA = 1
        private const val AUTHOR_SELLER = 2
        private const val AUTHOR_USER = 3

        const val ENTRY_POINT_NAV_BUTTON = "nav button"
        const val ENTRY_POINT_SHARE_LINK = "share link"
        const val ENTRY_POINT_PUSH_NOTIF = "push notif"

        const val UNIFIED_FEED_WATCH_VIDEO_POST = "/unified feed - watch video post - %s"

        private const val TAB_TYPE_FOR_YOU = "foryou"
        private const val TAB_TYPE_FOLLOWING = "following"
        private const val TAB_TYPE_CDP = "cdp"

        fun getPrefix(tabType: String) =
            when (tabType) {
                FeedBaseFragment.TAB_TYPE_FOR_YOU -> TAB_TYPE_FOR_YOU
                FeedBaseFragment.TAB_TYPE_FOLLOWING -> TAB_TYPE_FOLLOWING
                FeedBaseFragment.TAB_TYPE_CDP -> TAB_TYPE_CDP
                else -> ""
            }

        fun getPostType(typename: String, type: String, authorType: Int, isFollowing: Boolean) =
            when {
                typename == FeedXCard.TYPE_FEED_X_CARD_PLACEHOLDER && type == FeedXCard.TYPE_FEED_TOP_ADS -> POST_TYPE_TOPADS
                typename == FeedXCard.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT && isFollowing -> POST_TYPE_ASGC
                typename == FeedXCard.TYPE_FEED_X_CARD_PRODUCTS_HIGHLIGHT -> POST_TYPE_ASGC_RECOM
                authorType == AUTHOR_SELLER && isFollowing -> POST_TYPE_SGC
                authorType == AUTHOR_SELLER -> POST_TYPE_SGC_RECOM
                authorType == AUTHOR_USER && isFollowing -> POST_TYPE_UGC
                authorType == AUTHOR_USER -> POST_TYPE_UGC_RECOM
                authorType == AUTHOR_TOKOPEDIA && isFollowing -> POST_TYPE_ASGC
                authorType == AUTHOR_TOKOPEDIA -> POST_TYPE_ASGC_RECOM
                else -> ""
            }

        fun getContentType(typename: String, type: String, mediaType: String) = when {
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_CHANNEL -> CONTENT_TYPE_LIVE_VOD
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_SHORT_VIDEO -> CONTENT_TYPE_SHORT
            typename == FeedXCard.TYPE_FEED_X_CARD_PLAY && type == FeedXCard.TYPE_FEED_PLAY_LIVE -> CONTENT_TYPE_LIVE_PREVIEW
            else -> mediaType
        }
    }
}
