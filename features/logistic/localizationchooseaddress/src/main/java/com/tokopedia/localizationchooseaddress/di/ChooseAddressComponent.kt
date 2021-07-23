package com.tokopedia.localizationchooseaddress.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import dagger.Component

@ActivityScope
@Component(modules = [ChooseAddressModule::class, ChooseAddressViewModelModule::class], dependencies = [BaseAppComponent::class])
interface ChooseAddressComponent  {
    fun inject(chooseAddressWidget: ChooseAddressWidget)
    fun inject(fragment: ChooseAddressBottomSheet)
}