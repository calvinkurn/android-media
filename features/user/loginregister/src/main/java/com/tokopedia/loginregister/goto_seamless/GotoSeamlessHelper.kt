package com.tokopedia.loginregister.goto_seamless

import android.content.Context
import com.gojek.icp.identity.loginsso.ClientSSOBridge
import com.gojek.icp.identity.loginsso.Environment
import com.gojek.icp.identity.loginsso.SSOHostBridge
import com.gojek.icp.identity.loginsso.data.SSOHostData
import com.gojek.icp.identity.loginsso.data.models.Profile
import com.google.gson.Gson
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.goto_seamless.model.GojekProfileData
import javax.inject.Inject

class GotoSeamlessHelper @Inject constructor(@ApplicationContext val context: Context) {

    private val environment = if(GlobalConfig.DEBUG) Environment.Integration else Environment.Prod

    private val appId = if(GlobalConfig.isSellerApp()) GlobalConfig.PACKAGE_SELLER_APP else GlobalConfig.PACKAGE_CONSUMER_APP

    suspend fun getSsoData(): Map<String, String> {
        return ClientSSOBridge(appID = appId, appVersion = GlobalConfig.VERSION_NAME).retrieveSSOData(
            context = context,
            environment = environment
        )
    }

    suspend fun getGojekProfile(): GojekProfileData {
        val ssoData = getSsoData()
        val gojekProfile = Gson().fromJson(ssoData[KEY_PROFILE], GojekProfileData::class.java)
        gojekProfile.authCode = ssoData[KEY_AUTH_CODE] ?: ""
        return gojekProfile
    }

    suspend fun saveUserProfileToSDK(profile: Profile) {
        val ssoHostData = SSOHostData(
            clientId = context.getString(com.tokopedia.keys.R.string.gojek_sso_client_id),
            clientSecret = context.getString(com.tokopedia.keys.R.string.gojek_sso_client_secret),
            environment
        )

        val ssoDataBridge = SSOHostBridge.getSsoHostBridge()
        ssoDataBridge.initBridge(context, ssoHostData)
        ssoDataBridge.saveUserProfileData(context, profile)
    }

    suspend fun updateUserProfileToSDK(profile: Profile) {
        val ssoHostData = SSOHostData(
            clientId = context.getString(com.tokopedia.keys.R.string.gojek_sso_client_id),
            clientSecret = context.getString(com.tokopedia.keys.R.string.gojek_sso_client_secret),
            environment
        )

        val ssoDataBridge = SSOHostBridge.getSsoHostBridge()
        ssoDataBridge.initBridge(context, ssoHostData)
        ssoDataBridge.updateUserProfileData(context, profile)
    }

    companion object {
        const val KEY_AUTH_CODE = "auth_code"
        const val KEY_ERROR = "error"
        const val KEY_PROFILE = "profile"
    }
}
