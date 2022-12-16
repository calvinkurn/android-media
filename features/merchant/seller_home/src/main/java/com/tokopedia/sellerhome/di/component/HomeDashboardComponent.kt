package com.tokopedia.sellerhome.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.sellerhome.di.module.SellerHomeModule
import com.tokopedia.sellerhome.di.module.SellerHomeUseCaseModule
import com.tokopedia.sellerhome.di.module.SellerHomeViewModelModule
import com.tokopedia.sellerhome.di.module.SellerHomeWearModule
import com.tokopedia.sellerhome.di.scope.SellerHomeScope
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity
import com.tokopedia.sellerhome.view.fragment.SellerHomeFragment
import dagger.Component

/**
 * Created by @ilhamsuaib on 06/12/21.
 */

@SellerHomeScope
@Component(
    modules = [
        SellerHomeModule::class,
        SellerHomeUseCaseModule::class,
        SellerHomeViewModelModule::class,
        SellerHomeWearModule::class,
    ],
    dependencies = [BaseAppComponent::class]
)
interface HomeDashboardComponent {

    fun inject(sellerHomeActivity: SellerHomeActivity)

    fun inject(sellerHomeFragment: SellerHomeFragment)
}