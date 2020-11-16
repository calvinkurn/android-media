package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticdata.domain.model.Place

/**
 * Created by fwidjaja on 2019-05-14.
 */
interface AutocompleteBottomSheetListener : CustomerView {
    fun hideListPointOfInterest()
    fun onSuccessGetAutocompleteGeocode(dataUiModel: AutocompleteGeocodeDataUiModel)
    fun onSuccessGetAutocomplete(suggestedPlaces: Place)
    fun goToAddNewAddressNegative()
}