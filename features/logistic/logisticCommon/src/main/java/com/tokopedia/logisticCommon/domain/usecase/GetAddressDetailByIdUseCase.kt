package com.tokopedia.logisticCommon.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.domain.param.GetDetailAddressParam
import javax.inject.Inject

open class GetAddressDetailByIdUseCase @Inject constructor(
    private val getAddressDetailUseCase: GetAddressDetailUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<GetDetailAddressParam, KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse>(
    dispatcher.io
) {

    override suspend fun execute(params: GetDetailAddressParam): KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse {
        return getAddressDetailUseCase(params).keroGetAddress.data.find { params.addressIds == it.addrId.toString() }
            ?: KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse(addrId = 0L)
    }

    override fun graphqlQuery(): String {
        return ""
    }

    companion object {
        fun getParam(addressId: String, source: ManageAddressSource): GetDetailAddressParam {
            return GetDetailAddressParam(
                addressIds = addressId,
                source = source.source
            )
        }
    }
}
