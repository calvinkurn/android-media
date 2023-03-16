package com.tokopedia.topads.view.model

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManage
import com.tokopedia.topads.common.domain.model.TopAdsGetShopInfo
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductManageUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetShopInfoV1UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SeePerformanceTopAdsViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val topAdsGetDepositUseCase: TopAdsGetDepositUseCase,
    private val topAdsGetProductManageUseCase: TopAdsGetProductManageUseCase,
    private val topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase,
    private val topAdsGetShopInfoV1UseCase: TopAdsGetShopInfoV1UseCase,
    private val topAdsGetGroupIdUseCase: TopAdsGetPromoUseCase,
    private val userSession: UserSessionInterface,
) : BaseViewModel(dispatchers.io) {

    private val _topAdsDeposits: MutableLiveData<Result<Deposit>> = MutableLiveData()
    val topAdsDeposits: LiveData<Result<Deposit>> = _topAdsDeposits

    private val _productStatistics: MutableLiveData<Result<ProductStatisticsResponse>> =
        MutableLiveData()
    val productStatistics: LiveData<Result<ProductStatisticsResponse>> = _productStatistics

    private val _topAdsGetProductManage: MutableLiveData<TopAdsGetProductManage> = MutableLiveData()
    val topAdsGetProductManage: LiveData<TopAdsGetProductManage> = _topAdsGetProductManage

    private val _adId: MutableLiveData<String> = MutableLiveData()
    val adId: LiveData<String> = _adId

    private val _groupId: MutableLiveData<String> = MutableLiveData()
    val groupId: LiveData<String> = _groupId

    private val _topAdsGetShopInfo: MutableLiveData<TopAdsGetShopInfo> = MutableLiveData()
    val topAdsGetShopInfo: LiveData<TopAdsGetShopInfo> = _topAdsGetShopInfo

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
            val response = withContext(dispatchers.io) {
                topAdsGetProductManageUseCase.setParams(productId)
                topAdsGetProductManageUseCase.executeOnBackground()
            }
            _adId.postValue(response.data.adId)
            _topAdsGetProductManage.postValue(response)
        }) {
            _topAdsGetProductManage.postValue(null)
        }
    }

    fun getShopInfo() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                topAdsGetShopInfoV1UseCase.executeOnBackground()
            }
            _topAdsGetShopInfo.postValue(response)
        }) {
            _topAdsGetShopInfo.postValue(null)
        }
    }

    fun getGroupId() {
        topAdsGetGroupIdUseCase.setParams(adId.value ?: "", userSession.shopId)
        topAdsGetGroupIdUseCase.execute({
            _groupId.value = it.topAdsGetPromo.data[0].groupID
        }, {
            it.printStackTrace()
        })
    }

    fun getTopAdsProductStatistics(
        resources: Resources,
        startDate: String,
        endDate: String,
        goalId: Int = 0
    ) {
        topAdsGetProductStatisticsUseCase.setGraphqlQuery(
            GraphqlHelper.loadRawString(
                resources,
                com.tokopedia.topads.common.R.raw.gql_query_product_statistics
            )
        )
        topAdsGetProductStatisticsUseCase.setParams(
            startDate,
            endDate,
            listOf(_adId.value ?: ""),
            goalId = goalId
        )
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode({
            _productStatistics.value = Success(it)
        }, {
            _productStatistics.value = Fail(it)
            it.printStackTrace()
        })
    }
}
