package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.config.GlobalConfig
import com.tokopedia.devicefingerprint.datavisor.instance.VisorFingerprintInstance
import com.tokopedia.kyc_centralized.util.KycSharedPreference
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

@ActivityScope
class GotoKycInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val kycSharedPreference: KycSharedPreference,
    private val userSessionInterface: UserSessionInterface
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()
        requestBuilder.apply {
            addHeader(KEY_X_PROJECT_ID, kycSharedPreference.getProjectId())
            addHeader(KEY_X_TKPD_APP_NAME, GlobalConfig.getPackageApplicationName())
            addHeader(KEY_X_DEVICE, "$VALUE_ANDROID-${GlobalConfig.VERSION_NAME}")
            addHeader(KEY_ACCOUNTS_AUTHORIZATION, "$VALUE_BEARER ${userSessionInterface.accessToken}")
            addHeader(KEY_X_DATA_VISOR, VisorFingerprintInstance.getDVToken(context = context))
            addHeader(KEY_X_USER_LOCALE, VALUE_USER_LOCALE)
        }
        return chain.proceed(requestBuilder.build())
    }

    companion object {
        private const val VALUE_ANDROID = "android"
        private const val VALUE_BEARER = "Bearer"
        private const val VALUE_USER_LOCALE = "id_ID"
        private const val KEY_X_TKPD_APP_NAME = "X-Tkpd-App-Name"
        private const val KEY_ACCOUNTS_AUTHORIZATION = "Accounts-Authorization"
        private const val KEY_X_DEVICE = "X-Device"
        private const val KEY_X_PROJECT_ID = "x-project-id"
        private const val KEY_X_DATA_VISOR = "X-Datavisor"
        private const val KEY_X_USER_LOCALE = "x-user-locale"
    }
}
