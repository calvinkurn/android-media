package com.tokopedia.home.analytics.v2

import android.os.Bundle
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
            const val ITEM_NAME_FORMAT = "/ - p%s - merchant_voucher_multiple - banner - %s"
        }
    }

    fun getShopClicked(merchantVoucherShopClicked: MerchantVoucherShopClicked): Bundle {
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
        return bundle
    }
}