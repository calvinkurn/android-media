package com.tokopedia.topads.auto.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_CHANNEL
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_SOURCE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.AUTO_PS_TOGGLE_ON
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.BID_INFO_REQUEST_TYPE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.STATISTICS_ESTIMATION_TYPE
import com.tokopedia.topads.auto.data.TopadsAutoPsConstants.TOPADS_AUTO_PS_SOURCE
import com.tokopedia.topads.auto.domain.usecase.TopadsStatisticsEstimationAttributeUseCase
import com.tokopedia.topads.common.data.internal.ParamObject
import com.tokopedia.topads.common.data.model.AutoAdsParam
import com.tokopedia.topads.common.data.model.DataSuggestions
import com.tokopedia.topads.common.data.response.TopadsBidInfo
import com.tokopedia.topads.common.domain.interactor.BidInfoUseCase
import com.tokopedia.topads.common.domain.model.TopAdsAutoAdsModel
import com.tokopedia.topads.common.domain.usecase.TopAdsQueryPostAutoadsUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.user.session.UserSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import javax.inject.Inject

class AutoPsViewModel @Inject constructor(
    private val dispatcher: CoroutineDispatchers,
    private val userSession: UserSession,
    private val topadsStatisticsEstimationAttributeUseCase: TopadsStatisticsEstimationAttributeUseCase,
    private val bidInfoUseCase: BidInfoUseCase,
    private val topAdsQueryPostAutoadsUseCase: TopAdsQueryPostAutoadsUseCase,
) : BaseViewModel(dispatcher.main), CoroutineScope {

    private var lowClickDivider = Int.ONE
    private val _bidInfo = MutableLiveData<TopadsBidInfo>()
    val bidInfo: LiveData<TopadsBidInfo>
        get() = _bidInfo
    private val _autoAdsData : MutableLiveData<Result<TopAdsAutoAdsModel>> = MutableLiveData()
    val autoAdsData : LiveData<Result<TopAdsAutoAdsModel>> = _autoAdsData

    fun loadData() {
        launchCatchError(dispatcher.io, {
            val statisticEstimatorJob = async { getStatisticsEstimator() }
            statisticEstimatorJob.await()
            getBudgetInfo()
        }, {})
    }

    private fun getStatisticsEstimator() {
        launchCatchError(dispatcher.io, {
            val data = topadsStatisticsEstimationAttributeUseCase.execute(
                STATISTICS_ESTIMATION_TYPE,
                TOPADS_AUTO_PS_SOURCE
            )
            data.topadsStatisticsEstimationAttribute.data.firstOrNull()?.let {
                lowClickDivider = it.lowClickDivider
            }
        }, {})
    }

    fun getBudgetInfo(
    ) {
        launchCatchError(block = {
            val dummyId: MutableList<String> = mutableListOf()
            val suggestionsDefault = ArrayList<DataSuggestions>()
            suggestionsDefault.add(DataSuggestions(ParamObject.PRODUCT, dummyId))
            bidInfoUseCase.setParams(
                suggestionsDefault,
                BID_INFO_REQUEST_TYPE,
                TOPADS_AUTO_PS_SOURCE
            )
            bidInfoUseCase.executeQuerySafeMode({
                _bidInfo.postValue(it.topadsBidInfo)
            }, {})
        }) {}
    }

    fun postAutoPs(budget: Int){
        val param = AutoAdsParam(AutoAdsParam.Input(AUTO_PS_TOGGLE_ON, AUTO_PS_CHANNEL,
            budget, userSession.shopId, AUTO_PS_SOURCE
        ))
        topAdsQueryPostAutoadsUseCase.executeQuery(param){
            _autoAdsData.postValue(it)
        }
    }

    fun getPotentialImpression(budget: Int): String {
        return String.format("%,.0f", (budget / lowClickDivider).toDouble())
    }
}
