package com.tokopedia.shop.newinfo.di.component

import com.tokopedia.shop.common.di.component.ShopComponent
import com.tokopedia.shop.newinfo.di.module.ShopNewInfoModule
import com.tokopedia.shop.newinfo.di.scope.ShopNewInfoScope
import com.tokopedia.shop.newinfo.view.fragment.ShopNewInfoFragment
import dagger.Component

@ShopNewInfoScope
@Component(dependencies = [ShopComponent::class], modules = [ShopNewInfoModule::class])
interface ShopNewInfoComponent {
    fun inject(fragment: ShopNewInfoFragment)
}