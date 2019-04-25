package com.tokopedia.hotel.roomlist.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
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
import javax.inject.Inject

/**
 * @author by jessica on 23/04/19
 */

class GetHotelRoomListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

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

    suspend fun execute(rawQuery: String, hotelRoomListPageModel: HotelRoomListPageModel,
                        fromCloud: Boolean = true): Result<MutableList<HotelRoom>> {
        val requestParams = createRequestParam(hotelRoomListPageModel)
        val params = mapOf(PARAM_ROOM_LIST_PROPERTY to requestParams)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelRoomData.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val hotelRoomData = async {
                val response =  withContext(Dispatchers.IO) {
                    useCase.executeOnBackground().getSuccessData<HotelRoomData.Response>().response
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