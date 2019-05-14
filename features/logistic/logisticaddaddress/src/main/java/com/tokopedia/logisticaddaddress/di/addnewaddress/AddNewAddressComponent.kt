package com.tokopedia.logisticaddaddress.di.addnewaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.MapFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-05-09.
 */
@AddNewAddressScope
@Component(modules = [AddNewAddressModule::class], dependencies = [BaseAppComponent::class])
interface AddNewAddressComponent {
    fun inject(addNewAddressFragment: MapFragment)
}