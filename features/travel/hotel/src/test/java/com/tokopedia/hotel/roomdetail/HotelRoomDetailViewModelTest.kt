package com.tokopedia.hotel.roomdetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.hotel.roomdetail.presentation.viewmodel.HotelRoomDetailViewModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartData
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.usecase.HotelAddToCartUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * @author by jessica on 27/03/20
 */

@RunWith(JUnit4::class)
class HotelRoomDetailViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelRoomDetailViewModel: HotelRoomDetailViewModel

    @RelaxedMockK
    lateinit var hotelAddToCartUseCase: HotelAddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelRoomDetailViewModel = HotelRoomDetailViewModel(dispatcher, hotelAddToCartUseCase)
    }

    @Test
    fun addToCart_ifAtcSuccess() {
        //given
        coEvery { hotelAddToCartUseCase.execute(any(), any()) } returns Success(HotelAddCartData.Response(HotelAddCartData("aab")))

        //when
        hotelRoomDetailViewModel.addToCart("", HotelAddCartParam())

        //then
        assert(hotelRoomDetailViewModel.addCartResponseResult.value is Success)
        assert((hotelRoomDetailViewModel.addCartResponseResult.value as Success<HotelAddCartData.Response>).data.response.cartId == "aab")
    }

    @Test
    fun addToCart_ifAtcFail() {
        //given
        coEvery { hotelAddToCartUseCase.execute(any(), any()) } returns Fail(Throwable())

        //when
        hotelRoomDetailViewModel.addToCart("", HotelAddCartParam())

        //then
        assert(hotelRoomDetailViewModel.addCartResponseResult.value is Fail)
    }

}