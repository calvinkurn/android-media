package com.tokopedia.centralizedpromo.view.activity

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.sellermigration.SellerMigrationApplinkConst
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromoold.view.fragment.CentralizedPromoFragmentOld
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfigKey

class CentralizedPromoActivity : BaseSimpleActivity() {


    private var remoteConfig: FirebaseRemoteConfigImpl? = null

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        remoteConfig = FirebaseRemoteConfigImpl(this)
        super.onCreate(savedInstanceState)
    }

    override fun getNewFragment(): Fragment {
        val enableOldPage = remoteConfig?.getBoolean(
                RemoteConfigKey.ENABLE_OLD_IKLAN_PROMOSI_PAGE,
                false) == true

        return if (enableOldPage) {
            CentralizedPromoFragmentOld.createInstance()
        } else {
            CentralizedPromoFragment.createInstance()
        }
    }

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