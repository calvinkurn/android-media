package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramListUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmProgramListViewModel @Inject constructor(
    private val tokomemberDashGetProgramListUsecase: TokomemberDashGetProgramListUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
    ): BaseViewModel(dispatcher) {

    private val _tokomemberProgramListLiveData = MutableLiveData<Int>()
    val tokomemberProgramListLiveData: LiveData<Int> = _tokomemberProgramListLiveData

    private val _tokomemberProgramListResultLiveData = MutableLiveData<TokoLiveDataResult<ProgramList>>()
    val tokomemberProgramListResultLiveData: LiveData<TokoLiveDataResult<ProgramList>> = _tokomemberProgramListResultLiveData

    fun refreshList(state: Int){
        _tokomemberProgramListLiveData.postValue(state)
    }

    fun getProgramList(shopId: Int, cardID: Int, status: Int = -1, page: Int = 1, pageSize: Int = 10){
        tokomemberDashGetProgramListUsecase.cancelJobs()
        _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashGetProgramListUsecase.getProgramList({
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tokomemberProgramListResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, shopId, cardID, status, page, pageSize)
    }

}