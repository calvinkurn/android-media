package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.AutocompleteGeocodeResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.Data
import com.tokopedia.logisticaddaddress.domain.model.autocomplete_geocode.ResultsItem
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutoCompleteGeocodeResultUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutoCompleteGeocodeDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutoCompleteGeocodeResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-14.
 */
open class AutoCompleteGeocodeMapper @Inject constructor() {
    private val STATUS_OK = "OK"
    private val STATUS_ERROR = "ERROR"

    fun map(response: GraphqlResponse?) : AutoCompleteGeocodeResponseUiModel {
        var status = ""
        var dataUiModel = AutoCompleteGeocodeDataUiModel()
        val responseAutoComplete: AutocompleteGeocodeResponse? = response?.getData(AutocompleteGeocodeResponse::class.java)
        responseAutoComplete.let { responseAutoComplete ->
            responseAutoComplete?.keroAutocompleteGeocode.let {
                status = it?.status ?: STATUS_ERROR
                when (it?.status) {
                    STATUS_OK -> {
                        dataUiModel = mapData(it.data)
                    }
                }
            }
        }

        return AutoCompleteGeocodeResponseUiModel(status, dataUiModel)
    }

    private fun mapData(data: Data) : AutoCompleteGeocodeDataUiModel {
        return AutoCompleteGeocodeDataUiModel(
                results = data.results.map {
                    mapResult(it)
                }
        )
    }

    private fun mapResult(result: ResultsItem) : AutoCompleteGeocodeResultUiModel {
        return AutoCompleteGeocodeResultUiModel(
                name = result.name,
                placeId = result.placeId,
                vicinity = result.vicinity
        )
    }
}