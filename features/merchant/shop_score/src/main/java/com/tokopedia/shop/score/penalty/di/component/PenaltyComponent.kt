package com.tokopedia.shop.score.penalty.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.score.penalty.di.module.PenaltyModule
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyDetailFragment
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import dagger.Component

@PenaltyScope
@Component(modules = [PenaltyModule::class], dependencies = [BaseAppComponent::class])
interface PenaltyComponent {
    fun inject(shopPenaltyPageFragment: ShopPenaltyPageFragment)
    fun inject(shopPenaltyDetailFragment: ShopPenaltyDetailFragment)
    fun inject(bottomSheet: PenaltyFilterBottomSheet)
}