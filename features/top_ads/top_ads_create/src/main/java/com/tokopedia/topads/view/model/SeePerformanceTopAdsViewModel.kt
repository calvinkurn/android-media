package com.tokopedia.topads.view.model

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.topads.common.data.model.AdGroupsParams
import com.tokopedia.topads.common.data.model.CountDataItem
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.Deposit
import com.tokopedia.topads.common.data.response.SingleAdInFo
import com.tokopedia.topads.common.data.response.TopAdsGroupsResponse
import com.tokopedia.topads.common.data.response.nongroupItem.ProductStatisticsResponse
import com.tokopedia.topads.common.domain.interactor.TopAdsGetProductStatisticsUseCase
import com.tokopedia.topads.common.domain.interactor.TopAdsProductActionUseCase
import com.tokopedia.topads.common.domain.model.TopAdsGetProductManage
import com.tokopedia.topads.common.domain.model.TopAdsGetShopInfo
import com.tokopedia.topads.common.domain.query.GetTopadsDashboardGroupsV3
import com.tokopedia.topads.common.domain.usecase.TopAdsGetAutoAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetProductManageUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetPromoUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetShopInfoV1UseCase
import com.tokopedia.topads.common.domain.usecase.GetWhiteListedUserUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetTotalAdsAndKeywordsUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SeePerformanceTopAdsViewModel @Inject constructor(
    private val topAdsGetTotalAdsAndKeywordsUseCase: TopAdsGetTotalAdsAndKeywordsUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.io) {

    @Inject
    lateinit var topAdsGetDepositUseCase: TopAdsGetDepositUseCase
    @Inject
    lateinit var topAdsGetProductManageUseCase: TopAdsGetProductManageUseCase
    @Inject
    lateinit var topAdsGetProductStatisticsUseCase: TopAdsGetProductStatisticsUseCase
    @Inject
    lateinit var topAdsGetShopInfoV1UseCase: TopAdsGetShopInfoV1UseCase
    @Inject
    lateinit var topAdsGetGroupIdUseCase: TopAdsGetPromoUseCase
    @Inject
    lateinit var topAdsGetAutoAdsUseCase: TopAdsGetAutoAdsUseCase
    @Inject
    lateinit var topAdsGetGroupInfoUseCase: GraphqlUseCase<TopAdsGroupsResponse>
    @Inject
    lateinit var topAdsProductActionUseCase: TopAdsProductActionUseCase
    @Inject
    lateinit var whiteListedUserUseCase: GetWhiteListedUserUseCase

    private val _topAdsDeposits: MutableLiveData<Result<Deposit>> = MutableLiveData()
    val topAdsDeposits: LiveData<Result<Deposit>> = _topAdsDeposits

    private val _productStatistics: MutableLiveData<Result<ProductStatisticsResponse>> =
        MutableLiveData()
    val productStatistics: LiveData<Result<ProductStatisticsResponse>> = _productStatistics

    private val _topAdsGetProductManage: MutableLiveData<TopAdsGetProductManage> = MutableLiveData()
    val topAdsGetProductManage: LiveData<TopAdsGetProductManage> = _topAdsGetProductManage

    private val _topAdsGetAutoAds: MutableLiveData<AutoAdsResponse.TopAdsGetAutoAds> =
        MutableLiveData()
    val topAdsGetAutoAds: LiveData<AutoAdsResponse.TopAdsGetAutoAds> = _topAdsGetAutoAds

    private val _topAdsPromoInfo: MutableLiveData<SingleAdInFo> =
        MutableLiveData()
    val topAdsPromoInfo: LiveData<SingleAdInFo> = _topAdsPromoInfo

    private val _adId: MutableLiveData<String> = MutableLiveData()
    val adId: LiveData<String> = _adId

    private val _isSingleAds: MutableLiveData<Boolean> = MutableLiveData()
    val isSingleAds: LiveData<Boolean> = _isSingleAds

    private val _goalId: MutableLiveData<Int> = MutableLiveData(1) // default ad placement filter type is ALL - 1
    val goalId: LiveData<Int> = _goalId

    private val _topAdsGetShopInfo: MutableLiveData<TopAdsGetShopInfo> = MutableLiveData()
    val topAdsGetShopInfo: LiveData<TopAdsGetShopInfo> = _topAdsGetShopInfo

    private val _topAdsGetGroupInfo: MutableLiveData<TopAdsGroupsResponse> = MutableLiveData()
    val topAdsGetGroupInfo: LiveData<TopAdsGroupsResponse> = _topAdsGetGroupInfo

    private val _totalAdsAndKeywordsCount = MutableLiveData<List<CountDataItem>>()
    val totalAdsAndKeywordsCount: LiveData<List<CountDataItem>>
        get() = _totalAdsAndKeywordsCount

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
        }) {}
    }

    fun getShopInfo() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                topAdsGetShopInfoV1UseCase.executeOnBackground()
            }
            _topAdsGetShopInfo.postValue(response)
        }) {}
    }

    fun getPromoInfo() {
        topAdsGetGroupIdUseCase.setParams(adId.value ?: "", userSession.shopId)
        topAdsGetGroupIdUseCase.execute({
            _topAdsPromoInfo.value = it
            checkIsSingleAds()
        }, {
            it.printStackTrace()
        })
    }

    fun getTopAdsProductStatistics(
        resources: Resources,
        startDate: String,
        endDate: String,
        goalId: Int
    ) {
        topAdsGetProductStatisticsUseCase.setParams(
            startDate,
            endDate,
            listOf(_adId.value ?: ""),
            goalId = goalId
        )
        topAdsGetProductStatisticsUseCase.executeQuerySafeMode({
            _productStatistics.value = Success(it)
            if (_goalId.value != goalId) {
                _goalId.value = goalId
            }
        }, {
            _productStatistics.value = Fail(it)
            it.printStackTrace()
        })
    }

    fun setProductAction(
        action: String,
        adIds: List<String>,
        selectedFilter: String?
    ) {
        launchCatchError(block = {
            val params = topAdsProductActionUseCase.setParams(action, adIds, selectedFilter)
            topAdsProductActionUseCase.execute(params)
            getPromoInfo()
        }, onError = {
                it.printStackTrace()
            })
    }

    fun getGroupInfo() {
        topAdsGetGroupInfoUseCase.setRequestParams(
            mapOf(
                PARAM_KEY to AdGroupsParams(
                    shopId = userSession.shopId,
                    groupId = _topAdsPromoInfo.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID ?: ""
                )
            )
        )
        topAdsGetGroupInfoUseCase.setTypeClass(TopAdsGroupsResponse::class.java)
        topAdsGetGroupInfoUseCase.setGraphqlQuery(GetTopadsDashboardGroupsV3)
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                topAdsGetGroupInfoUseCase.executeOnBackground()
            }
            _topAdsGetGroupInfo.postValue(response)
        }) {}
    }

    fun getTotalAdsAndKeywordsCount(){
        launchCatchError(block = {
            val response = topAdsGetTotalAdsAndKeywordsUseCase(listOf(_topAdsPromoInfo.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID ?: ""))
            if (response.topAdsGetTotalAdsAndKeywords.errors.isEmpty())
                _totalAdsAndKeywordsCount.postValue(response.topAdsGetTotalAdsAndKeywords.data)
        }) {}
    }

    fun checkIsSingleAds() {
        _isSingleAds.value = _adId.value != EMPTY_AD_ID && (_topAdsPromoInfo.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID == null || _topAdsPromoInfo.value?.topAdsGetPromo?.data?.firstOrNull()?.groupID == EMPTY_GROUP_ID)
    }

    fun getAutoAdsInfo() {
        launchCatchError(block = {
            val response = withContext(dispatchers.io) {
                topAdsGetAutoAdsUseCase.executeOnBackground()
            }
            _topAdsGetAutoAds.postValue(response)
        }) {}
    }

    companion object {
        private const val PARAM_KEY = "queryInput"
        private const val EMPTY_AD_ID = "0"
        private const val EMPTY_GROUP_ID = "0"
    }
}
