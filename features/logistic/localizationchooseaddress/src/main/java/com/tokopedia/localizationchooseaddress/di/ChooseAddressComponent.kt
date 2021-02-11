package com.tokopedia.localizationchooseaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.localizationchooseaddress.ui.ChooseAddressBottomSheet
import dagger.Component

@ChooseAddressScope
@Component(modules = [ChooseAddressModule::class, ChooseAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ChooseAddressComponent  {
    fun inject(fragment: ChooseAddressBottomSheet)
}