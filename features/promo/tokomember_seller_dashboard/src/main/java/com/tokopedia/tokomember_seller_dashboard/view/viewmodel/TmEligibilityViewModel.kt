package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TmOnBoardingCheckUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberAuthenticatedUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberEligibilityUsecase
import com.tokopedia.tokomember_seller_dashboard.model.CheckEligibility
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.model.SellerData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmEligibilityViewModel @Inject constructor(
    private val tokomemberEligibilityUsecase: TokomemberEligibilityUsecase,
    private val tokomemberAuthenticatedUsecase: TokomemberAuthenticatedUsecase,
    private val tmOnBoardingCheckUsecase: TmOnBoardingCheckUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher
    ) : BaseViewModel(dispatcher) {

    private val _eligibilityCheckResultLiveData = MutableLiveData<Result<CheckEligibility>>()
    val eligibilityCheckResultLiveData: LiveData<Result<CheckEligibility>> = _eligibilityCheckResultLiveData

    private val _sellerInfoResultLiveData = MutableLiveData<Result<SellerData>>()
    val sellerInfoResultLiveData: LiveData<Result<SellerData>> = _sellerInfoResultLiveData

    private val _tokomemberOnboardingResultLiveData =
        MutableLiveData<Result<MembershipData>>()
    val tokomemberOnboardingResultLiveData: LiveData<Result<MembershipData>> =
        _tokomemberOnboardingResultLiveData

    fun checkEligibility(shopID: Int, isRegister: Boolean) {
        tokomemberEligibilityUsecase.cancelJobs()
        tokomemberEligibilityUsecase.checkEligibility({
            _eligibilityCheckResultLiveData.postValue(Success(it))
        }, {
            _eligibilityCheckResultLiveData.postValue(Fail(it))
        }, shopID, isRegister)
    }

    fun getOnboardingInfo(shopID: Int) {
        tmOnBoardingCheckUsecase.cancelJobs()
        tmOnBoardingCheckUsecase.getMemberOnboardingInfo({
            _tokomemberOnboardingResultLiveData.postValue(Success(it))
        }, {
            _tokomemberOnboardingResultLiveData.postValue(Fail(it))
        }, shopID)
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