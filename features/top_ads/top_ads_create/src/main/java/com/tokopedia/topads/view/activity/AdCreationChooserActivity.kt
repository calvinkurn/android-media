package com.tokopedia.topads.view.activity

import android.util.Log
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalTopAds
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.topads.common.constant.TopAdsCommonConstant
import com.tokopedia.topads.common.getSellerMigrationFeatureName
import com.tokopedia.topads.common.getSellerMigrationRedirectionApplinks
import com.tokopedia.topads.common.isFromPdpSellerMigration
import com.tokopedia.topads.create.R
import com.tokopedia.topads.di.CreateAdsComponent
import com.tokopedia.topads.di.DaggerCreateAdsComponent
import com.tokopedia.topads.view.fragment.AdCreationChooserFragment

class AdCreationChooserActivity : BaseSimpleActivity(), HasComponent<CreateAdsComponent> {

    override fun getNewFragment(): Fragment? {
        return AdCreationChooserFragment.newInstance()
    }

    override fun getComponent(): CreateAdsComponent {
        return DaggerCreateAdsComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).build()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.extras?.get(TopAdsCommonConstant.DIRECTED_FROM_MANAGE_OR_PDP) != true) {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalTopAds.TOPADS_DASHBOARD_INTERNAL).apply {
                if (isFromPdpSellerMigration(intent?.extras)) {
                    putExtra(SellerMigrationApplinkConst.QUERY_PARAM_FEATURE_NAME, getSellerMigrationFeatureName(intent?.extras))
                    putStringArrayListExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA, getSellerMigrationRedirectionApplinks(intent?.extras))
                }
            }

            startActivity(intent)
        }
        finish()
    }

    override fun setTitle(title: CharSequence?) {
        if (intent.extras?.get(TopAdsCommonConstant.DIRECTED_FROM_MANAGE_OR_PDP) != true) {
            Log.d("Naven", "For Onboarding")
            toolbar.setTitle(getString(R.string.mulai_beriklan))
        } else {
            Log.d("Naven", "For Create flow")
            toolbar.setTitle(getString(R.string.creat_ad))
        }
    }
}