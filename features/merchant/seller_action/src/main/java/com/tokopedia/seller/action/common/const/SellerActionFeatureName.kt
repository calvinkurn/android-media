package com.tokopedia.seller.action.common.const

import androidx.annotation.StringDef

@MustBeDocumented
@Retention(AnnotationRetention.SOURCE)
@StringDef(SellerActionFeatureName.ORDER_DETAIL, SellerActionFeatureName.ALL_ORDER)
annotation class SellerActionFeatureName {
    companion object {
        const val ORDER_DETAIL = "order_detail"
        const val ALL_ORDER = "all_order"
    }
}