package com.tokopedia.logisticaddaddress.di.addnewaddressrevamp

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageFragment
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.search.SearchPageFragment
import dagger.Component

@AddNewAddressRevampScope
@Component(modules = [AddNewAddressRevampModule::class, AddNewAddressRevampViewModelModule::class], dependencies = [BaseAppComponent::class])
interface AddNewAddressRevampComponent {
    fun inject(searchPageActivity: SearchPageActivity)
    fun inject(searchPageFragment: SearchPageFragment)
    fun inject(pinpointNewPageFragment: PinpointNewPageFragment)
}