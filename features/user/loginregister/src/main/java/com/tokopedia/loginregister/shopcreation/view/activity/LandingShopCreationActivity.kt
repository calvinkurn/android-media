package com.tokopedia.loginregister.shopcreation.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.shopcreation.view.fragment.LandingShopCreationFragment

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal.LANDING_SHOP_CREATION
 */

class LandingShopCreationActivity : BaseShopCreationActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return LandingShopCreationFragment.createInstance(bundle)
    }
}