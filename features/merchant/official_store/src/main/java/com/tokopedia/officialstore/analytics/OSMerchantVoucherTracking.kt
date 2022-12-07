package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FIELD_DIMENSION_38
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_FIVE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_THREE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_DASH_TWO_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_ITEM_NAME_FOUR_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_ITEM_NAME_THREE_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.FORMAT_UNDERSCORE_TWO_VALUES
import com.tokopedia.officialstore.analytics.OfficialStoreTracking.Companion.OS_MICROSITE_SINGLE
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object OSMerchantVoucherTracking : BaseTrackerConst() {
    private const val MERCHANT_VOUCHER_MULTIPLE_FORMAT = "merchant voucher multiple - %s"
    private const val MERCHANT_VOUCHER_MULTIPLE_UNDERSCORED = "merchant_voucher_multiple"
    private const val MERCHANT_VOUCHER_MULTIPLE = "merchant voucher multiple"
    private const val ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT = "%s / %s / %s"
    private const val VALUE_VIEW_COUPON = "view coupon"
    private const val VALUE_CLICK_SHOP = "click shop"
    private const val VALUE_CLICK_VIEW_ALL = "click view all"
    private const val VALUE_CLICK_PRODUCT_DETAIL = "click product detail"
    private const val DEFAULT_VALUE = ""
    private const val SHOP_DETAIL = "shop detail"
    private const val VALUE_TRACKER_ID_IMPRESSION_MVC = "28741"
    private const val VALUE_TRACKER_ID_CLICK_MVC_SHOP_NAME = "28742"
    private const val VALUE_TRACKER_ID_CLICK_MVC_VIEW_ALL = "28744"
    private const val VALUE_TRACKER_ID_CLICK_MVC_PRODUCT = "28747"

    // Row 33
    fun getMerchantVoucherView(
        element: CarouselMerchantVoucherDataModel,
        horizontalPosition: Int,
        categoryName: String
    ): Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = FORMAT_DASH_THREE_VALUES.format(
            DEFAULT_VALUE,
            element.couponType,
            DEFAULT_VALUE
        )
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = FORMAT_UNDERSCORE_TWO_VALUES.format(
            element.bannerId,
            element.shopId
        )
        val itemName = FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, MERCHANT_VOUCHER_MULTIPLE_UNDERSCORED, element.headerName)
        val listPromotions = arrayListOf(
            Promotion(
                creative = creativeName,
                position = creativeSlot,
                id = itemId,
                name = itemName
            )
        )

        return trackingBuilder.constructBasicPromotionView(
            event = Event.PROMO_VIEW,
            eventCategory = OS_MICROSITE_SINGLE,
            eventAction = MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(VALUE_VIEW_COUPON),
            eventLabel = FORMAT_DASH_FIVE_VALUES.format(MERCHANT_VOUCHER_MULTIPLE, element.channelId,
                element.headerName, element.brandId, categoryName),
            promotions = listPromotions
        )
            .appendBusinessUnit(BusinessUnit.DEFAULT)
            .appendCurrentSite(CurrentSite.DEFAULT)
            .appendUserId(element.userId)
            .appendCustomKeyValue(TrackerId.KEY, VALUE_TRACKER_ID_IMPRESSION_MVC)
            .build()
    }

    // Row 34
    fun getShopClicked(
        element: CarouselMerchantVoucherDataModel,
        horizontalPosition: Int,
        categoryName: String
    ): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(VALUE_CLICK_SHOP)
        )
        bundle.putString(Category.KEY, OS_MICROSITE_SINGLE)
        bundle.putString(
            Label.KEY,
            FORMAT_DASH_FIVE_VALUES.format(MERCHANT_VOUCHER_MULTIPLE, element.channelId,
                element.headerName, element.brandId, categoryName)
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)

        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            FORMAT_DASH_TWO_VALUES.format(SHOP_DETAIL, element.shopName)
        )
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(
            Promotion.ITEM_ID,
            FORMAT_UNDERSCORE_TWO_VALUES.format(element.bannerId, element.shopId)
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            FORMAT_ITEM_NAME_THREE_VALUES.format(categoryName, MERCHANT_VOUCHER_MULTIPLE_UNDERSCORED, element.headerName)
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        bundle.putString(UserId.KEY, element.userId)
        bundle.putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MVC_SHOP_NAME)
        return Pair(Event.SELECT_CONTENT, bundle)
    }

    // Row 36
    fun getClickViewAll(
        channelId: String,
        headerName: String,
        userId: String,
        categoryName: String
    ): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(
            Action.KEY,
            MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(VALUE_CLICK_VIEW_ALL)
        )
        bundle.putString(Category.KEY, OS_MICROSITE_SINGLE)
        bundle.putString(
            Label.KEY,
            FORMAT_DASH_FIVE_VALUES.format(MERCHANT_VOUCHER_MULTIPLE, channelId,
                headerName, DEFAULT_VALUE, categoryName)
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)
        bundle.putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MVC_VIEW_ALL)

        return Pair(Event.CLICK_HOMEPAGE, bundle)
    }

    // Row 37
    fun getClickProduct(
        element: CarouselMerchantVoucherDataModel,
        horizontalPosition: Int,
        categoryName: String
    ): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(VALUE_CLICK_PRODUCT_DETAIL)
        )
        bundle.putString(Category.KEY, OS_MICROSITE_SINGLE)
        bundle.putString(
            Label.KEY,
            FORMAT_DASH_FIVE_VALUES.format(MERCHANT_VOUCHER_MULTIPLE, element.channelId,
                element.headerName, element.brandId, categoryName)
        )
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(
            ItemList.KEY,
            FORMAT_ITEM_NAME_FOUR_VALUES.format(
                categoryName,
                MERCHANT_VOUCHER_MULTIPLE_UNDERSCORED,
                element.headerName,
                element.productAppLink,
            )
        )

        val item = Bundle()
        item.putString(Items.INDEX, (horizontalPosition + 1).toString())
        item.putString(Items.ITEM_BRAND, DEFAULT_VALUE)
        val itemCategory = ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT.format(
            DEFAULT_VALUE,
            DEFAULT_VALUE,
            DEFAULT_VALUE
        )
        item.putString(Items.ITEM_CATEGORY, itemCategory)
        item.putString(Items.ITEM_ID, element.productId)
        item.putString(Items.ITEM_NAME, DEFAULT_VALUE)
        item.putString(Items.ITEM_VARIANT, DEFAULT_VALUE)
        item.putString(Items.PRICE, element.productPrice)
        item.putString(FIELD_DIMENSION_38, element.attribution)

        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))
        bundle.putString(UserId.KEY, element.userId)
        bundle.putString(TrackerId.KEY, VALUE_TRACKER_ID_CLICK_MVC_PRODUCT)

        return Pair(Event.SELECT_CONTENT, bundle)
    }
}
