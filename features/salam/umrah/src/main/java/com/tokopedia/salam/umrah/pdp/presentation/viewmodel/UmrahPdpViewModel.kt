package com.tokopedia.salam.umrah.pdp.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.data.UmrahProductModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.pdp.presentation.usecase.UmrahPdpGetDataUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by M on 29/10/2019
 */
class UmrahPdpViewModel @Inject constructor(private val umrahPdpGetDataUseCase: UmrahPdpGetDataUseCase,
                                            dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val privatePdpResult = MutableLiveData<Result<UmrahProductModel.UmrahProduct>>()
    val pdpData: LiveData<Result<UmrahProductModel.UmrahProduct>>
        get() = privatePdpResult

    fun requestPdpData(searchQuery: String, slugName: String) {
        launch {
            val result = umrahPdpGetDataUseCase.executeUsecase(searchQuery, slugName)
            privatePdpResult.value = result
        }
    }
}