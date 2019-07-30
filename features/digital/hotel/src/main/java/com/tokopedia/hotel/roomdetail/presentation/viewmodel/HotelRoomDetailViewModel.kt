package com.tokopedia.hotel.roomdetail.presentation.viewmodel

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
}