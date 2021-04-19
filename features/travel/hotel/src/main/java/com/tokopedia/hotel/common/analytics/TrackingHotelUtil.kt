package com.tokopedia.hotel.common.analytics

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.common.travel.data.entity.TravelCollectiveBannerModel
import com.tokopedia.common.travel.data.entity.TravelRecentSearchModel
import com.tokopedia.common.travel.presentation.model.TravelVideoBannerModel
import com.tokopedia.common.travel.utils.TravelDateUtil
import com.tokopedia.hotel.booking.data.model.HotelCart
import com.tokopedia.hotel.booking.data.model.HotelPropertyRoom
import com.tokopedia.hotel.cancellation.data.HotelCancellationModel
import com.tokopedia.hotel.common.util.HotelUtils
import com.tokopedia.hotel.homepage.presentation.model.HotelHomepageModel
import com.tokopedia.hotel.roomlist.data.model.HotelAddCartParam
import com.tokopedia.hotel.roomlist.data.model.HotelRoom
import com.tokopedia.hotel.roomlist.data.model.HotelRoomListPageModel
import com.tokopedia.hotel.search.data.model.Property
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.data.model.params.SearchParam
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*
import com.tokopedia.user.session.UserSession
import kotlin.math.roundToLong

/**
 * @author by resakemal on 13/06/19
 */
class TrackingHotelUtil {

    private fun getIrisSessionId(context: Context): String = IrisSession(context).getSessionId()
    private fun getUserId(context: Context): String = UserSession(context).userId

