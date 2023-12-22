package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.request.EditPinpointParam
import com.tokopedia.logisticCommon.data.request.UpdatePinpointParam
import com.tokopedia.logisticCommon.data.response.KeroEditAddressResponse
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.domain.param.GetDetailAddressParam
import com.tokopedia.logisticCommon.domain.param.KeroEditAddressParam
import javax.inject.Inject

/*
* Use case for update pinpoint to existing address by giving address ID
* This use case combine two use cases
* First, this use case hits `GetAddressDetailByIdUseCase` to retrieve full address detail for edit address param
* Second, will hit `UpdatePinpointUseCase` with address detail along with the new latitude and longitude
*/
open class UpdatePinpointWithAddressIdUseCase @Inject constructor(
    private val getAddressDetailUseCase: GetAddressDetailByIdUseCase,
    private val updatePinpointUseCase: UpdatePinpointUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<KeroEditAddressParam, KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse>(
    dispatcher.io
) {
    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: KeroEditAddressParam): KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse {
        val addressDetails = getAddressDetailUseCase(params.toAddressDetailParam())
        return if (addressDetails.addrId != 0L) {
            val editedAddress = updatePinpointUseCase(
                addressDetails.copy(
                    latitude = params.latitude,
                    longitude = params.longitude,
                    address2 = "${params.latitude},${params.longitude}"
                ).toUpdatePinpointParam(params.source)
            )
            editedAddress.keroEditAddress.data
        } else {
            KeroEditAddressResponse.Data.KeroEditAddress.KeroEditAddressSuccessResponse(
                isSuccess = 0
            )
        }
    }

    private fun KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse.toUpdatePinpointParam(source: ManageAddressSource): UpdatePinpointParam {
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
                isTokonowRequest = true,
                source = source.source
            )
        )
    }

    private fun KeroEditAddressParam.toAddressDetailParam(): GetDetailAddressParam {
        return GetDetailAddressParam(
            addressIds = this.addressId,
            needToTrack = false,
            isManageAddressFlow = false,
            source = this.source.source
        )
    }
}
