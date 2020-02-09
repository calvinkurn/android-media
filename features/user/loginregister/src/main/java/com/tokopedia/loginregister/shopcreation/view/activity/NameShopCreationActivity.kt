package com.tokopedia.loginregister.shopcreation.view.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.shopcreation.view.fragment.NameShopCreationFragment

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalGlobal.NAME_SHOP_CREATION
 */

class NameShopCreationActivity : BaseShopCreationActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return NameShopCreationFragment.createInstance(bundle)
    }
}