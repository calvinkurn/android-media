package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticdata.data.autocomplete.SuggestedPlace
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutoCompleteMapper @Inject constructor() {

    fun mapAutoComplete(response: AutocompleteResponse): List<SuggestedPlace> {
        val dataResponse = response.keroMapsAutocomplete.data.predictions
        return dataResponse.map {
            SuggestedPlace(
                    it.structuredFormatting.mainText,
                    it.structuredFormatting.secondaryText,
                    it.placeId
            )
        }
    }

}