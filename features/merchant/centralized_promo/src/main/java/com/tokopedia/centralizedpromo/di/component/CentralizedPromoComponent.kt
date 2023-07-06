package com.tokopedia.centralizedpromo.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.centralizedpromo.di.module.CentralizedPromoModule
import com.tokopedia.centralizedpromo.di.module.CentralizedPromoViewModelModule
import com.tokopedia.centralizedpromo.di.scope.CentralizedPromoScope
import com.tokopedia.centralizedpromo.view.fragment.CentralizedPromoFragment
import com.tokopedia.centralizedpromo.view.fragment.FirstTimePromoBottomSheetFragment
import dagger.Component

@CentralizedPromoScope
@Component(
    modules = [
        CentralizedPromoModule::class,
        CentralizedPromoViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface CentralizedPromoComponent {

    fun inject(sellerHomeFragment: CentralizedPromoFragment)
    fun inject(firstTimePromoBottomSheetFragment: FirstTimePromoBottomSheetFragment)

}
