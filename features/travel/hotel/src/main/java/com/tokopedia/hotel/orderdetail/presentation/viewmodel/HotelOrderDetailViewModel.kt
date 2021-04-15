package com.tokopedia.hotel.orderdetail.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 13/05/19
 */

class HotelOrderDetailViewModel @Inject constructor(dispatcher: CoroutineDispatchers,
                                                    private val useCase: GetHotelOrderDetailUseCase,
                                                    private val crossSellingUseCase: TravelCrossSellingUseCase)
    : BaseViewModel(dispatcher.io) {

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()
    val crossSellData = MutableLiveData<Result<TravelCrossSelling>>()

    fun getOrderDetail(orderDetailQuery: String, crossSellQuery: String?, orderId: String, orderCategory: String) {
        launch {
            orderDetailData.postValue(useCase.execute(orderDetailQuery, orderId, orderCategory, true))
            if (crossSellQuery != null) crossSellData.postValue(crossSellingUseCase.execute(crossSellQuery, orderId, TravelCrossSellingUseCase.PARAM_HOTEL_PRODUCT))
        }
    }

}