package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramFormUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramListUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashUpdateProgramUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.requestparam.ProgramUpdateDataInput
import com.tokopedia.tokomember_seller_dashboard.model.ProgramDetailData
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.tokomember_seller_dashboard.model.ProgramUpdateResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashProgramViewmodel @Inject constructor(
    private val tokomemberDashGetProgramListUsecase: TokomemberDashGetProgramListUsecase,
    private val tokomemberDashGetProgramFormUsecase: TokomemberDashGetProgramFormUsecase,
    private val tokomemberDashUpdateProgramUsecase: TokomemberDashUpdateProgramUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberProgramResultLiveData = MutableLiveData<Result<ProgramDetailData>>()
    val tokomemberProgramResultLiveData: LiveData<Result<ProgramDetailData>> = _tokomemberProgramResultLiveData

    private val _tokomemberProgramListResultLiveData = MutableLiveData<Result<ProgramList>>()
    val tokomemberProgramListResultLiveData: LiveData<Result<ProgramList>> = _tokomemberProgramListResultLiveData

    private val _tokomemberProgramUpdateResultLiveData = MutableLiveData<Result<ProgramUpdateResponse>>()
    val tokomemberProgramUpdateResultLiveData: LiveData<Result<ProgramUpdateResponse>> = _tokomemberProgramUpdateResultLiveData

    fun getProgramList(shopId: Int, cardID: Int, status: Int = -1, page: Int = 1, pageSize: Int = 10){
        tokomemberDashGetProgramListUsecase.cancelJobs()
        tokomemberDashGetProgramListUsecase.getProgramList({
            _tokomemberProgramListResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramListResultLiveData.postValue(Fail(it))
        }, shopId, cardID, status, page, pageSize)
    }

    fun getProgramInfo(programID: Int ,shopId: Int ,actionType: String, query: String = "") {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        tokomemberDashGetProgramFormUsecase.getProgramInfo({
            _tokomemberProgramResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramResultLiveData.postValue(Fail(it))
        }, programID,shopId,actionType, query)
    }

    fun updateProgram(programUpdateDataInput: ProgramUpdateDataInput) {
        tokomemberDashUpdateProgramUsecase.cancelJobs()
        tokomemberDashUpdateProgramUsecase.updateProgram({
            _tokomemberProgramUpdateResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramUpdateResultLiveData.postValue(Fail(it))
        }, programUpdateDataInput)
    }


    override fun onCleared() {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        tokomemberDashGetProgramListUsecase.cancelJobs()
        tokomemberDashUpdateProgramUsecase.cancelJobs()
        super.onCleared()
    }

}