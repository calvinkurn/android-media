package com.tokopedia.topads.auto.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_CHANNEL
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_SOURCE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_TOGGLE_ON
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_REQUEST_TYPE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.GET_AUTO_ADS_SOURCE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.STATISTICS_ESTIMATION_TYPE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.TOPADS_AUTO_PS_SOURCE
import com.tokopedia.topads.auto.domain.usecase.TopadsStatisticsEstimationAttributeUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.AutoAdsResponse
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.data.response.TopadsGetBudgetRecommendationResponse
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.domain.usecase.TopAdsGetAutoAdsUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsGetDepositUseCase
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.topads.common.domain.usecase.TopadsGetBudgetRecommendationUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class AutoPsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSession,
    private val topadsStatisticsEstimationAttributeUseCase: TopadsStatisticsEstimationAttributeUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val topAdsQueryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase,
    private val getBudgetRecommendationUseCase: TopadsGetBudgetRecommendationUseCase,
    private val topAdsGetShopDepositUseCase: TopAdsGetDepositUseCase,
    private val topAdsGetAutoAdsUseCase: TopAdsGetAutoAdsUseCase,
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private var lowClickDivider = Int.ONE
    private var deposits = Int.ZERO
    private val _bidInfo = MutableLiveData<TopadsBidInfo>()
    val bidInfo: LiveData<TopadsBidInfo>
        get() = _bidInfo
    private val _autoAdsData: MutableLiveData<Result<TopAdsAutoAdsModel>> = MutableLiveData()
    val autoAdsData: LiveData<Result<TopAdsAutoAdsModel>>
        get() = _autoAdsData
    private val _budgetRecommendation: MutableLiveData<Result<TopadsGetBudgetRecommendationResponse>> =
        MutableLiveData()
    val budgetrecommendation: LiveData<Result<TopadsGetBudgetRecommendationResponse>>
        get() = _budgetRecommendation
    private val _topAdsGetAutoAds: MutableLiveData<AutoAdsResponse.TopAdsGetAutoAds> =
        MutableLiveData()
    val topAdsGetAutoAds: LiveData<AutoAdsResponse.TopAdsGetAutoAds> = _topAdsGetAutoAds


    fun loadData() {
        launchCatchError(dispatcher.io, {
            getBudgetRecommendations()
            getTopAdsDeposit()
            val statisticEstimatorJob = async { getStatisticsEstimator() }
            statisticEstimatorJob.await()
            val budgetInfoJob = async { getBudgetInfo() }
            budgetInfoJob.await()
            getAutoAds()
        }, {})
    }

    private fun getStatisticsEstimator() {
        launchCatchError(dispatcher.io, {
            val data = topadsStatisticsEstimationAttributeUseCase.execute(
                STATISTICS_ESTIMATION_TYPE, TOPADS_AUTO_PS_SOURCE
            )
            data.topadsStatisticsEstimationAttribute.data.firstOrNull()?.let {
                lowClickDivider = it.lowClickDivider
            }
        }, {})
    }

    private fun getAutoAds(){
        launchCatchError(block = {
            val response = withContext(dispatcher.io) {
                topAdsGetAutoAdsUseCase.setSource(GET_AUTO_ADS_SOURCE)
                topAdsGetAutoAdsUseCase.executeOnBackground()
            }
            _topAdsGetAutoAds.postValue(response)
        }) {}
    }

    fun getBudgetInfo(
    ) {
        launchCatchError(block = {
            val dummyId: MutableList<String> = mutableListOf()
            val suggestionsDefault = ArrayList<DataSuggestions>()
            suggestionsDefault.add(DataSuggestions(ParamObject.PRODUCT, dummyId))
            bidInfoUseCase.setParams(
                suggestionsDefault, AUTO_PS_REQUEST_TYPE, TOPADS_AUTO_PS_SOURCE
            )
            bidInfoUseCase.executeQuerySafeMode({
                _bidInfo.postValue(it.topadsBidInfo)
            }, {})
        }) {}
    }

    fun postAutoPs(budget: Int, toggle: String) {
        val param = AutoAdsParam(
            AutoAdsParam.Input(
                toggle, AUTO_PS_CHANNEL, budget, userSession.shopId, AUTO_PS_SOURCE
            )
        )
        topAdsQueryPostAutoadsUseCase.executeQuery(param) {
            _autoAdsData.postValue(it)
        }
    }

    private fun getBudgetRecommendations() {
        launchCatchError(block = {
            getBudgetRecommendationUseCase.setParams(
                source = TOPADS_AUTO_PS_SOURCE, requestType = AUTO_PS_REQUEST_TYPE
            )
            val response = getBudgetRecommendationUseCase.executeOnBackground()
            if (response.topadsGetBudgetRecommendation.errors.isEmpty())
                _budgetRecommendation.postValue(Success(response))
            else
                _budgetRecommendation.postValue(Fail(Throwable(response.topadsGetBudgetRecommendation.errors.firstOrNull()?.title)))
        }, onError = {
            Timber.e(it.message)
        })
    }

    private fun getTopAdsDeposit() {
        launchCatchError(block = {
            val response = topAdsGetShopDepositUseCase.executeOnBackground()
            deposits = response.topadsDashboardDeposits.data.amount
        }, onError = {
            it.printStackTrace()
        })
    }

    fun getPotentialImpression(budget: Int): String {
        return String.format("%,.0f", (budget / lowClickDivider).toDouble())
    }

    fun checkDeposits(): Boolean {
        return deposits > Int.ZERO
    }
}
