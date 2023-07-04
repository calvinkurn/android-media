package com.tokopedia.loginregister.shopcreation.view.phoneshop

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationActivity

/**
 * Created by Ade Fulki on 2019-12-18.
 * ade.hadian@tokopedia.com
 * For navigating to this class
 * @see com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION
 */

class PhoneShopCreationActivity : BaseShopCreationActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        if (intent.extras != null) {
            bundle.putAll(intent.extras)
        }
        return PhoneShopCreationFragment.createInstance(bundle)
    }
}
