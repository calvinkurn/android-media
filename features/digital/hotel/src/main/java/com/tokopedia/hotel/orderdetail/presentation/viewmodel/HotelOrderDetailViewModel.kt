package com.tokopedia.hotel.orderdetail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.data.entity.TravelCrossSelling
import com.tokopedia.common.travel.domain.TravelCrossSellingUseCase
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 13/05/19
 */

class HotelOrderDetailViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                    private val useCase: GetHotelOrderDetailUseCase,
                                                    private val crossSellingUseCase: TravelCrossSellingUseCase)
    : BaseViewModel(dispatcher) {

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()
    val crossSellData = MutableLiveData<Result<TravelCrossSelling>>()

    fun getOrderDetail(orderDetailQuery: String, crossSellQuery: String, orderId: String, orderCategory: String) {
        launch {
            orderDetailData.value = useCase.execute(orderDetailQuery, orderId, orderCategory, true)
            crossSellData.value = crossSellingUseCase.execute(crossSellQuery, orderId, TravelCrossSellingUseCase.PARAM_HOTEL_PRODUCT)
        }
    }

}