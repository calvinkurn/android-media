package com.tokopedia.loginregister.shopcreation.view.activity

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.tokopedia.loginregister.R
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

    override fun setupLayout(savedInstanceState: Bundle?) {
        super.setupLayout(savedInstanceState)

        val baseView = findViewById<View>(R.id.base_view)
        baseView.background = ContextCompat.getDrawable(baseContext, R.drawable.bg_landing_shop_creation)
    }
}