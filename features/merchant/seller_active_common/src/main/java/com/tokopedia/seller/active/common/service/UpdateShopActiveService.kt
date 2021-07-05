package com.tokopedia.seller.active.common.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.seller.active.common.di.DaggerUpdateShopActiveComponent
import com.tokopedia.seller.active.common.di.UpdateShopActiveModule
import com.tokopedia.seller.active.common.domain.usecase.UpdateShopActiveUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UpdateShopActiveService: JobIntentService(), CoroutineScope  {

    companion object {
        private const val JOB_ID = 223194556
        fun startService(context: Context) {
            try {
                val work = Intent(context, UpdateShopActiveService::class.java)
                enqueueWork(context, UpdateShopActiveService::class.java, JOB_ID, work)
            } catch (e: Exception) {
                e.printStackTrace()
                logExceptionToCrashlytics(e.fillInStackTrace())
            }
        }

        private fun logExceptionToCrashlytics(t: Throwable) {
            try {
                FirebaseCrashlytics.getInstance().recordException(t)
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
        }
    }

    @Inject
    lateinit var updateShopActiveUseCase: UpdateShopActiveUseCase

    override fun onCreate() {
        super.onCreate()
        DaggerUpdateShopActiveComponent.builder()
                .updateShopActiveModule(UpdateShopActiveModule())
                .build()
                .inject(this)
    }

    override fun onHandleWork(intent: Intent) {
        launchCatchError(block = {
            updateShopActiveUseCase.setParam()
            updateShopActiveUseCase.executeOnBackground()
        }) {
            it.printStackTrace()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}