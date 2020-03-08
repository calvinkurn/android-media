package com.tokopedia.sellerhome.settings.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.di.module.SellerHomeUseCaseModule
import com.tokopedia.sellerhome.di.module.SellerHomeViewModelModule
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.settings.view.OtherMenuFragment
import dagger.Component

@SellerHomeScope
@Component(modules = [
    SellerHomeModule::class,
    SellerHomeViewModelModule::class,
    SellerHomeUseCaseModule::class],
        dependencies = [BaseAppComponent::class])
interface OtherSettingComponent {
    fun inject(otherMenuFragment: OtherMenuFragment)
}