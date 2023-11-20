package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.localizationchooseaddress.domain.model.GetChosenAddressParam
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import javax.inject.Inject

/**
 * this usecase support feature that need warehouse location
 * host/fragment need to implement in their viewModel/Presenter
 */
class GetChosenAddressWarehouseLocUseCase @Inject constructor(
    private val getStateChosenAddressUseCase: GetStateChosenAddressUseCase,
    dispatcher: CoroutineDispatchers
) :
    CoroutineUseCase<String, GetStateChosenAddressResponse>(dispatcher.io) {

    override fun graphqlQuery(): String {
        return ""
    }

    override suspend fun execute(params: String): GetStateChosenAddressResponse {
        return getStateChosenAddressUseCase(GetChosenAddressParam(source = params, true)).response
    }
}
