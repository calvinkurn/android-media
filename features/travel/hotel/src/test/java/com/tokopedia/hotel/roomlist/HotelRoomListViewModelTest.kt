package com.tokopedia.hotel.roomlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartData
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.presentation.viewmodel.HotelRoomListViewModel
import com.tokopedia.hotel.roomlist.usecase.GetHotelRoomListUseCase
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
class HotelRoomListViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val dispatcher = CoroutineTestDispatchersProvider
    private lateinit var hotelRoomListViewModel: HotelRoomListViewModel

    @RelaxedMockK
    lateinit var getHotelRoomListUseCase: GetHotelRoomListUseCase

    @RelaxedMockK
    lateinit var hotelAddToCartUseCase: HotelAddToCartUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        hotelRoomListViewModel = HotelRoomListViewModel(dispatcher, getHotelRoomListUseCase, hotelAddToCartUseCase)
    }

    @Test
    fun initialState() {
        //when

        //then
        assert(!hotelRoomListViewModel.filterFreeBreakfast)
        assert(!hotelRoomListViewModel.filterFreeCancelable)
        assert(!hotelRoomListViewModel.isFilter)
        assert(hotelRoomListViewModel.roomList.isEmpty())
    }

    @Test
    fun assignFilterState() {
        //when
        hotelRoomListViewModel.filterFreeBreakfast = true
        hotelRoomListViewModel.filterFreeCancelable = true
        hotelRoomListViewModel.isFilter = true

        //then
        assert(hotelRoomListViewModel.filterFreeBreakfast)
        assert(hotelRoomListViewModel.filterFreeCancelable)
        assert(hotelRoomListViewModel.isFilter)
    }

    @Test
    fun getRoomList_shouldReturnSuccess() {
        //given
        val hotelRooms = mutableListOf(HotelRoom("1"),
                HotelRoom("2"), HotelRoom("3"))
        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Success(hotelRooms)

        //when
        hotelRoomListViewModel.roomList = hotelRooms
        hotelRoomListViewModel.getRoomList("", HotelRoomListPageModel())

        //then
        assert(hotelRoomListViewModel.roomList.size == 3)
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 3)
    }

    @Test
    fun getRoomList_shouldReturnFail() {
        //given
        coEvery {
            getHotelRoomListUseCase.execute(any(), any(), any())
        } returns Fail(Throwable())

        //when
        hotelRoomListViewModel.getRoomList("", HotelRoomListPageModel(), false)

        //then
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.isEmpty())
    }

    @Test
    fun clickFilter_ifFreeBreakfast_shouldShowCorrectData() {
        //given
        val roomList = mutableListOf<HotelRoom>()
        roomList.add(HotelRoom(roomId = "1", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "2", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))
        roomList.add(HotelRoom(roomId = "3", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "4", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))

        //when
        hotelRoomListViewModel.roomList = roomList
        hotelRoomListViewModel.clickFilter(true)

        //then
        assert(hotelRoomListViewModel.isFilter)
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 2)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data[0].roomId == "1")
    }

    @Test
    fun clickFilter_ifRefundable_shouldShowCorrectData() {
        //given
        val roomList = mutableListOf<HotelRoom>()
        roomList.add(HotelRoom(roomId = "1", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "2", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))
        roomList.add(HotelRoom(roomId = "3", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "4", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))

        //when
        hotelRoomListViewModel.roomList = roomList
        hotelRoomListViewModel.clickFilter(clickFreeCancelable = true)

        //then
        assert(hotelRoomListViewModel.isFilter)
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 2)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data[1].roomId == "3")
    }

    @Test
    fun clickFilter_ifFreeBfastAndRefundable_shouldShowCorrectData() {
        //given
        val roomList = mutableListOf<HotelRoom>()
        roomList.add(HotelRoom(roomId = "1", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "2", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))
        roomList.add(HotelRoom(roomId = "3", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "4", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))

        //when
        hotelRoomListViewModel.roomList = roomList
        hotelRoomListViewModel.clickFilter(clickFreeCancelable = true, clickFreeBreakfast = true)

        //then
        assert(hotelRoomListViewModel.isFilter)
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 1)
    }

    @Test
    fun clickFilter_ifNotRefundableAndNotFreeBfast_shouldShowCorrectData() {
        //given
        val roomList = mutableListOf<HotelRoom>()
        roomList.add(HotelRoom(roomId = "1", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "2", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))
        roomList.add(HotelRoom(roomId = "3", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "4", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))

        //when
        hotelRoomListViewModel.roomList = roomList
        hotelRoomListViewModel.clickFilter()

        //then
        assert(!hotelRoomListViewModel.isFilter)
        assert(!hotelRoomListViewModel.filterFreeBreakfast)
        assert(!hotelRoomListViewModel.filterFreeCancelable)
        assert(hotelRoomListViewModel.roomListResult.value is Success)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 4)
    }

    @Test
    fun addToCart_ifAtcSuccess() {
        //given
        coEvery { hotelAddToCartUseCase.execute(any(), any()) } returns Success(HotelAddCartData.Response(HotelAddCartData("aab")))

        //when
        hotelRoomListViewModel.addToCart("", HotelAddCartParam())

        //then
        assert(hotelRoomListViewModel.addCartResponseResult.value is Success)
        assert((hotelRoomListViewModel.addCartResponseResult.value as Success<HotelAddCartData.Response>).data.response.cartId == "aab")
    }

    @Test
    fun addToCart_ifAtcFail() {
        //given
        coEvery { hotelAddToCartUseCase.execute(any(), any()) } returns Fail(Throwable())

        //when
        hotelRoomListViewModel.addToCart("", HotelAddCartParam())

        //then
        assert(hotelRoomListViewModel.addCartResponseResult.value is Fail)
    }

    @Test
    fun clearFilter() {
        //given
        val roomList = mutableListOf<HotelRoom>()
        roomList.add(HotelRoom(roomId = "1", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "2", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = true), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))
        roomList.add(HotelRoom(roomId = "3", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = true)))
        roomList.add(HotelRoom(roomId = "4", breakfastInfo = HotelRoom.RoomBreakfastInfo(isBreakfastIncluded = false), refundInfo = HotelRoom.RefundInfo(isRefundable = false)))

        //when
        hotelRoomListViewModel.roomList = roomList
        hotelRoomListViewModel.clickFilter(clickFreeCancelable = true)
        hotelRoomListViewModel.clearFilter()

        //then
        assert(!hotelRoomListViewModel.filterFreeCancelable)
        assert(!hotelRoomListViewModel.filterFreeBreakfast)
        assert((hotelRoomListViewModel.roomListResult.value as Success<MutableList<HotelRoom>>).data.size == 4)
    }
}