package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel

/**
 * Created by fwidjaja on 2019-05-14.
 */
interface AutocompleteBottomSheetListener : CustomerView {
    fun hideListPointOfInterest()
    fun onSuccessGetAutocompleteGeocode(dataUiModel: AutocompleteGeocodeDataUiModel)
    fun onSuccessGetAutocomplete(dataUiModel: AutocompleteDataUiModel)
}