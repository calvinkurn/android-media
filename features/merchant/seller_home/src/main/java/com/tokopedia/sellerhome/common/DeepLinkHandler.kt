package com.tokopedia.sellerhome.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.order.DeeplinkMapperOrder
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

object DeepLinkHandler {

    fun handleAppLink(context: Context,
            intent: Intent?,
            callback: (page: PageFragment) -> Unit
    ) {
        if (null == intent) return

        val data = intent.data?.toString() ?: return

        when {
            //Seller Order Management (som)
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                val orderId = intent.data?.getQueryParameter(DeeplinkMapperOrder.QUERY_PARAM_ORDER_ID).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ALL_ORDER, searchKeyword, orderId = orderId))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_NEW_ORDER, searchKeyword))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_READY_TO_SHIP, searchKeyword))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_IN_SHIPPING, searchKeyword))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_DONE, searchKeyword))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED) -> {
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ORDER_CANCELLED, searchKeyword))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLATION_REQUEST) -> {
                val uri = intent.data
                val searchKeyword = intent.data?.getQueryParameter(AppLinkMapperSellerHome.QUERY_PARAM_SEARCH).orEmpty()
                val filterOrderType = uri?.getQueryParameter(AppLinkMapperSellerHome.FILTER_ORDER_TYPE).toIntOrZero()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ALL_ORDER, searchKeyword, filterOrderType))
            }

            //Product Manage
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST) -> {
                val uri = intent.data
                val filterId = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_FILTER).orEmpty()
                val searchKeyword = uri?.getQueryParameter(DeepLinkMapperProductManage.QUERY_PARAM_SEARCH).orEmpty()
                callback(PageFragment(FragmentType.PRODUCT, filterId, searchKeyword))
            }

            //Top Chat
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_CHAT) -> {
                callback(PageFragment(FragmentType.CHAT))
            }

            //Seller Home
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME) -> {
                callback(PageFragment(FragmentType.HOME))
            }

            else -> callback(PageFragment(FragmentType.HOME))
        }
    }
}