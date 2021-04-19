package com.tokopedia.salam.umrah.pdp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.pdp.presentation.usecase.UmrahPdpGetAvailabilityUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by M on 29/10/2019
 */
class UmrahPdpDetailViewModel @Inject constructor(private val umrahPdpGetAvailabilityUseCase: UmrahPdpGetAvailabilityUseCase,
                                                  dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val privatePdpAvailability = MutableLiveData<Result<Int>>()
    val pdpAvailability: LiveData<Result<Int>>
        get() = privatePdpAvailability

    fun getPdpAvailability(searchQuery: String, slugName: String) {
        launch {
            when (val result = slugName.let { umrahPdpGetAvailabilityUseCase.executeUsecase(searchQuery, it) }) {
                is Success -> privatePdpAvailability.value = Success(result.data.availableSeat)
                is Fail -> privatePdpAvailability.value = result
            }
        }
    }
}