package com.tokopedia.hotel.evoucher.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.presentation.activity.HotelOrderDetailActivity
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by furqan on 23/05/19
 */
class HotelEVoucherViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                 private val useCase: GetHotelOrderDetailUseCase)
    : BaseViewModel(dispatcher) {

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()

    fun getOrderDetail(rawQuery: String, orderId: String) {
        launch {
            orderDetailData.value = useCase.execute(rawQuery, orderId, HotelOrderDetailActivity.HOTEL_ORDER_CATEGORY, false)
        }
    }

}