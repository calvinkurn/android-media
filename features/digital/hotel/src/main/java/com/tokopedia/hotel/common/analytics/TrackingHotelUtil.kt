package com.tokopedia.hotel.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.search.data.model.Filter
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.params.ParamFilter
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import kotlin.math.roundToLong

/**
 * @author by resakemal on 13/06/19
 */
class TrackingHotelUtil {

    fun hotelBannerImpression(bannerId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(PROMO_VIEW, DIGITAL_NATIVE, BANNER_IMPRESSION,
                "$HOTEL_LABEL - $bannerId")
    }

    fun hotelClickBanner(bannerId: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(PROMO_CLICK, DIGITAL_NATIVE, CLICK_BANNER,
                "$HOTEL_LABEL - $bannerId")
    }

    fun hotelSelectDestination(destType: String, destination: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_DESTINATION,
                "$HOTEL_LABEL - $destType - $destination")
    }

    fun hotelSelectStayDate(dayDiff: Int, dateRange: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_STAY_DATE,
                "$HOTEL_LABEL - $dayDiff - $dateRange")
    }

    fun hotelSelectRoomGuest(roomCount: Int, adultCount: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_ROOM_GUEST,
                "$HOTEL_LABEL - $roomCount - $adultCount")
    }

    fun searchHotel(destType: String,
                    destination: String,
                    roomCount: Int,
                    guestCount: Int,
                    dayDiff: Int,
                    duration: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SEARCH_HOTEL,
                "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - $dayDiff - $duration")
    }

    fun hotelViewHotelListImpression(destination: String,
                                     searchParam: SearchParam,
                                     products: List<Property>) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val dayDiff = HotelUtils.countCurrentDayDifference(searchParam.checkIn)
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_VIEW
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_HOTEL_LIST_IMPRESSION
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destination - $roomCount - $guestCount - $dayDiff - $duration"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                "impressions", DataLayer.mapOf(
                PRODUCTS_LABEL, getViewHotelListProducts(products)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getViewHotelListProducts(listProduct: List<Property>): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        for (product in listProduct) {
            val map = HashMap<String, Any>()
            map["name"] = product.name
            map["direct_payment"] = product.isDirectPayment
            map["id"] = product.id

            map["price"] = if (product.roomPrice.isNotEmpty())
                product.roomPrice.first().priceAmount.roundToLong().toString() else "0"

            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun chooseHotel(property: Property,
                    checkInDate: String,
                    position: Int) {
        val hotelId = property.id
        val dayDiff = HotelUtils.countCurrentDayDifference(checkInDate)

        val price = if (property.roomPrice.isNotEmpty())
            property.roomPrice.first().priceAmount.roundToLong().toString() else "0"

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_HOTEL
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $dayDiff - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                "click", DataLayer.mapOf(PRODUCTS_LABEL, getChooseHotelProducts(property, position))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getChooseHotelProducts(property: Property, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        val map = HashMap<String, Any>()
        map["name"] = property.name
        map["id"] = property.id
        map["price"] = if (property.roomPrice.isNotEmpty())
            property.roomPrice.first().priceAmount.roundToLong().toString() else "0"
        map["position"] = position
        list.add(map)
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelUserClickFilter(filterValue: ParamFilter, filter: Filter) {
        val filter = (filterValue.maxPrice == filter.price.maxPrice)
                && (filterValue.minPrice == filter.price.minPrice)
                && (filterValue.paymentType == 0)
                && filterValue.propertyType.isEmpty()
                && (filter.filterReview.minReview.toInt() == filterValue.reviewScore)
                && filterValue.roomFacilities.isEmpty()
                && filterValue.star.isEmpty()

        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, USER_CLICK_FILTER,
                "$HOTEL_LABEL - true")
    }

    fun hotelUserClickSort(sortValue: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, USER_CLICK_SORT,
                "$HOTEL_LABEL - $sortValue")
    }

    fun hotelViewDetails(hotelName: String, hotelId: Int, available: Boolean, price: String, directPayment: Boolean) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = VIEW_HOTEL
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_HOTEL_PDP
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $available - $price - $directPayment"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                "view", DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        "name", hotelName,
                        "id", hotelId,
                        "price", price,
                        "available", available,
                        "direct_payment", directPayment
                )
        )
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickHotelPhoto(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_PHOTO,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelClickHotelReviews(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_REVIEWS,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelChooseViewRoom(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CHOOSE_VIEW_ROOM,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelViewRoomList(hotelId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, VIEW_ROOM_LIST,
                "$HOTEL_LABEL - $hotelId")
    }

    fun hotelChooseRoom(room: HotelRoom, position: Int) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.roundToLong()

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                "add", DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        "name", room.roomInfo.name,
                        "id", roomId,
                        "price", price,
                        "position", position
                )
        )
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickRoomListPhoto(hotelId: Int, roomId: String, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_LIST,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelClickRoomDetails(hotelId: Int, roomId: String, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_DETAILS,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelClickRoomDetailsPhoto(hotelId: Int, roomId: String, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_PDP,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelChooseRoomDetails(room: HotelRoom) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.roundToLong()

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM_DETAILS_PDP
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                "add", DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        "name", room.roomInfo.name,
                        "id", room.roomId,
                        "price", room.roomPrice.priceAmount.roundToLong()
                )
        )
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(personal: Boolean) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_NEXT,
                "$HOTEL_LABEL - $personal")
    }
}