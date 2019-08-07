package com.tokopedia.logisticaddaddress.di.addnewaddress

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.AddEditAddressFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetFragment
import dagger.Component

/**
 * Created by fwidjaja on 2019-05-09.
 */
@AddNewAddressScope
@Component(modules = [AddNewAddressModule::class], dependencies = [BaseAppComponent::class])
interface AddNewAddressComponent {
    fun inject(pinpointMapFragment: PinpointMapFragment)
    fun inject(autoCompleteBottomSheetFragment: AutocompleteBottomSheetFragment)
    fun inject(addEditAddressFragment: AddEditAddressFragment)
    fun inject(districtRecommendationBottomSheetFragment: DistrictRecommendationBottomSheetFragment)
}