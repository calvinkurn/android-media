package com.tokopedia.loginregister.login.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.tokopedia.loginregister.login.di.LoginComponentBuilder
import com.tokopedia.loginregister.login.domain.GetDefaultChosenAddressUseCase
import javax.inject.Inject

class GetDefaultChosenAddressService : JobIntentService() {

    @Inject
    lateinit var getDefaultChosenAddressUseCase: GetDefaultChosenAddressUseCase

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        application?.let {
            LoginComponentBuilder.getComponent(it).inject(this)
        }
    }

    override fun onHandleWork(intent: Intent) {
        try {
            getDefaultChosenAddressUseCase.getDefaultChosenAddress( {
                //no-op
            }, {
                it.printStackTrace()
            } )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val JOB_ID = 998877

        fun startService(context: Context) {
            try {
                val intent = Intent(context, GetDefaultChosenAddressService::class.java)
                enqueueWork(context, GetDefaultChosenAddressService::class.java, JOB_ID, intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}