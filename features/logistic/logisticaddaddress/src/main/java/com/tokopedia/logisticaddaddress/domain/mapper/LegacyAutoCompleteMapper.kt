package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.Data
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.PredictionsItem
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.StructuredFormatting
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompletePredictionUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteStructuredFormattingUiModel
import javax.inject.Inject

class LegacyAutoCompleteMapper @Inject constructor() {
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
