package com.tokopedia.promousage.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.promousage.view.bottomsheet.PromoUsageBottomSheet
import dagger.Component

@ActivityScope
@Component(
    dependencies = [BaseAppComponent::class],
    modules = [PromoUsageViewModelModule::class, PromoUsageModule::class]
)
interface PromoUsageComponent {
    fun inject(bottomSheet: PromoUsageBottomSheet)
}
