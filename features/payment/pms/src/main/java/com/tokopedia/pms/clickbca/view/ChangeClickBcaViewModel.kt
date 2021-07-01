package com.tokopedia.pms.clickbca.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.clickbca.data.model.EditKlikbca
import com.tokopedia.pms.clickbca.domain.ChangeClickBcaUseCase
import com.tokopedia.pms.paymentlist.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChangeClickBcaViewModel @Inject constructor(
    private val changeClickBcaUseCase: ChangeClickBcaUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _editResult = MutableLiveData<Result<EditKlikbca>>()
    val editResultLiveData: LiveData<Result<EditKlikbca>> =
        _editResult


    fun changeClickBcaUserId(
        transactionId: String,
        merchantCode: String,
        newClickBcaUserId: String
    ) {
        changeClickBcaUseCase.cancelJobs()
        changeClickBcaUseCase.changeClickBcaId(
            ::onChangeClickBcaSuccess,
            ::onChangeClickBcaError,
            transactionId, merchantCode, newClickBcaUserId
        )
    }

    private fun onChangeClickBcaError(throwable: Throwable) {
        _editResult.postValue(Fail(throwable))
    }

    private fun onChangeClickBcaSuccess(editKlikbca: EditKlikbca) {
        _editResult.postValue(Success(editKlikbca))
    }

    override fun onCleared() {
        changeClickBcaUseCase.cancelJobs()
        super.onCleared()
    }
}