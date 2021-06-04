package com.tokopedia.localizationchooseaddress.domain.usecase

import com.tokopedia.localizationchooseaddress.data.repository.ChooseAddressRepository
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressQglResponse
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

/**
 * this usecase support feature that need warehouse location
 * host/fragment need to implement in their viewModel/Presenter
 */
class GetChosenAddressWarehouseLocUseCase @Inject constructor(private val chooseAddressRepository: ChooseAddressRepository) : UseCase<GetStateChosenAddressQglResponse>() {

    private var source = ""

    fun getStateChosenAddress(success : (GetStateChosenAddressResponse)-> Unit, onFail:(Throwable)-> Unit, source: String){
        execute({
            success(it.response)
        },{
            onFail(it)
        })

        this.source = source
    }

    override suspend fun executeOnBackground(): GetStateChosenAddressQglResponse {
        return chooseAddressRepository.getStateChosenAddress(source, true)
    }

}