package com.tokopedia.sellerhome.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.centralized_promo.view.fragment.CentralizedPromoFragment
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.di.module.SellerHomeUseCaseModule
import com.tokopedia.sellerhome.di.module.SellerHomeViewModelModule
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.settings.view.fragment.MenuSettingFragment
import com.tokopedia.sellerhome.settings.view.fragment.OtherMenuFragment
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import dagger.Component

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

@SellerHomeScope
@Component(
        modules = [
            SellerHomeModule::class,
            SellerHomeUseCaseModule::class,
            SellerHomeViewModelModule::class
        ],
        dependencies = [BaseAppComponent::class]
)
interface SellerHomeComponent {

    fun inject(sellerHomeFragment: SellerHomeFragment)
    fun inject(sellerHomeFragment: MenuSettingFragment)
    fun inject(sellerHomeFragment: OtherMenuFragment)
    fun inject(sellerHomeFragment: CentralizedPromoFragment)
}