package com.tokopedia.hotel.roomdetail.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartData
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.hotel.roomlist.usecase.HotelAddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by resakemal on 09/05/19
 */

class HotelRoomDetailViewModel @Inject constructor(
        val dispatcher: CoroutineDispatcher,
        private val useCase: HotelAddToCartUseCase)
    : BaseViewModel(dispatcher) {

    val addCartResponseResult = MutableLiveData<Result<HotelAddCartData.Response>>()

    fun addToCart(rawQuery: String, hotelAddCartParam: HotelAddCartParam) {
        launch {
            addCartResponseResult.value = useCase.execute(rawQuery, hotelAddCartParam)
        }
    }

    private fun mapToHotelRoomListPageModel(addCartParam: HotelAddCartParam): HotelRoomListPageModel {
        val param = HotelRoomListPageModel()
        param.propertyId = addCartParam.propertyId
        param.checkIn = addCartParam.checkIn
        param.checkOut = addCartParam.checkOut
        param.adult = addCartParam.adult
        param.child = 0
        param.room = addCartParam.roomCount
        return param
    }

    private fun getCurrentRoom(roomList: List<HotelRoom>, currentRoomName: String): Result<HotelRoom> {
        for (room in roomList) {
            if (room.roomInfo.name == currentRoomName) return Success(room)
        }
        return Fail(Throwable(FAIL_TO_REFRESH_ROOM_MESSAGE))
    }

    companion object {
        const val FAIL_TO_REFRESH_ROOM_MESSAGE = "failedToRefreshRoom"
    }
}