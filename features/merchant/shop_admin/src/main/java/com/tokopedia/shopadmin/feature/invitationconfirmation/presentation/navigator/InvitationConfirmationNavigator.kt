package com.tokopedia.shopadmin.feature.invitationconfirmation.presentation.navigator

import androidx.fragment.app.Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.config.GlobalConfig
import com.tokopedia.shopadmin.common.constants.Constants

class InvitationConfirmationNavigator(
    private val fragment: Fragment
) {

    fun goToInvitationAccepted(shopName: String) {
        val params =
            mapOf<String, Any>(Constants.SHOP_NAME_PARAM to shopName)
        val appLink = UriUtil.buildUriAppendParams(
            ApplinkConstInternalMarketplace.ADMIN_ACCEPTED,
            params
        )
        RouteManager.route(fragment.context, appLink)
    }

    fun goToButtonOrToolbarActionPage() {
        val appLink = if (GlobalConfig.isSellerApp()) {
            ApplinkConst.LOGIN
        } else {
            ApplinkConst.HOME
        }
        RouteManager.route(fragment.context, appLink)
    }

    fun goToOtp(email: String, phoneNumber: String) {
        val intent =
            RouteManager.getIntent(fragment.context, ApplinkConstInternalUserPlatform.COTP).apply {
                putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
                putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_EMAIL)
                putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, MODE_EMAIL)
                putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phoneNumber)
                putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
                putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
            }
        fragment.startActivityForResult(intent, REQUEST_OTP)
    }

    fun goToLogin() {
        val intent = RouteManager.getIntent(fragment.context, ApplinkConst.LOGIN)
        fragment.startActivityForResult(intent, REQUEST_LOGIN)
    }

    companion object {
        const val REQUEST_LOGIN = 459
        const val REQUEST_OTP = 659
        const val MODE_EMAIL = "email"
        const val OTP_TYPE_EMAIL = 141
    }

}
