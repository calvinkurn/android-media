package com.tokopedia.seller.active.common.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.tokopedia.seller.active.common.di.DaggerUpdateShopActiveComponent
import com.tokopedia.seller.active.common.di.UpdateShopActiveModule
import com.tokopedia.seller.active.common.domain.usecase.UpdateShopActiveUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class UpdateShopActiveService: JobIntentService(), CoroutineScope  {

    companion object {
        private const val JOB_ID = 223194556
        fun startService(context: Context) {
            val work = Intent(context, UpdateShopActiveService::class.java)
            enqueueWork(context, UpdateShopActiveService::class.java, JOB_ID, work)
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
        launch {
            updateShopActiveUseCase.setParam()
            updateShopActiveUseCase.executeOnBackground()
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}