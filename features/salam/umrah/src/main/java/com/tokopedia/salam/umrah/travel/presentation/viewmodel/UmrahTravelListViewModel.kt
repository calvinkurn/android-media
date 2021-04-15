package com.tokopedia.salam.umrah.travel.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.salam.umrah.common.data.UmrahTravelAgentsEntity
import com.tokopedia.salam.umrah.common.usecase.UmrahTravelAgentsUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject


class UmrahTravelListViewModel @Inject constructor(private val umrahTravelAgentsUseCase: UmrahTravelAgentsUseCase,
                                                  dispatcher: CoroutineDispatchers)
    : BaseViewModel(dispatcher.main) {

    private val mutableTravelAgents = MutableLiveData<Result<UmrahTravelAgentsEntity>>()
    val travelAgents : LiveData<Result<UmrahTravelAgentsEntity>>
    get() = mutableTravelAgents

    fun requestTravelAgentsData(rawQuery: String,page:Int,isLoadFromCloud:Boolean){
        val flags = listOf(FLAGS_TRAVEL)
        launch {
            val result = umrahTravelAgentsUseCase.executeUseCase(rawQuery,
                    isLoadFromCloud, page,LIMIT, flags)
            mutableTravelAgents.value = result
        }
    }

    companion object{
        const val FLAGS_TRAVEL = "TRAVEL_AGENT_FEATURED_ON_LIST"
        const val LIMIT = 20
    }

}