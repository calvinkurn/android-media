package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.autofill.AutofillResponse
import com.tokopedia.logisticaddaddress.domain.model.autofill.Data
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-17.
 */
class AutofillMapper @Inject constructor() {

    fun map(response: GraphqlResponse?) : AutofillResponseUiModel {
        var dataUiModel = AutofillDataUiModel()
        val responseAutofill: AutofillResponse? = response?.getData(AutofillResponse::class.java)
        responseAutofill.let { response ->
            response?.keroMapsAutofill?.let { keroMapsAutofill ->
                dataUiModel = mapData(keroMapsAutofill.data)
            }
        }
        return AutofillResponseUiModel(dataUiModel)
    }

    private fun mapData(data: Data) : AutofillDataUiModel {
        return AutofillDataUiModel(
                title = data.title,
                formattedAddress = data.formattedAddress,
                latitude = data.latitude,
                longitude = data.longitude,
                districtId = data.districtId,
                provinceId = data.provinceId,
                cityId = data.cityId,
                postalCode = data.postalCode
        )
    }
}