package com.tokopedia.topads.view.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManage
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductManageUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.utils.date.DateUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class SeePerformanceTopAdsViewModel @Inject constructor(
    dispatchers: CoroutineDispatchers,
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase,
    private val topAdsGetProductManageUseCase: TopAdsGetProductManageUseCase,
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase
) : BaseViewModel(dispatchers.io) {

    private val _topAdsDeposits: MutableLiveData<Result<Deposit>> = MutableLiveData()
    val topAdsDeposits: LiveData<Result<Deposit>> = _topAdsDeposits
    private val _productStatistics: MutableLiveData<Result<ProductStatisticsResponse>> =
        MutableLiveData()
    val productStatistics: LiveData<Result<ProductStatisticsResponse>> = _productStatistics
    private var _adId: String? = null
    private val _topAdsGetProductManage: MutableLiveData<TopAdsGetProductManage> = MutableLiveData()
    val topAdsGetProductManage: LiveData<TopAdsGetProductManage> = _topAdsGetProductManage
    private var startDate: String? = null
    private var endDate: String? = null

    fun setStartDate() {

    }

    fun getStartDate(): String {
        return startDate ?: DateUtil.getCurrentDate().formatTo(DateUtil.YYYY_MM_DD)
    }

    fun setEndDate() {

    }

    fun getEndDate(): String {
        return endDate ?: (DateUtil.getCurrentDate()).formatTo(DateUtil.YYYY_MM_DD)
    }

    fun getTopAdsDeposit() {
        topAdsGetDepositUseCase.execute({
            _topAdsDeposits.value = Success(it)
        }, {
            _topAdsDeposits.value = Fail(it)
            it.printStackTrace()
        })
    }


    fun getProductManage(productId: String) {
        launchCatchError(block = {
            _topAdsGetProductManage.postValue(withContext(Dispatchers.IO) {
                topAdsGetProductManageUseCase.setParams(productId)
                topAdsGetProductManageUseCase.executeOnBackground()
            })
            _adId = _topAdsGetProductManage.value?.data?.adId
        }) {
            _topAdsGetProductManage.postValue(null)
        }
    }

    fun getTopAdsProductStatistics(
        startDate: String,
        endDate: String,
        goalId: Int = 0
    ) {
        topAdsGetProductStatisticsUseCase.setParams(startDate, endDate, listOf(_adId?:""), "", 0, goalId)
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode({
            _productStatistics.value = Success(it)
        }, {
            _productStatistics.value = Fail(it)
            it.printStackTrace()
        })
    }
}
