package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.navigator

import android.content.Context
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shopadmin.common.constants.Constants
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam

class InvitationConfirmationNavigator(
    private val context: Context,
    private val invitationConfirmationParam: InvitationConfirmationParam
) {
    fun goToInvitationAccepted() {
        val params = mapOf<String, Any>(Constants.SHOP_NAME_PARAM to invitationConfirmationParam.getShopName())
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalMarketplace.ADMIN_INVITATION,
            params
        )
        RouteManager.route(context, appLink)
    }

    fun goToShopAccount() {
        val appLink = if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }
        RouteManager.route(context, appLink)
    }

    fun goToHomeBuyer() {
        RouteManager.route(context, ApplinkConst.HOME)
    }
}