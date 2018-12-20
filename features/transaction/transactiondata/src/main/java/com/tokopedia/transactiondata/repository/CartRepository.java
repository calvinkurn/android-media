package com.tokopedia.transactiondata.repository;

import com.google.gson.Gson;
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
                return new Gson().fromJson("{\n" +
                        "    \"errors\": null,\n" +
                        "    \"error_code\": 0,\n" +
                        "    \"is_multiple\": 0,\n" +
                        "    \"is_coupon_active\": 0,\n" +
                        "    \"is_shipping_recommendation\": 1,\n" +
                        "    \"group_address\": [\n" +
                        "      {\n" +
                        "        \"errors\": [\n" +
                        "          \n" +
                        "        ],\n" +
                        "        \"user_address\": {\n" +
                        "          \"address_id\": 4649145,\n" +
                        "          \"address_name\": \"alamat alamat alamat alamat alamat alamat alamat alamat alamat\",\n" +
                        "          \"address\": \"alamat alamat alamat alamat alamat alamat alamat alamat alamat\",\n" +
                        "          \"postal_code\": \"11460\",\n" +
                        "          \"phone\": \"012345678\",\n" +
                        "          \"receiver_name\": \"namaku\",\n" +
                        "          \"status\": 2,\n" +
                        "          \"country\": \"Indonesia\",\n" +
                        "          \"province_id\": 13,\n" +
                        "          \"province_name\": \"DKI Jakarta\",\n" +
                        "          \"city_id\": 174,\n" +
                        "          \"city_name\": \"Jakarta Barat\",\n" +
                        "          \"district_id\": 2255,\n" +
                        "          \"district_name\": \"Kalideres\",\n" +
                        "          \"address_2\": \"-6.134293699999976,106.70577330000003\",\n" +
                        "          \"latitude\": \"-6.134293699999976\",\n" +
                        "          \"longitude\": \"106.70577330000003\"\n" +
                        "        },\n" +
                        "        \"group_shop\": [\n" +
                        "          {\n" +
                        "            \"errors\": [\n" +
                        "              \n" +
                        "            ],\n" +
                        "            \"shipping_id\": 1,\n" +
                        "            \"sp_id\": 1,\n" +
                        "            \"dropshiper\": {\n" +
                        "              \"name\": \"Toko Boleh\",\n" +
                        "              \"telp_no\": \"02156756758\"\n" +
                        "            },\n" +
                        "            \"is_insurance\": true,\n" +
                        "            \"shop\": {\n" +
                        "              \"shop_id\": 480125,\n" +
                        "              \"user_id\": 5512646,\n" +
                        "              \"shop_name\": \"tokoko\",\n" +
                        "              \"shop_image\": \"https:\\/\\/imagerouter-staging.tokopedia.com\\/img\\/215-square\\/default_v3-shopnophoto.png\",\n" +
                        "              \"shop_url\": \"https:\\/\\/staging.tokopedia.com\\/tokoko\",\n" +
                        "              \"shop_status\": 1,\n" +
                        "              \"is_gold\": 1,\n" +
                        "              \"is_gold_badge\": false,\n" +
                        "              \"is_official\": 0,\n" +
                        "              \"is_free_returns\": 0,\n" +
                        "              \"address_id\": 2258,\n" +
                        "              \"postal_code\": \"11430\",\n" +
                        "              \"latitude\": \"-6.1783437\",\n" +
                        "              \"longitude\": \"106.80456400000003\",\n" +
                        "              \"district_id\": 2258,\n" +
                        "              \"district_name\": \"Palmerah\",\n" +
                        "              \"origin\": 2258,\n" +
                        "              \"address_street\": \"\",\n" +
                        "              \"province_id\": 13,\n" +
                        "              \"city_id\": 174,\n" +
                        "              \"city_name\": \"DKI Jakarta\"\n" +
                        "            },\n" +
                        "            \"shop_shipments\": [\n" +
                        "              {\n" +
                        "                \"ship_id\": 1,\n" +
                        "                \"ship_name\": \"JNE\",\n" +
                        "                \"ship_code\": \"jne\",\n" +
                        "                \"ship_logo\": \"kurir-jne.png\",\n" +
                        "                \"ship_prods\": [\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 1,\n" +
                        "                    \"ship_prod_name\": \"Reguler\",\n" +
                        "                    \"ship_group_name\": \"regular\",\n" +
                        "                    \"ship_group_id\": 1004,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"is_dropship_enabled\": 1\n" +
                        "              },\n" +
                        "              {\n" +
                        "                \"ship_id\": 10,\n" +
                        "                \"ship_name\": \"Go-Send\",\n" +
                        "                \"ship_code\": \"gojek\",\n" +
                        "                \"ship_logo\": \"kurir-gosend.png\",\n" +
                        "                \"ship_prods\": [\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 28,\n" +
                        "                    \"ship_prod_name\": \"Instant Courier\",\n" +
                        "                    \"ship_group_name\": \"instant\",\n" +
                        "                    \"ship_group_id\": 1000,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  },\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 20,\n" +
                        "                    \"ship_prod_name\": \"Same Day\",\n" +
                        "                    \"ship_group_name\": \"sameday\",\n" +
                        "                    \"ship_group_id\": 1002,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"is_dropship_enabled\": 0\n" +
                        "              },\n" +
                        "              {\n" +
                        "                \"ship_id\": 12,\n" +
                        "                \"ship_name\": \"Ninja Xpress\",\n" +
                        "                \"ship_code\": \"ninja\",\n" +
                        "                \"ship_logo\": \"kurir-ninja.png\",\n" +
                        "                \"ship_prods\": [\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 25,\n" +
                        "                    \"ship_prod_name\": \"Reguler\",\n" +
                        "                    \"ship_group_name\": \"regular\",\n" +
                        "                    \"ship_group_id\": 1004,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"is_dropship_enabled\": 0\n" +
                        "              },\n" +
                        "              {\n" +
                        "                \"ship_id\": 13,\n" +
                        "                \"ship_name\": \"Grab\",\n" +
                        "                \"ship_code\": \"grab\",\n" +
                        "                \"ship_logo\": \"kurir-grab.png\",\n" +
                        "                \"ship_prods\": [\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 37,\n" +
                        "                    \"ship_prod_name\": \"Instant\",\n" +
                        "                    \"ship_group_name\": \"instant\",\n" +
                        "                    \"ship_group_id\": 1000,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  },\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 24,\n" +
                        "                    \"ship_prod_name\": \"Same Day\",\n" +
                        "                    \"ship_group_name\": \"sameday\",\n" +
                        "                    \"ship_group_id\": 1002,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  },\n" +
                        "                  {\n" +
                        "                    \"ship_prod_id\": 26,\n" +
                        "                    \"ship_prod_name\": \"Next Day\",\n" +
                        "                    \"ship_group_name\": \"nextday\",\n" +
                        "                    \"ship_group_id\": 1003,\n" +
                        "                    \"additional_fee\": 0,\n" +
                        "                    \"minimum_weight\": 0\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"is_dropship_enabled\": 0\n" +
                        "              }\n" +
                        "            ],\n" +
                        "            \"products\": [\n" +
                        "              {\n" +
                        "                \"errors\": null,\n" +
                        "                \"product_id\": 15139121,\n" +
                        "                \"product_name\": \"keong\",\n" +
                        "                \"product_price_fmt\": \"Rp 19.000\",\n" +
                        "                \"product_price\": 19000,\n" +
                        "                \"product_wholesale_price\": 19000,\n" +
                        "                \"product_wholesale_price_fmt\": \"Rp 19.000\",\n" +
                        "                \"product_weight_fmt\": \"2.000 gr\",\n" +
                        "                \"product_weight\": 2000,\n" +
                        "                \"product_total_weight_fmt\": \"2 kg\",\n" +
                        "                \"product_total_weight\": 2000,\n" +
                        "                \"product_condition\": 1,\n" +
                        "                \"product_url\": \"https:\\/\\/staging.tokopedia.com\\/tokoko\\/keong\",\n" +
                        "                \"product_returnable\": 0,\n" +
                        "                \"product_is_free_returns\": 0,\n" +
                        "                \"product_is_preorder\": 0,\n" +
                        "                \"product_cashback\": \"\",\n" +
                        "                \"product_min_order\": 1,\n" +
                        "                \"product_invenage_value\": 0,\n" +
                        "                \"product_switch_invenage\": 0,\n" +
                        "                \"product_price_currency\": 1,\n" +
                        "                \"product_image_src_200_square\": \"https:\\/\\/imagerouter-staging.tokopedia.com\\/img\\/200-square\\/product-1\\/2017\\/9\\/29\\/5512646\\/5512646_92f653fa-1cc0-4322-9c25-edaaff52ef4d_736_736.jpg\",\n" +
                        "                \"product_notes\": \"tolong segera kirim ya2\",\n" +
                        "                \"product_quantity\": 1,\n" +
                        "                \"product_menu_id\": 1402968,\n" +
                        "                \"product_finsurance\": 0,\n" +
                        "                \"product_fcancel_partial\": 0,\n" +
                        "                \"product_shipment\": [\n" +
                        "                  {\n" +
                        "                    \"shipment_id\": \"1000\",\n" +
                        "                    \"service_id\": [\n" +
                        "                      \"1000\",\n" +
                        "                      \"1002\",\n" +
                        "                      \"1003\",\n" +
                        "                      \"1004\"\n" +
                        "                    ]\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"product_shipment_mapping\": [\n" +
                        "                  {\n" +
                        "                    \"shipment_id\": \"1000\",\n" +
                        "                    \"shipping_ids\": \"10,13\",\n" +
                        "                    \"service_ids\": [\n" +
                        "                      {\n" +
                        "                        \"service_id\": \"1000\",\n" +
                        "                        \"sp_ids\": \"28,37\"\n" +
                        "                      },\n" +
                        "                      {\n" +
                        "                        \"service_id\": \"1002\",\n" +
                        "                        \"sp_ids\": \"20,21,24\"\n" +
                        "                      },\n" +
                        "                      {\n" +
                        "                        \"service_id\": \"1003\",\n" +
                        "                        \"sp_ids\": \"23,26,6,16,11,33\"\n" +
                        "                      },\n" +
                        "                      {\n" +
                        "                        \"service_id\": \"1004\",\n" +
                        "                        \"sp_ids\": \"25,3,10,15,1,18,27,8\"\n" +
                        "                      }\n" +
                        "                    ]\n" +
                        "                  }\n" +
                        "                ],\n" +
                        "                \"product_cat_id\": 520,\n" +
                        "                \"product_catalog_id\": 0,\n" +
                        "                \"purchase_protection_plan_data\": {\n" +
                        "                    \"protection_available\": true,\n" +
                        "                    \"protection_type_id\": 123,      \n" +
                        "                    \"protection_price_per_product\": 1000,      \n" +
                        "                    \"protection_price\": 2000,      \n" +
                        "                    \"protection_title\": \"keong gadget\",      \n" +
                        "                    \"protection_subtitle\": \"protection\",      \n" +
                        "                    \"protection_link_text\": \"Proteksi Gadget\",      \n" +
                        "                    \"protection_link_url\": \"http://www.tokopedia.com/asuransi/proteksi-gadget\",     \n" +
                        "                    \"protection_opt_in\": true\n" +
                        "                }\n" +
                        "              }\n" +
                        "            ]\n" +
                        "          }\n" +
                        "        ]\n" +
                        "      }\n" +
                        "    ],\n" +
                        "    \"kero_token\": \"Tokopedia+Kero:++Z12+FGQ1YdTb6fxwCqLMk1zBU=\",\n" +
                        "    \"kero_discom_token\": \"Tokopedia+Kero:0kKx05miyG6SX3TMZkBWwrbAv6k=\",\n" +
                        "    \"kero_unix_time\": 1510307115,\n" +
                        "    \"enable_partial_cancel\": false,\n" +
                        "    \"donation\": {\n" +
                        "      \"Title\": \"TopDonasi\",\n" +
                        "      \"Nominal\": 100,\n" +
                        "      \"Description\": \"\"\n" +
                        "    },\n" +
                        "    \"cod\": {\n" +
                        "      \"is_cod\": true,\n" +
                        "      \"counter_cod\" : 1\n" +
                        "    },\n" +
                        "    \"message\": {\n" +
                        "      \"message_info\": \"Pilih <b>Bayar di Tempat</b> untuk pembayaran lewat kurir saat pesanan tiba.\",\n" +
                        "      \"message_link\": \"http://testurl.com\",\n" +
                        "      \"message_logo\": \"http://linklogo.com\"\n" +
                        "    },\n" +
                        "    \"is_one_click_shipment\": 0,\n" +
                        "    \"is_robinhood\": 0,\n" +
                        "    \"promo_suggestion\": {\n" +
                        "      \"cta\": \"\",\n" +
                        "      \"cta_color\": \"\",\n" +
                        "      \"is_visible\": 0,\n" +
                        "      \"promo_code\": \"\",\n" +
                        "      \"text\": \"\"\n" +
                        "    },\n" +
                        "    \"autoapply\": {\n" +
                        "      \"success\": false,\n" +
                        "      \"code\": \"\",\n" +
                        "      \"is_coupon\": 0,\n" +
                        "      \"discount_amount\": 0,\n" +
                        "      \"title_description\": \"\",\n" +
                        "      \"message_success\": \"\",\n" +
                        "      \"promo_id\": 0\n" +
                        "    },\n" +
                        "    \"autoapply_v2\" : {\n" +
                        "      \"message\" : {\n" +
                        "        \"state\" : \"red\",\n" +
                        "        \"color\" : \"#ea212d\",\n" +
                        "        \"text\" : \"Promo belum memenuhi minimum pembelian Rp100.000\"\n" +
                        "      },\n" +
                        "      \"code\" : \"RED\",\n" +
                        "      \"promo_code_id\" : 0,\n" +
                        "      \"title_description\" : \"Promo cashback Rp10.000\",\n" +
                        "      \"is_coupon\" : 0\n" +
                        "    }\n" +
                        "  }", ShipmentAddressFormDataResponse.class);
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
