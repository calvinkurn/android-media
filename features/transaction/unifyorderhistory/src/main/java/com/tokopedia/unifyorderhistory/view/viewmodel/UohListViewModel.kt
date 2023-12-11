package com.tokopedia.unifyorderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartOccMultiRequestParams
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartOccMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.unifyorderhistory.data.model.FlightResendEmail
import com.tokopedia.unifyorderhistory.data.model.LsPrintData
import com.tokopedia.unifyorderhistory.data.model.PmsNotification
import com.tokopedia.unifyorderhistory.data.model.RechargeSetFailData
import com.tokopedia.unifyorderhistory.data.model.TrainResendEmail
import com.tokopedia.unifyorderhistory.data.model.TrainResendEmailParam
import com.tokopedia.unifyorderhistory.data.model.UohAtcBuyAgainWidgetData
import com.tokopedia.unifyorderhistory.data.model.UohFilterCategory
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrder
import com.tokopedia.unifyorderhistory.data.model.UohFinishOrderParam
import com.tokopedia.unifyorderhistory.data.model.UohListOrder
import com.tokopedia.unifyorderhistory.data.model.UohListParam
import com.tokopedia.unifyorderhistory.domain.FlightResendEmailUseCase
import com.tokopedia.unifyorderhistory.domain.GetUohFilterCategoryUseCase
import com.tokopedia.unifyorderhistory.domain.GetUohPmsCounterUseCase
import com.tokopedia.unifyorderhistory.domain.LsPrintFinishOrderUseCase
import com.tokopedia.unifyorderhistory.domain.RechargeSetFailUseCase
import com.tokopedia.unifyorderhistory.domain.TrainResendEmailUseCase
import com.tokopedia.unifyorderhistory.domain.UohFinishOrderUseCase
import com.tokopedia.unifyorderhistory.domain.UohListUseCase
import com.tokopedia.unifyorderhistory.util.UohConsts.BUY_AGAIN_PAGE_NAME
import com.tokopedia.unifyorderhistory.util.UohConsts.PAGE_NAME
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_ADS_COUNT
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_DIMEN_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_INVENTORY_ID
import com.tokopedia.unifyorderhistory.util.UohIdlingResource
import com.tokopedia.unifyorderhistory.util.UohUtils.asFail
import com.tokopedia.unifyorderhistory.util.UohUtils.asSuccess
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class UohListViewModel @Inject constructor(
    dispatcher: CoroutineDispatchers,
    private val getUohFilterCategoryUseCase: GetUohFilterCategoryUseCase,
    private val uohListUseCase: UohListUseCase,
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val uohFinishOrderUseCase: UohFinishOrderUseCase,
    private val atcMultiProductsUseCase: AddToCartMultiUseCase,
    private val addToCartOccUseCase: AddToCartOccMultiUseCase,
    private val lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase,
    private val flightResendEmailUseCase: FlightResendEmailUseCase,
    private val trainResendEmailUseCase: TrainResendEmailUseCase,
    private val rechargeSetFailUseCase: RechargeSetFailUseCase,
    private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
    private val atcUseCase: AddToCartUseCase,
    private val getUohPmsCounterUseCase: GetUohPmsCounterUseCase
) : BaseViewModel(dispatcher.main) {

    private val _filterCategoryResult = MutableLiveData<Result<UohFilterCategory>>()
    val filterCategoryResult: LiveData<Result<UohFilterCategory>>
        get() = _filterCategoryResult

    private val _orderHistoryListResult = MutableLiveData<Result<UohListOrder.UohOrders>>()
    val orderHistoryListResult: LiveData<Result<UohListOrder.UohOrders>>
        get() = _orderHistoryListResult

    private val _uohItemDelayResult = MutableLiveData<Result<Pair<UohListOrder, Int>>>()
    val uohItemDelayResult: LiveData<Result<Pair<UohListOrder, Int>>>
        get() = _uohItemDelayResult

    private val _recommendationListResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationListResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationListResult

    private val _finishOrderResult = MutableLiveData<Result<UohFinishOrder.FinishOrderBuyer>>()
    val finishOrderResult: LiveData<Result<UohFinishOrder.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _atcMultiResult = MutableLiveData<Result<AtcMultiData>>()
    val atcMultiResult: LiveData<Result<AtcMultiData>>
        get() = _atcMultiResult

    private val _atcOccMultiResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcOccMultiResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcOccMultiResult

    private val _lsPrintFinishOrderResult = MutableLiveData<Result<LsPrintData>>()
    val lsPrintFinishOrderResult: LiveData<Result<LsPrintData>>
        get() = _lsPrintFinishOrderResult

    private val _flightResendEmailResult = MutableLiveData<Result<FlightResendEmail.Data>>()
    val flightResendEmailResult: LiveData<Result<FlightResendEmail.Data>>
        get() = _flightResendEmailResult

    private val _trainResendEmailResult = MutableLiveData<Result<TrainResendEmail>>()
    val trainResendEmailResult: LiveData<Result<TrainResendEmail>>
        get() = _trainResendEmailResult

    private val _rechargeSetFailResult = MutableLiveData<Result<RechargeSetFailData>>()
    val rechargeSetFailResult: LiveData<Result<RechargeSetFailData>>
        get() = _rechargeSetFailResult

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    private val _tdnBannerResult = MutableLiveData<Result<TopAdsImageViewModel>>()
    val tdnBannerResult: LiveData<Result<TopAdsImageViewModel>>
        get() = _tdnBannerResult

    private val _getUohPmsCounterResult = MutableLiveData<Result<PmsNotification>>()
    val getUohPmsCounterResult: LiveData<Result<PmsNotification>>
        get() = _getUohPmsCounterResult

    private val _buyAgainWidgetResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val buyAgainWidgetResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _buyAgainWidgetResult

    private val _atcBuyAgainResult = MutableLiveData<UohAtcBuyAgainWidgetData>()
    val atcBuyAgainResult: LiveData<UohAtcBuyAgainWidgetData>
        get() = _atcBuyAgainResult

    fun loadOrderList(paramOrder: UohListParam) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = uohListUseCase(Pair(paramOrder, false))
            _orderHistoryListResult.value = Success(result.uohOrders)
            UohIdlingResource.decrement()
        }, onError = {
                _orderHistoryListResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun loadUohItemDelay(isDelay: Boolean, paramOrder: UohListParam, index: Int) {
        // provide flag isDelay for hansel
        launchCatchError(block = {
            val result = uohListUseCase(Pair(paramOrder, isDelay))
            _uohItemDelayResult.value = Success(Pair(result, index))
        }, onError = {
                _uohItemDelayResult.value = Fail(it)
            })
    }

    fun doFinishOrder(paramFinishOrder: UohFinishOrderParam) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = uohFinishOrderUseCase(paramFinishOrder)
            result.finishOrderBuyer.orderId = paramFinishOrder.orderId
            _finishOrderResult.value = Success(result.finishOrderBuyer)
            UohIdlingResource.decrement()
        }, onError = {
                _finishOrderResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun loadFilterCategory() {
        launchCatchError(block = {
            val result = getUohFilterCategoryUseCase(Unit)
            _filterCategoryResult.value = Success(result)
        }, onError = {
                _filterCategoryResult.value = Fail(it)
            })
    }

    fun doTrainResendEmail(param: TrainResendEmailParam) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = trainResendEmailUseCase(param)
            _trainResendEmailResult.value = Success(result)
            UohIdlingResource.decrement()
        }, onError = {
                _trainResendEmailResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun doRechargeSetFail(orderId: Int) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = rechargeSetFailUseCase(orderId)
            _rechargeSetFailResult.value = Success(result)
            UohIdlingResource.decrement()
        }, onError = {
                _rechargeSetFailResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun doLsPrintFinishOrder(verticalId: String) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = lsPrintFinishOrderUseCase(verticalId)
            _lsPrintFinishOrderResult.value = Success(result)
            UohIdlingResource.decrement()
        }, onError = {
                _lsPrintFinishOrderResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun loadPmsCounter(shopId: String) {
        launch {
            _getUohPmsCounterResult.value = getUohPmsCounterUseCase.executeSuspend(shopId)
        }
    }

    fun loadRecommendationList(pageNumber: Int) {
        UohIdlingResource.increment()
        launch {
            try {
                val recommendationData = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = pageNumber,
                        pageName = PAGE_NAME
                    )
                )
                _recommendationListResult.value = (recommendationData.asSuccess())
            } catch (e: Exception) {
                Timber.d(e)
                _recommendationListResult.value = Fail(e.fillInStackTrace())
            }
            UohIdlingResource.decrement()
        }
    }

    fun loadBuyAgain() {
        UohIdlingResource.increment()
        launch {
            try {
                val recommendationData = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                        pageNumber = 0,
                        pageName = BUY_AGAIN_PAGE_NAME
                    )
                )
                _buyAgainWidgetResult.value = (recommendationData.asSuccess())
            } catch (e: Exception) {
                Timber.d(e)
                _buyAgainWidgetResult.value = Fail(e.fillInStackTrace())
            }
            UohIdlingResource.decrement()
        }
    }

    fun doAtcMulti(userId: String, atcMultiQuery: String, listParam: ArrayList<AddToCartMultiParam>, verticalCategory: String) {
        UohIdlingResource.increment()
        launch {
            val result = (atcMultiProductsUseCase.execute(userId, atcMultiQuery, listParam))
            _atcMultiResult.value = result
            UohIdlingResource.decrement()
        }
    }

    fun doAtcBuyAgain(
        userId: String,
        atcMultiQuery: String,
        listParam: ArrayList<AddToCartMultiParam>,
        recommItem: RecommendationItem,
        index: Int
    ) {
        UohIdlingResource.increment()
        launch {
            val result = (atcMultiProductsUseCase.execute(userId, atcMultiQuery, listParam))
            _atcBuyAgainResult.value = UohAtcBuyAgainWidgetData(recommItem, index, result)
            UohIdlingResource.decrement()
        }
    }

    fun doAtcOccMulti(atcOccParams: AddToCartOccMultiRequestParams) {
        UohIdlingResource.increment()
        launchCatchError(block = {
            val result = addToCartOccUseCase.setParams(atcOccParams).executeOnBackground()
                .mapToAddToCartDataModel()
            if (result.isStatusError()) {
                val errorMessage = result.getAtcErrorMessage() ?: ""
                _atcOccMultiResult.postValue(MessageErrorException(errorMessage).asFail())
            } else {
                _atcOccMultiResult.postValue(result.asSuccess())
            }
            UohIdlingResource.decrement()
        }, onError = {
                _atcOccMultiResult.value = Fail(it)
                UohIdlingResource.decrement()
            })
    }

    fun doFlightResendEmail(invoiceId: String, email: String) {
        UohIdlingResource.increment()
        launch {
            _flightResendEmailResult.value = (flightResendEmailUseCase.executeSuspend(invoiceId, email))
            UohIdlingResource.decrement()
        }
    }

    fun loadTdnBanner() {
        launch {
            try {
                val params = topAdsImageViewUseCase.getQueryMap("", TDN_INVENTORY_ID, "", TDN_ADS_COUNT, TDN_DIMEN_ID, "", "", "")
                val tdnData = topAdsImageViewUseCase.getImageData(params)
                if (tdnData.isNotEmpty()) _tdnBannerResult.value = (tdnData[0].asSuccess())
            } catch (e: Exception) {
                Timber.d(e)
                _recommendationListResult.value = Fail(e.fillInStackTrace())
            }
        }
    }

    fun doAtc(atcParams: AddToCartRequestParams) {
        UohIdlingResource.increment()
        launch {
            try {
                atcUseCase.setParams(atcParams)
                val result = atcUseCase.executeOnBackground()
                _atcResult.value = Success(result)
                UohIdlingResource.decrement()
            } catch (e: Exception) {
                Timber.d(e)
                _atcResult.value = Fail(e)
                UohIdlingResource.decrement()
            }
        }
    }
}
