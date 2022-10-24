package com.tokopedia.applink.purchaseplatform

import android.content.Context
import android.net.Uri
import com.tokopedia.applink.FirebaseRemoteConfigInstance
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RemoteConfigKey
import com.tokopedia.remoteconfig.RollenceKey

object DeeplinkMapperWishlist {
    private const val QUERY_PARAM_WH_ID = "whid="
    private const val PATH_WAREHOUSE_ID = "warehouse_id"
    private const val QUERY_EXT_PARAM = "extParam"

    fun getRegisteredNavigationWishlist(context: Context): String {
        return if (isUsingWishlistCollection(context)) {
            ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION
        } else {
            ApplinkConstInternalPurchasePlatform.WISHLIST_V2
        }
    }

    fun isUsingWishlistCollection(context: Context): Boolean {
        return isEnableRemoteConfigWishlistCollection(context) && isEnableRollenceWishlistCollection()
    }

    private fun isEnableRemoteConfigWishlistCollection(context: Context) = FirebaseRemoteConfigInstance.get(context).getBoolean(RemoteConfigKey.ENABLE_WISHLIST_COLLECTION)

    private fun isEnableRollenceWishlistCollection(): Boolean {
        return try {
            val remoteConfigRollenceValue = RemoteConfigInstance.getInstance().abTestPlatform.getString(RollenceKey.WISHLIST_COLLECTION, RollenceKey.WISHLIST_CONTROL_VARIANT)
            return (remoteConfigRollenceValue == RollenceKey.WISHLIST_EXPERIMENT_VARIANT)

        } catch (e: Exception) {
            true
        }
    }

    /**
     * map a http url to tokopedia applink. example :
     * Url = https://www.tokopedia.com/vibostech/cetakan-es-batu-panjang-silicone-finger-ice-tray-anti-pecah-fingerice?extParam=whid=65233,
     * AppLink = tokopedia-android-internal://marketplace/product-detail/2797415493/?warehouse_id=65233"
     * */
    fun mapPdpAppLinkFromHttp(url: String, productId: String): String {
        return try {
            val pdpUrl = UriUtil.buildUri(ApplinkConstInternalMarketplace.PRODUCT_DETAIL, productId)
            val uri = Uri.parse(url)
            val extParam = uri.getQueryParameter(QUERY_EXT_PARAM)

            if (extParam.isNullOrBlank()) return url

            val whId = extParam.replace(QUERY_PARAM_WH_ID, "")
            Uri.parse(pdpUrl)
                .buildUpon()
                .appendQueryParameter(PATH_WAREHOUSE_ID, whId)
                .build()
                .toString()
        } catch (e: Exception) {
            url
        }
    }
}
