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
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
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
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListViewModel @Inject constructor(dispatcher: BuyerDispatcherProvider,
                                           private val uohListUseCase: UohListUseCase,
                                           private val getRecommendationUseCase: GetRecommendationUseCase,
                                           private val uohFinishOrderUseCase: UohFinishOrderUseCase,
                                           private val atcMultiProductsUseCase: AddToCartMultiUseCase,
                                           private val lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase,
                                           private val flightResendEmailUseCase: FlightResendEmailUseCase,
                                           private val trainResendEmailUseCase: TrainResendEmailUseCase,
                                           private val rechargeSetFailUseCase: RechargeSetFailUseCase,
                                           private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.ui()) {

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

    fun loadOrderList(orderQuery: String, paramOrder: UohListParam) {
        UohIdlingResource.increment()
        launch {
            _orderHistoryListResult.postValue(uohListUseCase.execute(paramOrder, orderQuery))
        }
    }

    fun loadRecommendationList(pageNumber: Int) {
        UohIdlingResource.increment()
        launch {
            val recommendationData = getRecommendationUseCase.getData(
                    GetRecommendationRequestParam(
                            pageNumber = pageNumber,
                            pageName = UohConsts.PAGE_NAME))
            _recommendationListResult.postValue(recommendationData.asSuccess())
        }
    }

    fun doFinishOrder(finishOrderQuery: String, paramFinishOrder: UohFinishOrderParam) {
        UohIdlingResource.increment()
        launch {
            _finishOrderResult.postValue(uohFinishOrderUseCase.execute(finishOrderQuery, paramFinishOrder))
        }
    }

    fun doAtcMulti(userId: String, atcMultiQuery: String, listParam: ArrayList<AddToCartMultiParam>) {
        UohIdlingResource.increment()
        launch {
            _atcMultiResult.postValue(atcMultiProductsUseCase.execute(userId, atcMultiQuery, listParam))
        }
    }

    fun doLsPrintFinishOrder(lsPrintQuery: String, verticalId: String) {
        UohIdlingResource.increment()
        launch {
            _lsPrintFinishOrderResult.postValue(lsPrintFinishOrderUseCase.execute(lsPrintQuery, verticalId))
        }
    }

    fun doFlightResendEmail(flightResendQuery: String, invoiceId: String, email: String) {
        UohIdlingResource.increment()
        launch {
            _flightResendEmailResult.postValue(flightResendEmailUseCase.execute(flightResendQuery, invoiceId, email))
        }
    }

    fun doTrainResendEmail(trainResendQuery: String, param: TrainResendEmailParam) {
        UohIdlingResource.increment()
        launch {
            _trainResendEmailResult.postValue(trainResendEmailUseCase.execute(trainResendQuery, param))
        }
    }

    fun doRechargeSetFail(rechargeSetFailQuery: String, orderId: Int) {
        UohIdlingResource.increment()

        launch {
            _rechargeSetFailResult.postValue(rechargeSetFailUseCase.execute(rechargeSetFailQuery, orderId))
        }
    }


    fun doAtc(atcParams: AddToCartRequestParams) {
        val requestParams = RequestParams.create()
        requestParams.putObject(AddToCartUseCase.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST, atcParams)
        atcUseCase.execute(requestParams, object : Subscriber<AddToCartDataModel>() {
            override fun onNext(addToCartDataModel: AddToCartDataModel?) {
                addToCartDataModel?.let {
                    _atcResult.postValue(Success(it))
                }
            }

            override fun onCompleted() {}

            override fun onError(e: Throwable?) {
                _atcResult.postValue(e?.let { Fail(it) })
            }
        })
    }
}