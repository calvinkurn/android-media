package com.tokopedia.tkpd.thankyou.data.mapper;

import android.util.Log;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentMethod;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.transaction.cart.model.cartdata.CartItem;
import com.tokopedia.transaction.cart.model.cartdata.CartProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerMapper implements Func1<Response<GraphqlResponse<PaymentGraphql>>, Boolean> {
    private static final String DATA = "data";

    private static final String EVENT = "event";
    private static final String PAYMENT_ID = "payment_id";
    private static final String PAYMENT_STATUS = "payment_status";
    private static final String PAYMENT_TYPE = "payment_type";
    private static final String SHOP_ID = "shop_id";
    private static final String SHOP_TYPE = "shopType";
    private static final String LOGISTIC_TYPE = "logistic_type";
    private static final String ECOMMERCE = "ecommerce";
    private static final String USER_ID = "userId";

    private static final String CURRENCY_CODE = "currencyCode";
    private static final String PURCHASE = "purchase";

    private static final String ACTION_FIELD = "actionField";
    private static final String PRODUCTS = "products";

    private static final String ID = "id";
    private static final String AFFILIATION = "affiliation";
    private static final String REVENUE = "revenue";
    private static final String TAX = "tax";
    private static final String SHIPPING = "shipping";
    private static final String COUPON = "coupon";

    private static final String NAME = "name";
    private static final String PRICE = "price";
    private static final String BRAND = "brand";
    private static final String CATEGORY = "category";
    private static final String VARIANT = "variant";
    private static final String QUANTITY = "quantity";

    private GlobalCacheManager globalCacheManager;
    private Gson gson;
    private SessionHandler sessionHandler;

    public MarketplaceTrackerMapper(Gson gson,
                                    SessionHandler sessionHandler) {
        this.globalCacheManager = new GlobalCacheManager();
        this.gson = gson;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> response) {
        if (isResponseValid(response)) {
            JsonObject cacheData = new JsonParser().parse(
                    globalCacheManager.getValueString(TkpdCache.Key.CART_CACHE_TRACKER)
            ).getAsJsonObject();

            List<CartItem> cartItemList = gson.fromJson(
                    cacheData.get(DATA),
                    new TypeToken<List<CartItem>>() {
                    }.getType()
            );
            PaymentData paymentData = response.body().getData().getPayment();
            paymentData.setCoupon(cacheData.get(COUPON).isJsonNull() ? "" : cacheData.get(COUPON).toString());

            if (cartItemList != null) {
                for (CartItem cartItem : cartItemList) {
                    processData(paymentData, cartItem);
                }
            }
            return true;
        }

        return false;
    }

    private boolean isResponseValid(Response<GraphqlResponse<PaymentGraphql>> graphqlResponse) {
        return graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null
                && graphqlResponse.body().getData().getPayment() != null;
    }

    @SuppressWarnings("unchecked")
    private void processData(PaymentData payment, CartItem cartItem) {
        PurchaseTracking.marketplace(getTrackingData(payment, cartItem));
    }

    private Map<String, Object> getTrackingData(PaymentData paymentData, CartItem cartItem) {
        String shopId = "";
        if (cartItem.getCartShop() != null) {
            shopId = cartItem.getCartShop().getShopId();
        }

        String logisticType = "";
        if (cartItem.getCartShipments() != null) {
            logisticType = cartItem.getCartShipments().getShipmentId();
        }

        return DataLayer.mapOf(
                EVENT, PurchaseTracking.TRANSACTION,
                SHOP_ID, shopId,
                PAYMENT_ID, String.valueOf(paymentData.getPaymentId()),
                PAYMENT_TYPE, getPaymentType(paymentData.getPaymentMethod()),
                LOGISTIC_TYPE, logisticType,
                USER_ID, sessionHandler.getLoginID(),
                ECOMMERCE, getEcommerceData(paymentData, cartItem)
        );
    }

    private String getPaymentType(PaymentMethod paymentMethod) {
        if (paymentMethod != null && paymentMethod.getMethod() != null) {
            switch (paymentMethod.getMethod()) {
                case ThanksTrackerConst.Template.TRANSFER:
                    if (paymentMethod.getTransfer() != null && paymentMethod.getTransfer().getGateway() != null) {
                        return paymentMethod.getTransfer().getGateway().getGatewayName();
                    }
                    break;
                case ThanksTrackerConst.Template.INSTANT:
                    if (paymentMethod.getInstant() != null && paymentMethod.getInstant().getGateway() != null) {
                        return paymentMethod.getInstant().getGateway().getGatewayName();
                    }
                    break;
                default:
                    if (paymentMethod.getDefer() != null
                            && paymentMethod.getDefer().getGateway() != null) {
                        return paymentMethod.getDefer().getGateway().getGatewayName();
                    }
                    break;
            }
        }

        return "";
    }

    private Map<String, Object> getEcommerceData(PaymentData paymentData, CartItem cartItem) {
        return DataLayer.mapOf(
                CURRENCY_CODE, "IDR",
                PURCHASE, getPurchaseData(paymentData, cartItem)
        );
    }

    private Map<String, Object> getPurchaseData(PaymentData paymentData, CartItem cartItem) {
        List<Object> products = getProductList(paymentData, cartItem.getCartProducts());
        return DataLayer.mapOf(
                ACTION_FIELD, getActionField(paymentData, cartItem),
                PRODUCTS, DataLayer.listOf(products.toArray(new Object[products.size()]))
        );
    }

    private List<Object> getProductList(PaymentData paymentData, List<CartProduct> cartProducts) {
        List<Object> products = new ArrayList<>();
        for (CartProduct cartProduct : cartProducts) {
            products.add(DataLayer.mapOf(
                    COUPON, paymentData.getCoupon(),
                    ID, cartProduct.getProductId(),
                    NAME, cartProduct.getProductName(),
                    PRICE, cartProduct.getProductPrice(),
                    QUANTITY, cartProduct.getProductQuantity()
            ));
        }
        return products;
    }

    private Map<String, Object> getActionField(PaymentData paymentData, CartItem cartItem) {
        return DataLayer.mapOf(
                COUPON, paymentData.getCoupon(),
                REVENUE, cartItem.getCartTotalAmount(),
                SHIPPING, cartItem.getCartShippingRate()
        );
    }
}
