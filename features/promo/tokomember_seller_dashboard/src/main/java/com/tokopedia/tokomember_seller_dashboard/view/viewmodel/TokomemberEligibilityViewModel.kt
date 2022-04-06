package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberEligibilityUsecase
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberEligibilityViewModel @Inject constructor(
    private val tokomemberEligibilityUsecase: TokomemberEligibilityUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _eligibilityCheckResultLiveData = MutableLiveData<Result<CheckEligibility>>()
    val eligibilityCheckResultLiveData: LiveData<Result<CheckEligibility>> = _eligibilityCheckResultLiveData

    fun checkEligibility(shopID: Int, isRegister: Boolean) {
        tokomemberEligibilityUsecase.cancelJobs()
        tokomemberEligibilityUsecase.checkEligibility({
            _eligibilityCheckResultLiveData.postValue(Success(it))
        }, {
            _eligibilityCheckResultLiveData.postValue(Fail(it))
        }, shopID, isRegister)
    }

    override fun onCleared() {
        tokomemberEligibilityUsecase.cancelJobs()
        super.onCleared()
    }

}