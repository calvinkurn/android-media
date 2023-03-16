package com.tokopedia.insurance.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.insurance.InsuranceInfoBottomSheet
import dagger.Component

@ActivityScope
@Component(modules = [InsuranceInfoModule::class], dependencies = [BaseAppComponent::class])
interface InsuranceInfoComponent {
    fun inject(bottomSheet: InsuranceInfoBottomSheet)
}
