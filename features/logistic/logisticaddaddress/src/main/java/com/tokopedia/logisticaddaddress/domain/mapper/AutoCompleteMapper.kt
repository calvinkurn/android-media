package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.logisticCommon.data.response.AutoCompleteResponse
import com.tokopedia.logisticCommon.data.response.GetDistrictResponse
import com.tokopedia.logisticCommon.data.response.Prediction
import com.tokopedia.logisticCommon.domain.model.Place
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.domain.model.shopLocation.DistrictLocation
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutoCompleteMapper @Inject constructor() {

    fun mapAutoComplete(response: AutoCompleteResponse): Place {
        val errorCode = response.keroMapsAutocomplete.errorCode
        return Place().apply {
            this.data = suggestedPlaceData(response.keroMapsAutocomplete.AData.predictions)
            this.errorCode = errorCode
        }
    }

    private fun suggestedPlaceData(response: List<Prediction>) : List<SuggestedPlace> {
        return response.map {
            SuggestedPlace(
                    it.structuredFormatting.mainText,
                    it.structuredFormatting.secondaryText,
                    it.placeId
            )
        }
    }

    fun mapDistrictLoc(response: GetDistrictResponse): DistrictLocation {
        val data = response.keroPlacesGetDistrict.data
        return DistrictLocation(
                data.title,
                data.latitude,
                data.longitude
        )
    }

}