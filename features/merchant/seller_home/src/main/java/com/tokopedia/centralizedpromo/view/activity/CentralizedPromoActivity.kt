package com.tokopedia.centralizedpromo.view.activity

import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.centralizedpromoold.view.fragment.CentralizedPromoFragmentOld

class CentralizedPromoActivity : BaseSimpleActivity() {

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    //TODO change with remote config
    override fun getNewFragment(): Fragment = CentralizedPromoFragmentOld.createInstance()

    private fun handleIntent(intent: Intent?) {
        intent?.run {
            getStringExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)?.let { sellerMigrationNextDestinatonApplink ->
                RouteManager.getIntent(this@CentralizedPromoActivity, sellerMigrationNextDestinatonApplink).run {
                    addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(this)
                }
                removeExtra(SellerMigrationApplinkConst.SELLER_MIGRATION_APPLINKS_EXTRA)
                return@run
            }
        }
    }

}