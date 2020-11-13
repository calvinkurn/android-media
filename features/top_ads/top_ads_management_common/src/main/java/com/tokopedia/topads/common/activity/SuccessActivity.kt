package com.tokopedia.topads.common.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.topads.common.fragment.OnSuccessFragment
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration

const val EXTRA_TITLE = "title"
const val EXTRA_SUBTITLE = "subTitle"
const val EXTRA_BUTTON = "button"
class SuccessActivity : BaseSimpleActivity() {
    override fun getNewFragment(): Fragment? {
        return OnSuccessFragment.newInstance(Bundle().apply {
            intent?.let {
                putString(EXTRA_TITLE, it.getStringExtra(EXTRA_TITLE))
                putString(EXTRA_SUBTITLE, it.getStringExtra(EXTRA_SUBTITLE))
                putString(EXTRA_BUTTON, it.getStringExtra(EXTRA_BUTTON))
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
            if (isFromPdpSellerMigration(intent?.extras)) {
                putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(intent?.extras))
                putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(intent?.extras))
            }
        }
        startActivity(intent)
        finish()
    }
}
