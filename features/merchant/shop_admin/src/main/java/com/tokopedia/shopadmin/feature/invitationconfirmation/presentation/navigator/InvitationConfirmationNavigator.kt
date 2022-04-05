package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shopadmin.common.constants.Constants
import com.tokopedia.shopadmin.feature.invitationconfirmation.domain.param.InvitationConfirmationParam

class InvitationConfirmationNavigator(
    private val fragment: Fragment,
    private val invitationConfirmationParam: InvitationConfirmationParam
) {
    fun goToInvitationAccepted() {
        val params =
            mapOf<String, Any>(Constants.SHOP_NAME_PARAM to invitationConfirmationParam.getShopName())
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalMarketplace.ADMIN_INVITATION,
            params
        )
        RouteManager.route(fragment.context, appLink)
    }

    fun goToShopAccount() {
        val appLink = if (GlobalConfig.isSellerApp()) {
            ApplinkConstInternalSellerapp.SELLER_HOME
        } else {
            ApplinkConstInternalSellerapp.SELLER_MENU
        }
        RouteManager.route(fragment.context, appLink)
    }

    fun goToHomeBuyer() {
        RouteManager.route(fragment.context, ApplinkConst.HOME)
    }

    fun goToOtp(email: String) {
        val intent = RouteManager.getIntent(fragment.context, ApplinkConstInternalGlobal.COTP)
        intent.apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
            putExtra(
                ApplinkConstInternalGlobal.PARAM_OTP_TYPE,
                OTP_TYPE_EMAIL
            )
            putExtra(
                ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE,
                MODE_EMAIL
            )
            putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
            putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        }
        fragment.startActivityForResult(intent, REQUEST_OTP)
    }

    companion object {
        const val REQUEST_LOGIN = 459
        const val REQUEST_OTP = 659
        const val MODE_EMAIL = "email"
        const val OTP_TYPE_EMAIL = 150
    }
}