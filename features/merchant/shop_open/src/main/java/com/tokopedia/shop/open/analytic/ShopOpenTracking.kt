package com.tokopedia.shop.open.analytic

import android.content.Context

import com.tokopedia.seller.SellerModuleRouter
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

import java.util.HashMap

import com.tokopedia.shop.open.analytic.ShopOpenTrackingConstant.*

/**
 * Created by zulfikarrahman on 1/8/18.
 */

class ShopOpenTracking(private val sellerModuleRouter: SellerModuleRouter, private val userSession: UserSessionInterface) {

    private fun eventOpenShop(category: String, action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(
            ShopOpenTrackingConstant.CLICK_CREATE_SHOP,
            category,
            action,
            label
        )
    }

    fun eventOpenShopBiodataSuccess() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_SUCCESS,
            "")
    }

    fun eventMoEngageOpenShop(sendMoEngageCreateShopEvent: String) {
        TrackApp.getInstance().moEngage.sendTrackEvent(
            mapOf(
                "screen_name" to sendMoEngageCreateShopEvent,
                "User_ID" to userSession.userId,
                "Email" to userSession.email,
                "Mobile Number" to userSession.phoneNumber
            ),
            "Open_Shop_Screen_Launched"
        )
    }

    fun eventOpenShopBiodataError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_ERROR,
            labelError
        )
    }

    fun eventOpenShopBiodataErrorWithData(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_ERROR_WITH_DATA,
            labelError
        )
    }

    fun eventOpenShopBiodataNameError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_ERROR_NAME,
            labelError
        )
    }

    fun eventOpenShopBiodataDomainError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_BIODATA_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_ERROR_DOMAIN,
            labelError
        )
    }

    fun eventOpenShopFormSuccess() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_INFO_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
            ""
        )
    }

    fun eventOpenShopFormError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_INFO_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
            labelError
        )
    }

    fun eventOpenShopShippingSuccess() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
            ""
        )
    }

    fun eventOpenShopShippingError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
            labelError
        )
    }

    fun eventOpenShopShippingServices(courierName: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHIPPING_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_CARGO_SERVICES,
            courierName
        )
    }

    fun eventOpenShopThanksClickLearnMore() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_LEARN_MORE,
            ""
        )
    }

    fun eventOpenShopThanksClickAddProduct() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_ADD_PRODUCT,
            ""
        )
    }

    fun eventOpenShopThanksGoToMyShop() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_THANKS_PAGE,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_MY_SHOP_PAGE,
            ""
        )
    }

    fun eventOpenShopLocationForm(addressDetail: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_ADDRESS_LIST,
            addressDetail
        )
    }

    fun eventOpenShopPinPointSelected() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_PINPOINT_LOCATION,
            ""
        )
    }

    fun eventOpenShopPinPointDeleted() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_DELETE_PINPOINT_LOCATION,
            ""
        )
    }

    fun eventOpenShopLocationNext() {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_SUCCESS,
            ""
        )
    }

    fun eventOpenShopLocationError(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR,
            labelError
        )
    }

    fun eventOpenShopLocationErrorWithData(labelError: String) {
        eventOpenShop(
            ShopOpenTrackingConstant.OPEN_SHOP_SHOP_LOCATION_FORM,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP_ERROR_WITH_DATA,
            labelError
        )
    }

    fun eventOpenShopDomainReserveNext(label: String) {
        eventOpenShop(ShopOpenTrackingConstant.OPEN_SHOP_DOMAIN_RESERVE,
            ShopOpenTrackingConstant.OPEN_SHOP_CLICK_NEXT_STEP,
            label)
    }

    fun eventSellShortcut() {
        val eventTracking = HashMap<String, Any>()
        eventTracking[EVENT] = LONG_CLICK
        eventTracking[EVENT_CATEGORY] = LONG_PRESS
        eventTracking[EVENT_ACTION] = CLICK_SELL
        eventTracking[EVENT_LABEL] = TAKE_TO_SHOP
        eventTracking[USER_ID] = if (userSession.isLoggedIn) userSession.userId else "0"
        TrackApp.getInstance().gtm.sendGeneralEvent(eventTracking)
    }
}
