package com.tokopedia.applink.tokofood

import android.net.Uri
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood

object DeeplinkMapperTokoFood {

    // merchant
    const val PARAM_MERCHANT_ID = "merchantId"
    const val PARAM_PRODUCT_ID = "product_id"

    //post purchase
    const val PATH_ORDER_ID = "orderId"

    //category
    const val PAGE_TITLE_PARAM = "pageTitle"
    const val OPTION_PARAM = "option"
    const val CUISINE_PARAM = "cuisine"
    const val SORT_BY_PARAM = "sortBy"

    fun getTokoFoodMerchantInternalAppLink(idList: List<String>?): String {
        val merchantId = idList?.getOrNull(0).orEmpty()
        val productId = idList?.getOrNull(1).orEmpty()
        return Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
                .buildUpon()
                .appendQueryParameter(PARAM_MERCHANT_ID, merchantId)
                .appendQueryParameter(PARAM_PRODUCT_ID, productId)
                .build().toString()
    }

    fun getTokoFoodPostPurchaseInternalAppLink(uri: Uri): String {
        val orderId = uri.lastPathSegment
        return Uri.parse(ApplinkConstInternalTokoFood.POST_PURCHASE)
            .buildUpon()
            .appendQueryParameter(PATH_ORDER_ID, orderId)
            .build().toString()
    }

    fun getTokoFoodHomeInternalAppLink(): String {
        return ApplinkConstInternalTokoFood.HOME
    }

    fun getTokoFoodCategoryInternalAppLink(uri: Uri): String {
        val pageTitle = uri.getQueryParameter(PAGE_TITLE_PARAM) ?: ""
        val option = uri.getQueryParameter(OPTION_PARAM) ?: ""
        val cuisine = uri.getQueryParameter(CUISINE_PARAM) ?: ""
        val sortBy = uri.getQueryParameter(SORT_BY_PARAM) ?: ""

        return Uri.parse(ApplinkConstInternalTokoFood.CATEGORY)
            .buildUpon()
            .appendQueryParameter(PAGE_TITLE_PARAM, pageTitle)
            .appendQueryParameter(OPTION_PARAM, option)
            .appendQueryParameter(CUISINE_PARAM, cuisine)
            .appendQueryParameter(SORT_BY_PARAM, sortBy)
            .build().toString()
    }
}