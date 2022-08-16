package com.tokopedia.tkpd.flashsale.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.common.di.ShopCommonModule
import com.tokopedia.tkpd.flashsale.di.module.TokopediaFlashSaleModule
import com.tokopedia.tkpd.flashsale.di.module.TokopediaFlashSaleViewModelModule
import com.tokopedia.tkpd.flashsale.di.scope.TokopediaFlashSaleScope
import com.tokopedia.tkpd.flashsale.presentation.list.child.FlashSaleListFragment
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleListActivity
import com.tokopedia.tkpd.flashsale.presentation.list.container.FlashSaleContainerFragment
import dagger.Component

@TokopediaFlashSaleScope
@Component(
    modules = [TokopediaFlashSaleModule::class, TokopediaFlashSaleViewModelModule::class, ShopCommonModule::class],
    dependencies = [BaseAppComponent::class]
)
interface TokopediaFlashSaleComponent {
    fun inject(activity: FlashSaleListActivity)
    fun inject(fragment: FlashSaleContainerFragment)
    fun inject(fragment: FlashSaleListFragment)
}