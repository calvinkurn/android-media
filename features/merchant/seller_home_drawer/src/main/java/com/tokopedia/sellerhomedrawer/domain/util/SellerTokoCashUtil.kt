package com.tokopedia.sellerhomedrawer.domain.util

import android.content.Context
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.SellerHomeHeaderWalletAction
import com.tokopedia.sellerhomedrawer.data.SellerTokoCashData
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerTokoCashAction
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerWalletAction
import com.tokopedia.sellerhomedrawer.data.userdata.Wallet
import com.tokopedia.sellerhomedrawer.data.userdata.wallet.Action
import java.util.*

class SellerTokoCashUtil {
    
    companion object {

        fun convertToViewModel(sellerTokoCashData: SellerTokoCashData): SellerDrawerTokoCash {
            val drawerTokoCash = SellerDrawerTokoCash()
            val action = sellerTokoCashData.action
            if (action != null)
                drawerTokoCash.drawerTokoCashAction = convertToActionViewModel(action)
            drawerTokoCash.homeHeaderWalletAction = convertToActionHomeHeader(sellerTokoCashData)
            drawerTokoCash.drawerWalletAction = convertToActionDrawer(sellerTokoCashData)
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
            data.redirectUrlActionButton = if (sellerTokoCashData.action?.getRedirectUrl() == null)
                ""
            else
                sellerTokoCashData.action?.getRedirectUrl()
            data.abTags = if (sellerTokoCashData.abTags == null)
                ArrayList()
            else
                sellerTokoCashData.abTags
            return data
        }

        private fun convertToActionDrawer(sellerTokoCashData: SellerTokoCashData): SellerDrawerWalletAction {
            val appLinkBalance = sellerTokoCashData.appLinks
            val data = SellerDrawerWalletAction()
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action?.getmText()
            data.isVisibleActionButton = sellerTokoCashData.action?.getmVisibility() != null && sellerTokoCashData.action?.getmVisibility() == "1"
            data.typeAction = if (sellerTokoCashData.link)
                SellerDrawerWalletAction.TYPE_ACTION_BALANCE
            else
                SellerDrawerWalletAction.TYPE_ACTION_ACTIVATION
            data.appLinkActionButton = if (sellerTokoCashData.action?.getmAppLinks() == null)
                ""
            else
                sellerTokoCashData.action?.getmAppLinks()
            data.redirectUrlActionButton = if (sellerTokoCashData.action?.getRedirectUrl() == null)
                ""
            else
                sellerTokoCashData.action?.getRedirectUrl()
            return data
        }

        private fun convertToActionViewModel(action: Action): SellerDrawerTokoCashAction {
            val drawerTokoCashAction = SellerDrawerTokoCashAction()
            drawerTokoCashAction.text = action.text
            drawerTokoCashAction.redirectUrl = action.redirectUrl
            return drawerTokoCashAction
        }

        private fun convertToActionHomeHeader(sellerTokoCashData: Wallet, context: Context): SellerHomeHeaderWalletAction {
            val data = SellerHomeHeaderWalletAction()
            val appLinkBalance = sellerTokoCashData.applinks
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action?.text
            data.visibleActionButton = sellerTokoCashData.action?.visibility != null && sellerTokoCashData.action?.visibility == "1"
            sellerTokoCashData.linked?.let { isLinked ->
                data.linked = isLinked
                if (isLinked) {
                    data.appLinkActionButton = if (sellerTokoCashData.action?.applinks == null)
                        ""
                    else
                        sellerTokoCashData.action?.applinks
                } else {
                    data.appLinkActionButton = sellerTokoCashData.action?.applinks
                    data.labelActionButton = sellerTokoCashData.action?.text
                    data.labelTitle = sellerTokoCashData.text
                }

            }

            data.redirectUrlActionButton = if (sellerTokoCashData.action?.redirectUrl == null)
                ""
            else
                sellerTokoCashData.action?.redirectUrl


            val abTags = ArrayList<String>()
            if (sellerTokoCashData.abTags != null) {
                var index = 0
                for (abtag in sellerTokoCashData.abTags) {
                    abtag.tag?.let {
                        abTags.add(it)
                    }
                    index++
                }
            }
            data.abTags = abTags
            return data
        }

        private fun convertToActionDrawer(sellerTokoCashData: Wallet, context: Context): SellerDrawerWalletAction {
            val appLinkBalance = sellerTokoCashData.applinks
            val data = SellerDrawerWalletAction()
            data.labelTitle = sellerTokoCashData.text

            data.appLinkBalance = appLinkBalance ?: ""
            data.redirectUrlBalance = if (sellerTokoCashData.redirectUrl == null) "" else sellerTokoCashData.redirectUrl
            data.balance = sellerTokoCashData.balance
            data.labelActionButton = sellerTokoCashData.action?.text
            data.isVisibleActionButton = sellerTokoCashData.action?.visibility != null && sellerTokoCashData.action?.visibility == "1"
            sellerTokoCashData.linked?.let { isLinked ->
                data.typeAction = if (isLinked)
                    SellerDrawerWalletAction.TYPE_ACTION_BALANCE
                else
                    SellerDrawerWalletAction.TYPE_ACTION_ACTIVATION
            }
            data.appLinkActionButton = if (sellerTokoCashData.action?.applinks == null)
                ""
            else
                sellerTokoCashData.action?.applinks
            data.redirectUrlActionButton = if (sellerTokoCashData.action?.redirectUrl == null)
                ""
            else
                sellerTokoCashData.action?.redirectUrl

            if (sellerTokoCashData.linked!!) {
                data.appLinkActionButton = if (sellerTokoCashData.action?.applinks == null)
                    ""
                else
                    sellerTokoCashData.action?.applinks
            } else {
                data.appLinkActionButton = sellerTokoCashData.action?.applinks
                data.labelActionButton = sellerTokoCashData.action?.text
                data.labelTitle = sellerTokoCashData.text
            }

            return data
        }

        private fun convertToActionViewModel(action: com.tokopedia.sellerhomedrawer.data.Action?): SellerDrawerTokoCashAction? {
            if (action == null) {
                return null
            }

            val drawerTokoCashAction = SellerDrawerTokoCashAction()
            drawerTokoCashAction.text = action.getmText()
            drawerTokoCashAction.redirectUrl = action.getRedirectUrl()
            return drawerTokoCashAction
        }

    }
}