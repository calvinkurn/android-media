package com.tokopedia.hotel.common.analytics

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils.*

/**
 * @author by resakemal on 13/06/19
 */
class TrackingHotelUtil {

    fun hotelSelectDestination(destination: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_DESTINATION,
                "$HOTEL_LABEL - $destination")
    }

    fun hotelSelectStayDate(dayDiff: Int, dateRange: String){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_STAY_DATE,
                "$HOTEL_LABEL - $dayDiff - $dateRange")
    }

    fun hotelSelectRoomGuest(roomCount: Int, adultCount: Int, childCount: Int){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SELECT_ROOM_GUEST,
                "$HOTEL_LABEL - $roomCount - $adultCount - $childCount")
    }

    fun searchHotel(hotelType: String,
                    destination: String,
                    roomCount: Int,
                    guestCount: Int,
                    dayDiff: Int,
                    duration: Int){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, SEARCH_HOTEL,
                "$HOTEL_LABEL - $hotelType - $destination - $roomCount - $guestCount - $dayDiff - $duration")
    }

    fun hotelViewHotelListImpression(destination: String,
                                     roomCount: Int,
                                     guestCount: Int,
                                     dayDiff: Int,
                                     duration: Int,
                                     products: List<HotelImpressionProduct>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, PRODUCT_VIEW)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, VIEW_HOTEL_LIST_IMPRESSION)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $destination - $roomCount - $guestCount - $dayDiff - $duration")

        val productsObj = mapOf(PRODUCTS_LABEL to products)
        val impressionsObj = mapOf("impressions" to productsObj)
        map.put(ECOMMERCE_LABEL, impressionsObj)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun chooseHotel(hotelId: Int,
                    dayDiff: Int,
                    price: Int,
                    promotions: List<HotelPromotions>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, PRODUCT_CLICK)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_HOTEL)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $dayDiff - $price")

        val promotionsObj = mapOf("promotions" to promotions)
        val clickObj = mapOf("click" to promotionsObj)
        map.put(ECOMMERCE_LABEL, clickObj)
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

    fun hotelChooseRoom(hotelId: Int,
                        roomId: String,
                        price: Int,
                        products: List<HotelPromoProduct>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, ADD_TO_CART)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_ROOM)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $roomId - $price")

        val productsObj = mapOf(PRODUCTS_LABEL to products)
        val addObj = mapOf(ADD_LABEL to productsObj)
        map.put(ECOMMERCE_LABEL, addObj)
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

    fun hotelChooseRoomDetails(hotelId: Int,
                        roomId: String,
                        price: Int,
                        products: List<HotelPromoProduct>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, ADD_TO_CART)
        map.put(EVENT_CATEGORY, DIGITAL_NATIVE)
        map.put(EVENT_ACTION, CHOOSE_ROOM_DETAILS_PDP)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $hotelId - $roomId - $price")

        val productsObj = mapOf(PRODUCTS_LABEL to products)
        val addObj = mapOf(ADD_LABEL to productsObj)
        map.put(ECOMMERCE_LABEL, addObj)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelClickNext(personal: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_NATIVE, CLICK_NEXT,
                "$HOTEL_LABEL - $personal")
    }

    fun hotelClickBayar(clickBayar: String,
                        products: List<HotelCheckoutProduct>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, CHECKOUT)
        map.put(EVENT_CATEGORY, DIGITAL_CHECKOUT)
        map.put(EVENT_ACTION, CLICK_BAYAR)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $clickBayar")

        val productsObj = mapOf(PRODUCTS_LABEL to products)
        val checkoutObj = mapOf(ADD_LABEL to productsObj)
        map.put(ECOMMERCE_LABEL, checkoutObj)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    fun hotelApplyPromo(success: Boolean){
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOTEL, DIGITAL_CHECKOUT, APPLY_PROMO,
                "$HOTEL_LABEL - $success")
    }

    fun hotelViewPurchaseAttempt(purchaseAttemptStatus: String,
                                 purchase: HotelPurchase,
                                 products: List<HotelRoomBooking>){
        val map = mutableMapOf<String, Any?>()
        map.put(EVENT, PURCHASE)
        map.put(EVENT_CATEGORY, DIGITAL_THANKS)
        map.put(EVENT_ACTION, VIEW_PURCHASE_ATTEMPT)
        map.put(EVENT_LABEL, "$HOTEL_LABEL - $purchaseAttemptStatus")

        val ecommerceMap = mutableMapOf<String, Any?>()
        ecommerceMap.put(PRODUCTS_LABEL, products)
        ecommerceMap.put("purchase", purchase)
        ecommerceMap.put("currencyCode", "IDR")
        map.put(ECOMMERCE_LABEL, ecommerceMap)
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(map)
    }

    data class HotelImpressionProduct(
        @SerializedName("name")
        @Expose
        val hotelName: String = "",

        @SerializedName("payment")
        @Expose
        val paymentAvailability: String = "",

        @SerializedName("id")
        @Expose
        val hotelId: Int = 0,

        @SerializedName("price")
        @Expose
        val cheapestPrice: Int = 0
    )

    data class HotelPromotions(
        @SerializedName("products")
        @Expose
        val products: List<HotelPromoProduct> = listOf()
    )

    data class HotelPromoProduct(
        @SerializedName("name")
        @Expose
        val hotelName: String = "",

        @SerializedName("id")
        @Expose
        val productId: String = "",

        @SerializedName("price")
        @Expose
        val price: Int = 0,

        @SerializedName("position")
        @Expose
        val productPosition: Int = 0
    )

    open class HotelRoomBooking(
        @SerializedName("name")
        @Expose
        val hotelName: String = "",

        @SerializedName("id")
        @Expose
        val productId: String = "",

        @SerializedName("room")
        @Expose
        val roomCount: Int = 0,

        @SerializedName("time")
        @Expose
        val duration: Int = 0,

        @SerializedName("price")
        @Expose
        val price: Int = 0
    )

    data class HotelCheckoutProduct(
        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",

        @SerializedName("promo_benefit")
        @Expose
        val promoBenefit: String = ""
    ): HotelRoomBooking()

    data class HotelPurchase(
        @SerializedName("id")
        @Expose
        val orderId: String = "",

        @SerializedName("sales_price")
        @Expose
        val totalPrice: Int = 0,

        @SerializedName("promo_code")
        @Expose
        val promoCode: String = "",

        @SerializedName("pym_gateway")
        @Expose
        val paymentGateway: String = ""
    )
}