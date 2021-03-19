package com.tokopedia.shop.score.penalty.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.score.penalty.di.scope.PenaltyScope
import com.tokopedia.shop.score.penalty.presentation.fragment.PenaltyPageFragment
import dagger.Component

@PenaltyScope
@Component(modules = [PenaltyComponent::class], dependencies = [BaseAppComponent::class])
interface PenaltyComponent {
    fun inject(penaltyPageFragment: PenaltyPageFragment)
}