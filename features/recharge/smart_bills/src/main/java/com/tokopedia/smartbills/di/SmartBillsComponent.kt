package com.tokopedia.smartbills.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.smartbills.presentation.fragment.SmartBillsFragment
import dagger.Component

@SmartBillsScope
@Component(modules = [SmartBillsModule::class, SmartBillsViewModelModule::class], dependencies = [BaseAppComponent::class])
interface SmartBillsComponent {

    fun inject(smartBillsFragment: SmartBillsFragment)

}