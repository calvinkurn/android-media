package com.tokopedia.hotel.common.analytics

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.homepage.data.cloud.entity.HotelPromoEntity
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
                                     destinationType: String,
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
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - $dayDiff - $duration"
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
            map[DIRECT_PAYMENT_LABEL] = product.isDirectPayment
            map[ID_LABEL] = product.id
            map[POSITION_LABEL] = index
            map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
            map[VARIANT_LABEL] = "${product.isDirectPayment} - ${product.isDirectPayment}" // TODO product.isAvailable
            map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL

            map[PRICE_LABEL] = if (product.roomPrice.isNotEmpty())
                product.roomPrice.first().priceAmount.roundToLong().toString() else "0"

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
        val dayDiff = HotelUtils.countCurrentDayDifference(searchParam.checkIn)
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_HOTEL
        map[EVENT_LABEL] = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - $dayDiff - $duration"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL), // TODO create constant for /hotel/
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
            property.roomPrice.first().priceAmount.roundToLong().toString() else "0"
        map[POSITION_LABEL] = position
        map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
        map[VARIANT_LABEL] = "${property.isDirectPayment} - ${property.isDirectPayment}" // TODO product.isAvailable
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

    fun hotelViewDetails(hotelName: String, hotelId: Int, available: Boolean, price: String, directPayment: Boolean) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = VIEW_PRODUCT
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_HOTEL_PDP
        // TODO change to HOTEL_LABEL, destType, destination, total room, total guest, checkin day, total stay
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $available - $price - $directPayment"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL, // TODO add to constant
                DETAIL_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL), //  change to constant
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, hotelName,
                        ID_LABEL, hotelId,
                        PRICE_LABEL, price,
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "$DIRECT_PAYMENT_LABEL - $AVAILABLE_LABEL",
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

    fun hotelChooseViewRoom(hotelId: Int, price: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CHOOSE_VIEW_ROOM,
                "$HOTEL_LABEL - $hotelId - $price")
        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id
    }

    fun hotelViewRoomList(hotelId: Int) {
//        TrackApp.getInstance().gtm.sendGeneralEvent(VIEW_HOTEL, DIGITAL_NATIVE, VIEW_ROOM_LIST,
//                "$HOTEL_LABEL - $hotelId")
        // TODO change to impression
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_VIEW
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = VIEW_ROOM_LIST

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id
//        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        // TODO get all data from fragment / the view
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                IMPRESSIONS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, "{hotel name{",
                        ID_LABEL, "{hotel id}",
                        PRICE_LABEL, "{hotel price}",
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "{flag is direct payment} - {is available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                        POSITION_LABEL, "{position}"
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelChooseRoom(room: HotelRoom, position: Int) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.roundToLong()

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                ADD_LABEL, DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, room.roomInfo.name,
                        ID_LABEL, roomId,
                        PRICE_LABEL, price,
                        QUANTITY_LABEL, ONE_LABEL,
                        VARIANT_LABEL, "{isDirectPayment} - {isAvailable}",
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

    fun hotelClickRoomDetails(hotelId: Int, roomId: String, price: String) {
//        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_ROOM_DETAILS,
//                "$HOTEL_LABEL - $hotelId - $roomId - $price")

        // TODO change to impression
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = PRODUCT_CLICK
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_ROOM_DETAILS

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id
//        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        // TODO get all data from fragment / the view
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                    ACTION_FIELD_LABEL, DataLayer.mapOf( LIST_LABEL, SLASH_HOTEL_SLASH_LABEL ),
                    PRODUCTS_LABEL, DataLayer.listOf(
                                    DataLayer.mapOf(
                                            NAME_LABEL, "{hotel name}",
                                            ID_LABEL, "{hotel id}",
                                            PRICE_LABEL, "{hotel price}",
                                            LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                                            VARIANT_LABEL, "{flag is direct payment} - {is available}",
                                            CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                                            POSITION_LABEL, ""
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

    fun hotelChooseRoomDetails(room: HotelRoom) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomId = room.roomId
        val price = room.roomPrice.priceAmount.roundToLong()

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = ADD_TO_CART
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CHOOSE_ROOM_DETAILS_PDP

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id
        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                ADD_LABEL, DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, room.roomInfo.name,
                        ID_LABEL, room.roomId,
                        PRICE_LABEL, room.roomPrice.priceAmount.roundToLong(),
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "{flag is direct payment} - {is available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                        POSITION_LABEL, "{position}"
                        )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(personal: Boolean) {
//        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_NEXT,
//                "$HOTEL_LABEL - $personal")

        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CHECKOUT
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_NEXT

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id, flag personal
//        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(
                    STEP_LABEL, ONE_LABEL,
                    OPTION_LABEL, "click checkout"),
                PRODUCTS_LABEL, DataLayer.listOf(
                    DataLayer.mapOf(
                            NAME_LABEL, "{room name}",
                            ID_LABEL, "{room id}",
                            PRICE_LABEL, "{total room price}",
                            QUANTITY_LABEL,ONE_LABEL,
                            VARIANT_LABEL, "{flag is direct payment} - {is available}",
                            CATEGORY_LABEL, HOTEL_CONTENT_LABEL
                    )
                )
            )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickPay(personal: Boolean) {
        val map = mutableMapOf<String, Any?>()
        map[EVENT] = CHECKOUT
        map[EVENT_CATEGORY] = DIGITAL_NATIVE
        map[EVENT_ACTION] = CLICK_BAYAR

        // TODO change label to HOTEL_LABEL, destType, destination, totalRoom, totalGuest, checkin day, totalstay, hotel id, flag personal
//        map[EVENT_LABEL] = "$HOTEL_LABEL - $hotelId - $roomId - $price"
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                    ACTION_FIELD_LABEL, DataLayer.mapOf(
                        STEP_LABEL, TWO_LABEL,
                        OPTION_LABEL, CLICK_BAYAR),
                    PRODUCTS_LABEL, DataLayer.listOf(
                        DataLayer.mapOf(
                                NAME_LABEL, "{room name}",
                                ID_LABEL, "{room id}",
                                PRICE_LABEL, "{total room price}",
                                QUANTITY_LABEL,ONE_LABEL,
                                VARIANT_LABEL, "{flag is direct payment} - {is available}",
                                CATEGORY_LABEL, HOTEL_CONTENT_LABEL
                        )
                    )
                )
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelApplyPromo(promoCode: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, APPLY_PROMO,
                "$HOTEL_LABEL - $promoCode")
    }
}