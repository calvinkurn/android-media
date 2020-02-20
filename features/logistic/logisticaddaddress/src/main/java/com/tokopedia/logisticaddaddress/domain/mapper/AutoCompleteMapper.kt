package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.data.entity.response.AddressResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.AutocompleteResponse
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.Data
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.PredictionsItem
import com.tokopedia.logisticaddaddress.domain.model.autocomplete.StructuredFormatting
import com.tokopedia.logisticaddaddress.domain.model.get_district.GetDistrictResponse
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompletePredictionUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteResponseUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteStructuredFormattingUiModel
import com.tokopedia.logisticaddaddress.features.autocomplete.model.SavedAddress
import com.tokopedia.logisticaddaddress.features.autocomplete.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.features.autocomplete.model.ValidatedDistrict
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

    fun mapAddress(response: AddressResponse): List<SavedAddress> {
        val data = response.keroAddressCorner.data
        return data.map {
            SavedAddress(
                    it.addrId,
                    it.addrName,
                    it.address1,
                    it.latitude,
                    it.longitude
            )
        }
    }

    fun mapValidate(response: GetDistrictResponse): ValidatedDistrict {
        val data = response.keroPlacesGetDistrict.data
        return data?.let {
            ValidatedDistrict(
                    it.title,
                    it.latitude,
                    it.longitude
            )
        } ?: ValidatedDistrict()
    }

}