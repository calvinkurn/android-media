package com.tokopedia.logisticaddaddress.domain.mapper

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.model.add_address.AddAddressResponse
import com.tokopedia.logisticaddaddress.domain.model.add_address.Data
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressResponseUiModel
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-28.
 */
class AddAddressMapper @Inject constructor() {
    private val STATUS_OK = "OK"

    @Suppress("NAME_SHADOWING")
    fun map(response: GraphqlResponse?) : AddAddressResponseUiModel {
        var status = ""
        var dataUiModel = AddAddressDataUiModel()
        val responseAddAddress: AddAddressResponse? = response?.getData(AddAddressResponse::class.java)
        responseAddAddress.let { response ->
            response?.keroAddAddress?.let {
                status = it.status
                when (status) {
                    STATUS_OK -> {
                        dataUiModel = mapData(it.data)
                    }
                }
            }
        }
        return AddAddressResponseUiModel(dataUiModel, status)
    }

    private fun mapData(data: Data) : AddAddressDataUiModel {
        return AddAddressDataUiModel(
                addressId = data.addrId,
                isSuccess = data.isSuccess
        )
    }
}