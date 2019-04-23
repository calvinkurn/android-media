package com.tokopedia.hotel.roomlist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.roomlist.data.model.*
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListViewModel @Inject constructor(
        val dispatcher: CoroutineDispatcher,
        val useCase: GetHotelRoomListUseCase) : BaseViewModel(dispatcher){

    var roomList: List<HotelRoom> = listOf()
    val roomListResult = MutableLiveData<Result<MutableList<HotelRoom>>>()

    var filterFreeBreakfast = false
    var filterFreeCancelable = false
    var filterPayInHotel = false
    var isFilter = false

    fun getRoomList(rawQuery: String, hotelRoomListPageModel: HotelRoomListPageModel, fromCloud: Boolean = true) {
        isFilter = false
        launch {
            roomListResult.value = useCase.execute(rawQuery, hotelRoomListPageModel, fromCloud)
            doFilter()
        }
    }

    fun clickFilter(clickFreeBreakfast: Boolean = false, clickFreeCancelable: Boolean = false, clickPayInHotel: Boolean = false) {
        if (clickFreeBreakfast) filterFreeBreakfast = !filterFreeBreakfast
        if (clickFreeCancelable) filterFreeCancelable = !filterFreeCancelable
        if (clickPayInHotel) filterPayInHotel = !filterPayInHotel
        doFilter()
    }

    fun doFilter() {
        if (filterFreeBreakfast || filterFreeCancelable || filterPayInHotel)  {
            isFilter = true
            var list: MutableList<HotelRoom> = arrayListOf()
            for (room in roomList) {
                var valid = true
                if (filterPayInHotel && !true) valid = false
                if (filterFreeCancelable && !room.refundInfo.isRefundable) valid = false
                if (filterFreeBreakfast && !room.breakfastInfo.isBreakfastIncluded) valid = false
                if (valid) list.add(room)
            }
            roomListResult.value = Success(list)
        } else {
            isFilter = false
            roomListResult.value = Success(roomList.toMutableList())
        }
    }
}