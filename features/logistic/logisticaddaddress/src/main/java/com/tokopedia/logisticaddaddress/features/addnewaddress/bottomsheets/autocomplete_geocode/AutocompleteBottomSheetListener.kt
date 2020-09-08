package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace

/**
 * Created by fwidjaja on 2019-05-14.
 */
interface AutocompleteBottomSheetListener : CustomerView {
    fun hideListPointOfInterest()
    fun onSuccessGetAutocompleteGeocode(dataUiModel: AutocompleteGeocodeDataUiModel)
    fun onSuccessGetAutocomplete(suggestedPlaces: List<SuggestedPlace>)
    fun goToAddNewAddressNegative()
}