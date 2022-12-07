package com.tokopedia.hotel.roomlist.usecase

import com.tokopedia.gql_query_annotation.GqlQueryInterface
import com.tokopedia.graphql.coroutines.data.extensions.getSuccessData
import com.tokopedia.graphql.coroutines.domain.interactor.MultiRequestGraphqlUseCase
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomData
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.roomlist.data.model.RoomListParam
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by jessica on 23/04/19
 */

class GetHotelRoomListUseCase @Inject constructor(val useCase: MultiRequestGraphqlUseCase) {

    private fun createRequestParam(hotelRoomListPageModel: HotelRoomListPageModel): RoomListParam {
        val roomListParam = RoomListParam()
        roomListParam.propertyId = hotelRoomListPageModel.propertyId
        roomListParam.checkIn = hotelRoomListPageModel.checkIn
        roomListParam.checkOut = hotelRoomListPageModel.checkOut
        roomListParam.guest.adult = hotelRoomListPageModel.adult
        val childAge = IntArray(hotelRoomListPageModel.child)
        for (i in 0 until hotelRoomListPageModel.child) {
            childAge[i] = CHILD_AGE
        }
        roomListParam.guest.childAge = childAge.toList()
        roomListParam.room = hotelRoomListPageModel.room
        return roomListParam
    }

    suspend fun execute(rawQuery: GqlQueryInterface, hotelRoomListPageModel: HotelRoomListPageModel,
                        fromCloud: Boolean = true): Result<MutableList<HotelRoom>> {
        val requestParams = createRequestParam(hotelRoomListPageModel)
        val params = mapOf(PARAM_ROOM_LIST_PROPERTY to requestParams)

        if (fromCloud) useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.ALWAYS_CLOUD).build())
        else useCase.setCacheStrategy(GraphqlCacheStrategy.Builder(CacheType.CACHE_FIRST).build())

        useCase.clearRequest()

        return try {
            val graphqlRequest = GraphqlRequest(rawQuery, HotelRoomData.Response::class.java, params)
            useCase.addRequest(graphqlRequest)

            val hotelRoomData = useCase.executeOnBackground().getSuccessData<HotelRoomData.Response>().response
            Success(mappingObjects(hotelRoomData, hotelRoomListPageModel.propertyName))
        } catch (throwable: Throwable) {
            Fail(throwable)
        }
    }

    private fun mappingObjects(hotelRoomData: HotelRoomData, hotelName: String): MutableList<HotelRoom> {
        val propertyInfo: HotelRoom.AdditionalPropertyInfo =
                HotelRoom.AdditionalPropertyInfo(hotelRoomData.propertyId, hotelRoomData.isAddressRequired,
                        hotelRoomData.isCvCRequired, hotelRoomData.isDirectPayment, hotelRoomData.isEnabled,
                        hotelName, hotelRoomData.deals.tagging)
        for (room in hotelRoomData.rooms) {
            room.additionalPropertyInfo = propertyInfo
        }
        return hotelRoomData.rooms.toMutableList()
    }

    companion object {
        const val PARAM_ROOM_LIST_PROPERTY = "data"

        private const val CHILD_AGE = 4
    }

}