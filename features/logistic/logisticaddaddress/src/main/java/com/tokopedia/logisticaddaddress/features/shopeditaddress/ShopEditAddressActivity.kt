package com.tokopedia.logisticaddaddress.features.shopeditaddress

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.logisticaddaddress.di.shopeditaddress.DaggerShopEditAddressComponent
import com.tokopedia.logisticaddaddress.di.shopeditaddress.ShopEditAddressComponent

class ShopEditAddressActivity : BaseSimpleActivity(), HasComponent<ShopEditAddressComponent> {

    override fun getNewFragment(): Fragment? {
        return ShopEditAddressFragment()
    }

    override fun getComponent(): ShopEditAddressComponent {
        return DaggerShopEditAddressComponent.builder()
                .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                .build()
    }

}