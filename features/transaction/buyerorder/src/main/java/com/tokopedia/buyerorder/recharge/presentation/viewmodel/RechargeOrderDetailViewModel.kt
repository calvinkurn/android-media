package com.tokopedia.buyerorder.recharge.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.domain.RechargeOrderDetailUseCase
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.async
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailViewModel @Inject constructor(
        private val orderDetailUseCase: RechargeOrderDetailUseCase,
        private val getRecommendationUseCaseCoroutine: GetRecommendationUseCase,
        private val bestSellerMapper: BestSellerMapper,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _orderDetailData = MutableLiveData<Result<RechargeOrderDetailModel>>()
    val orderDetailData: LiveData<Result<RechargeOrderDetailModel>>
        get() = _orderDetailData

    private val _topadsData = MutableLiveData<Result<BestSellerDataModel>>()
    val topadsData: LiveData<Result<BestSellerDataModel>>
        get() = _topadsData

    fun fetchData(requestParams: RechargeOrderDetailRequest) {
        launchCatchError(block = {
            val orderDetailDeferred = fetchOrderDetailDataAsync(requestParams)
            _orderDetailData.postValue(orderDetailDeferred.await())
            fetchTopAdsData()
        }) {
            it.printStackTrace()
        }
    }

    fun getOrderDetailResultData(): RechargeOrderDetailModel? =
            orderDetailData.value?.let {
                if (it is Success) {
                    it.data
                } else {
                    null
                }
            }

    fun getTopAdsData(): BestSellerDataModel? =
            topadsData.value?.let {
                if (it is Success) {
                    it.data
                } else {
                    null
                }
            }

    private fun fetchOrderDetailDataAsync(requestParams: RechargeOrderDetailRequest) =
            async {
                orderDetailUseCase.execute(requestParams)
            }

    private suspend fun fetchTopAdsData() {
        try {
            val data = getRecommendationUseCaseCoroutine.getData(GetRecommendationRequestParam())
            val bestSellerDataModel = bestSellerMapper.mappingRecommendationWidget(data.first())
            _topadsData.value = Success(bestSellerDataModel)
        } catch (t: Throwable) {
            Fail(t)
        }
    }

}