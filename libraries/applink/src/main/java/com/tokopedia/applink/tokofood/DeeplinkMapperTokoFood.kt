package com.tokopedia.applink.tokofood

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalOrder
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.remoteconfig.RemoteConfigKey

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
    const val BRAND_UID_PARAM = "brand_uid"
    const val SORT_BY_PARAM = "sortBy"

    fun mapperInternalApplinkTokoFood(context: Context, uri: Uri): String {
        val url = uri.toString()
        return when {
            url.startsWith(ApplinkConst.TokoFood.HOME) || url.startsWith(ApplinkConst.TokoFood.GOFOOD) -> getTokoFoodHomeInternalAppLink(context)
            url.startsWith(ApplinkConst.TokoFood.CATEGORY) -> getTokoFoodCategoryInternalAppLink(context, uri)
            isMatchPattern(ApplinkConst.TokoFood.POST_PURCHASE, uri) -> getTokoFoodPostPurchaseInternalAppLink(uri)
            isMatchPattern(ApplinkConst.TokoFood.MERCHANT, uri) -> getTokoFoodMerchantInternalAppLink(
                    context, getUriIdList(ApplinkConst.TokoFood.MERCHANT, uri), uri)
            url.startsWith(ApplinkConst.TokoFood.TOKOFOOD_ORDER) -> { ApplinkConstInternalOrder.UNIFY_ORDER_TOKOFOOD }
            url.startsWith(ApplinkConst.TokoFood.SEARCH) ->
                if (getIsTokofoodGtpMigration(context)) ApplinkConstInternalTokoFood.SEARCH else ApplinkConstInternalTokoFood.SEARCH_OLD
            else -> url
        }
    }

    fun getTokoFoodMerchantInternalAppLink(context: Context, idList: List<String>?, uri: Uri): String {
        val merchantApplink =
            if (getIsTokofoodGtpMigration(context)) {
                ApplinkConstInternalTokoFood.MERCHANT
            } else {
                ApplinkConstInternalTokoFood.MERCHANT_OLD
            }
        val merchantId = idList?.getOrNull(0).orEmpty()
        val productId = uri.getQueryParameter(PARAM_PRODUCT_ID).orEmpty()
        return Uri.parse(merchantApplink)
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

    private fun getTokoFoodHomeInternalAppLink(context: Context): String {
        return if (getIsTokofoodGtpMigration(context)) {
            ApplinkConstInternalTokoFood.HOME
        } else {
            ApplinkConstInternalTokoFood.HOME_OLD
        }
    }

    private fun getTokoFoodCategoryInternalAppLink(context: Context, uri: Uri): String {
        val categoryApplink =
            if (getIsTokofoodGtpMigration(context)) {
                ApplinkConstInternalTokoFood.CATEGORY
            } else {
                ApplinkConstInternalTokoFood.CATEGORY_OLD
            }
        val pageTitle = uri.getQueryParameter(PAGE_TITLE_PARAM) ?: ""
        val option = uri.getQueryParameter(OPTION_PARAM) ?: ""
        val cuisine = uri.getQueryParameter(CUISINE_PARAM) ?: ""
        val sortBy = uri.getQueryParameter(SORT_BY_PARAM) ?: ""
        val brandUId = uri.getQueryParameter(BRAND_UID_PARAM) ?: ""

        return Uri.parse(categoryApplink)
            .buildUpon()
            .appendQueryParameter(PAGE_TITLE_PARAM, pageTitle)
            .appendQueryParameter(OPTION_PARAM, option)
            .appendQueryParameter(CUISINE_PARAM, cuisine)
            .appendQueryParameter(SORT_BY_PARAM, sortBy)
            .appendQueryParameter(BRAND_UID_PARAM, brandUId)
            .build().toString()
    }

    private fun getUriIdList(pattern:String, uri: Uri): List<String>? {
        return UriUtil.matchWithPattern(pattern, uri)
    }

    private fun isMatchPattern(pattern:String, uri: Uri): Boolean {
        return UriUtil.matchWithPattern(pattern, uri) != null
    }

    private fun getIsTokofoodGtpMigration(context: Context): Boolean {
        return FirebaseRemoteConfigInstance.get(context).getBoolean(RemoteConfigKey.IS_TOKOFOOD_NEW_GTP_FLOW)
            .orFalse()
    }
}
