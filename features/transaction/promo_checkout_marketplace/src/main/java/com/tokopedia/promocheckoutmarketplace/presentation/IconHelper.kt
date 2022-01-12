package com.tokopedia.promocheckoutmarketplace.presentation

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promocheckoutmarketplace.data.response.PromoInfo

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
            else -> return -1
        }
    }

}