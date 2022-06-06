package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberAuthenticatedUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberEligibilityUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberUserEligibilityUsecase
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.model.SellerData
import com.tokopedia.tokomember_seller_dashboard.model.TmUserElligibilityResponseData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmEligibilityViewModel @Inject constructor(
    private val tokomemberEligibilityUsecase: TokomemberEligibilityUsecase,
    private val tokomemberAuthenticatedUsecase: TokomemberAuthenticatedUsecase,
    private val tmUserEligibilityUsecase: TokomemberUserEligibilityUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
    ) : BaseViewModel(dispatcher) {

    private val _eligibilityCheckResultLiveData = MutableLiveData<Result<CheckEligibility>>()
    val eligibilityCheckResultLiveData: LiveData<Result<CheckEligibility>> = _eligibilityCheckResultLiveData

    private val _userEligibilityCheckResultLiveData = MutableLiveData<Result<TmUserElligibilityResponseData>>()
    val userEligibilityCheckResultLiveData: LiveData<Result<TmUserElligibilityResponseData>> = _userEligibilityCheckResultLiveData

    private val _sellerInfoResultLiveData = MutableLiveData<Result<SellerData>>()
    val sellerInfoResultLiveData: LiveData<Result<SellerData>> = _sellerInfoResultLiveData

    fun checkEligibility(shopID: Int, isRegister: Boolean) {
        tokomemberEligibilityUsecase.cancelJobs()
        tokomemberEligibilityUsecase.checkEligibility({
            _eligibilityCheckResultLiveData.postValue(Success(it))
        }, {
            _eligibilityCheckResultLiveData.postValue(Fail(it))
        }, shopID, isRegister)
    }

    fun checkUserEligibility(){
        tmUserEligibilityUsecase.cancelJobs()
        tmUserEligibilityUsecase.checkUserEligibility({
            _userEligibilityCheckResultLiveData.postValue(Success(it))
        }, {
            _userEligibilityCheckResultLiveData.postValue(Fail(it))
        })

    }

    fun getSellerInfo() {
        tokomemberAuthenticatedUsecase.cancelJobs()
        tokomemberAuthenticatedUsecase.getSellerInfo({
            _sellerInfoResultLiveData.postValue(Success(it))
        }, {
            _sellerInfoResultLiveData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        tokomemberEligibilityUsecase.cancelJobs()
        super.onCleared()
    }

}