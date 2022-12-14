package com.tokopedia.insurance.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.insurance.InsuranceInfoBottomSheet
import dagger.Component

@InsuranceInfoScope
@Component(modules = [InsuranceInfoModule::class], dependencies = [BaseAppComponent::class])
interface InsuranceInfoComponent {
    fun inject(bottomSheet: InsuranceInfoBottomSheet)
}
