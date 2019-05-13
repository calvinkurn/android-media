package com.tokopedia.hotel.booking.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.usecase.GetHotelCartDataUseCase
import com.tokopedia.hotel.roomlist.data.model.*
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
import com.tokopedia.hotel.roomlist.usecase.HotelAddToCartUseCase
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

/**
 * @author by resakemal on 13/05/19
 */

class HotelBookingViewModel @Inject constructor(
        val dispatcher: CoroutineDispatcher,
        private val useCase: GetHotelCartDataUseCase)
    : BaseViewModel(dispatcher) {

    val hotelCartResult = MutableLiveData<Result<HotelCart>>()

    fun getcartData(rawQuery: String, cartId: String, fromCloud: Boolean = true) {
        launch {
            hotelCartResult.value = useCase.execute(rawQuery, cartId, fromCloud)
        }
    }
}