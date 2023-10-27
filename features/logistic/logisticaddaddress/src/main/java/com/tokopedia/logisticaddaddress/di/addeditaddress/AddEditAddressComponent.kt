package com.tokopedia.logisticaddaddress.di.addeditaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.di.AddAddressBaseModule
import com.tokopedia.logisticaddaddress.features.addeditaddress.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.features.addeditaddress.search.SearchPageFragment
import com.tokopedia.logisticaddaddress.features.pinpoint.PinpointFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        AddAddressBaseModule::class,
        AddEditAddressViewModelModule::class

    ],
    dependencies = [BaseAppComponent::class]
)
interface AddEditAddressComponent {
    fun inject(searchPageFragment: SearchPageFragment)
    fun inject(addressFormFragment: AddressFormFragment)
    fun inject(pinpointFragment: PinpointFragment)
}
