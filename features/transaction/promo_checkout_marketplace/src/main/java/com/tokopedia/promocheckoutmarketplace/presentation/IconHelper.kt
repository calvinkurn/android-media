package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo
import com.tokopedia.promocheckoutmarketplace.data.response.SubSection

object IconHelper {

    fun getIcon(dictionary: String): Int {
        when (dictionary) {
            PromoInfo.ICON_USER -> {
                return IconUnify.USER
            }
            PromoInfo.ICON_INFORMATION -> {
                return IconUnify.INFORMATION
            }
            PromoInfo.ICON_FINANCE -> {
                return IconUnify.FINANCE
            }
            PromoInfo.ICON_CLOCK -> {
                return IconUnify.CLOCK
            }
            PromoInfo.ICON_COURIER -> {
                return IconUnify.COURIER
            }
            PromoInfo.ICON_TOKO_MEMBER -> {
                return IconUnify.TOKOMEMBER
            }
            SubSection.ICON_COUPON -> {
                return IconUnify.COUPON
            }
            SubSection.ICON_BADGE_OS_FILLED -> {
                return IconUnify.BADGE_OS_FILLED
            }
            SubSection.ICON_BADGE_PMPRO_FILLED -> {
                return IconUnify.BADGE_PMPRO_FILLED
            }
            SubSection.ICON_BADGE_PM_FILLED -> {
                return IconUnify.BADGE_PM_FILLED
            }
            SubSection.ICON_SHOP_FILLED -> {
                return IconUnify.SHOP_FILLED
            }
            SubSection.ICON_BADGE_NOW_FILLED -> {
                return IconUnify.BADGE_NOW_FILLED
            }
            else -> return IconUnify.IMAGE_BROKEN
        }
    }

}