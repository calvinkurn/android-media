package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashCardUsecase
import com.tokopedia.tokomember_seller_dashboard.model.CardData
import com.tokopedia.tokomember_seller_dashboard.model.TmMembershipCardResponse
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashCreateCardViewModel @Inject constructor(
    private val tokomemberDashCardUsecase: TokomemberDashCardUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberCardResultLiveData = MutableLiveData<Result<CardData>>()
    val tokomemberCardResultLiveData: LiveData<Result<CardData>> =
        _tokomemberCardResultLiveData

    fun getCardInfo(cardID: Int) {
        tokomemberDashCardUsecase.cancelJobs()
        tokomemberDashCardUsecase.getMembershipCardInfo({
            _tokomemberCardResultLiveData.postValue(Success(it))
        }, {
            _tokomemberCardResultLiveData.postValue(Fail(it))
        }, 3827)
    }

    override fun onCleared() {
        tokomemberDashCardUsecase.cancelJobs()
        super.onCleared()
    }

}