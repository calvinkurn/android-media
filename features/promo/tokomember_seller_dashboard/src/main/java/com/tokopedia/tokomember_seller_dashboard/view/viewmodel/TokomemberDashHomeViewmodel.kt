package com.tokopedia.tokomember_seller_dashboard.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.tokomember_seller_dashboard.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.tokomember_seller_dashboard.domain.TokomemberDashHomeUsecase
import com.tokopedia.tokomember_seller_dashboard.model.TmDashHomeResponse
import com.tokopedia.tokomember_seller_dashboard.util.TokoLiveDataResult
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TokomemberDashHomeViewmodel @Inject constructor(
    private val tokomemberDashHomeUsecase: TokomemberDashHomeUsecase,
    @CoroutineMainDispatcher dispatcher: CoroutineDispatcher,
) : BaseViewModel(dispatcher) {

    private val _tokomemberHomeRefreshLiveData = MutableLiveData<Int>()
    val tokomemberHomeRefreshLiveData: LiveData<Int> = _tokomemberHomeRefreshLiveData

    private val _tokomemberHomeResultLiveData = MutableLiveData<TokoLiveDataResult<TmDashHomeResponse>>()
    val tokomemberHomeResultLiveData: LiveData<TokoLiveDataResult<TmDashHomeResponse>> = _tokomemberHomeResultLiveData

    fun refreshHomeData(state: Int){
        _tokomemberHomeRefreshLiveData.postValue(state)
    }
    fun getHomePageData(shopId: Int) {
        tokomemberDashHomeUsecase.cancelJobs()
        _tokomemberHomeResultLiveData.postValue(TokoLiveDataResult.loading())
        tokomemberDashHomeUsecase.getHomeData({
            _tokomemberHomeResultLiveData.postValue(TokoLiveDataResult.success(it))
        }, {
            _tokomemberHomeResultLiveData.postValue(TokoLiveDataResult.error(it))
        }, shopId)
    }

    override fun onCleared() {
        tokomemberDashHomeUsecase.cancelJobs()
        super.onCleared()
    }

}