package com.tokopedia.shop.score.performance.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.score.performance.di.module.ShopPerformanceModule
import com.tokopedia.shop.score.performance.di.scope.ShopPerformanceScope
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetPerformanceDetail
import com.tokopedia.shop.score.performance.presentation.bottomsheet.BottomSheetShopTooltipLevel
import com.tokopedia.shop.score.performance.presentation.fragment.ShopPerformancePageFragment
import dagger.Component

@ShopPerformanceScope
@Component(modules = [ShopPerformanceModule::class], dependencies = [BaseAppComponent::class])
interface ShopPerformanceComponent {
    fun inject(fragment: ShopPerformancePageFragment)
    fun inject(bottomSheetPerformanceDetail: BottomSheetPerformanceDetail)
    fun inject(bottomSheetShopTooltipLevel: BottomSheetShopTooltipLevel)
}