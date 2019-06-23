package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.AutocompleteGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.Data
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.ResultsItem
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResultUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */
open class AutocompleteGeocodeMapper @Inject constructor() {
    private val STATUS_OK = "OK"

    fun map(response: GraphqlResponse?): AutocompleteGeocodeResponseUiModel {
        var status = ""
        var dataUiModel = AutocompleteGeocodeDataUiModel()
        val responseAutoComplete: AutocompleteGeocodeResponse? = response?.getData(AutocompleteGeocodeResponse::class.java)
        responseAutoComplete?.let { it ->
            status = it.keroAutocompleteGeocode.status
            when (status) {
                STATUS_OK -> dataUiModel = mapData(it.keroAutocompleteGeocode.data)
            }
        }

        return AutocompleteGeocodeResponseUiModel(status, dataUiModel)
    }

    private fun mapData(data: Data): AutocompleteGeocodeDataUiModel {
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