package com.tokopedia.applink.tokofood

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

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

    fun mapperInternalApplinkTokoFood(uri: Uri): String {
        val url = uri.toString()
        if (isGoToFoodPage()){
            return when {
                url.startsWith(ApplinkConst.TokoFood.HOME) -> getTokoFoodHomeInternalAppLink()
                url.startsWith(ApplinkConst.TokoFood.CATEGORY) -> getTokoFoodCategoryInternalAppLink(uri)
                url.startsWith(ApplinkConst.TokoFood.POST_PURCHASE) -> getTokoFoodPostPurchaseInternalAppLink(uri)
                isMatchPattern(ApplinkConst.TokoFood.MERCHANT, uri) -> getTokoFoodMerchantInternalAppLink(
                    getUriIdList(ApplinkConst.TokoFood.MERCHANT, uri), uri)
                else -> url
            }
        } else {
            return ApplinkConst.HOME
        }
    }

    fun getTokoFoodMerchantInternalAppLink(idList: List<String>?, uri: Uri): String {
        val merchantId = idList?.getOrNull(0).orEmpty()
        val productId = uri.getQueryParameter(PARAM_PRODUCT_ID).orEmpty()
        return Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
            .buildUpon()
            .appendQueryParameter(PARAM_MERCHANT_ID, merchantId)
            .appendQueryParameter(PARAM_PRODUCT_ID, productId)
            .build().toString()
    }

    private fun getTokoFoodPostPurchaseInternalAppLink(uri: Uri): String {
        val orderId = uri.lastPathSegment
        return Uri.parse(ApplinkConstInternalTokoFood.POST_PURCHASE)
            .buildUpon()
            .appendQueryParameter(PATH_ORDER_ID, orderId)
            .build().toString()
    }

    private fun getTokoFoodHomeInternalAppLink(): String {
        return ApplinkConstInternalTokoFood.HOME
    }

    private fun getTokoFoodCategoryInternalAppLink(uri: Uri): String {
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

    private fun isGoToFoodPage(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.KEY_ROLLENCE_FOOD, ""
            ) == RollenceKey.KEY_ROLLENCE_FOOD
        } catch (e: Exception) {
            false
        }
    }

    private fun getUriIdList(pattern:String, uri: Uri): List<String>? {
        return UriUtil.matchWithPattern(pattern, uri)
    }

    private fun isMatchPattern(pattern:String, uri: Uri): Boolean {
        return UriUtil.matchWithPattern(pattern, uri) != null
    }
}