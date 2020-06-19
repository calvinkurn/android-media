package com.tokopedia.applink.productmanage

import android.net.Uri
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig

/**
 * Created By @ilhamsuaib on 01/04/20
 */

object DeepLinkMapperProductManage {

    const val QUERY_PARAM_FILTER = "filter"
    private const val PATH_SEGMENT_EDIT = "edit"

    /**
     * @param deepLink : tokopedia://product/edit/12345
     * @return tokopedia-android-internal://marketplace/product-edit-item/12345/
     * */
    fun getEditProductInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        return when {
            //tokopedia://product/edit/12345
            uri.pathSegments.size == 2 && uri.pathSegments[0] == PATH_SEGMENT_EDIT -> {
                val productId = uri.pathSegments[1]
                Uri.parse(ApplinkConstInternalMechant.MERCHANT_OPEN_PRODUCT_PREVIEW)
                        .buildUpon()
                        .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_ID, productId)
                        .appendQueryParameter(ApplinkConstInternalMechant.QUERY_PARAM_MODE, ApplinkConstInternalMechant.MODE_EDIT_PRODUCT)
                        .build()
                        .toString()
            }
            else -> ""
        }
    }

    /**
     * @param deepLink : tokopedia://seller/product/manage?filter=isEmptyStockOnly
     * @return if seller app or shouldRedirectToSellerApp : tokopedia-android-internal://sellerapp/sellerhome-product-list?filter=isEmptyStockOnly
     * @return if not seller app : tokopedia-android-internal://marketplace/product-manage-list?filter=isEmptyStockOnly
     * */
    fun getProductListInternalAppLink(deepLink: String): String {
        val uri = Uri.parse(deepLink)
        val filterId = uri.getQueryParameter(QUERY_PARAM_FILTER).orEmpty()
        val shouldRedirectToSellerApp = uri.getBooleanQueryParameter(RouteManager.KEY_REDIRECT_TO_SELLER_APP, false)
        return if (GlobalConfig.isSellerApp() || shouldRedirectToSellerApp) {
            if (filterId.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_FILTER to filterId)
                UriUtil.buildUriAppendParam(ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST, param)
            } else {
                ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST
            }
        } else {
            if (filterId.isNotBlank()) {
                val param = mapOf(QUERY_PARAM_FILTER to filterId)
                UriUtil.buildUriAppendParam(ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST, param)
            } else {
                ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST
            }
        }
    }
}