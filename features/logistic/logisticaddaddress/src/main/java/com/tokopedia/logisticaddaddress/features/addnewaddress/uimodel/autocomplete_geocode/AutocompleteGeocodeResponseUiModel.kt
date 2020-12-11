package com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode

/**
 * Created by fwidjaja on 2019-05-14.
 */
data class AutocompleteGeocodeResponseUiModel (
        var status: String = "",
        var data: AutocompleteGeocodeDataUiModel = AutocompleteGeocodeDataUiModel())

data class AutocompleteGeocodeDataUiModel (
        var results: List<AutocompleteGeocodeResultUiModel> = emptyList())

data class AutocompleteGeocodeResultUiModel (
        var name: String = "",
        var placeId: String = "",
        var vicinity: String = "")