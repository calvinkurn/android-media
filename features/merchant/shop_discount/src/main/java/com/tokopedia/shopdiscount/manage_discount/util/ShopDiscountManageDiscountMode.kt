package com.tokopedia.shopdiscount.manage_discount.util

import androidx.annotation.StringDef
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.CREATE
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.UPDATE

@Retention(AnnotationRetention.SOURCE)
@StringDef(CREATE, UPDATE)
annotation class ShopDiscountManageDiscountMode {
    companion object {
        const val CREATE = "create"
        const val UPDATE = "update"
    }
}