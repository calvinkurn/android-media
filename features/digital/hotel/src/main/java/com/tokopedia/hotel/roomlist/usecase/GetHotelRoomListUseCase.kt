package com.tokopedia.hotel.roomlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.common.getSuccessData
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomData
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.data.model.RoomListParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext

/**
 * @author by jessica on 23/04/19
 */

class GetHotelRoomListUseCase(graphqlRepository: GraphqlRepository): MultiRequestGraphqlUseCase(graphqlRepository) {

    fun createRequestParam(hotelRoomListPageModel: HotelRoomListPageModel): RoomListParam {
        val roomListParam = RoomListParam()
        roomListParam.propertyId = hotelRoomListPageModel.propertyId
        roomListParam.checkIn = hotelRoomListPageModel.checkIn
        roomListParam.checkOut = hotelRoomListPageModel.checkOut
        roomListParam.guest.adult = hotelRoomListPageModel.adult
        var childAge = IntArray(hotelRoomListPageModel.child)
        for (i in 0 until hotelRoomListPageModel.child){
            childAge[i] = 4
        }
        roomListParam.guest.childAge = childAge.toList()
        roomListParam.room = hotelRoomListPageModel.room
        return roomListParam
    }

    suspend fun execute(rawQuery: String, hotelRoomListPageModel: HotelRoomListPageModel): Result<MutableList<HotelRoom>> {
        val requestParams = createRequestParam(hotelRoomListPageModel)
        val params = mapOf(PARAM_ROOM_LIST_PROPERTY to requestParams)
        clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelRoomData.Response::class.java, params)
            addRequest(graphqlRequest)

            val hotelRoomData = async {
                val response =  withContext(Dispatchers.IO) {
                    executeOnBackground().getSuccessData<HotelRoomData.Response>().response
                }
                response
            }
            return Success(hotelRoomData.await().rooms.toMutableList())
        } catch (throwable: Throwable) {
            return Fail(throwable)
        }
    }

    companion object {
        const val PARAM_ROOM_LIST_PROPERTY = "data"
    }

}