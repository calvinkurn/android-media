package com.tokopedia.product.manage.feature.list.service

import android.content.Context
import android.content.Intent
import com.example.seller.active.common.service.UpdateShopActiveBaseService
import com.tokopedia.product.manage.feature.list.di.ProductManageListInstance

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
        ProductManageListInstance.getComponent(application).inject(this)
    }
}