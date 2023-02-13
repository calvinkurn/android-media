package com.tokopedia.createpost.common.analyics


import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Action.FORMAT_THREE_PARAM
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Action.FORMAT_TWO_PARAM
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Category.CONTENT_FEED_CREATION
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Event.CLICK
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Event.CLICK_FEED
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Event.CONTENT
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Event.PRODUCT_CLICK
import com.tokopedia.createpost.common.analyics.CreatePostAnalytics.Event.PRODUCT_VIEW
import com.tokopedia.createpost.common.view.plist.ShopPageProduct
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import java.lang.Exception
import javax.inject.Inject

/**
 * @author by yfsx on 05/11/18.
 */
class CreatePostAnalytics @Inject
constructor(private val userSession: UserSessionInterface) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm


    companion object {
        private val PARAM_EVENT_NAME = "event"
        private val PARAM_EVENT_CATEGORY = "eventCategory"
        private val PARAM_EVENT_ACTION = "eventAction"
        private val PARAM_EVENT_LABEL = "eventLabel"
        private const val KEY_BUSINESS_UNIT_EVENT = "businessUnit"
        private const val KEY_CURRENT_SITE_EVENT = "currentSite"
        private const val MARKETPLACE = "tokopediamarketplace"
        private const val KEY_ECOMMERCE = "ecommerce"
        private val KEY_USER_ID = "userId"
        const val CURRENCY_CODE = "currencyCode"
        const val CURRENCY_CODE_IDR = "IDR"
        const val ACTION_FIELD = "actionField"
        const val LIST = "list"
        const val LIST_TYPE_CREATION_TAGGING_PAGE = "/feed - creation tagging page"
        const val PRODUCTS = "products"
        const val IMPRESSIONS = "impressions"

    }

    private object Event {
        const val CLICK_FEED = "clickFeed"
        const val CLICK = "click"
        const val CONTENT = "content"
        const val PRODUCT_CLICK = "productClick"
        const val PRODUCT_VIEW = "productView"
    }

    private object Category {
        const val CONTENT_FEED_CREATION = "content feed creation"
    }

    private object Action {
        const val CLICK_POST_TAG = "click post to tag"
        const val CLICK_PRODUCT_TAG_ICON = "click tag product icon"
        const val CLICK_PRODUCT_TAG_BUBBLE = "click product tagging bubble"
        const val CLICK_OPEN_PRODUCT_TAG_BOTTOM_SHEET = "click open product tag bottomsheet"
        const val CLICK_DELETE_PRODUCT_TAG_POST = "click delete icon on product tag post"
        const val CLICK_DELETE_PRODUCT_TAG_BOTTOMSHEET = "click delete icon on product tag bottomsheet"
        const val CLICK_BACK_ON_TAGGING_PAGE = "click back on tagging page"
        const val CLICK_EXIT_ON_CONFIRMATION_POPUP = "click exit on confirmation popup"
        const val CLICK_CONTINUE_ON_CONFIRMATION_POPUP = "click continue tag on confirmation popup"
        const val CLICK_NEXT_ON_PRODUCT_TAGGING_PAGE = "click next on tagging page"
        const val CLICK_POST_BUTTON = "click on post button"
        const val CLICK_BACK_ON_PREVIEW = "click back on preview page"
        const val CLICK_ON_CAPTION_BOX = "click on caption box"
        const val CLICK_ON_SEARCH_BAR = "click on search bar"
        const val CLICK_ON_SORT_CRITERIA = "click on sort criteria"
        const val CLICK_ON_SORT_BUTTON = "click on sort button"

        const val VIEW_ON_PRODUCT_CARD = "view on product card"
        const val CLICK_ON_PRODUCT_CARD = "click on product card"

        const val FORMAT_TWO_PARAM = "%s - %s"
        const val FORMAT_THREE_PARAM = "%s - %s - %s"

    }

    private object Product {
        const val ID = "id"
        const val NAME = "name"
        const val PRICE = "price"
        const val BRAND = "brand"
        const val CATEGORY = "category"
        const val VARIANT = "variant"
        const val INDEX = "index"
    }

     fun eventClickOnImageToTag(mediaType: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_POST_TAG,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                mediaType
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickTagProductIcon(mediaType: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_PRODUCT_TAG_ICON,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                mediaType
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickProductTagBubble(mediaType: String, productId: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_PRODUCT_TAG_BUBBLE,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                userSession.shopId,
                mediaType,
                productId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventOpenProductTagBottomSheet(mediaType: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_OPEN_PRODUCT_TAG_BOTTOM_SHEET,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                mediaType
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventDeleteProductTagPost(mediaType: String, productId: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_DELETE_PRODUCT_TAG_POST,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                userSession.shopId,
                mediaType,
                productId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventDeleteProductTagBottomSheet(
        mediaType: String,
        productId: String,
    ) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_DELETE_PRODUCT_TAG_BOTTOMSHEET,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_THREE_PARAM,
                userSession.shopId,
                mediaType,
                productId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickBackOnProductTaggingPage() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_BACK_ON_TAGGING_PAGE,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickExitOnConfirmationPopup() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_EXIT_ON_CONFIRMATION_POPUP,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickContinueOnConfirmationPopup() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_CONTINUE_ON_CONFIRMATION_POPUP,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventNextOnProductTaggingPage(totalMedia: Int) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_NEXT_ON_PRODUCT_TAGGING_PAGE,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                totalMedia.toString()
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,

            )
        analyticTracker.sendGeneralEvent(map)
    }


     fun eventClickOnSortButton() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_ON_SORT_BUTTON,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickOnSortCriteria(criteria: String) {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_ON_SORT_CRITERIA,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                criteria
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickOnSearchBar() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_ON_SEARCH_BAR,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickOnCaptionBox() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_ON_CAPTION_BOX,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickBackOnPreviewPage() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_BACK_ON_PREVIEW,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }

     fun eventClickPostOnPreviewPage() {
        val map = mapOf(
            PARAM_EVENT_NAME to CLICK_FEED,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_POST_BUTTON,
            PARAM_EVENT_LABEL to userSession.shopId,
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE
        )
        analyticTracker.sendGeneralEvent(map)
    }
    fun eventProductPageProductItemClicked(
        product: ShopPageProduct,
        position: Int,
    ) {
        val ecommerceMap = DataLayer.mapOf(CLICK, mapOf(
            ACTION_FIELD to mapOf(
                LIST to LIST_TYPE_CREATION_TAGGING_PAGE
            ),
            PRODUCTS to getSingleProductList(product, position + 1)
        )
        )
        val map = mapOf(
            PARAM_EVENT_NAME to PRODUCT_CLICK,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.CLICK_ON_PRODUCT_CARD,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                product.pId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_USER_ID to userSession.userId,
            KEY_ECOMMERCE to ecommerceMap
        )
        analyticTracker.sendEnhanceEcommerceEvent(map)

    }

    fun eventProductPageProductItemViewed(
        product: ShopPageProduct,
        position: Int,
    ) {
        val ecommerceMap = DataLayer.mapOf(
            CURRENCY_CODE, CURRENCY_CODE_IDR,
            IMPRESSIONS, getSingleProductList(product, position + 1)
        )
        val map = mapOf(
            PARAM_EVENT_NAME to PRODUCT_VIEW,
            PARAM_EVENT_CATEGORY to CONTENT_FEED_CREATION,
            PARAM_EVENT_ACTION to Action.VIEW_ON_PRODUCT_CARD,
            PARAM_EVENT_LABEL to String.format(
                FORMAT_TWO_PARAM,
                userSession.shopId,
                product.pId
            ),
            KEY_BUSINESS_UNIT_EVENT to CONTENT,
            KEY_CURRENT_SITE_EVENT to MARKETPLACE,
            KEY_USER_ID to userSession.userId,
            KEY_ECOMMERCE to ecommerceMap
        )
        analyticTracker.sendEnhanceEcommerceEvent(map)

    }

    private fun getSingleProductList(item: ShopPageProduct, index: Int): List<Map<String, Any>> {
        val list: MutableList<Map<String, Any>> = mutableListOf()

        list.add(createProductItemMap(item, index))
        return list
    }

    private fun createProductItemMap(item: ShopPageProduct, index: Int): Map<String, Any> =
        DataLayer.mapOf(
            Product.INDEX, index,
            Product.BRAND, "",
            Product.CATEGORY, "",
            Product.ID, item.pId,
            Product.NAME, item.name,
            Product.VARIANT, "",
            Product.PRICE,
            if (item.campaign?.dPrice?.toLong() != 0L) formatStringPrice(item.campaign?.dPrice
                ?: "") else formatStringPrice(
                item.price?.priceIdr ?: ""),
        )
    private fun formatStringPrice(price: String): String {
        return try {
            (price.replace("[^\\d]".toRegex(), ""))
        } catch (e: Exception) {
            ""
        }
    }
}
