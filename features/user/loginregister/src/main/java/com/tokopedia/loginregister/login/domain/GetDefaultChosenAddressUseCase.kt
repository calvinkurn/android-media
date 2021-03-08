package com.tokopedia.loginregister.login.domain

import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.response.DefaultChosenAddressData
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import javax.inject.Inject

class GetDefaultChosenAddressUseCase @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository) : com.tokopedia.usecase.coroutines.UseCase<GetDefaultChosenAddressGqlResponse>() {

    fun getDefaultChosenAddress(success : (DefaultChosenAddressData)-> Unit, onFail:(Throwable)-> Unit){
        execute({
            success(it.response.data)
        },{
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): GetDefaultChosenAddressGqlResponse {
        return chooseAddressRepo.getDefaultChosenAddress(null, "addr")
    }


}