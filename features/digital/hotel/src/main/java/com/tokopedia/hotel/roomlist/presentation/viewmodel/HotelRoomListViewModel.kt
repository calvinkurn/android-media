package com.tokopedia.hotel.roomlist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.roomlist.data.model.*
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.hotel.roomlist.usecase.HotelAddToCartUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListViewModel @Inject constructor(
        val dispatcher: CoroutineDispatcher,
        private val useCase: GetHotelRoomListUseCase,
        private val addToCartUsecase: HotelAddToCartUseCase)
    : BaseViewModel(dispatcher) {

    var roomList: List<HotelRoom> = listOf()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()
    val addCartResponseResult = MutableLiveData<Result<HotelAddCartData.Response>>()

    var filterFreeBreakfast = false
    var filterFreeCancelable = false
    var isFilter = false

    fun getRoomList(rawQuery: String, hotelRoomListPageModel: HotelRoomListPageModel, fromCloud: Boolean = true) {
        isFilter = false
        launch {
            roomListResult.value = useCase.execute(rawQuery, hotelRoomListPageModel, fromCloud)
            doFilter()
        }
    }

    fun clickFilter(clickFreeBreakfast: Boolean = false, clickFreeCancelable: Boolean = false) {
        if (clickFreeBreakfast) filterFreeBreakfast = !filterFreeBreakfast
        if (clickFreeCancelable) filterFreeCancelable = !filterFreeCancelable
        doFilter()
    }

    fun doFilter() {
        if (filterFreeBreakfast || filterFreeCancelable) {
            isFilter = true
            var list: MutableList<HotelRoom> = arrayListOf()
            for (room in roomList) {
                var valid = true
                if (filterFreeCancelable && !room.refundInfo.isRefundable) valid = false
                if (filterFreeBreakfast && !room.breakfastInfo.isBreakfastIncluded) valid = false
                if (valid) list.add(room)
            }
            roomListResult.value = Success(list)
        } else {
            roomListResult.value = Success(roomList.toMutableList())
        }
    }

    fun addToCart(rawQuery: String, hotelAddCartParam: HotelAddCartParam) {
        launch {
            addCartResponseResult.value = addToCartUsecase.execute(rawQuery, hotelAddCartParam)
        }
    }

    fun clearFilter() {
        filterFreeCancelable = false
        filterFreeBreakfast = false
        roomListResult.value = Success(roomList.toMutableList())
    }
}