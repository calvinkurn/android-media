package com.tokopedia.logisticaddaddress.di.addnewaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.MapFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutoCompleteGeocodeBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutoCompleteGeocodeBottomSheetPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district.GetDistrictBottomSheetFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-05-09.
 */
@AddNewAddressScope
@Component(modules = [AddNewAddressModule::class], dependencies = [BaseAppComponent::class])
interface AddNewAddressComponent {
    fun inject(addNewAddressFragment: MapFragment)
    fun inject(autoCompleteGeocodeBottomSheetFragment: AutoCompleteGeocodeBottomSheetFragment)
    fun inject(getDistrictBottomSheetFragment: GetDistrictBottomSheetFragment)
}