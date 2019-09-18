package com.tokopedia.shop.search.view.activity

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.ShopComponentInstance
import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.search.view.fragment.ShopSearchProductFragment

class ShopSearchProductActivity : BaseSimpleActivity(), HasComponent<ShopComponent> {
    override fun getNewFragment() = ShopSearchProductFragment.createInstance()

    private var component: ShopComponent? = null

    override fun getComponent(): ShopComponent {
        if (component == null) {
            component = ShopComponentInstance.getComponent(application)
        }
        return component!!
    }
}