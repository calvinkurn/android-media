package com.tokopedia.dropoff.domain.mapper

import com.tokopedia.logisticCommon.data.response.AddressResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.dropoff.ui.autocomplete.model.ValidatedDistrict
import com.tokopedia.logisticCommon.domain.model.SavedAddress
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import javax.inject.Inject

class AutoCompleteMapper @Inject constructor() {

    fun mapAutoComplete(response: AutoCompleteResponse): List<SuggestedPlace> {
        val dataResponse = response.keroMapsAutocomplete.AData.predictions
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
        return ValidatedDistrict(
                data.title,
                data.latitude,
                data.longitude
        )

    }

}