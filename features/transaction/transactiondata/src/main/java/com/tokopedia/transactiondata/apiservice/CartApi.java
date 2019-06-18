package com.tokopedia.transactiondata.apiservice;

import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.transactiondata.constant.TransactionDataApiUrl;

import java.util.Map;

import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author anggaprasetiyo on 24/01/18.
 */

public interface CartApi {

    // TODO: 17/6/19 remove this API
    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_ADD_TO_CART)
    Observable<Response<CartResponse>> postAddToCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_ADD_TO_CART_ONE_CLICK_SHIPMENT)
    Observable<Response<CartResponse>> postAddToCartOneClickShipment(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_CART_LIST_MULTIPLE_ADDRESS)
    Observable<Response<CartResponse>> getCartList(@FieldMap Map<String, String> params);

    @GET(TransactionDataApiUrl.Cart.PATH_SHOP_GROUP_LIST)
    Observable<Response<CartResponse>> getShopGroupList(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_REMOVE_FROM_CART)
    Observable<Response<CartResponse>> postDeleteCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_UPDATE_CART)
    Observable<Response<CartResponse>> postUpdateCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_SHIPPING_ADDRESS)
    Observable<Response<CartResponse>> postSetShippingAddress(@FieldMap Map<String, String> params);

    @GET(TransactionDataApiUrl.Cart.PATH_SHIPMENT_ADDRESS_FORM_DIRECT)
    Observable<Response<CartResponse>> getShipmentAddressForm(@QueryMap Map<String, String> params);

    @GET(TransactionDataApiUrl.Cart.PATH_SHIPMENT_ADDRESS_ONE_CLICK_CHECKOUT)
    Observable<Response<CartResponse>> getShipmentAddressFormOneClickCheckout(@QueryMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_CHECKOUT)
    Observable<Response<CartResponse>> checkout(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_RESET_CART)
    Observable<Response<CartResponse>> resetCart(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_CHECK_PROMO_CODE_CART_LIST)
    Observable<Response<CartResponse>> checkPromoCodeCartList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_CHECK_PROMO_CODE_CART_COURIER)
    Observable<Response<CartResponse>> checkPromoCodeCartShipment(@FieldMap Map<String, String> params);

    @GET(TransactionDataApiUrl.Cart.PATH_COUPON_LIST)
    Observable<Response<CartResponse>> getCouponList(@QueryMap Map<String, String> params);

    @GET(TransactionDataApiUrl.Cart.PATH_NOTIFICATION_COUNTER)
    Observable<Response<CartResponse>> getNotificationCounter();

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_CANCEL_AUTO_APPLY_COUPON)
    Observable<String> cancelAutoApplyCoupon(@Header(AuthUtil.HEADER_DEVICE) String os,
                                             @FieldMap Map<String, String> params);

//    public static final String PATH_UPDATE_STATE_BY_PAYMENT = "api/" + VERSION + "/update_state_by_payment";
//    public static final String PATH_NOTIFICATION_COUNTER = "api/" + VERSION + "/counter";
//    public static final String PATH_SAVE_PICKUP_STORE_POINT = "api/" + VERSION + "/save_pickup_store_point";

    @FormUrlEncoded
    @POST(TransactionDataApiUrl.Cart.PATH_SAVE_SHIPMENT)
    Observable<Response<CartResponse>> postSaveShipmentState(@FieldMap Map<String, String> params);

}
