package com.tokopedia.hotel.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelPropertyRoom
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
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

    fun hotelBannerImpression(hotelPromoEntity: HotelPromoEntity, position: Int) {

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PROMO_VIEW
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = BANNER_IMPRESSION
        map[EVENT_LABEL] = "$HOTEL_LABEL - ${hotelPromoEntity.promoId}"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, getPromoList(hotelPromoEntity, position)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun getPromoList(hotelPromoEntity: HotelPromoEntity, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID_LABEL] = hotelPromoEntity.promoId
        map[NAME_LABEL] = SLASH_HOTEL_LABEL
        map[POSITION_LABEL] = position
        map[CREATIVE_LABEL] = "DG_${hotelPromoEntity.attributes.promoCode}"
        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelClickBanner(hotelPromoEntity: HotelPromoEntity, position: Int) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PROMO_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_BANNER
        map[EVENT_LABEL] = "$HOTEL_LABEL - ${hotelPromoEntity.promoId}"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, getPromoList(hotelPromoEntity, position)
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelSelectDestination(destType: String, destination: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_DESTINATION,
                "$HOTEL_LABEL - $destType - $destination")
    }

    fun hotelSelectStayDate(checkInDate: String, dateRange: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_STAY_DATE,
                "$HOTEL_LABEL - $checkInDate - $dateRange")
    }

    fun hotelSelectRoomGuest(roomCount: Int, adultCount: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_ROOM_GUEST,
                "$HOTEL_LABEL - $roomCount - $adultCount")
    }

    fun searchHotel(destType: String,
                    destination: String,
                    roomCount: Int,
                    guestCount: Int,
                    checkInDate: String,
                    duration: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SEARCH_HOTEL,
                "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - $checkInDate - $duration")
    }

    fun hotelViewHotelListImpression(destination: String,
                                     destinationType: String,
                                     searchParam: SearchParam,
                                     products: List<Property>) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_VIEW
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_HOTEL_LIST_IMPRESSION
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                IMPRESSIONS_LABEL, getViewHotelListProducts(products)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getViewHotelListProducts(listProduct: List<Property>): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        listProduct.forEachIndexed { index, product ->
            val map = HashMap<String, Any>()
            map[NAME_LABEL] = product.name
            map[ID_LABEL] = product.id
            map[POSITION_LABEL] = positionTracker(index)
            map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
            map[VARIANT_LABEL] = "${product.isDirectPayment} - ${product.roomAvailability > 0}"
            map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
            map[PRICE_LABEL] = if (product.roomPrice.isNotEmpty())
                product.roomPrice.first().priceAmount.roundToLong() else 0

            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun chooseHotel(destination: String,
                    destinationType: String,
                    searchParam: SearchParam,
                    property: Property,
                    position: Int) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_HOTEL
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, getChooseHotelProducts(property, position))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getChooseHotelProducts(property: Property, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        val map = HashMap<String, Any>()
        map[NAME_LABEL] = property.name
        map[ID_LABEL] = property.id
        map[PRICE_LABEL] = if (property.roomPrice.isNotEmpty())
            property.roomPrice.first().priceAmount.roundToLong() else 0
        map[POSITION_LABEL] = positionTracker(position)
        map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
        map[VARIANT_LABEL] = "${property.isDirectPayment} - ${property.roomAvailability > 0}"
        map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
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

    fun hotelViewDetails(hotelHomepageModel: HotelHomepageModel,
                         hotelName: String, hotelId: Int, available: Boolean,
                         price: String, directPayment: Boolean) {

        val roomCount = hotelHomepageModel.roomCount
        val guestCount = hotelHomepageModel.adultCount
        val duration = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        val destinationType = hotelHomepageModel.locType
        val destination = if (hotelHomepageModel.locName.isEmpty()) hotelName else hotelHomepageModel.locName

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = VIEW_PRODUCT
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_HOTEL_PDP
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelHomepageModel.checkInDate)} - $duration"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                DETAIL_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, hotelName,
                        ID_LABEL, hotelId,
                        PRICE_LABEL, price,
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "$directPayment - $available",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL
                )
        )
        )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickHotelPhoto(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_PHOTO,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelClickHotelReviews(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_HOTEL_REVIEWS,
                "$HOTEL_LABEL - $hotelId - $price")
    }

    fun hotelChooseViewRoom(hotelHomepageModel: HotelHomepageModel, hotelId: Int, hotelName: String) {
        val roomCount = hotelHomepageModel.roomCount
        val guestCount = hotelHomepageModel.adultCount
        val duration = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        val destinationType = hotelHomepageModel.locType
        val destination = if (hotelHomepageModel.locName.isEmpty()) hotelName else hotelHomepageModel.locName

        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CHOOSE_VIEW_ROOM,
                "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${hotelHomepageModel.checkInDate} - $duration - $hotelId")
    }

    fun hotelViewRoomList(hotelId: Int, hotelRoomListPageModel: HotelRoomListPageModel, roomList: List<HotelRoom>) {
        val roomCount = hotelRoomListPageModel.room
        val guestCount = hotelRoomListPageModel.adult
        val duration = HotelUtils.countDayDifference(hotelRoomListPageModel.checkIn, hotelRoomListPageModel.checkOut)
        val destinationType = hotelRoomListPageModel.destinationType
        val destination = hotelRoomListPageModel.destinationName

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_VIEW
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_ROOM_LIST

        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelRoomListPageModel.checkIn)} - $duration - $hotelId"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                IMPRESSIONS_LABEL, getViewHotelListRoom(roomList)
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getViewHotelListRoom(roomList: List<HotelRoom>): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        roomList.forEachIndexed { index, hotelRoom ->
            val map = HashMap<String, Any>()
            map[NAME_LABEL] = hotelRoom.roomInfo.name
            map[ID_LABEL] = hotelRoom.roomId
            map[POSITION_LABEL] = positionTracker(index)
            map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
            map[VARIANT_LABEL] = "${hotelRoom.additionalPropertyInfo.isDirectPayment} - ${hotelRoom.available}"
            map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
            map[PRICE_LABEL] = hotelRoom.roomPrice.priceAmount.roundToLong()

            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelChooseRoom(room: HotelRoom, hotelAddCartParam: HotelAddCartParam) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomCount = hotelAddCartParam.rooms.size
        val guestCount = hotelAddCartParam.adult
        val duration = HotelUtils.countDayDifference(hotelAddCartParam.checkIn, hotelAddCartParam.checkOut)
        val destinationType = hotelAddCartParam.destinationType
        val destination = hotelAddCartParam.destinationName

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelAddCartParam.checkIn)} - $duration - $hotelId"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                ADD_LABEL, DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, room.roomInfo.name,
                        ID_LABEL, room.roomId,
                        PRICE_LABEL, room.roomPrice.priceAmount.roundToLong(),
                        QUANTITY_LABEL, ONE_LABEL,
                        VARIANT_LABEL, "${room.additionalPropertyInfo.isDirectPayment} - ${room.available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL
                    )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickRoomListPhoto(hotelId: Int, roomId: String, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_LIST,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelClickRoomDetails(hotelRoom: HotelRoom, hotelRoomListPageModel: HotelRoomListPageModel, position: Int) {
        val hotelId = hotelRoom.additionalPropertyInfo.propertyId
        val roomCount = hotelRoomListPageModel.room
        val guestCount = hotelRoomListPageModel.adult
        val duration = HotelUtils.countDayDifference(hotelRoomListPageModel.checkIn, hotelRoomListPageModel.checkOut)
        val destinationType = hotelRoomListPageModel.destinationType
        val destination = hotelRoomListPageModel.destinationName

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_ROOM_DETAILS
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelRoomListPageModel.checkIn)} - $duration - $hotelId"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                    ACTION_FIELD_LABEL, DataLayer.mapOf( LIST_LABEL, SLASH_HOTEL_SLASH_LABEL ),
                    PRODUCTS_LABEL, DataLayer.listOf(
                                    DataLayer.mapOf(
                                            NAME_LABEL, hotelRoom.roomInfo.name,
                                            ID_LABEL, hotelRoom.roomId,
                                            PRICE_LABEL, hotelRoom.roomPrice.priceAmount.roundToLong(),
                                            LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                                            VARIANT_LABEL, "${hotelRoom.additionalPropertyInfo.isDirectPayment} - ${hotelRoom.available}",
                                            CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                                            POSITION_LABEL, positionTracker(position)
                                    )
                            )
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

    }

    fun hotelClickRoomDetailsPhoto(hotelId: Int, roomId: String, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_PHOTO_ON_ROOM_PDP,
                "$HOTEL_LABEL - $hotelId - $roomId - $price")
    }

    fun hotelChooseRoomDetails(room: HotelRoom, position: Int, hotelAddCartParam: HotelAddCartParam) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomCount = hotelAddCartParam.rooms.size
        val guestCount = hotelAddCartParam.adult
        val duration = HotelUtils.countDayDifference(hotelAddCartParam.checkIn, hotelAddCartParam.checkOut)
        val destinationType = hotelAddCartParam.destinationType
        val destination = hotelAddCartParam.destinationName

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM_DETAILS_PDP
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelAddCartParam.checkIn)} - $duration - $hotelId"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                ADD_LABEL, DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, room.roomInfo.name,
                        ID_LABEL, room.roomId,
                        PRICE_LABEL, room.roomPrice.priceAmount.roundToLong(),
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "${room.additionalPropertyInfo.isDirectPayment} - ${room.available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                        POSITION_LABEL, "$position"
                        )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(hotelCart: HotelCart, destType: String, destination: String, personal: Boolean) {
        val hotelId = hotelCart.property.propertyID
        val roomCount = hotelCart.property.rooms.size
        val guestCount = hotelCart.cart.adult
        val duration = HotelUtils.countDayDifference(hotelCart.cart.checkIn, hotelCart.cart.checkOut)

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CHECKOUT
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_NEXT
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - ${convertDate(hotelCart.cart.checkIn)} - $duration - $hotelId - $personal"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(
                    STEP_LABEL, ONE_LABEL,
                    OPTION_LABEL, CLICK_CHECKOUT),
                PRODUCTS_LABEL, getHotelListRoomCart(hotelCart.property.rooms, hotelCart.property.isDirectPayment,
                hotelCart.cart.totalPriceAmount)
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getHotelListRoomCart(roomList: List<HotelPropertyRoom>, isDirectPayment: Boolean, totalPrice: Float): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        roomList.forEachIndexed { index, hotelRoom ->
            val map = HashMap<String, Any>()
            map[NAME_LABEL] = hotelRoom.roomName
            map[ID_LABEL] = hotelRoom.roomID
            map[POSITION_LABEL] = positionTracker(index)
            map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
            map[VARIANT_LABEL] = "$isDirectPayment - true"
            map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
            map[PRICE_LABEL] = totalPrice.roundToLong()

            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelApplyPromo(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, APPLY_PROMO,
                "$HOTEL_LABEL - $promoCode")
    }

    private fun convertDate(date: String): String =
            TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.YYYYMMDD, date)

    private fun positionTracker(index: Int): Int {
        return index+1
    }

}