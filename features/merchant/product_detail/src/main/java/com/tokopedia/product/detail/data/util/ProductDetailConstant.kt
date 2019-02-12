package com.tokopedia.product.detail.data.util

import android.support.annotation.IntDef

object ProductDetailConstant {
    const val PRD_STATE_ACTIVE = 1
    const val PRD_STATE_PENDING = -1
    const val PRD_STATE_WAREHOUSE = 3

    const val MAINAPP_SHOW_PDP_TOPADS = "mainapp_show_pdp_topads"

    @IntDef(BUY, ADD_TO_CART, BUY_NOW)
    @Retention(AnnotationRetention.SOURCE)
    annotation class ProductAction {}

    const val BUY = 0
    const val ADD_TO_CART = 1
    const val BUY_NOW = 2
}