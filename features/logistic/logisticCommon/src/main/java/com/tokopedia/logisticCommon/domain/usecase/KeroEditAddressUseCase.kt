package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.request.EditPinpointParam
import com.tokopedia.logisticCommon.data.request.UpdatePinpointParam
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.domain.param.GetDetailAddressParam
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import javax.inject.Inject

open class KeroEditAddressUseCase @Inject constructor(
    private val getAddressDetailUseCase: GetAddressDetailUseCase,
    private val updatePinpointUseCase: UpdatePinpointUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<KeroEditAddressParam, KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>(
    dispatcher.io
) {

//    init {
//        setGraphqlQuery(KeroEditAddressQuery)
//        setTypeClass(KeroEditAddressResponse.Data::class.java)
//    }

//    suspend fun execute(
//        addressId: String,
//        latitude: String,
//        longitude: String
//    ): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
//        val addressParam = keroGetAddressUseCase.execute(addressId)?.copy(
//            latitude = latitude,
//            longitude = longitude,
//            secondAddress = "$latitude,$longitude"
//        )
//        return if (addressParam == null) {
//            KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(isSuccess = 0)
//        } else {
//            setRequestParams(KeroEditAddressQuery.createRequestParams(addressParam))
//            executeOnBackground().keroEditAddress.data
//        }
//    }

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: KeroEditAddressParam): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
        val addressDetails = getAddressDetailUseCase(params.addressId.toAddressDetailParam())
        val address =
            addressDetails.keroGetAddress.data.find { it.addrId.toString() == params.addressId }
        return if (address != null) {
            val editedAddress = updatePinpointUseCase(
                address.copy(
                    latitude = params.latitude,
                    longitude = params.longitude
                ).toUpdatePinpointParam()
            )
            editedAddress.keroEditAddress.data
        } else {
            KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(
                isSuccess = 0
            )
        }
    }

    private fun KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse.toUpdatePinpointParam(): UpdatePinpointParam {
        return UpdatePinpointParam(
            input = EditPinpointParam(
                addressId = this.addrId,
                address1 = this.address1,
                address2 = this.address2,
                addressName = this.addrName,
                city = this.city.toString(),
                district = this.district.toString(),
                province = this.province.toString(),
                phone = this.phone,
                postalCode = this.postalCode,
                receiverName = this.receiverName,
                isTokonowRequest = true
            )
        )
    }

    private fun String.toAddressDetailParam(): GetDetailAddressParam {
        // todo source
        return GetDetailAddressParam(
            addressIds = this,
            needToTrack = false,
            isManageAddressFlow = false
        )
    }
}