    fun openScreen(context: Context?, screenName: String) {
        if (screenName.isNotEmpty()) {
            val map = getTrackingMapWithHeader(context, screenName)
            TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName, map)
        }
    }

    fun hotelBannerImpression(context: Context?, hotelPromoEntity: TravelCollectiveBannerModel.Banner?, position: Int, screenName: String) {
        hotelPromoEntity?.let {
            val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
            map.addGeneralEvent(PROMO_VIEW, BANNER_IMPRESSION, "$HOTEL_LABEL - ${it.id}")
            map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(PROMOTIONS_LABEL, getBannerList(hotelPromoEntity, position)))
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
        }
    }

    fun hotelClickBanner(context: Context?, hotelPromoEntity: TravelCollectiveBannerModel.Banner, position: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(PROMO_CLICK, CLICK_BANNER, "$HOTEL_LABEL - ${hotelPromoEntity.id}")
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(PROMOTIONS_LABEL, getBannerList(hotelPromoEntity, position)))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelVideoBannerImpression(context: Context?, travelVideoBannerModel: TravelVideoBannerModel, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(PROMO_VIEW, VIDEO_BANNER_IMPRESSION, "$HOTEL_LABEL - $LABEL_VIDEO_BANNER")
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_VIEW, DataLayer.mapOf(
                PROMOTIONS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        ID_LABEL, travelVideoBannerModel.id,
                        NAME_LABEL, SLASH_HOTEL_LABEL,
                        POSITION_LABEL, 0,
                        CREATIVE_LABEL, travelVideoBannerModel.description
                )
        )))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickVideoBanner(context: Context?, travelVideoBannerModel: TravelVideoBannerModel, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(PROMO_CLICK, CLICK_VIDEO_BANNER, "$HOTEL_LABEL - $LABEL_VIDEO_BANNER")
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_CLICK, DataLayer.mapOf(
                PROMOTIONS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        ID_LABEL, travelVideoBannerModel.id,
                        NAME_LABEL, SLASH_HOTEL_LABEL,
                        POSITION_LABEL, 0,
                        CREATIVE_LABEL, travelVideoBannerModel.description
                )
        )))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getBannerList(hotelPromoEntity: TravelCollectiveBannerModel.Banner, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[ID_LABEL] = hotelPromoEntity.id
        map[NAME_LABEL] = SLASH_HOTEL_LABEL
        map[POSITION_LABEL] = position
        map[CREATIVE_LABEL] = "DG_${hotelPromoEntity.attribute.promoCode}"
        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelClickChangeDestination(context: Context?, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, CLICK_WIDGET_SELECT_DESTINATION, HOTEL_LABEL)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelSelectDestination(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, SELECT_DESTINATION, "$HOTEL_LABEL - $destType - $destination")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelSelectStayDate(context: Context?, checkInDate: String, dateRange: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, SELECT_STAY_DATE, "$HOTEL_LABEL - ${convertDate(checkInDate)} - $dateRange")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelSelectRoomGuest(context: Context?, roomCount: Int, adultCount: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, SELECT_ROOM_GUEST, "$HOTEL_LABEL - $roomCount - $adultCount")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchViewFullMap(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destType - $destination"
        map.addGeneralEvent(VIEW_HOTEL_IRIS, VIEW_FULL_MAP, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchCloseMap(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destType - $destination"
        map.addGeneralEvent(CLICK_HOTEL, VIEW_CLOSE_MAP, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchClickMyLocation(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $LABEL_HOTEL_SEARCH_MAP - $destType - $destination"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_MY_LOCATION, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchNearLocation(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $LABEL_HOTEL_SEARCH_MAP - $destType - $destination"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_SEARCH_NEAR_LOCATION, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchHotelNotFound(context: Context?, destType: String, destination: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $LABEL_HOTEL_SEARCH_MAP - $destType - $destination"
        map.addGeneralEvent(VIEW_HOTEL_IRIS, SEE_HOTEL_NOT_FOUND, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun searchHotel(context: Context?, destType: String, destination: String, roomCount: Int, guestCount: Int,
                    checkInDate: String, duration: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - ${convertDate(checkInDate)} - $duration"
        map.addGeneralEvent(CLICK_HOTEL, SEARCH_HOTEL, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelViewHotelListImpression(context: Context?, destination: String, destinationType: String, searchParam: SearchParam,
                                     products: List<Property>, currentListDataSize: Int, screenName: String) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map.addGeneralEvent(PRODUCT_VIEW, VIEW_HOTEL_LIST_IMPRESSION, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(CURRENCY_LABEL, IDR_LABEL, IMPRESSIONS_LABEL,
                getPropertyList(products, currentListDataSize = currentListDataSize))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelViewHotelListMapImpression(context: Context?, destination: String, destinationType: String, searchParam: SearchParam,
                                        products: List<Property>, currentListDataSize: Int, screenName: String) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $LABEL_HOTEL_SEARCH_MAP - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map.addGeneralEvent(PRODUCT_VIEW, VIEW_HOTEL_LIST_IMPRESSION, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(CURRENCY_LABEL, IDR_LABEL, IMPRESSIONS_LABEL,
                getPropertyList(products, currentListDataSize = currentListDataSize))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun chooseHotel(context: Context?, destination: String, destinationType: String, searchParam: SearchParam,
                    property: Property, position: Int, screenName: String) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map.addGeneralEvent(PRODUCT_CLICK, CHOOSE_HOTEL, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, getPropertyList(listOf(property), currentIndex = position))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun chooseHotelFromMap(context: Context?, destination: String, destinationType: String, searchParam: SearchParam,
                    property: Property, position: Int, screenName: String) {
        val roomCount = searchParam.room
        val guestCount = searchParam.guest.adult
        val duration = HotelUtils.countDayDifference(searchParam.checkIn, searchParam.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $LABEL_HOTEL_SEARCH_MAP - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(searchParam.checkIn)} - $duration"
        map.addGeneralEvent(PRODUCT_CLICK, CHOOSE_HOTEL_FROM_MAP, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, getPropertyList(listOf(property), currentIndex = position))
        )
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getPropertyList(listProduct: List<Property>, currentListDataSize: Int = 0, currentIndex: Int = 0): List<Any> {
        val list = ArrayList<Map<String, Any>>()
        listProduct.forEachIndexed { index, product ->
            val map = HashMap<String, Any>()
            map[NAME_LABEL] = product.name
            map[ID_LABEL] = product.id
            map[POSITION_LABEL] = currentListDataSize + positionTracker(index) + currentIndex
            map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
            map[VARIANT_LABEL] = "${product.isDirectPayment} - ${product.roomAvailability > 0}"
            map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
            map[PRICE_LABEL] = if (product.roomPrice.isNotEmpty()) product.roomPrice.first().priceAmount.roundToLong() else 0
            list.add(map)
        }
        return DataLayer.listOf(*list.toTypedArray<Any>())
    }

    fun hotelUserClickSort(context: Context?, sortValue: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, USER_CLICK_SORT, "$HOTEL_LABEL - $sortValue")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelViewDetails(context: Context?, hotelHomepageModel: HotelHomepageModel,
                         hotelName: String, hotelId: Long, available: Boolean,
                         price: String, directPayment: Boolean, screenName: String) {

        val roomCount = hotelHomepageModel.roomCount
        val guestCount = hotelHomepageModel.adultCount
        val duration = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        val destinationType = hotelHomepageModel.locType
        val destination = if (hotelHomepageModel.locName.isEmpty()) hotelName else hotelHomepageModel.locName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelHomepageModel.checkInDate)} - $duration"
        map.addGeneralEvent(VIEW_PRODUCT, VIEW_HOTEL_PDP, eventLabel)
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

    fun hotelClickHotelPhoto(context: Context?, hotelId: Long, price: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, CLICK_HOTEL_PHOTO, "$HOTEL_LABEL - $hotelId - $price")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelClickHotelReviews(context: Context?, hotelId: Long, price: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, CLICK_HOTEL_REVIEWS, "$HOTEL_LABEL - $hotelId - $price")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelChooseViewRoom(context: Context?, hotelHomepageModel: HotelHomepageModel, hotelId: Long,
                            hotelName: String, screenName: String) {
        val roomCount = hotelHomepageModel.roomCount
        val guestCount = hotelHomepageModel.adultCount
        val duration = HotelUtils.countDayDifference(hotelHomepageModel.checkInDate, hotelHomepageModel.checkOutDate)
        val destinationType = hotelHomepageModel.locType
        val destination = if (hotelHomepageModel.locName.isEmpty()) hotelName else hotelHomepageModel.locName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelHomepageModel.checkInDate)} - $duration - $hotelId"
        map.addGeneralEvent(CLICK_HOTEL, CHOOSE_VIEW_ROOM, eventLabel)
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelViewRoomList(context: Context?, hotelId: Long, hotelRoomListPageModel: HotelRoomListPageModel,
                          roomList: List<HotelRoom>, screenName: String) {
        val roomCount = hotelRoomListPageModel.room
        val guestCount = hotelRoomListPageModel.adult
        val duration = HotelUtils.countDayDifference(hotelRoomListPageModel.checkIn, hotelRoomListPageModel.checkOut)
        val destinationType = hotelRoomListPageModel.destinationType
        val destination = hotelRoomListPageModel.destinationName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelRoomListPageModel.checkIn)} - $duration - $hotelId"
        map.addGeneralEvent(PRODUCT_VIEW, VIEW_ROOM_LIST, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(CURRENCY_LABEL, IDR_LABEL, IMPRESSIONS_LABEL, getViewHotelListRoom(roomList))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelViewRoomDetail(context: Context?, roomDetail: HotelRoom, addToCartParam: HotelAddCartParam,
                            position: Int, screenName: String) {

        val duration = HotelUtils.countDayDifference(addToCartParam.checkIn, addToCartParam.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - ${addToCartParam.destinationType} - ${addToCartParam.destinationName} - ${addToCartParam.roomCount} - ${addToCartParam.adult} - ${convertDate(addToCartParam.checkIn)} - $duration - ${addToCartParam.propertyId}"
        map.addGeneralEvent(VIEW_PRODUCT, VIEW_ROOM_DETAILS, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                DETAIL_LABEL, DataLayer.mapOf(ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, getRoomDetailData(roomDetail, position)))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getRoomDetailData(room: HotelRoom, position: Int): List<Any> {
        val list = ArrayList<Map<String, Any>>()

        val map = HashMap<String, Any>()
        map[NAME_LABEL] = room.roomInfo.name
        map[ID_LABEL] = room.roomId
        map[POSITION_LABEL] = position
        map[LIST_LABEL] = SLASH_HOTEL_SLASH_LABEL
        map[PRICE_LABEL] = room.roomPrice.priceAmount.roundToLong()
        map[CATEGORY_LABEL] = HOTEL_CONTENT_LABEL
        map[VARIANT_LABEL] = "${room.additionalPropertyInfo.isDirectPayment} - ${room.available}"

        list.add(map)

        return DataLayer.listOf(*list.toTypedArray<Any>())
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

    fun hotelChooseRoom(context: Context?, room: HotelRoom, hotelAddCartParam: HotelAddCartParam, screenName: String) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val guestCount = hotelAddCartParam.adult
        val duration = HotelUtils.countDayDifference(hotelAddCartParam.checkIn, hotelAddCartParam.checkOut)
        val destinationType = hotelAddCartParam.destinationType
        val destination = hotelAddCartParam.destinationName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - ${hotelAddCartParam.roomCount} - $guestCount - ${convertDate(hotelAddCartParam.checkIn)} - $duration - $hotelId"
        map.addGeneralEvent(ADD_TO_CART, CHOOSE_ROOM, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CURRENCY_LABEL, IDR_LABEL,
                ADD_LABEL, DataLayer.mapOf(
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, room.roomInfo.name,
                        ID_LABEL, room.roomId,
                        PRICE_LABEL, room.roomPrice.priceAmount.roundToLong(),
                        QUANTITY_LABEL, hotelAddCartParam.roomCount,
                        VARIANT_LABEL, "${room.additionalPropertyInfo.isDirectPayment} - ${room.available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL
                ))))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickRoomListPhoto(context: Context?, hotelId: Int, roomId: String, price: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, CLICK_ROOM_PHOTO_ON_ROOM_LIST, "$HOTEL_LABEL - $hotelId - $roomId - $price")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelClickRoomDetails(context: Context?, hotelRoom: HotelRoom, hotelRoomListPageModel: HotelRoomListPageModel,
                              position: Int, screenName: String) {
        val hotelId = hotelRoom.additionalPropertyInfo.propertyId
        val roomCount = hotelRoomListPageModel.room
        val guestCount = hotelRoomListPageModel.adult
        val duration = HotelUtils.countDayDifference(hotelRoomListPageModel.checkIn, hotelRoomListPageModel.checkOut)
        val destinationType = hotelRoomListPageModel.destinationType
        val destination = hotelRoomListPageModel.destinationName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelRoomListPageModel.checkIn)} - $duration - $hotelId"
        map.addGeneralEvent(PRODUCT_CLICK, CLICK_ROOM_DETAILS, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CLICK_LABEL, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(LIST_LABEL, SLASH_HOTEL_SLASH_LABEL),
                PRODUCTS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        NAME_LABEL, hotelRoom.roomInfo.name,
                        ID_LABEL, hotelRoom.roomId,
                        PRICE_LABEL, hotelRoom.roomPrice.priceAmount.roundToLong(),
                        LIST_LABEL, SLASH_HOTEL_SLASH_LABEL,
                        VARIANT_LABEL, "${hotelRoom.additionalPropertyInfo.isDirectPayment} - ${hotelRoom.available}",
                        CATEGORY_LABEL, HOTEL_CONTENT_LABEL,
                        POSITION_LABEL, positionTracker(position)
                ))))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)

    }

    fun hotelClickRoomDetailsPhoto(context: Context?, hotelId: Int, roomId: String, price: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, CLICK_ROOM_PHOTO_ON_ROOM_PDP, "$HOTEL_LABEL - $hotelId - $roomId - $price")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelChooseRoomDetails(context: Context?, room: HotelRoom, position: Int,
                               hotelAddCartParam: HotelAddCartParam, screenName: String) {
        val hotelId = room.additionalPropertyInfo.propertyId
        val roomCount = hotelAddCartParam.roomCount
        val guestCount = hotelAddCartParam.adult
        val duration = HotelUtils.countDayDifference(hotelAddCartParam.checkIn, hotelAddCartParam.checkOut)
        val destinationType = hotelAddCartParam.destinationType
        val destination = hotelAddCartParam.destinationName

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destinationType - $destination - $roomCount - $guestCount - ${convertDate(hotelAddCartParam.checkIn)} - $duration - $hotelId"
        map.addGeneralEvent(ADD_TO_CART, CHOOSE_ROOM_DETAILS_PDP, eventLabel)
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
                        POSITION_LABEL, "$position",
                        QUANTITY_LABEL, hotelAddCartParam.roomCount
                ))))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(context: Context?, hotelCart: HotelCart, destType: String, destination: String, roomCount: Int,
                       guestCount: Int, personal: Boolean, screenName: String) {
        val hotelId = hotelCart.property.propertyID
        val duration = HotelUtils.countDayDifference(hotelCart.cart.checkIn, hotelCart.cart.checkOut)

        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $destType - $destination - $roomCount - $guestCount - ${convertDate(hotelCart.cart.checkIn)} - $duration - $hotelId - $personal"
        map.addGeneralEvent(CHECKOUT, CLICK_NEXT, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(
                CHECKOUT, DataLayer.mapOf(
                ACTION_FIELD_LABEL, DataLayer.mapOf(
                STEP_LABEL, ONE_LABEL,
                OPTION_LABEL, CLICK_CHECKOUT),
                PRODUCTS_LABEL, getHotelListRoomCart(hotelCart.property.rooms, hotelCart.property.isDirectPayment,
                hotelCart.cart.totalPriceAmount)))
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

    fun hotelApplyPromo(context: Context?, promoCode: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        map.addGeneralEvent(CLICK_HOTEL, APPLY_PROMO, "$HOTEL_LABEL - $promoCode")
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun hotelLastSearchImpression(context: Context?, lastSearchItems: TravelRecentSearchModel.Item, position: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $REGION_LABEL - ${lastSearchItems.title} - 1 - 2 - ${lastSearchItems.subtitle} - 1"
        map.addGeneralEvent(PROMO_VIEW, ACTION_LAST_SEARCH_IMPRESSION, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_VIEW, getECommerceDataLastSearch(lastSearchItems, position))
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelLastSearchClick(context: Context?, lastSearchItems: TravelRecentSearchModel.Item, position: Int, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $REGION_LABEL - ${lastSearchItems.title} - 1 - 2 - ${lastSearchItems.subtitle} - 1"
        map.addGeneralEvent(PROMO_CLICK, ACTION_LAST_SEARCH_CLICK, eventLabel)
        map[ECOMMERCE_LABEL] = DataLayer.mapOf(PROMO_CLICK, getECommerceDataLastSearch(lastSearchItems, position))

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun getECommerceDataLastSearch(lastSearchItems: TravelRecentSearchModel.Item, position: Int): MutableMap<String, Any> {
        return DataLayer.mapOf(
                PROMOTIONS_LABEL, DataLayer.listOf(
                DataLayer.mapOf(
                        ID_LABEL, position + 1,
                        NAME_LABEL, "$LAST_SEARCH_LABEL - ${lastSearchItems.title}",
                        CREATIVE_LABEL, lastSearchItems.appUrl,
                        POSITION_LABEL, position + 1
                )))
    }

    fun hotelClickChangeSearch(context: Context?, type: String, name: String, totalRoom: Int,
                               totalGuest: Int, checkIn: String, checkOut: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $type - $name - $totalRoom - $totalGuest - ${convertDate(checkIn)} - ${HotelUtils.countDayDifference(checkIn, checkOut)}"
        map.addGeneralEvent(CLICK_HOTEL, ACTION_CLICK_CHANGE_SEARCH, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickSaveChangeSearch(context: Context?, type: String, name: String, totalRoom: Int,
                              totalGuest: Int, checkIn: String, checkOut: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $type - $name - $totalRoom - $totalGuest - ${convertDate(checkIn)} - ${HotelUtils.countDayDifference(checkIn, checkOut)}"
        map.addGeneralEvent(CLICK_HOTEL, ACTION_SAVE_CHANGE_SEARCH, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun viewHotelCancellationPage(context: Context?, invoiceId: String, hotelCancellationModel: HotelCancellationModel, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val orderAmount = hotelCancellationModel.payment.detail.firstOrNull()?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val cancellationFee = hotelCancellationModel.payment.detail.getOrNull(1)?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val refundAmount = hotelCancellationModel.payment.summary.firstOrNull()?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val eventLabel = "$HOTEL_LABEL - $invoiceId - $orderAmount - $cancellationFee - $refundAmount"
        map.addGeneralEvent(VIEW_HOTEL_IRIS, VIEW_CANCELLATION_PAGE, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickNextOnCancellationPage(context: Context?, invoiceId: String, hotelCancellationModel: HotelCancellationModel, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val orderAmount = hotelCancellationModel.payment.detail.firstOrNull()?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val cancellationFee = hotelCancellationModel.payment.detail.getOrNull(1)?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val refundAmount = hotelCancellationModel.payment.summary.firstOrNull()?.amount
                ?: HOTEL_DEFAULT_VALUE_ZERO_STRING
        val eventLabel = "$HOTEL_LABEL - $invoiceId - $orderAmount - $cancellationFee - $refundAmount"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_NEXT_ON_CANCELLATION_PAGE, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickSubmitCancellation(context: Context?, invoiceId: String, hotelCancellationModel: HotelCancellationModel, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val orderAmount = hotelCancellationModel.payment.detail.firstOrNull()?.amount ?: "0"
        val cancellationFee = hotelCancellationModel.payment.detail.getOrNull(1)?.amount ?: "0"
        val refundAmount = hotelCancellationModel.payment.summary.firstOrNull()?.amount ?: "0"
        val eventLabel = "$HOTEL_LABEL - $invoiceId - $orderAmount - $cancellationFee - $refundAmount"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_SUBMIT_CANCELLATION, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun viewCancellationStatus(context: Context?, invoiceId: String, orderAmount: String, cancellationFee: String, refundAmount: String,
                               status: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $invoiceId - $orderAmount - $cancellationFee - $refundAmount - $status"
        map.addGeneralEvent(VIEW_HOTEL_IRIS, VIEW_CANCELLATION_STATUS, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickOnCancellationStatusActionButton(context: Context?, buttonLabel: String, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $buttonLabel"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_ON_CANCELLATION_RESULT_PAGE, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickOnQuickFilter(context: Context?, screenName: String, filterName: String, position: Int) {
        val pos = position + 2
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $filterName - $pos"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_QUICK_FILTER_ON_SRP, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickOnAdvancedFilter(context: Context?, screenName: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - Filters - 1"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_QUICK_FILTER_ON_SRP, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun clickSubmitFilterOnBottomSheet(context: Context?, screenName: String, selectedFilter: List<ParamFilterV2>) {
        for (filter in selectedFilter) {
            val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
            val eventLabel = "$HOTEL_LABEL - ${filter.name} - ${filter.values.joinToString()}"
            map.addGeneralEvent(CLICK_HOTEL, CLICK_USER_CLICK_FILTER, eventLabel)
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
        }
    }

    fun clickShareUrl(context: Context, screenName: String, hotelId: String, hotelPrice: String) {
        val map = getTrackingMapWithHeader(context, screenName) as MutableMap<String, Any>
        val eventLabel = "$HOTEL_LABEL - $hotelId - $hotelPrice"
        map.addGeneralEvent(CLICK_HOTEL, CLICK_SHARE_ICON, eventLabel)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    private fun convertDate(date: String): String =
            TravelDateUtil.formatDate(TravelDateUtil.YYYY_MM_DD, TravelDateUtil.YYYYMMDD, date)

    private fun positionTracker(index: Int): Int = index + 1

    private fun getTrackingMapWithHeader(context: Context?, screenName: String): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()
        map[SCREEN_NAME] = screenName
        map[CURRENT_SITE] = TOKOPEDIA_DIGITAL_HOTEL
        map[CLIENT_ID] = TrackApp.getInstance().gtm.clientIDString ?: ""
        map[SESSION_IRIS] = if (context != null) getIrisSessionId(context) else ""
        map[USER_ID] = if (context != null) getUserId(context) else ""
        map[BUSINESS_UNIT] = TRAVELENTERTAINMENT_LABEL
        map[CATEGORY_LABEL] = HOTEL_LABEL
        return map
    }

    private fun MutableMap<String, Any>.addGeneralEvent(event: String, action: String, label: String): MutableMap<String, Any>? {
        this[EVENT] = event
        this[EVENT_CATEGORY] = DIGITAL_NATIVE
        this[EVENT_ACTION] = action
        this[EVENT_LABEL] = label
        return this
    }

    companion object {
        const val HOTEL_DEFAULT_VALUE_ZERO_STRING = "0"
    }
}