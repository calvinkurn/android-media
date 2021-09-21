package com.tokopedia.manageaddress.ui.shoplocation

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.manageaddress.di.DaggerShopLocationComponent
import com.tokopedia.manageaddress.di.ShopLocationComponent

class ShopLocationActivity : BaseSimpleActivity(), HasComponent<ShopLocationComponent> {

    override fun getNewFragment(): Fragment? {
        return ShopLocationFragment()
    }

    override fun getComponent(): ShopLocationComponent {
        return DaggerShopLocationComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }


}