package com.tokopedia.loginregister.login.domain

import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressResponse
import javax.inject.Inject

class GetDefaultChosenAddressUseCase @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository) : com.tokopedia.usecase.coroutines.UseCase<GetDefaultChosenAddressGqlResponse>() {

    private val SOURCE_LOGIN = "login"

    fun getDefaultChosenAddress(success: (GetDefaultChosenAddressResponse) -> Unit, onFail: (Throwable) -> Unit) {
        execute({
            success(it.response)
        }, {
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): GetDefaultChosenAddressGqlResponse {
        return chooseAddressRepo.getDefaultChosenAddress(null, SOURCE_LOGIN, true)
    }
}
