package com.tokopedia.shop.score.penalty.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.score.penalty.di.module.PenaltyModule
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.presentation.activity.ShopPenaltyPageActivity
import com.tokopedia.shop.score.penalty.presentation.bottomsheet.PenaltyFilterBottomSheet
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyDetailFragment
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment
import com.tokopedia.shop.score.penalty.presentation.old.bottomsheet.PenaltyFilterBottomSheetOld
import com.tokopedia.shop.score.penalty.presentation.old.fragment.ShopPenaltyPageOldFragment
import dagger.Component

@PenaltyScope
@Component(modules = [PenaltyModule::class], dependencies = [BaseAppComponent::class])
interface PenaltyComponent {
    fun inject(shopPenaltyPageActivity: ShopPenaltyPageActivity)
    fun inject(shopPenaltyPageFragment: ShopPenaltyPageFragment)
    fun inject(shopPenaltyPageOldFragment: ShopPenaltyPageOldFragment)
    fun inject(shopPenaltyDetailFragment: ShopPenaltyDetailFragment)
    fun inject(bottomSheet: PenaltyFilterBottomSheet)
    fun inject(bottomSheet: PenaltyFilterBottomSheetOld)
}
