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
import com.tokopedia.hotel.roomlist.data.model.mapper.RoomListModelMapper
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
    val roomListResult = MutableLiveData<Result<MutableList<RoomListModel>>>()

    fun initRoomListParam(propertyId: Int, checkIn: String, checkOut: String, adult: Int, child: Int, room: Int) {
        roomListParam.propertyId = propertyId
        roomListParam.checkIn = checkIn
        roomListParam.checkOut = checkOut
        roomListParam.guest.adult = adult
//        var childAge = IntArray(child)
//        for (i in 0..child - 1){
//            childAge[i] = 10
//        }
//        roomListParam.guest.childAge = childAge.toList()
        roomListParam.room = room
    }

    fun getRoomList(rawQuery: String, dummy: String) {
        launchCatchError(block = {
            val params = mapOf(PARAM_ROOM_LIST_PROPERTY to roomListParam)
            val graphqlRequest = GraphqlRequest(rawQuery, HotelRoomData.Response::class.java, params)

            val response = withContext(Dispatchers.IO){
                graphqlRepository.getReseponse(listOf(graphqlRequest))
            }

            roomListResult.value = Success(roomListModelMapper.transform(response.getSuccessData<HotelRoomData.Response>().response))
        }){
            it.printStackTrace()
            val gson = Gson()
            roomListResult.value = Success(roomListModelMapper.transform(gson.fromJson(dummy, HotelRoomData.Response::class.java).response))
        }
    }


    companion object {
        private const val PARAM_ROOM_LIST_PROPERTY = "data"
    }
}