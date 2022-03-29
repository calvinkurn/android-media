package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberAuthenticatedUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashIntroUsecase
import com.tokopedia.tokomember_seller_dashboard.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashIntroViewModel @Inject constructor(
    private val tokomemberDashIntroUsecase: TokomemberDashIntroUsecase,
    private val tokomemberAuthenticatedUsecase: TokomemberAuthenticatedUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _sellerInfoResultLiveData =
        MutableLiveData<Result<SellerData>>()
    val sellerInfoResultLiveData: LiveData<Result<SellerData>> =
        _sellerInfoResultLiveData

    private val _tokomemberOnboardingResultLiveData =
        MutableLiveData<Result<MembershipData>>()
    val tokomemberOnboardingResultLiveData: LiveData<Result<MembershipData>> =
        _tokomemberOnboardingResultLiveData

    fun getIntroInfo(shopID: Int) {
        tokomemberDashIntroUsecase.cancelJobs()
        tokomemberDashIntroUsecase.getMemberOnboardingInfo({
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
        tokomemberAuthenticatedUsecase.cancelJobs()
        tokomemberDashIntroUsecase.cancelJobs()
        super.onCleared()
    }

}