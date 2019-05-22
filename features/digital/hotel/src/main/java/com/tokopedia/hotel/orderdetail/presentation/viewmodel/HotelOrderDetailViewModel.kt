package com.tokopedia.hotel.orderdetail.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.orderdetail.data.model.HotelOrderDetail
import com.tokopedia.hotel.orderdetail.usecase.GetHotelOrderDetailUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by jessica on 13/05/19
 */

class HotelOrderDetailViewModel @Inject constructor(dispatcher: CoroutineDispatcher,
                                                    @Named("dummy_order_detail")
                                                    private val dummyOrderDetail: String,
                                                    private val useCase: GetHotelOrderDetailUseCase) : BaseViewModel(dispatcher) {

    val orderDetailData = MutableLiveData<Result<HotelOrderDetail>>()

    fun getOrderDetail(rawQuery: String, orderId: String, orderCategory: String) {
        launch {
            orderDetailData.value = useCase.execute(rawQuery, orderId, orderCategory, true)
        }
//        val gson = Gson()
//        orderDetailData.value = Success(gson.fromJson(dummyOrderDetail, HotelOrderDetail.Response::class.java).response)
    }

}