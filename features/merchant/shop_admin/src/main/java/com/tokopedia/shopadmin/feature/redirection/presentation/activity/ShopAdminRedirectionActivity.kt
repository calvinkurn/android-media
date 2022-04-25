package com.tokopedia.shopadmin.feature.redirection.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shopadmin.feature.redirection.di.component.DaggerShopAdminRedirectionComponent
import com.tokopedia.shopadmin.feature.redirection.di.component.ShopAdminRedirectionComponent
import com.tokopedia.shopadmin.feature.redirection.presentation.fragment.ShopAdminRedirectionFragment

class ShopAdminRedirectionActivity : BaseSimpleActivity(),
    HasComponent<ShopAdminRedirectionComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    override fun getNewFragment(): Fragment = ShopAdminRedirectionFragment.newInstance()

    override fun getComponent(): ShopAdminRedirectionComponent {
        return DaggerShopAdminRedirectionComponent
            .builder()
            .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
            .build()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }
}