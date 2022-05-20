package com.tokopedia.shopadmin.feature.redirection.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.shopadmin.common.utils.ShopAdminComponentInstance
import com.tokopedia.shopadmin.feature.redirection.di.component.DaggerShopAdminRedirectionComponent
import com.tokopedia.shopadmin.feature.redirection.di.component.ShopAdminRedirectionComponent
import com.tokopedia.shopadmin.feature.redirection.presentation.fragment.ShopAdminRedirectionFragment

class ShopAdminRedirectionActivity : BaseSimpleActivity(),
    HasComponent<ShopAdminRedirectionComponent> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideToolbar()
    }

    override fun getNewFragment(): Fragment {
        val bundle: Bundle? = intent?.let {
            Bundle().apply {
                val paramName = it.getStringExtra(ShopAdminDeepLinkMapper.FROM_PARAM).orEmpty()
                putString(ShopAdminDeepLinkMapper.FROM_PARAM, paramName)
            }
        }
        return ShopAdminRedirectionFragment.newInstance(bundle)
    }

    override fun getComponent(): ShopAdminRedirectionComponent {
        return DaggerShopAdminRedirectionComponent
            .builder()
            .shopAdminComponent(ShopAdminComponentInstance.getShopAdminComponent(application))
            .build()
    }

    private fun hideToolbar() {
        supportActionBar?.hide()
    }
}