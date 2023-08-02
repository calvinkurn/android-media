package com.tokopedia.logisticaddaddress.di.addnewaddressrevamp

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.di.AddAddressBaseModule
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageFragment
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetRevamp
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageFragment
import dagger.Component

@ActivityScope
@Component(
    modules = [
        AddAddressBaseModule::class,
        AddNewAddressRevampViewModelModule::class

    ],
    dependencies = [BaseAppComponent::class]
)
interface AddNewAddressRevampComponent {
    fun inject(searchPageFragment: SearchPageFragment)
    fun inject(pinpointNewPageFragment: PinpointNewPageFragment)
    fun inject(addressFormFragment: AddressFormFragment)
    fun inject(discomBottomSheetRevamp: DiscomBottomSheetRevamp)
}
