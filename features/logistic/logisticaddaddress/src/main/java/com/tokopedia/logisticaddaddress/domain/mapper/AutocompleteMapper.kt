package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.Data
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.PredictionsItem
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.StructuredFormatting
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutocompleteMapper @Inject constructor() {

    fun map(response: GraphqlResponse?): AutocompleteResponseUiModel {
        var dataUiModel = AutocompleteDataUiModel()
        val responseAutocomplete: AutocompleteResponse? = response?.getData(AutocompleteResponse::class.java)
        responseAutocomplete.let { response ->
            response?.keroMapsAutocomplete?.let { keroMapsAutocomplete ->
                dataUiModel = keroMapsAutocomplete.data.let { mapData(it) }
            }
        }
        return AutocompleteResponseUiModel(dataUiModel)
    }

    fun mapLean(response: AutocompleteResponse): List<AutoCompleteResultUi> {
        val dataResponse = response.keroMapsAutocomplete.data.predictions
        return dataResponse.map {
            AutoCompleteResultUi(
                    it.types,
                    it.matchedSubstrings,
                    it.terms,
                    it.structuredFormatting,
                    it.description, it.placeId
            )
        }
    }

    private fun mapData(data: Data): AutocompleteDataUiModel {
        val listPredictions = data.predictions.map {
            mapPrediction(it)
        }

        return AutocompleteDataUiModel(listPredictions)
    }

    private fun mapPrediction(predictionsItem: PredictionsItem): AutocompletePredictionUiModel {
        var structuredFormattingUiModel = AutocompleteStructuredFormattingUiModel()
        var placeId: String
        predictionsItem.let {
            placeId = it.placeId
            structuredFormattingUiModel = mapStructuredFormatting(it.structuredFormatting)
        }
        return AutocompletePredictionUiModel(placeId, structuredFormattingUiModel)
    }

    private fun mapStructuredFormatting(structuredFormatting: StructuredFormatting): AutocompleteStructuredFormattingUiModel {
        return AutocompleteStructuredFormattingUiModel(
                mainText = structuredFormatting.mainText,
                secondaryText = structuredFormatting.secondaryText
        )
    }
}