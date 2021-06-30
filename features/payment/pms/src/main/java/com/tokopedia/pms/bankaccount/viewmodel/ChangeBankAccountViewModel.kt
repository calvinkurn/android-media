package com.tokopedia.pms.bankaccount.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.pms.bankaccount.data.model.EditTransfer
import com.tokopedia.pms.bankaccount.domain.SaveAccountDetailUseCase
import com.tokopedia.pms.paymentlist.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ChangeBankAccountViewModel @Inject constructor(
    private val saveAccountDetailUseCase: SaveAccountDetailUseCase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _saveDetailLiveData = MutableLiveData<Result<EditTransfer>>()
    val saveDetailLiveData: LiveData<Result<EditTransfer>> = _saveDetailLiveData

    fun saveDetailAccount(
        transactionId: String, merchantCode: String,
        accountName: String, accountNumber: String, notes: String, destinationBankId: String,
    ) {
        saveAccountDetailUseCase.cancelJobs()
        saveAccountDetailUseCase.saveDetailAccount(
            ::onSaveDetailSuccess, ::onSaveDetailError,
            transactionId, merchantCode, accountName, accountNumber, notes, destinationBankId,
         )
    }

    private fun onSaveDetailError(throwable: Throwable) {
        _saveDetailLiveData.postValue(Fail(throwable))
    }

    private fun onSaveDetailSuccess(editTransfer: EditTransfer) {
        _saveDetailLiveData.postValue(Success(editTransfer))
    }

    override fun onCleared() {
        saveAccountDetailUseCase.cancelJobs()
        super.onCleared()
    }

}