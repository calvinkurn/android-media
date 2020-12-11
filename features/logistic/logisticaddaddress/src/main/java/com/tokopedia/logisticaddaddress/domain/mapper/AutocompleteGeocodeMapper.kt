package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticCommon.data.response.AutoCompleteGeocodeResponse
import com.tokopedia.logisticCommon.data.response.GeoData
import com.tokopedia.logisticCommon.data.response.ResultsItem
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResultUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */
open class AutocompleteGeocodeMapper @Inject constructor() {
    private val STATUS_OK = "OK"

    fun map(response: AutoCompleteGeocodeResponse): AutocompleteGeocodeResponseUiModel {
        val status = response.keroAutocompleteGeocode.status
        var dataUiModel = AutocompleteGeocodeDataUiModel()
        if (status == STATUS_OK) {
            return AutocompleteGeocodeResponseUiModel().apply {
                data = mapData(response.keroAutocompleteGeocode.data)
            }
        }
        return AutocompleteGeocodeResponseUiModel(status, dataUiModel)
    }

    private fun mapData(data: GeoData): AutocompleteGeocodeDataUiModel {
        return AutocompleteGeocodeDataUiModel(
                results = data.results.map {
                    mapResult(it)
                }
        )
    }

    private fun mapResult(result: ResultsItem): AutocompleteGeocodeResultUiModel {
        return AutocompleteGeocodeResultUiModel(
                name = result.name,
                placeId = result.placeId,
                vicinity = result.vicinity
        )
    }
}