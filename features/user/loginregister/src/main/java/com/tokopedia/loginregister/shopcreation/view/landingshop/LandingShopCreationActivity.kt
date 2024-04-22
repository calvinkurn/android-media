package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationActivity
import com.tokopedia.loginregister.R as loginregisterR

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.LANDING_SHOP_CREATION
 */

class LandingShopCreationActivity : BaseShopCreationActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return LandingShopCreationFragment.createInstance(bundle)
    }

    fun switchToKycBridgeFragment() {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                loginregisterR.id.parent_view,
                KycBridgingFragment.createInstance(bundle),
                KYC_BRIDGE_FRAGMENT_TAG
            )
            .commit()
    }

    fun switchToKycStatusFragment() {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        supportFragmentManager.beginTransaction()
            .replace(
                loginregisterR.id.parent_view,
                ShopCreationKycStatusFragment.createInstance(),
                KYC_STATUS_FRAGMENT_TAG
            )
            .commit()
    }

    companion object {
        private const val KYC_BRIDGE_FRAGMENT_TAG = "KycBridgeFragmentTag"
        private const val KYC_STATUS_FRAGMENT_TAG = "KycStatusFragmentTag"
    }
}
