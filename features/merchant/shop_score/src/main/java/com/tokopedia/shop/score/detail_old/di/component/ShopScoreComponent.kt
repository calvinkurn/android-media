package com.tokopedia.shop.score.detail_old.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shop.score.detail_old.di.module.ShopScoreModule
import com.tokopedia.shop.score.detail_old.di.module.ViewModelModule
import com.tokopedia.shop.score.detail_old.di.scope.ShopScoreScope
import com.tokopedia.shop.score.detail_old.view.fragment.ShopScoreDetailFragment
import dagger.Component

@ShopScoreScope
@Component(
    modules = [
        ShopScoreModule::class,
        ViewModelModule::class
    ],
    dependencies = [BaseAppComponent::class]
)
interface ShopScoreComponent {

    fun inject(fragment: ShopScoreDetailFragment)
}