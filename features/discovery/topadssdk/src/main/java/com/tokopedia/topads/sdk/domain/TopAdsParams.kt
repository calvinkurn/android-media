package com.tokopedia.topads.sdk.domain

import com.tokopedia.topads.sdk.base.TKPDMapParam

class TopAdsParams {
    val param: TKPDMapParam<String, String> = TKPDMapParam()

    companion object {
        const val KEY_EP = "ep"
        const val KEY_DEVICE = "device"
        const val KEY_ITEM = "item"
        const val KEY_HEADLINE_PRODUCT_COUNT = "headline_product_count"
        const val KEY_WITH_TEMPLATE = "with_template"
        const val KEY_SRC = "src"
        const val KEY_TEMPLATE_ID = "template_id"
        const val KEY_PAGE = "page"
        const val KEY_DEPARTEMENT_ID = "dep_id"
        const val DEFAULT_KEY_ITEM = "2"
        const val DEFAULT_KEY_EP = "product"
        const val DEFAULT_KEY_DEVICE = "android"
        const val DEFAULT_KEY_PAGE = "1"
        const val DEFAULT_KEY_SRC = "search"
    }

    init {
        param[KEY_ITEM] = DEFAULT_KEY_ITEM
        param[KEY_DEVICE] = DEFAULT_KEY_DEVICE
        param[KEY_PAGE] = DEFAULT_KEY_PAGE
    }
}
