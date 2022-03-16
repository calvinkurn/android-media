package com.tokopedia.buyerorder.recharge.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.buyerorder.recharge.data.request.RechargeOrderDetailRequest
import com.tokopedia.buyerorder.recharge.domain.RechargeOrderDetailUseCase
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel
import com.tokopedia.digital.digital_recommendation.domain.DigitalRecommendationUseCase
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.recommendation_widget_common.domain.coroutines.GetRecommendationUseCase
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.widget.bestseller.mapper.BestSellerMapper
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 28/10/2021
 */
class RechargeOrderDetailViewModel @Inject constructor(
        private val orderDetailUseCase: RechargeOrderDetailUseCase,
        private val getRecommendationUseCaseCoroutine: GetRecommendationUseCase,
        private val bestSellerMapper: BestSellerMapper,
        private val recommendationUseCase: DigitalRecommendationUseCase,
        dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    private val _orderDetailData = MutableLiveData<Result<RechargeOrderDetailModel>>()
    val orderDetailData: LiveData<Result<RechargeOrderDetailModel>>
        get() = _orderDetailData

    private val _topadsData = MutableLiveData<Result<BestSellerDataModel>>()
    val topadsData: LiveData<Result<BestSellerDataModel>>
        get() = _topadsData

    private val _recommendationPosition = MutableLiveData<Result<List<String>>>()
    val recommendationPosition: LiveData<Result<List<String>>>
        get() = _recommendationPosition

    fun fetchData(requestParams: RechargeOrderDetailRequest) {
        launchCatchError(block = {
            val orderDetailValue = orderDetailUseCase.execute(requestParams)
            _orderDetailData.postValue(orderDetailValue)

            val skeletonData = recommendationUseCase.getRecommendationPosition(
                    DigitalRecommendationPage.RECOMMENDATION_SKELETON,
                    emptyList(),
                    emptyList()
            )
            _recommendationPosition.postValue(skeletonData)
        }) {
            it.printStackTrace()
        }
    }

    fun getDigitalRecommendationPosition(): Int =
            if (recommendationPosition.value != null &&
                    (recommendationPosition.value is Success) &&
                    (recommendationPosition.value as Success).data.isNotEmpty()) {
                if ((recommendationPosition.value as Success).data[0] == DigitalRecommendationUseCase.DG_PERSO_CHANNEL_NAME) {
                    FIRST_POSITION
                } else {
                    SECOND_POSITION
                }
            } else {
                ZERO_POSITION
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

    fun getRecommendationWidgetPositionData(): List<String>? =
            recommendationPosition.value?.let {
                if (it is Success) {
                    it.data
                } else {
                    null
                }
            }

    fun fetchTopAdsData() {
        launchCatchError(block = {
            val data = getRecommendationUseCaseCoroutine.getData(GetRecommendationRequestParam(
                    pageName = TOPADS_PAGE_NAME,
                    xSource = TOPADS_SOURCE
            ))
            val bestSellerDataModel = bestSellerMapper.mappingRecommendationWidget(data.first())
            _topadsData.postValue(Success(bestSellerDataModel))
        }) {
            _topadsData.postValue(Fail(it))
        }
    }

    companion object {
        private const val FIRST_POSITION = 1
        private const val SECOND_POSITION = 2
        private const val ZERO_POSITION = 0

        private const val TOPADS_PAGE_NAME = "dg_order_details"
        private const val TOPADS_SOURCE = "digital"
    }

}