package com.tokopedia.loginregister.di.modules

import android.content.Context
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.const.LoginConstants.RollenceKey.DIRECT_LOGIN_BIOMETRIC
import com.tokopedia.loginregister.login.di.LoginModule
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.constants.SessionConstants.FirebaseConfig.CONFIG_LOGIN_ENCRYPTION
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk

object FakeLoginModule : LoginModule() {

    override fun provideAbTestPlatform(): AbTestPlatform {
        return mockk {
            every { fetchByType(any()) } just Runs
            every { getString(LoginEmailPhoneFragment.ROLLENCE_KEY_GOTO_SEAMLESS, any()) } returns "true"
            every { getString(LoginConstants.RollenceKey.LOGIN_PAGE_BIOMETRIC, any()) } returns "true"
            every { getString(DIRECT_LOGIN_BIOMETRIC, any()) } returns ""
        }
    }

    override fun provideFirebaseRemoteConfig(context: Context): RemoteConfig {
        return object : RemoteConfig {
            override fun getKeysByPrefix(prefix: String?): MutableSet<String> {
                TODO("Not yet implemented")
            }

            override fun getBoolean(key: String?): Boolean {
                return getBoolean(key, false)
            }

            override fun getBoolean(key: String?, defaultValue: Boolean): Boolean {
                return when (key) {
                    CONFIG_LOGIN_ENCRYPTION -> true
                    else -> false
                }
            }

            override fun getDouble(key: String?): Double {
                TODO("Not yet implemented")
            }

            override fun getDouble(key: String?, defaultValue: Double): Double {
                TODO("Not yet implemented")
            }

            override fun getLong(key: String?): Long {
                TODO("Not yet implemented")
            }

            override fun getLong(key: String?, defaultValue: Long): Long {
                TODO("Not yet implemented")
            }

            override fun getString(key: String?): String {
                TODO("Not yet implemented")
            }

            override fun getString(key: String?, defaultValue: String?): String {
                TODO("Not yet implemented")
            }

            override fun setString(key: String?, value: String?) {
                TODO("Not yet implemented")
            }

            override fun fetch(listener: RemoteConfig.Listener?) {
                TODO("Not yet implemented")
            }
        }
    }
}
