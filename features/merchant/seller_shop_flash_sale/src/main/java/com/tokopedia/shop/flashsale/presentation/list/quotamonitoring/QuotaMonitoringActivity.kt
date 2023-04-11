package com.tokopedia.shop.flashsale.presentation.list.quotamonitoring

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.seller_shop_flash_sale.R

class QuotaMonitoringActivity : BaseSimpleActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, QuotaMonitoringActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun getLayoutRes() = R.layout.ssfs_activity_quota_monitoring
    override fun getNewFragment() = QuotaMonitoringFragment.newInstance()
    override fun getParentViewResourceID() = R.id.container

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ssfs_activity_quota_monitoring)
    }
}