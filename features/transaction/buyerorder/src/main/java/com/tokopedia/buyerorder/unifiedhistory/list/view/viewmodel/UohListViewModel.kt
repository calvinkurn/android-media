package com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonArray
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils.asSuccess
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.buyerorder.unifiedhistory.list.domain.*
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListViewModel @Inject constructor(dispatcher: BuyerDispatcherProvider,
                                           private val uohListUseCase: UohListUseCase,
                                           private val getRecommendationUseCase: GetRecommendationUseCase,
                                           private val uohFinishOrderUseCase: UohFinishOrderUseCase,
                                           private val atcMultiProductsUseCase: AtcMultiProductsUseCase,
                                           private val lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase,
                                           private val flightResendEmailUseCase: FlightResendEmailUseCase,
                                           private val trainResendEmailUseCase: TrainResendEmailUseCase,
                                           private val rechargeSetFailUseCase: RechargeSetFailUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderHistoryListResult = MutableLiveData<Result<UohListOrder.Data.UohOrders>>()
    val orderHistoryListResult: LiveData<Result<UohListOrder.Data.UohOrders>>
        get() = _orderHistoryListResult

    private val _recommendationListResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationListResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationListResult

    private val _finishOrderResult = MutableLiveData<Result<UohFinishOrder.Data.FinishOrderBuyer>>()
    val finishOrderResult: LiveData<Result<UohFinishOrder.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _atcResult = MutableLiveData<Result<AtcMultiData>>()
    val atcResult: LiveData<Result<AtcMultiData>>
        get() = _atcResult

    private val _lsPrintFinishOrderResult = MutableLiveData<Result<LsPrintData.Data>>()
    val lsPrintFinishOrderResult: LiveData<Result<LsPrintData.Data>>
        get() = _lsPrintFinishOrderResult

    private val _flightResendEmailResult = MutableLiveData<Result<FlightResendEmail.Data>>()
    val flightResendEmailResult: LiveData<Result<FlightResendEmail.Data>>
        get() = _flightResendEmailResult

    private val _trainResendEmailResult = MutableLiveData<Result<TrainResendEmail.Data>>()
    val trainResendEmailResult: LiveData<Result<TrainResendEmail.Data>>
        get() = _trainResendEmailResult

    private val _rechargeSetFailResult = MutableLiveData<Result<RechargeSetFailData.Data>>()
    val rechargeSetFailResult: LiveData<Result<RechargeSetFailData.Data>>
        get() = _rechargeSetFailResult

    fun loadOrderList(orderQuery: String, paramOrder: UohListParam) {
        launch {
            _orderHistoryListResult.postValue(uohListUseCase.execute(paramOrder, orderQuery))
        }
    }

    fun loadRecommendationList(pageNumber: Int) {
        launch {
            val recommendationData = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                            pageNumber = pageNumber,
                            pageName = UohConsts.PAGE_NAME))
            _recommendationListResult.postValue(recommendationData.asSuccess())
        }
    }

    fun doFinishOrder(finishOrderQuery: String, paramFinishOrder: UohFinishOrderParam) {
        launch {
            _finishOrderResult.postValue(uohFinishOrderUseCase.execute(finishOrderQuery, paramFinishOrder))
        }
    }

    fun doAtc(atcMultiQuery: String, listParam: JsonArray) {
        launch {
            _atcResult.postValue(atcMultiProductsUseCase.execute(atcMultiQuery, listParam))
        }
    }

    fun doLsPrintFinishOrder(lsPrintQuery: String, verticalId: String) {
        launch {
            _lsPrintFinishOrderResult.postValue(lsPrintFinishOrderUseCase.execute(lsPrintQuery, verticalId))
        }
    }

    fun doFlightResendEmail(flightResendQuery: String, invoiceId: String, email: String) {
        launch {
            _flightResendEmailResult.postValue(flightResendEmailUseCase.execute(flightResendQuery, invoiceId, email))
        }
    }

    fun doTrainResendEmail(trainResendQuery: String, param: TrainResendEmailParam) {
        launch {
            _trainResendEmailResult.postValue(trainResendEmailUseCase.execute(trainResendQuery, param))
        }
    }

    fun doRechargeSetFail(rechargeSetFailQuery: String, orderId: Int) {
        launch {
            _rechargeSetFailResult.postValue(rechargeSetFailUseCase.execute(rechargeSetFailQuery, orderId))
        }
    }
}