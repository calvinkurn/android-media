package com.tokopedia.seller.menu.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.seller.menu.di.module.SellerMenuModule
import com.tokopedia.seller.menu.di.scope.SellerMenuScope
import com.tokopedia.seller.menu.presentation.fragment.SellerMenuFragment
import dagger.Component

@SellerMenuScope
@Component(
    modules = [SellerMenuModule::class],
    dependencies = [BaseAppComponent::class]
)
interface SellerMenuComponent {

    fun inject(fragment: SellerMenuFragment)
}