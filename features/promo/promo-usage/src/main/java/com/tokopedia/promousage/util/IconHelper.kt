package com.tokopedia.promousage.util

import com.tokopedia.iconunify.IconUnify
import com.tokopedia.promousage.R
import com.tokopedia.promousage.domain.entity.PromoItemInfo

object IconHelper {

    fun isCustomIcon(dictionary: String): Boolean {
        return when (dictionary) {
            PromoItemInfo.ICON_DOT -> true
            else -> false
        }
    }

    fun shouldShowIcon(dictionary: String): Boolean {
        return dictionary != PromoItemInfo.ICON_NONE
    }

    fun getIcon(dictionary: String): Int {
        return when (dictionary) {
            PromoItemInfo.ICON_DOT -> {
                R.drawable.promo_usage_ic_dot
            }

            else -> IconUnify.IMAGE_BROKEN
        }
    }
}
