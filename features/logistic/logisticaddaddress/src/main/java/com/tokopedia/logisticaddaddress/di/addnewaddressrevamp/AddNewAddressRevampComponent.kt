package com.tokopedia.logisticaddaddress.di.addnewaddressrevamp

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.abstraction.common.di.scope.ActivityScope
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.AddressFormFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageFragment
import dagger.Component

@ActivityScope
@Component(modules = [AddNewAddressRevampModule::class, AddNewAddressRevampViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AddNewAddressRevampComponent {
    fun inject(searchPageActivity: SearchPageActivity)
    fun inject(searchPageFragment: SearchPageFragment)
    fun inject (pinpointNewPageActivity: PinpointNewPageActivity)
    fun inject(pinpointNewPageFragment: PinpointNewPageFragment)
    fun inject(addressFormActivity: AddressFormActivity)
    fun inject(addressFormFragment: AddressFormFragment)
}