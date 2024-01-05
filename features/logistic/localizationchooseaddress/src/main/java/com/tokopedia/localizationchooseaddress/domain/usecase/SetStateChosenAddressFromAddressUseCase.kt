package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.StateChooseAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.SetStateChosenAddressQqlResponse
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import javax.inject.Inject

class SetStateChosenAddressFromAddressUseCase @Inject constructor(
    private val setStateChosenAddressUseCase: SetStateChosenAddressUseCase,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<RecipientAddressModel, SetStateChosenAddressQqlResponse>(dispatcher.io) {
    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: RecipientAddressModel): SetStateChosenAddressQqlResponse {
        val param = StateChooseAddressParam(
            params.addressStatus, params.id.toLong(), params.recipientName,
            params.addressName, params.latitude, params.longitude,
            params.destinationDistrictId.toLong(), params.postalCode,
            true
        )
        return setStateChosenAddressUseCase(param)
    }
}
