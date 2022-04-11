package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashHomeUsecase
import com.tokopedia.tokomember_seller_dashboard.model.ProgramList
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashHomeViewmodel @Inject constructor(
    private val tokomemberDashHomeUsecase: TokomemberDashHomeUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberHomeResultLiveData = MutableLiveData<Result<ProgramList>>()
    val tokomemberHomeResultLiveData: LiveData<Result<ProgramList>> = _tokomemberHomeResultLiveData

    fun getHomePageData(shopId: Int, cardID: Int, status: Int = -1, page: Int = 1, pageSize: Int = 10) {
        tokomemberDashHomeUsecase.cancelJobs()
        tokomemberDashHomeUsecase.getHomeData({
            _tokomemberHomeResultLiveData.postValue(Success(it))
        }, {
            _tokomemberHomeResultLiveData.postValue(Fail(it))
        }, shopId, cardID, status, page, pageSize)
    }

    override fun onCleared() {
        tokomemberDashHomeUsecase.cancelJobs()
        super.onCleared()
    }

}