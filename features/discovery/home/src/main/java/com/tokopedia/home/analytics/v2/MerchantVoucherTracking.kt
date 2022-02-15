package com.tokopedia.home.analytics.v2

import android.os.Bundle
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherDetailClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherProductClicked
import com.tokopedia.home_component.model.merchantvoucher.MerchantVoucherShopClicked
import com.tokopedia.track.builder.util.BaseTrackerConst

/**
 * Created by dhaba
 */
object MerchantVoucherTracking : BaseTrackerConst() {
    private class CustomAction {
        companion object {
            const val MERCHANT_VOUCHER_MULTIPLE_FORMAT = "merchant voucher multiple - %s"
            const val CLICK_SHOP = "click shop"
            const val SHOP_DETAIL = "shop detail"
            const val CREATIVE_NAME_FORMAT = "%s - %s"
            const val ITEM_ID_FORMAT = "%s_%s"
            const val MERCHANT_VOUCHER_MULTIPLE = "merchant_voucher_multiple"
            const val ITEM_NAME_FORMAT = "/ - p%s - $MERCHANT_VOUCHER_MULTIPLE - banner - %s"
            const val CREATIVE_NAME_VOUCHER_DETAIL_FORMAT = "voucher detail - %s - %s - %s"
            const val ITEM_NAME_VOUCHER_DETAIL_FORMAT = "/ - p%s - $MERCHANT_VOUCHER_MULTIPLE - banner - %s"
            const val ITEM_LIST_PRODUCT_DETAIL_FORMAT = "/ - p%s - product - %s - %s - %s - %s - %s - %s"
            const val ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT = "%s / %s / %s"
        }
    }

    fun getShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_SHOP)
        )
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(Label.KEY, merchantVoucherShopClicked.shopId)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)

        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CustomAction.CREATIVE_NAME_FORMAT.format(CustomAction.SHOP_DETAIL, merchantVoucherShopClicked.shopName)
        )
        promotion.putString(Promotion.CREATIVE_SLOT, merchantVoucherShopClicked.horizontalCardPosition)
        promotion.putString(Promotion.ITEM_ID, CustomAction.ITEM_ID_FORMAT.format(merchantVoucherShopClicked.bannerId, merchantVoucherShopClicked.shopId))
        promotion.putString(Promotion.ITEM_NAME, CustomAction.ITEM_NAME_FORMAT.format(merchantVoucherShopClicked.positionWidget, merchantVoucherShopClicked.headerName))
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        bundle.putString(UserId.KEY, merchantVoucherShopClicked.userId)
        return Pair(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getClickVoucherDetail(merchantVoucherDetailClicked: MerchantVoucherDetailClicked): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_SHOP)
        )
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(Label.KEY, merchantVoucherDetailClicked.shopId)
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)

        val promotion = Bundle()
        promotion.putString(
            Promotion.CREATIVE_NAME,
            CustomAction.CREATIVE_NAME_VOUCHER_DETAIL_FORMAT.format(
                merchantVoucherDetailClicked.couponCode,
                merchantVoucherDetailClicked.couponCode,
                merchantVoucherDetailClicked.creativeName
            )
        )
        promotion.putString(Promotion.CREATIVE_SLOT, merchantVoucherDetailClicked.horizontalCardPosition)
        promotion.putString(
            Promotion.ITEM_ID,
            CustomAction.ITEM_ID_FORMAT.format(
                merchantVoucherDetailClicked.bannerId,
                merchantVoucherDetailClicked.shopId
            )
        )
        promotion.putString(
            Promotion.ITEM_NAME,
            CustomAction.ITEM_NAME_VOUCHER_DETAIL_FORMAT.format(
                merchantVoucherDetailClicked.positionWidget,
                merchantVoucherDetailClicked.headerName
            )
        )
        bundle.putParcelableArrayList(Promotion.KEY, arrayListOf(promotion))
        bundle.putString(UserId.KEY, merchantVoucherDetailClicked.userId)
        return Pair(Ecommerce.PROMO_CLICK, bundle)
    }

    fun getClickProduct(merchantVoucherProductClicked: MerchantVoucherProductClicked): Pair<String, Bundle> {
        val bundle = Bundle()
        bundle.putString(Event.KEY, Event.SELECT_CONTENT)
        bundle.putString(
            Action.KEY,
            CustomAction.MERCHANT_VOUCHER_MULTIPLE_FORMAT.format(CustomAction.CLICK_SHOP)
        )
        bundle.putString(Category.KEY, Category.HOMEPAGE)
        bundle.putString(Label.KEY, CustomAction.CREATIVE_NAME_FORMAT.format(merchantVoucherProductClicked.productId, merchantVoucherProductClicked.shopId))
        bundle.putString(BusinessUnit.KEY, BusinessUnit.DEFAULT)
        bundle.putString(CurrentSite.KEY, CurrentSite.DEFAULT)
        bundle.putString(
            ItemList.KEY,
            CustomAction.ITEM_LIST_PRODUCT_DETAIL_FORMAT.format(merchantVoucherProductClicked.positionWidget,
            merchantVoucherProductClicked.topAds,
            merchantVoucherProductClicked.carousel,
            merchantVoucherProductClicked.recommendationType,
            merchantVoucherProductClicked.recomPageName,
            merchantVoucherProductClicked.buType)
        )

        val item = Bundle()
        item.putString(Items.DIMENSION_83, Items.DIMENSION_83_DEFAULT)
        item.putString(Items.INDEX, merchantVoucherProductClicked.horizontalCardPosition)
        item.putString(Items.ITEM_BRAND, merchantVoucherProductClicked.productBrand)
        val itemCategory = CustomAction.ITEM_CATEGORY_PRODUCT_DETAIL_FORMAT.format(
            merchantVoucherProductClicked.catNameLevel1,
            merchantVoucherProductClicked.catNameLevel2,
            merchantVoucherProductClicked.catNameLevel3
        )
        item.putString(Items.ITEM_CATEGORY, itemCategory)
        item.putString(Items.ITEM_ID, merchantVoucherProductClicked.productId)
        item.putString(Items.ITEM_NAME, merchantVoucherProductClicked.productName)
        item.putString(Items.ITEM_VARIANT, merchantVoucherProductClicked.productVariant)
        item.putString(Items.PRICE, merchantVoucherProductClicked.productPrice)

        bundle.putParcelableArrayList(Items.KEY, arrayListOf(item))

        return Pair(Ecommerce.PRODUCT_CLICK, bundle)
    }
}