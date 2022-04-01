package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashGetProgramFormUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramFormData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashProgramViewmodel @Inject constructor(
    private val tokomemberDashGetProgramFormUsecase: TokomemberDashGetProgramFormUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberProgramResultLiveData = MutableLiveData<Result<ProgramFormData>>()
    val tokomemberProgramResultLiveData: LiveData<Result<ProgramFormData>> =
        _tokomemberProgramResultLiveData

    fun getProgramInfo(cardID: Int) {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        tokomemberDashGetProgramFormUsecase.getProgramInfo({
            _tokomemberProgramResultLiveData.postValue(Success(it))
        }, {
            _tokomemberProgramResultLiveData.postValue(Fail(it))
        }, 0,6553698,"create")
    }

    override fun onCleared() {
        tokomemberDashGetProgramFormUsecase.cancelJobs()
        super.onCleared()
    }

}