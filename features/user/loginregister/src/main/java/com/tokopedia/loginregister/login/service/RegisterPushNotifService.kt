package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import com.crashlytics.android.Crashlytics
import com.tokopedia.loginfingerprint.utils.crypto.Cryptography
import com.tokopedia.loginregister.BuildConfig
import com.tokopedia.loginregister.common.SignaturePref
import com.tokopedia.loginregister.login.di.LoginComponentBuilder
import com.tokopedia.loginregister.login.domain.RegisterPushNotifUseCase
import com.tokopedia.loginregister.login.domain.pojo.RegisterPushNotifData
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * Created by Ade Fulki on 28/09/20.
 */

class RegisterPushNotifService : JobIntentService() {

    @field:Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var registerPushNotifUseCase: RegisterPushNotifUseCase

    @RequiresApi(Build.VERSION_CODES.M)
    @Nullable
    @Inject
    lateinit var cryptography: Cryptography

    @Inject
    lateinit var signaturePref: SignaturePref

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    override fun onHandleWork(intent: Intent) {
        try {
            if(cryptography.isInitialized()) {
                val signature = cryptography.generateRegisterPushNotifSignature(userId = userSession.userId, deviceId = userSession.deviceId)
                signature.let { it ->
                    signaturePref.signature = it.signature
                    registerPushNotifUseCase.executeCoroutines(
                            cryptography.getPublicKey(),
                            it.signature,
                            it.datetime,
                            {
                                it.success
                            },
                            {
                                it.printStackTrace()
                            })
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            logExceptionToCrashlytic(e)
        }
    }

    private fun initInjector() {
        application?.let {
            LoginComponentBuilder.getComponent(it).inject(this)
        }
    }

    companion object {

        const val JOB_ID = 34578

        fun startService(context: Context) {
            try {
                val intent = Intent(context, RegisterPushNotifService::class.java)
                enqueueWork(context, RegisterPushNotifService::class.java, JOB_ID, intent)
            } catch (e: Exception) {
                e.printStackTrace()
                logExceptionToCrashlytic(e)
            }
        }

        private fun logExceptionToCrashlytic(exception: Exception) {
            try {
                if (!BuildConfig.DEBUG) {
                    Crashlytics.logException(exception)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}