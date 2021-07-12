package com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohIdlingResource
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils.asSuccess
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.*
import com.tokopedia.buyerorder.unifiedhistory.list.domain.*
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import rx.Subscriber
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                           private val uohListUseCase: UohListUseCase,
                                           private val getRecommendationUseCase: GetRecommendationUseCase,
                                           private val uohFinishOrderUseCase: UohFinishOrderUseCase,
                                           private val atcMultiProductsUseCase: AddToCartMultiUseCase,
                                           private val lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase,
                                           private val flightResendEmailUseCase: FlightResendEmailUseCase,
                                           private val trainResendEmailUseCase: TrainResendEmailUseCase,
                                           private val rechargeSetFailUseCase: RechargeSetFailUseCase,
                                           private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

    private val _orderHistoryListResult = MutableLiveData<Result<UohListOrder.Data.UohOrders>>()
    val orderHistoryListResult: LiveData<Result<UohListOrder.Data.UohOrders>>
        get() = _orderHistoryListResult

    private val _recommendationListResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationListResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationListResult

    private val _finishOrderResult = MutableLiveData<Result<UohFinishOrder.Data.FinishOrderBuyer>>()
    val finishOrderResult: LiveData<Result<UohFinishOrder.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _atcMultiResult = MutableLiveData<Result<AtcMultiData>>()
    val atcMultiResult: LiveData<Result<AtcMultiData>>
        get() = _atcMultiResult

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

    private val _atcResult = MutableLiveData<Result<AddToCartDataModel>>()
    val atcResult: LiveData<Result<AddToCartDataModel>>
        get() = _atcResult

    fun loadOrderList(paramOrder: UohListParam) {
        UohIdlingResource.increment()
        launch {
            _orderHistoryListResult.value = uohListUseCase.executeSuspend(paramOrder)
            UohIdlingResource.decrement()
        }
    }

    fun loadRecommendationList(pageNumber: Int) {
        UohIdlingResource.increment()
        launch {
            try {
                val recommendationData = getRecommendationUseCase.getData(
                        GetRecommendationRequestParam(
                                pageNumber = pageNumber,
                                pageName = UohConsts.PAGE_NAME))
                _recommendationListResult.value = (recommendationData.asSuccess())
            } catch (e: Exception) {
                Timber.d(e)
                _recommendationListResult.value = Fail(e.fillInStackTrace())
            }
            UohIdlingResource.decrement()
        }
    }

    fun doFinishOrder(paramFinishOrder: UohFinishOrderParam) {
        UohIdlingResource.increment()
        launch {
            _finishOrderResult.value = (uohFinishOrderUseCase.executeSuspend(paramFinishOrder))
            UohIdlingResource.decrement()
        }
    }

    fun doAtcMulti(userId: String, atcMultiQuery: String, listParam: ArrayList<AddToCartMultiParam>) {
        UohIdlingResource.increment()
        launch {
            _atcMultiResult.value = (atcMultiProductsUseCase.execute(userId, atcMultiQuery, listParam))
            UohIdlingResource.decrement()
        }
    }

    fun doLsPrintFinishOrder(verticalId: String) {
        UohIdlingResource.increment()
        launch {
            _lsPrintFinishOrderResult.value = (lsPrintFinishOrderUseCase.executeSuspend(verticalId))
            UohIdlingResource.decrement()
        }
    }

    fun doFlightResendEmail(invoiceId: String, email: String) {
        UohIdlingResource.increment()
        launch {
            _flightResendEmailResult.value = (flightResendEmailUseCase.executeSuspend(invoiceId, email))
            UohIdlingResource.decrement()
        }
    }

    fun doTrainResendEmail(param: TrainResendEmailParam) {
        UohIdlingResource.increment()
        launch {
            _trainResendEmailResult.value = (trainResendEmailUseCase.executeSuspend(param))
            UohIdlingResource.decrement()
        }
    }

    fun doRechargeSetFail(orderId: Int) {
        UohIdlingResource.increment()
        launch {
            _rechargeSetFailResult.value = (rechargeSetFailUseCase.executeSuspend(orderId))
            UohIdlingResource.decrement()
        }
    }


    fun doAtc(atcParams: AddToCartRequestParams) {
        UohIdlingResource.increment()
        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)
        try {
            atcUseCase.execute(requestParams, object : Subscriber<AddToCartDataModel>() {
                override fun onNext(addToCartDataModel: AddToCartDataModel?) {
                    addToCartDataModel?.let {
                        _atcResult.value = (Success(it))
                        UohIdlingResource.decrement()
                    }
                }

                override fun onCompleted() {}

                override fun onError(e: Throwable?) {
                    _atcResult.value = (e?.let { Fail(it) })
                    UohIdlingResource.decrement()
                }
            })
        } catch (e: Exception) {
            Timber.d(e)
            _atcResult.value = Fail(e.fillInStackTrace())
            UohIdlingResource.decrement()
        }
    }
}