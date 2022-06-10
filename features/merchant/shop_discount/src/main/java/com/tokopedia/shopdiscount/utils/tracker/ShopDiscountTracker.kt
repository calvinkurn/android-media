package com.tokopedia.shopdiscount.utils.tracker

import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode
import com.tokopedia.shopdiscount.utils.constant.EMPTY_STRING
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_ADD_PRODUCT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_PG
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CLICK_SAVE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.CREATE
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.EDIT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.PHYSICAL_GOODS_BUSINESS_UNIT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.SLASH_PRICE_LIST_OF_PRODUCTS
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.SLASH_PRICE_SET_DISCOUNT
import com.tokopedia.shopdiscount.utils.constant.TrackerConstant.TOKOPEDIA_SELLER
import com.tokopedia.track.builder.Tracker
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ShopDiscountTracker @Inject constructor(private val userSession: UserSessionInterface) {

    fun sendClickAddProductEvent() {
        Tracker.Builder()
            .setBusinessUnit(PHYSICAL_GOODS_BUSINESS_UNIT)
            .setEventCategory(SLASH_PRICE_LIST_OF_PRODUCTS)
            .setCurrentSite(TOKOPEDIA_SELLER)
            .setShopId(userSession.shopId)
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_ADD_PRODUCT)
            .setEventLabel(EMPTY_STRING)
            .build()
            .send()
    }

    fun sendClickSaveSlashPrice(mode: String) {
        val eventCategory = String.format(
            SLASH_PRICE_SET_DISCOUNT,
            getSlashPriceSetType(mode)
        )
        Tracker.Builder()
            .setBusinessUnit(PHYSICAL_GOODS_BUSINESS_UNIT)
            .setEventCategory(eventCategory)
            .setCurrentSite(TOKOPEDIA_SELLER)
            .setShopId(userSession.shopId)
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_SAVE)
            .setEventLabel(EMPTY_STRING)
            .build()
            .send()
    }

    private fun getSlashPriceSetType(mode: String): String {
        return when (mode) {
            ShopDiscountManageDiscountMode.CREATE -> {
                CREATE
            }
            else -> {
                EDIT
            }
        }
    }
}