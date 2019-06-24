package com.tokopedia.hotel.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.PropertySearch
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

/**
 * @author by resakemal on 13/06/19
 */
class TrackingHotelUtil {

    fun hotelBannerImpression(bannerId: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, BANNER_IMPRESSION,
                "$HOTEL_LABEL - $bannerId")
    }

    fun hotelClickBanner(bannerId: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_BANNER,
                "$HOTEL_LABEL - $bannerId")
    }

    fun hotelSelectDestination(destType: String, destination: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_DESTINATION,
                "$HOTEL_LABEL - $destType - $destination")
    }

    fun hotelSelectStayDate(dayDiff: Int, dateRange: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_STAY_DATE,
                "$HOTEL_LABEL - $dayDiff - $dateRange")
    }

    fun hotelSelectRoomGuest(roomCount: Int, adultCount: Int){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_ROOM_GUEST,
                "$HOTEL_LABEL - $roomCount - $adultCount")
    }

    fun searchHotel(destType: String,
                    destination: String,
                    roomCount: Int,
                    guestCount: Int,
                    dayDiff: Int,
                    duration: Int){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SEARCH_HOTEL,
                "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - $dayDiff - $duration")
    }

    fun hotelViewHotelListImpression(destination: String,
                                     searchParam: SearchParam,
                                     products: List<Property>){
        val map = mutableMapOf<String, Any?>()
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val dayDiff = HotelUtils.countCurrentDayDifference(searchParam.checkIn)
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut )

        map.put(EVENT, PRODUCT_VIEW)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, VIEW_HOTEL_LIST_IMPRESSION)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $destination - $roomCount - $guestCount - $dayDiff - $duration")
        map.put(ECOMMERCE_LABEL, DataLayer.mapOf(
                "impressions", DataLayer.mapOf(
                        PRODUCTS_LABEL, DataLayer.listOf(products.map {
                                DataLayer.mapOf(
                                        "name", it.name,
                                        "id", it.id,
                                        "price", it.roomPrice.firstOrNull() ?: 0,
                                        "direct_payment", it.isDirectPayment
                                )
                        })
                )
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun chooseHotel(property: Property,
                    checkInDate: String,
                    products: List<Property>){
        val map = mutableMapOf<String, Any?>()
        val hotelId = property.id
        val dayDiff = HotelUtils.countCurrentDayDifference(checkInDate)
        val price = property.roomPrice.firstOrNull() ?: 0

        map.put(EVENT, PRODUCT_CLICK)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_HOTEL)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $dayDiff - $price")
        map.put(ECOMMERCE_LABEL, DataLayer.mapOf(
                "click", DataLayer.mapOf(
                        PRODUCTS_LABEL, DataLayer.listOf(products.mapIndexed { index, it ->
                                DataLayer.mapOf(
                                        "name", it.name,
                                        "id", it.id,
                                        "price", it.roomPrice.firstOrNull() ?: 0,
                                        "position", index
                                )
                        })
                )
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelUserClickFilter(filterValue: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, USER_CLICK_FILTER,
                "$HOTEL_LABEL - $filterValue")
    }

    fun hotelUserClickSort(sortValue: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, USER_CLICK_SORT,
                "$HOTEL_LABEL - $sortValue")
    }

    fun hotelViewDetails(hotelId: Int, available: Boolean, price: Int, directPayment: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, VIEW_HOTEL_PDP,
                "$HOTEL_LABEL - $hotelId - $available - $price - $directPayment")
    }

    fun hotelClickHotelPhoto(hotelId: Int, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_PHOTO,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelClickHotelReviews(hotelId: Int, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_REVIEWS,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelChooseViewRoom(hotelId: Int, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CHOOSE_VIEW_ROOM,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelViewRoomList(hotelId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, VIEW_ROOM_LIST,
                "$HOTEL_LABEL - $hotelId")
    }

    fun hotelChooseRoom(room: HotelRoom,
                        products: List<HotelRoom>){
        val map = mutableMapOf<String, Any?>()
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.toInt()

        map.put(EVENT, ADD_TO_CART)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_ROOM)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $roomId - $price")
        map.put(ECOMMERCE_LABEL, DataLayer.mapOf(
                "add", DataLayer.mapOf(
                        PRODUCTS_LABEL, DataLayer.listOf(products.mapIndexed { index, it ->
                                DataLayer.mapOf(
                                        "name", it.roomInfo.name,
                                        "id", it.roomId,
                                        "price", it.roomPrice.priceAmount.toInt(),
                                        "position", index
                                )
                        })
                )
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickRoomListPhoto(hotelId: Int, roomId: String, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_LIST,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelClickRoomDetails(hotelId: Int, roomId: String, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_DETAILS,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelClickRoomDetailsPhoto(hotelId: Int, roomId: String, price: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_PDP,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelChooseRoomDetails(room: HotelRoom){
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.toInt()

        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, ADD_TO_CART)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_ROOM_DETAILS_PDP)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $roomId - $price")
        map.put(ECOMMERCE_LABEL, DataLayer.mapOf(
                "add", DataLayer.mapOf(
                        PRODUCTS_LABEL, DataLayer.listOf(listOf(room).map{
                                DataLayer.mapOf(
                                        "name", it.roomInfo.name,
                                        "id", it.roomId,
                                        "price", it.roomPrice.priceAmount.toInt()
                                )
                        })
                )
        ))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(personal: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_NEXT,
                "$HOTEL_LABEL - $personal")
    }
}