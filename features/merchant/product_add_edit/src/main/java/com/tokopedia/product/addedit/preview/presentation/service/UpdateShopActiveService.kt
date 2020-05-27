package com.tokopedia.product.addedit.preview.presentation.service

import android.content.Context
import android.content.Intent
import com.example.seller.active.common.service.UpdateShopActiveBaseService
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.product.addedit.common.AddEditProductComponentBuilder
import com.tokopedia.product.addedit.preview.di.AddEditProductPreviewModule
import com.tokopedia.product.addedit.preview.di.DaggerAddEditProductPreviewComponent

class UpdateShopActiveService: UpdateShopActiveBaseService() {

    companion object {
        private const val JOB_ID = 223194556

        fun startService(context: Context, device: String) {
            val work = Intent(context, UpdateShopActiveService::class.java).apply { putExtra(EXTRA_DEVICE, device) }
            enqueueWork(context, UpdateShopActiveService::class.java, JOB_ID, work)
        }
    }

    override fun onCreate() {
        super.onCreate()
        initInjector()
    }

    private fun initInjector() {
        val baseMainApplication = applicationContext as BaseMainApplication
        DaggerAddEditProductPreviewComponent.builder()
                .addEditProductComponent(AddEditProductComponentBuilder.getComponent(baseMainApplication))
                .addEditProductPreviewModule(AddEditProductPreviewModule())
                .build()
                .inject(this)
    }
}