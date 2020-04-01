package com.tokopedia.sellerhome.common

import android.content.Intent
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.shop.common.data.source.cloud.query.param.option.FilterOption

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

            //Product Manage
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_PRODUCT_MANAGE_LIST) -> {
                callback(PageFragment(FragmentType.PRODUCT))
            }

            //Top Chat
            data.startsWith(ApplinkConstInternalSellerapp.SELLER_HOME_CHAT) -> {
                callback(PageFragment(FragmentType.CHAT))
            }
        }
    }
}