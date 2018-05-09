package com.tokopedia.transactiondata.repository;

import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.notifcounter.NotifCounterCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartRepository {

    Observable<CartDataListResponse> getCartList(Map<String, String> param);

    Observable<DeleteCartDataResponse> deleteCartData(Map<String, String> param);

    Observable<AddToCartDataResponse> addToCartData(Map<String, String> param);

    Observable<UpdateCartDataResponse> updateCartData(Map<String, String> param);

    Observable<ShippingAddressDataResponse> shippingAddress(Map<String, String> param);

    Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(Map<String, String> param);

    Observable<ResetCartDataResponse> resetCart(Map<String, String> param);

    Observable<CheckoutDataResponse> checkout(Map<String, String> param);

    Observable<CheckPromoCodeCartListDataResponse> checkPromoCodeCartList(Map<String, String> param);

    Observable<CheckPromoCodeFinalDataResponse> checkPromoCodeCartShipment(Map<String, String> param);

    Observable<CouponDataResponse> getCouponList(Map<String, String> param);

    Observable<NotifCounterCartDataResponse> getNotificationCounter();

}
