package com.tokopedia.hotel.roomlist.presentation.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.roomlist.data.model.HotelRoomData
import com.tokopedia.hotel.roomlist.data.model.RoomListModel
import com.tokopedia.hotel.roomlist.data.model.RoomListParam
import com.tokopedia.hotel.roomlist.data.mapper.RoomListModelMapper
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.withContext
import javax.inject.Inject

/**
 * @author by jessica on 15/04/19
 */

class HotelRoomListViewModel @Inject constructor(
        val graphqlRepository: GraphqlRepository,
        val dispatcher: CoroutineDispatcher,
        val roomListModelMapper: RoomListModelMapper) : BaseViewModel(dispatcher){

    private val roomListParam: RoomListParam = RoomListParam()
    private var roomList: List<RoomListModel> = listOf()
    val roomListResult = MutableLiveData<Result<MutableList<RoomListModel>>>()

    var filterFreeBreakfast = false
    var filterFreeCancelable = false
    var filterPayInHotel = false

    fun initRoomListParam(hotelRoomListPageModel: HotelRoomListPageModel) {
        roomListParam.propertyId = hotelRoomListPageModel.propertyId
        roomListParam.checkIn = hotelRoomListPageModel.checkIn
        roomListParam.checkOut = hotelRoomListPageModel.checkOut
        roomListParam.guest.adult = hotelRoomListPageModel.adult
        var childAge = IntArray(hotelRoomListPageModel.child)
        for (i in 0..hotelRoomListPageModel.child - 1){
            childAge[i] = 4
        }
        roomListParam.guest.childAge = childAge.toList()
        roomListParam.room = hotelRoomListPageModel.room
    }

    fun getRoomList(rawQuery: String, dummy: String) {
        launchCatchError(block = {
            val params = mapOf(PARAM_ROOM_LIST_PROPERTY to roomListParam)
            val graphqlRequest = GraphqlRequest(rawQuery, HotelRoomData.Response::class.java, params)

            val response = withContext(Dispatchers.IO){
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }.getSuccessData<HotelRoomData.Response>().response

            roomListResult.value = Success(roomListModelMapper.transform(response))
            roomList = roomListModelMapper.transform(response)
        }){
            it.printStackTrace()
            val gson = Gson()
            roomListResult.value = Success(roomListModelMapper.transform(gson.fromJson(dummy, HotelRoomData.Response::class.java).response))
            roomList = roomListModelMapper.transform(gson.fromJson(dummy, HotelRoomData.Response::class.java).response)
        }
    }

    fun filter(clickFreeBreakfast: Boolean = false, clickFreeCancelable: Boolean = false, clickPayInHotel: Boolean = false) {
        if (clickFreeBreakfast) filterFreeBreakfast = !filterFreeBreakfast
        if (clickFreeCancelable) filterFreeCancelable = !filterFreeCancelable
        if (clickPayInHotel) filterPayInHotel = !filterPayInHotel

        if (filterFreeBreakfast || filterFreeCancelable || filterPayInHotel)  {
            var list: MutableList<RoomListModel> = arrayListOf()
            for (room in roomList) {
                var valid = true
                if (filterPayInHotel && !room.payInHotel) valid = false
                if (filterFreeCancelable && !room.isRefundable) valid = false
                if (filterFreeBreakfast && !room.breakfastIncluded) valid = false
                if (valid) list.add(room)
            }
            roomListResult.value = Success(list)
        } else roomListResult.value = Success(roomList.toMutableList())
    }

    companion object {
        private const val PARAM_ROOM_LIST_PROPERTY = "data"
    }
}