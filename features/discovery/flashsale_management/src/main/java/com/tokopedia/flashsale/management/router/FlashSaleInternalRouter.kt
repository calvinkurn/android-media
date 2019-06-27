package com.tokopedia.flashsale.management.router

import android.content.Context
import android.content.Intent
import com.tokopedia.flashsale.management.view.activity.CampaignActivity

/**
 * Created by hendry on 23/11/18.
 */
class FlashSaleInternalRouter {
    companion object {
        @JvmStatic
        fun getFlashSaleDashboardActivity(context: Context): Intent {
            return CampaignActivity.createIntent(context)
        }
    }
}