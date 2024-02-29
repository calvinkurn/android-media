package com.tokopedia.shopdiscount.manage_discount.util

import androidx.annotation.StringDef
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.CREATE
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.DELETE
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.OPT_OUT_SUBSIDY
import com.tokopedia.shopdiscount.manage_discount.util.ShopDiscountManageDiscountMode.Companion.UPDATE

@Retention(AnnotationRetention.SOURCE)
@StringDef(CREATE, UPDATE, DELETE, OPT_OUT_SUBSIDY)
annotation class ShopDiscountManageDiscountMode {
    companion object {
        const val CREATE = "create"
        const val UPDATE = "update"
        const val DELETE = "delete"
        const val OPT_OUT_SUBSIDY = "opt-out-subsidy"
    }
}
