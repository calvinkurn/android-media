package com.tokopedia.topads.common.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.topads.common.fragment.OnSuccessFragment

class SuccessActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OnSuccessFragment.newInstance()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
            if (isFromPdpSellerMigration()) {
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName())
                putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks())
            }
        }
        startActivity(intent)
        finish()
    }

    private fun getSellerMigrationRedirectionApplinks(): ArrayList<String> {
        return ArrayList(intent?.getStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA).orEmpty())
    }

    private fun getSellerMigrationFeatureName(): String {
        return intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).orEmpty()
    }

    private fun isFromPdpSellerMigration(): Boolean {
        return !intent?.getStringExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME).isNullOrEmpty()
    }
}
