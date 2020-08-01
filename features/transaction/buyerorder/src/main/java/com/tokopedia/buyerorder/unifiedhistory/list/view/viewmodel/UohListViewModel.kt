package com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohConsts
import com.tokopedia.buyerorder.unifiedhistory.common.util.UohUtils.asSuccess
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.domain.UohListUseCase
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
                                           private val getRecommendationUseCase: GetRecommendationUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderHistoryListResult = MutableLiveData<Result<UohListOrder.Data.UohOrders>>()
    val orderHistoryListResult: LiveData<Result<UohListOrder.Data.UohOrders>>
        get() = _orderHistoryListResult

    private val _recommendationListResult = MutableLiveData<Result<List<RecommendationWidget>>>()
    val recommendationListResult: LiveData<Result<List<RecommendationWidget>>>
        get() = _recommendationListResult

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
}