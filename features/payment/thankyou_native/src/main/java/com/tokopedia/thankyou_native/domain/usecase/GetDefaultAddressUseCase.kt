package com.tokopedia.thankyou_native.domain.usecase

import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.response.DefaultChosenAddressData
import com.tokopedia.localizationchooseaddress.domain.response.GetDefaultChosenAddressGqlResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class GetDefaultAddressUseCase @Inject constructor(private val chooseAddressRepo: ChooseAddressRepository)
    : UseCase<GetDefaultChosenAddressGqlResponse>() {

    private val SOURCE_TYP = "typ"

    fun getDefaultChosenAddress(success : (DefaultChosenAddressData)-> Unit, onFail:(Throwable)-> Unit){
        execute({
            success(it.response.data)
        },{
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): GetDefaultChosenAddressGqlResponse {
        return chooseAddressRepo.getDefaultChosenAddress(null, SOURCE_TYP, false)
    }

}