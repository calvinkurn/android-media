package com.tokopedia.unifyorderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.topads.sdk.domain.interactor.TopAdsImageViewUseCase
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel
import com.tokopedia.topads.sdk.utils.TopAdsIrisSession
import com.tokopedia.unifyorderhistory.analytics.UohAnalytics
import com.tokopedia.unifyorderhistory.analytics.data.model.ECommerceAdd
import com.tokopedia.unifyorderhistory.data.model.*
import com.tokopedia.unifyorderhistory.domain.*
import com.tokopedia.unifyorderhistory.util.UohConsts
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_ADS_COUNT
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_DIMEN_ID
import com.tokopedia.unifyorderhistory.util.UohConsts.TDN_INVENTORY_ID
import com.tokopedia.unifyorderhistory.util.UohIdlingResource
import com.tokopedia.unifyorderhistory.util.UohUtils.asSuccess
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                           private val getUohFilterCategoryUseCase: GetUohFilterCategoryUseCase,
                                           private val uohListUseCase: UohListUseCase,
                                           private val getRecommendationUseCase: GetRecommendationUseCase,
                                           private val uohFinishOrderUseCase: UohFinishOrderUseCase,
                                           private val atcMultiProductsUseCase: AddToCartMultiUseCase,
                                           private val lsPrintFinishOrderUseCase: LsPrintFinishOrderUseCase,
                                           private val flightResendEmailUseCase: FlightResendEmailUseCase,
                                           private val trainResendEmailUseCase: TrainResendEmailUseCase,
                                           private val rechargeSetFailUseCase: RechargeSetFailUseCase,
                                           private val topAdsImageViewUseCase: TopAdsImageViewUseCase,
                                           private val irisSession: TopAdsIrisSession,
                                           private val atcUseCase: AddToCartUseCase) : BaseViewModel(dispatcher.main) {

    private val _filterCategoryResult = MutableLiveData<Result<UohFilterCategory.Data>>()
    val filterCategoryResult: LiveData<Result<UohFilterCategory.Data>>
        get() = _filterCategoryResult

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

    private val _tdnBannerResult = MutableLiveData<Result<TopAdsImageViewModel>>()
    val tdnBannerResult: LiveData<Result<TopAdsImageViewModel>>
        get() = _tdnBannerResult

    fun loadFilterCategory() {
        launch {
            _filterCategoryResult.value = getUohFilterCategoryUseCase.executeSuspend()
        }
    }

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

    fun doAtcMulti(userId: String, atcMultiQuery: String, listParam: ArrayList<AddToCartMultiParam>, verticalCategory: String) {
        UohIdlingResource.increment()
        launch {
            val result = (atcMultiProductsUseCase.execute(userId, atcMultiQuery, listParam))
            _atcMultiResult.value = result

            // analytics
            val arrayListProducts = arrayListOf<ECommerceAdd.Add.Products>()
            listParam.forEachIndexed { index, product ->
                arrayListProducts.add(
                        ECommerceAdd.Add.Products(
                                name = product.productName,
                                id = product.productId.toString(),
                                price = product.productPrice.toString(),
                                quantity = product.qty.toString(),
                                dimension79 = product.shopId.toString()
                        ))
            }

            if (result is Success) {
                UohAnalytics.clickBeliLagiOnOrderCardMP("", userId, arrayListProducts,
                        verticalCategory, result.data.atcMulti.buyAgainData.listProducts.firstOrNull()?.cartId.toString())
            }

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

    fun loadTdnBanner() {
        launch {
            try {
                val params = topAdsImageViewUseCase.getQueryMap("", TDN_INVENTORY_ID, "", TDN_ADS_COUNT, TDN_DIMEN_ID, "", "", "")
                val tdnData = topAdsImageViewUseCase.getImageData(params,irisSession.getSessionId())
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