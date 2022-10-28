package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashIntroUsecase
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberIntroSectionMapperUsecase
import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroItem
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TmDashIntroViewModel @Inject constructor(
    private val tokomemberDashIntroUsecase: TokomemberDashIntroUsecase,
    private val tokomemberIntroSectionMapperUsecase: TokomemberIntroSectionMapperUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberOnboardingResultLiveData =
        MutableLiveData<Result<MembershipData>>()
    val tokomemberOnboardingResultLiveData: LiveData<Result<MembershipData>> =
        _tokomemberOnboardingResultLiveData

    private val _tokomemberIntroSectionResultLiveData =
        MutableLiveData<Result<TokomemberIntroItem>>()
    val tokomemberIntroSectionResultLiveData: LiveData<Result<TokomemberIntroItem>> =
        _tokomemberIntroSectionResultLiveData

    fun getIntroInfo(shopID: Int) {
        tokomemberDashIntroUsecase.cancelJobs()
        tokomemberDashIntroUsecase.getMemberOnboardingInfo({
            _tokomemberOnboardingResultLiveData.postValue(
                Success(it)
            )
            getIntroSectionData(it)
        }, {
            _tokomemberOnboardingResultLiveData.postValue(Fail(it))
        }, shopID)
    }

    fun getIntroSectionData(membershipData: MembershipData) {
        tokomemberIntroSectionMapperUsecase.cancelJobs()
        tokomemberIntroSectionMapperUsecase.getIntroSectionData(membershipData,{
            _tokomemberIntroSectionResultLiveData.postValue(Success(it))
        }, {
            _tokomemberIntroSectionResultLiveData.postValue(Fail(it))
        })
    }

    override fun onCleared() {
        tokomemberDashIntroUsecase.cancelJobs()
        tokomemberIntroSectionMapperUsecase.cancelJobs()
        super.onCleared()
    }

}