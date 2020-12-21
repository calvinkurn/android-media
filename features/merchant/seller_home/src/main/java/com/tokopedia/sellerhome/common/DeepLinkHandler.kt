package com.tokopedia.sellerhome.common

import android.content.Intent
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.productmanage.DeepLinkMapperProductManage
import com.tokopedia.applink.sellerhome.AppLinkMapperSellerHome
import com.tokopedia.kotlin.extensions.view.toIntOrZero

/**
 * Created By @ilhamsuaib on 2020-03-05
 */

object DeepLinkHandler {

    fun handleAppLink(
            intent: Intent?,
            callback: (page: PageFragment) -> Unit
    ) {
        if (null == intent) return

        val data = intent.data?.toString() ?: return

        when {
            //Seller Order Management (som)
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_ALL) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ALL_ORDER))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_NEW_ORDER) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_NEW_ORDER))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_READY_TO_SHIP) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_READY_TO_SHIP))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_SHIPPED) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_IN_SHIPPING))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_DONE) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_DONE))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLED) -> {
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ORDER_CANCELLED))
            }
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_SOM_CANCELLATION_REQUEST) -> {
                val uri = intent.data
                val filterOrderType = uri?.getQueryParameter(AppLinkMapperSellerHome.FILTER_ORDER_TYPE).toIntOrZero()
                callback(PageFragment(FragmentType.ORDER, SomTabConst.STATUS_ALL_ORDER, orderType = filterOrderType))
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
        }
    }
}