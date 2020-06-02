package com.tokopedia.changepassword.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.changepassword.view.fragment.ChangePasswordFragment
import com.tokopedia.managepassword.view.activity.HasPasswordActivity
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig

class ChangePasswordActivity : BaseSimpleActivity() {

    private lateinit var remoteConfig: RemoteConfig

    override fun getNewFragment(): Fragment {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return ChangePasswordFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!::remoteConfig.isInitialized) {
            remoteConfig = FirebaseRemoteConfigImpl(this)
        }

        if (isDirectToForgotPassword()) {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
            startActivity(intent)
            finish()
        }
    }

    private fun isDirectToForgotPassword(): Boolean {
        return remoteConfig.getBoolean(REMOTE_DIRECT_TO_FORGOT_PASSWORD,false)
    }

    companion object {
        private const val REMOTE_DIRECT_TO_FORGOT_PASSWORD = "android_direct_to_forgot_password"
    }
}
