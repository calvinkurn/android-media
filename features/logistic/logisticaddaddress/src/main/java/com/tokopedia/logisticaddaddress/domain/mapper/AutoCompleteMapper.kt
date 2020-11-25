package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.PredictionsItem
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutoCompleteMapper @Inject constructor() {

    fun mapAutoComplete(response: AutocompleteResponse): Place {
        val errorCode = response.keroMapsAutocomplete.errorCode
        return Place().apply {
            this.data = suggestedPlaceData(response.keroMapsAutocomplete.data.predictions)
            this.errorCode = errorCode
        }
    }

    private fun suggestedPlaceData(response: List<PredictionsItem>) : List<SuggestedPlace> {
        return response.map {
            SuggestedPlace(
                    it.structuredFormatting.mainText,
                    it.structuredFormatting.secondaryText,
                    it.placeId
            )
        }
    }

}