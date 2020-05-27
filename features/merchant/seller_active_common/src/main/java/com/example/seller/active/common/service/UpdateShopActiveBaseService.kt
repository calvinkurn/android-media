package com.example.seller.active.common.service

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import com.example.seller.active.common.domain.usecase.UpdateShopActiveUseCase
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class UpdateShopActiveBaseService: JobIntentService(), CoroutineScope  {

    companion object {
        const val EXTRA_DEVICE = "extra_device"
    }

    @Inject
    lateinit var updateShopActiveUseCase: UpdateShopActiveUseCase

    override fun onHandleWork(intent: Intent) {
        val device = intent.getStringExtra(EXTRA_DEVICE) ?: ""
        launchCatchError(block = {
            updateShopActiveUseCase.setParam(device)
            val updateShopActiveUseCase = updateShopActiveUseCase.executeOnBackground()
            Log.d("Update Shop Active", device)
            Log.d("Update Shop Active", updateShopActiveUseCase.updateShopActive.success.toString())
            Log.d("Update Shop Active", updateShopActiveUseCase.updateShopActive.message)
        }, onError = {
            it.printStackTrace()
        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO
}