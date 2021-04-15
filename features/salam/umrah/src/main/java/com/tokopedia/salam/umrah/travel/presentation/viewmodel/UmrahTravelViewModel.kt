package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.salam.umrah.travel.data.UmrahTravelAgentBySlugNameEntity
import com.tokopedia.salam.umrah.travel.presentation.usecase.UmrahTravelUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by Firman on 22/1/20
 */

class UmrahTravelViewModel  @Inject constructor(private val umrahTravelUseCase: UmrahTravelUseCase,
                                                dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val mutableTravelAgentResult = MutableLiveData<Result<UmrahTravelAgentBySlugNameEntity>>()
    val travelAgentData: LiveData<Result<UmrahTravelAgentBySlugNameEntity>>
        get() = mutableTravelAgentResult

    fun requestTravelData(rawQuery: String, slugName: String) {
        launch {
            val result = umrahTravelUseCase.executeUsecase(rawQuery, slugName)
            mutableTravelAgentResult.value = result
        }
    }

}