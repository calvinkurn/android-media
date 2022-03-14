package com.tokopedia.officialstore.analytics

import android.os.Bundle
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselMerchantVoucherDataModel
import com.tokopedia.track.builder.BaseTrackerBuilder
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object OSMerchantVoucherTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val MERCHANT_VOUCHER_MULTIPLE_FORMAT = "merchant voucher multiple - %s"
            const val CLICK_PRODUCT_DETAIL = "click product detail"
            const val CLICK_SHOP = "click shop"
            const val SHOP_DETAIL = "shop detail"
            const val CREATIVE_NAME_FORMAT = "%s - %s"
            const val ITEM_LIST_PRODUCT_DETAIL_FORMAT = "/official-store/%s - %s - %s - %s"
            const val ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT = "%s / %s / %s"
            const val DIMENSION_38_FORMAT = "%s_%s_%s_%s_%s_%s"
            const val CLICK_VIEW_ALL = "click view all card"
            const val ITEM_ID_FORMAT = "%s_%s"
            const val MERCHANT_VOUCHER_MULTIPLE = "merchant_voucher_multiple"
            const val ITEM_NAME_FORMAT = "/official-store/%s - $MERCHANT_VOUCHER_MULTIPLE - %s"
            const val DEFAULT_VALUE = ""
            const val VIEW_COUPON = "view coupon"
            const val CREATIVE_NAME_VIEW_COUPON_FORMAT = "%s - %s - %s"
        }
    }

    fun getShopClicked(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_SHOP)
        )
        bundle.putString(Category.KEY, OfficialStoreTracking.OS_MICROSITE_SINGLE)
        bundle.putString(Label.KEY, element.shopId)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)

        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CustomAction.CREATIVE_NAME_FORMAT.format(CustomAction.SHOP_DETAIL, element.shopName)
        )
        promotion.putString(Promotion.CREATIVE_SLOT, (horizontalPosition + 1).toString())
        promotion.putString(Promotion.ITEM_ID, CustomAction.ITEM_ID_FORMAT.format(element.bannerId, element.shopId))
        promotion.putString(Promotion.ITEM_NAME, CustomAction.ITEM_NAME_FORMAT.format(CustomAction.DEFAULT_VALUE, element.headerName))
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        bundle.putString(UserId.KEY, element.userId)
        return Pair(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getClickViewAll(headerName: String, userId: String): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.CLICK_HOMEPAGE)
        bundle.putString(Action.KEY, CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_VIEW_ALL) )
        bundle.putString(Category.KEY, OfficialStoreTracking.OS_MICROSITE_SINGLE)
        bundle.putString(Label.KEY, headerName)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(UserId.KEY, userId)

        return Pair(Event.CLICK_HOMEPAGE, bundle)
    }

    fun getClickProduct(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_PRODUCT_DETAIL)
        )
        bundle.putString(Category.KEY, OfficialStoreTracking.OS_MICROSITE_SINGLE)
        bundle.putString(Label.KEY, CustomAction.CREATIVE_NAME_FORMAT.format(element.productId, element.shopId))
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(
            ItemList.KEY,
            CustomAction.ITEM_LIST_PRODUCT_DETAIL_FORMAT.format()
        )

        val item = Bundle()
        item.putString(Items.DIMENSION_83, CustomAction.DIMENSION_38_FORMAT.format(
            "",
            "",
            "",
            "",
            "",
            ""
        ))
        item.putString(Items.INDEX, (horizontalPosition + 1).toString())
        item.putString(Items.ITEM_BRAND, CustomAction.DEFAULT_VALUE)
        val itemCategory = CustomAction.ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT.format(
            CustomAction.DEFAULT_VALUE,
            CustomAction.DEFAULT_VALUE,
            CustomAction.DEFAULT_VALUE
        )
        item.putString(Items.ITEM_CATEGORY, itemCategory)
        item.putString(Items.ITEM_ID, element.productId)
        item.putString(Items.ITEM_NAME, CustomAction.DEFAULT_VALUE)
        item.putString(Items.ITEM_VARIANT, CustomAction.DEFAULT_VALUE)
        item.putString(Items.PRICE, element.productPrice)

        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))
        bundle.putString(UserId.KEY, element.userId)

        return Pair(Ecommerce.PRODUCT_CLICK, bundle)
    }

    fun getMerchantVoucherView(element: CarouselMerchantVoucherDataModel, horizontalPosition: Int) : Map<String, Any> {
        val trackingBuilder = BaseTrackerBuilder()
        val creativeName = CustomAction.CREATIVE_NAME_VIEW_COUPON_FORMAT.format(
            CustomAction.DEFAULT_VALUE,
            element.couponType,
            CustomAction.DEFAULT_VALUE
        )
        val creativeSlot = (horizontalPosition + 1).toString()
        val itemId = CustomAction.ITEM_ID_FORMAT.format(
            element.bannerId,
            element.shopId
        )
        val itemName =
            CustomAction.ITEM_NAME_FORMAT.format(CustomAction.DEFAULT_VALUE, element.headerName)
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
                eventCategory = OfficialStoreTracking.OS_MICROSITE_SINGLE,
                eventAction = CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.VIEW_COUPON),
                eventLabel = Label.NONE,
                promotions = listPromotions)
                .appendBusinessUnit(BusinessUnit.DEFAULT)
                .appendCurrentSite(CurrentSite.DEFAULT)
                .appendUserId(element.userId)
                .build()
    }
}