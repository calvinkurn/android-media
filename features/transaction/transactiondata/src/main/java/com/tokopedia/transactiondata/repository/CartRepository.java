package com.tokopedia.transactiondata.repository;

import com.tokopedia.transactiondata.apiservice.CartApi;
import com.tokopedia.transactiondata.apiservice.CartResponse;
import com.tokopedia.transactiondata.entity.response.addtocart.AddToCartDataResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartDataListResponse;
import com.tokopedia.transactiondata.entity.response.cartlist.CartMultipleAddressDataListResponse;
import com.tokopedia.transactiondata.entity.response.checkout.CheckoutDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodecartlist.CheckPromoCodeCartListDataResponse;
import com.tokopedia.transactiondata.entity.response.checkpromocodefinal.CheckPromoCodeFinalDataResponse;
import com.tokopedia.transactiondata.entity.response.couponlist.CouponDataResponse;
import com.tokopedia.transactiondata.entity.response.deletecart.DeleteCartDataResponse;
import com.tokopedia.transactiondata.entity.response.notifcounter.NotifCounterCartDataResponse;
import com.tokopedia.transactiondata.entity.response.resetcart.ResetCartDataResponse;
import com.tokopedia.transactiondata.entity.response.saveshipmentstate.SaveShipmentStateResponse;
import com.tokopedia.transactiondata.entity.response.shippingaddress.ShippingAddressDataResponse;
import com.tokopedia.transactiondata.entity.response.shippingaddressform.ShipmentAddressFormDataResponse;
import com.tokopedia.transactiondata.entity.response.updatecart.UpdateCartDataResponse;

import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 29/01/18.
 */

public class CartRepository implements ICartRepository {

    private CartApi cartApi;

    @Inject
    public CartRepository(CartApi cartApi) {
        this.cartApi = cartApi;
    }

    @Override
    public Observable<CartMultipleAddressDataListResponse> getCartList(Map<String, String> param) {
        return cartApi.getCartList(param).map(
                new Func1<Response<CartResponse>, CartMultipleAddressDataListResponse>() {
                    @Override
                    public CartMultipleAddressDataListResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(CartMultipleAddressDataListResponse.class);
                    }
                });
    }

    @Override
    public Observable<CartDataListResponse> getShopGroupList(Map<String, String> param) {
        return cartApi.getShopGroupList(param).map(new Func1<Response<CartResponse>, CartDataListResponse>() {
            @Override
            public CartDataListResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(CartDataListResponse.class);
            }
        });
    }

    @Override
    public Observable<DeleteCartDataResponse> deleteCartData(Map<String, String> param) {
        return cartApi.postDeleteCart(param).map(
                new Func1<Response<CartResponse>, DeleteCartDataResponse>() {
                    @Override
                    public DeleteCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(DeleteCartDataResponse.class);
                    }
                });
    }

    @Override
    public Observable<AddToCartDataResponse> addToCartData(Map<String, String> param) {
        return cartApi.postAddToCart(param).map(
                new Func1<Response<CartResponse>, AddToCartDataResponse>() {
                    @Override
                    public AddToCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(AddToCartDataResponse.class);
                    }
                }
        );
    }

    @Override
    public Observable<AddToCartDataResponse> addToCartDataOneClickShipment(Map<String, String> param) {
        return cartApi.postAddToCartOneClickShipment(param).map(
                new Func1<Response<CartResponse>, AddToCartDataResponse>() {
                    @Override
                    public AddToCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(AddToCartDataResponse.class);
                    }
                }
        );
    }

    @Override
    public Observable<UpdateCartDataResponse> updateCartData(Map<String, String> param) {
        return cartApi.postUpdateCart(param).map(new Func1<Response<CartResponse>, UpdateCartDataResponse>() {
            @Override
            public UpdateCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(UpdateCartDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ShippingAddressDataResponse> setShippingAddress(Map<String, String> param) {
        return cartApi.postSetShippingAddress(param).map(new Func1<Response<CartResponse>, ShippingAddressDataResponse>() {
            @Override
            public ShippingAddressDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ShippingAddressDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ShipmentAddressFormDataResponse> getShipmentAddressForm(Map<String, String> param) {
        return cartApi.getShipmentAddressForm(param).map(new Func1<Response<CartResponse>, ShipmentAddressFormDataResponse>() {
            @Override
            public ShipmentAddressFormDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ShipmentAddressFormDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ShipmentAddressFormDataResponse> getShipmentAddressFormOneClickCheckout(Map<String, String> param) {
        return cartApi.getShipmentAddressFormOneClickCheckout(param).map(new Func1<Response<CartResponse>, ShipmentAddressFormDataResponse>() {
            @Override
            public ShipmentAddressFormDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ShipmentAddressFormDataResponse.class);
            }
        });
    }

    @Override
    public Observable<ResetCartDataResponse> resetCart(Map<String, String> param) {
        return cartApi.resetCart(param).map(new Func1<Response<CartResponse>, ResetCartDataResponse>() {
            @Override
            public ResetCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(ResetCartDataResponse.class);
            }
        });
    }

    @Override
    public Observable<CheckoutDataResponse> checkout(Map<String, String> param) {
        return cartApi.checkout(param).map(new Func1<Response<CartResponse>, CheckoutDataResponse>() {
            @Override
            public CheckoutDataResponse call(Response<CartResponse> cartResponseResponse) {
                return cartResponseResponse.body().convertDataObj(CheckoutDataResponse.class);
            }
        });
    }

    @Override
    public Observable<CheckPromoCodeCartListDataResponse> checkPromoCodeCartList(Map<String, String> param) {
        return cartApi.checkPromoCodeCartList(param).map(
                new Func1<Response<CartResponse>, CheckPromoCodeCartListDataResponse>() {
                    @Override
                    public CheckPromoCodeCartListDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(CheckPromoCodeCartListDataResponse.class);
                    }
                });
    }

    @Override
    public Observable<CheckPromoCodeFinalDataResponse> checkPromoCodeCartShipment(Map<String, String> param) {
        return cartApi.checkPromoCodeCartShipment(param).map(
                new Func1<Response<CartResponse>, CheckPromoCodeFinalDataResponse>() {
                    @Override
                    public CheckPromoCodeFinalDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(CheckPromoCodeFinalDataResponse.class);
                    }
                });
    }

    @Override
    public Observable<CouponDataResponse> getCouponList(Map<String, String> param) {
        return cartApi.getCouponList(param).map(
                new Func1<Response<CartResponse>, CouponDataResponse>() {
                    @Override
                    public CouponDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(CouponDataResponse.class);
                    }
                }
        );
    }

    @Override
    public Observable<NotifCounterCartDataResponse> getNotificationCounter() {
        return cartApi.getNotificationCounter().map(
                new Func1<Response<CartResponse>, NotifCounterCartDataResponse>() {
                    @Override
                    public NotifCounterCartDataResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(NotifCounterCartDataResponse.class);
                    }
                }
        );
    }

    @Override
    public Observable<String> cancelAutoApplyCoupon(String os, Map<String, String> params) {
        return cartApi.cancelAutoApplyCoupon(os, params);
    }

    @Override
    public Observable<SaveShipmentStateResponse> saveShipmentState(Map<String, String> params) {
        return cartApi.postSaveShipmentState(params).map(
                new Func1<Response<CartResponse>, SaveShipmentStateResponse>() {
                    @Override
                    public SaveShipmentStateResponse call(Response<CartResponse> cartResponseResponse) {
                        return cartResponseResponse.body().convertDataObj(SaveShipmentStateResponse.class);
                    }
                });
    }
}
