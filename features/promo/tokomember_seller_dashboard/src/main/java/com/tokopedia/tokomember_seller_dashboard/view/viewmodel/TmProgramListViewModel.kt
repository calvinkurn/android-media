package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmProgramListViewModel @Inject constructor(
    private val tokomemberDashGetProgramListUsecase: TokomemberDashGetProgramListUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ): BaseViewModel(dispatcher) {

    private val _tokomemberProgramListLiveData = MutableLiveData<Int>()
    val tokomemberProgramListLiveData: LiveData<Int> = _tokomemberProgramListLiveData

    private val _tokomemberProgramListResultLiveData = MutableLiveData<Result<ProgramList>>()
    val tokomemberProgramListResultLiveData: LiveData<Result<ProgramList>> = _tokomemberProgramListResultLiveData

    fun refreshList(state: Int){
        _tokomemberProgramListLiveData.postValue(state)
        Log.d("REFRESH_TAG", "refreshList: $state")
    }

    fun getProgramList(shopId: Int, cardID: Int, status: Int = -1, page: Int = 1, pageSize: Int = 10){
        tokomemberDashGetProgramListUsecase.cancelJobs()
        tokomemberDashGetProgramListUsecase.getProgramList({
            _tokomemberProgramListResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramListResultLiveData.postValue(Fail(it))
        }, shopId, cardID, status, page, pageSize)
    }

}