package com.tokopedia.hotel.roomdetail.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.common.travel.utils.TravelDispatcherProvider
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartData
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.usecase.HotelAddToCartUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author by resakemal on 09/05/19
 */

class HotelRoomDetailViewModel @Inject constructor(
        dispatcher: TravelDispatcherProvider,
        private val useCase: HotelAddToCartUseCase)
    : BaseViewModel(dispatcher.io()) {

    val addCartResponseResult = MutableLiveData<Result<HotelAddCartData.Response>>()

    fun addToCart(rawQuery: String, hotelAddCartParam: HotelAddCartParam) {
        launch {
            addCartResponseResult.postValue(useCase.execute(rawQuery, hotelAddCartParam))
        }
    }
}