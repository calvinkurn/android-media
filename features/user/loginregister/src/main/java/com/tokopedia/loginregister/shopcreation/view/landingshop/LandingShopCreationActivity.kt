package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationActivity

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
                com.tokopedia.loginregister.R.id.parent_view,
                KycBridgingFragment.createInstance(bundle),
                "kyc_bridge_tag"
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
                com.tokopedia.loginregister.R.id.parent_view,
                ShopCreationKycStatusFragment.createInstance(),
                "kyc_bridge_tag_2"
            )
            .commit()
    }
}
