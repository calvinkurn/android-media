package com.tokopedia.purchase_platform.common.data.repository;

import com.tokopedia.purchase_platform.features.cart.data.model.response.CartDataListResponse;
import com.tokopedia.purchase_platform.features.checkout.subfeature.multiple_address.data.model.response.CartMultipleAddressDataListResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.checkout.CheckoutDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CheckPromoCodeCartListDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.CheckPromoCodeFinalDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.CouponDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.DeleteCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.NotifCounterCartDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.ResetCartDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.SaveShipmentStateResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.ShippingAddressDataResponse;
import com.tokopedia.purchase_platform.features.checkout.data.model.response.shipment_address_form.ShipmentAddressFormDataResponse;
import com.tokopedia.purchase_platform.features.cart.data.model.response.UpdateCartDataResponse;

import java.util.Map;

import rx.Observable;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public interface ICartRepository {

    Observable<CartMultipleAddressDataListResponse> getCartList(Map<String, String> param);

    Observable<CartDataListResponse> getShopGroupList(Map<String, String> param);

    Observable<DeleteCartDataResponse> deleteCartData(Map<String, String> param);

    Observable<UpdateCartDataResponse> updateCartData(Map<String, String> param);

    Observable<ShippingAddressDataResponse> setShippingAddress(Map<String, String> param);

    Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(Map<String, String> param);

    Observable<ShipmentAddressFormDataResponse> getShipmentAddressFormOneClickCheckout(Map<String, String> param);

    Observable<ResetCartDataResponse> resetCart(Map<String, String> param);

    Observable<CheckoutDataResponse> checkout(Map<String, String> param);

    Observable<CheckPromoCodeCartListDataResponse> checkPromoCodeCartList(Map<String, String> param);

    Observable<CheckPromoCodeFinalDataResponse> checkPromoCodeCartShipment(Map<String, String> param);

    Observable<CouponDataResponse> getCouponList(Map<String, String> param);

    Observable<NotifCounterCartDataResponse> getNotificationCounter();

    Observable<String> cancelAutoApplyCoupon(String os, Map<String, String> params);

    Observable<SaveShipmentStateResponse> saveShipmentState(Map<String, String> params);
}
