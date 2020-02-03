package com.tokopedia.util

import android.content.Context
import com.tokopedia.core.drawer2.data.pojo.Wallet
import com.tokopedia.core.drawer2.data.pojo.topcash.Action
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCash
import com.tokopedia.core.drawer2.data.viewmodel.DrawerTokoCashAction
import com.tokopedia.core.drawer2.data.viewmodel.DrawerWalletAction
import com.tokopedia.core.drawer2.data.viewmodel.HomeHeaderWalletAction
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.SellerHomeHeaderWalletAction
import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import java.util.*

class SellerTokoCashUtil {
    
    companion object {

        fun convertToViewModel(sellerTokoCashData: SellerTokoCashData): SellerDrawerTokoCash {
            val drawerTokoCash = SellerDrawerTokoCash()
            val action = sellerTokoCashData.action
            if (action != null)
//                drawerTokoCash.drawerTokoCashAction = convertToActionViewModel(action)
            drawerTokoCash.homeHeaderWalletAction = convertToActionHomeHeader(sellerTokoCashData)
//            drawerTokoCash.drawerWalletAction = convertToActionDrawer(sellerTokoCashData)
            return drawerTokoCash
        }

        fun convertToActionHomeHeader(sellerTokoCashData: SellerTokoCashData): SellerHomeHeaderWalletAction {
            val data = SellerHomeHeaderWalletAction()
            val appLinkBalance = sellerTokoCashData.appLinks
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action?.getmText()
            data.visibleActionButton = sellerTokoCashData.action?.getmVisibility() != null && sellerTokoCashData.action?.getmVisibility() == "1"
            data.linked = sellerTokoCashData.link
            data.appLinkActionButton = if (sellerTokoCashData.action?.getmAppLinks() == null)
                ""
            else
                sellerTokoCashData.action?.getmAppLinks()
            data.redirectUrlActionButton = if (sellerTokoCashData.action?.redirectUrl == null)
                ""
            else
                sellerTokoCashData.action?.redirectUrl
            data.abTags = if (sellerTokoCashData.abTags == null)
                ArrayList()
            else
                sellerTokoCashData.abTags
            return data
        }

        private fun convertToActionDrawer(sellerTokoCashData: SellerTokoCashData): DrawerWalletAction {
            val appLinkBalance = sellerTokoCashData.appLinks
            val data = DrawerWalletAction()
//            data.labelTitle = sellerTokoCashData.text
//
//            data.appLinkBalance = appLinkBalance ?: ""
//            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
//            data.balance = sellerTokoCashData.balance
//            data.labelActionButton = sellerTokoCashData.action.getmText()
//            data.isVisibleActionButton = sellerTokoCashData.action.getmVisibility() != null && sellerTokoCashData.action.getmVisibility() == "1"
//            data.typeAction = if (sellerTokoCashData.link)
//                DrawerWalletAction.TYPE_ACTION_BALANCE
//            else
//                DrawerWalletAction.TYPE_ACTION_ACTIVATION
//            data.appLinkActionButton = if (sellerTokoCashData.action.getmAppLinks() == null)
//                ""
//            else
//                sellerTokoCashData.action.getmAppLinks()
//            data.redirectUrlActionButton = if (sellerTokoCashData.action.redirectUrl == null)
//                ""
//            else
//                sellerTokoCashData.action.redirectUrl
            return data
        }

        private fun convertToActionViewModel(action: Action): DrawerTokoCashAction {
            val drawerTokoCashAction = DrawerTokoCashAction()
            drawerTokoCashAction.text = action.getmText()
            drawerTokoCashAction.redirectUrl = action.redirectUrl
            return drawerTokoCashAction
        }

        fun convertToViewModel(sellerTokoCashData: Wallet, context: Context): DrawerTokoCash {
            val drawerTokoCash = DrawerTokoCash()
            drawerTokoCash.drawerTokoCashAction = convertToActionViewModel(sellerTokoCashData.action)
            drawerTokoCash.homeHeaderWalletAction = convertToActionHomeHeader(sellerTokoCashData, context)
            drawerTokoCash.drawerWalletAction = convertToActionDrawer(sellerTokoCashData, context)
            return drawerTokoCash
        }

        private fun convertToActionHomeHeader(sellerTokoCashData: Wallet, context: Context): HomeHeaderWalletAction {
            val data = HomeHeaderWalletAction()
            val appLinkBalance = sellerTokoCashData.applinks
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action.text
            data.isVisibleActionButton = sellerTokoCashData.action.visibility != null && sellerTokoCashData.action.visibility == "1"
            data.isLinked = sellerTokoCashData.linked!!

            if (sellerTokoCashData.linked!!) {
                data.appLinkActionButton = if (sellerTokoCashData.action.applinks == null)
                    ""
                else
                    sellerTokoCashData.action.applinks
            } else {
                data.appLinkActionButton = sellerTokoCashData.action.applinks
                data.labelActionButton = sellerTokoCashData.action.text
                data.labelTitle = sellerTokoCashData.text
            }

            data.redirectUrlActionButton = if (sellerTokoCashData.action.redirectUrl == null)
                ""
            else
                sellerTokoCashData.action.redirectUrl


            val abTags = ArrayList<String>()
            if (sellerTokoCashData.abTags != null) {
                var index = 0
                for (abtag in sellerTokoCashData.abTags) {
                    abTags.add(abtag.tag)
                    index++
                }
            }
            data.abTags = abTags
            return data
        }

        private fun convertToActionDrawer(sellerTokoCashData: Wallet, context: Context): DrawerWalletAction {
            val appLinkBalance = sellerTokoCashData.applinks
            val data = DrawerWalletAction()
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action.text
            data.isVisibleActionButton = sellerTokoCashData.action.visibility != null && sellerTokoCashData.action.visibility == "1"
            data.typeAction = if (sellerTokoCashData.linked)
                DrawerWalletAction.TYPE_ACTION_BALANCE
            else
                DrawerWalletAction.TYPE_ACTION_ACTIVATION
            data.appLinkActionButton = if (sellerTokoCashData.action.applinks == null)
                ""
            else
                sellerTokoCashData.action.applinks
            data.redirectUrlActionButton = if (sellerTokoCashData.action.redirectUrl == null)
                ""
            else
                sellerTokoCashData.action.redirectUrl

            if (sellerTokoCashData.linked!!) {
                data.appLinkActionButton = if (sellerTokoCashData.action.applinks == null)
                    ""
                else
                    sellerTokoCashData.action.applinks
            } else {
                data.appLinkActionButton = sellerTokoCashData.action.applinks
                data.labelActionButton = sellerTokoCashData.action.text
                data.labelTitle = sellerTokoCashData.text
            }

            return data
        }

        private fun convertToActionViewModel(action: com.tokopedia.core.drawer2.data.pojo.Action?): DrawerTokoCashAction? {
            if (action == null) {
                return null
            }

            val drawerTokoCashAction = DrawerTokoCashAction()
            drawerTokoCashAction.text = action.text
            drawerTokoCashAction.redirectUrl = action.redirectUrl
            return drawerTokoCashAction
        }

    }
}