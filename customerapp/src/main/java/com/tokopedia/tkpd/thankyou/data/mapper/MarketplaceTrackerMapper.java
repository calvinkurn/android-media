package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentMethod;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker.ActionField;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker.Ecommerce;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker.MarketplaceTrackerData;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker.Product;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.tracker.Purchase;
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
    private static final String COUPON = "coupon";
    private GlobalCacheManager globalCacheManager;
    private Gson gson;

    public MarketplaceTrackerMapper(Gson gson) {
        this.globalCacheManager = new GlobalCacheManager();
        this.gson = gson;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> response) {
        if(isResponseValid(response)) {
            JsonObject cacheData = new JsonParser().parse(
                    globalCacheManager.getValueString(TkpdCache.Key.CART_CACHE_TRACKER)
            ).getAsJsonObject();

            List<CartItem> cartItemList = gson.fromJson(
                    cacheData.get(DATA),
                    new TypeToken<List<CartItem>>() {}.getType()
            );
            PaymentData paymentData = response.body().getData().getPayment();
            paymentData.setCoupon(cacheData.get(COUPON).isJsonNull() ? "" : cacheData.get(COUPON).toString());

            if(cartItemList != null) {
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
        MarketplaceTrackerData marketplaceTrackerData = getTrackingData(payment, cartItem);
        String rawTrackingData = gson.toJson(marketplaceTrackerData);
        PurchaseTracking.marketplace(gson.fromJson(rawTrackingData, LinkedTreeMap.class));
    }

    private MarketplaceTrackerData getTrackingData(PaymentData paymentData, CartItem cartItem) {
        MarketplaceTrackerData trackingData = new MarketplaceTrackerData();

        if(cartItem.getCartShop() != null) {
            trackingData.setShopId(cartItem.getCartShop().getShopId());
        }

        trackingData.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        trackingData.setShopType("");

        trackingData.setPaymentType(
                getPaymentType(paymentData.getPaymentMethod())
        );

        if(cartItem.getCartShipments() != null) {
            trackingData.setLogisticType(cartItem.getCartShipments().getShipmentId());
        }

        trackingData.setEcommerce(
                getEcommerceData(paymentData, cartItem)
        );

        return trackingData;
    }

    private String getPaymentType(PaymentMethod paymentMethod) {
        if(paymentMethod != null && paymentMethod.getMethod() != null) {
            switch (paymentMethod.getMethod()) {
                case ThanksTrackerConst.Template.TRANSFER:
                    if(paymentMethod.getTransfer() != null && paymentMethod.getTransfer().getGateway() != null) {
                        return paymentMethod.getTransfer().getGateway().getGatewayName();
                    }
                    break;
                case ThanksTrackerConst.Template.INSTANT:
                    if(paymentMethod.getInstant() != null && paymentMethod.getInstant().getGateway() != null) {
                        return paymentMethod.getInstant().getGateway().getGatewayName();
                    }
                    break;
                default:
                    if(paymentMethod.getDefer() != null
                            && paymentMethod.getDefer().getGateway() != null) {
                        return paymentMethod.getDefer().getGateway().getGatewayName();
                    }
                    break;
            }
        }

        return "";
    }

    private Ecommerce getEcommerceData(PaymentData paymentData, CartItem cartItem) {
        Ecommerce ecommerce = new Ecommerce();
        ecommerce.setCurrencyCode("IDR");
        ecommerce.setPurchase(
                getPurchaseData(paymentData,cartItem)
        );

        return ecommerce;
    }

    private Purchase getPurchaseData(PaymentData paymentData, CartItem cartItem) {
        Purchase purchase = new Purchase();
        purchase.setActionField(getActionField(paymentData, cartItem));
        purchase.setProducts(getProductList(paymentData, cartItem.getCartProducts()));
        return purchase;
    }

    private List<Product> getProductList(PaymentData paymentData, List<CartProduct> cartProducts) {
        List<Product> products = new ArrayList<>();
        for(CartProduct cartProduct : cartProducts) {
            Product product = new Product();
//            product.setBrand();
//            product.setCategory(cartProduct.);
            product.setCoupon(paymentData.getCoupon());
            product.setId(cartProduct.getProductId());
            product.setName(cartProduct.getProductName());
            product.setPrice(cartProduct.getProductPrice());
            product.setQuantity(cartProduct.getProductQuantity());
//            product.setVariant(cartProduct.);
            products.add(product);
        }
        return products;
    }

    private ActionField getActionField(PaymentData paymentData, CartItem cartItem) {
        ActionField actionField = new ActionField();
        actionField.setId(String.valueOf(paymentData.getPaymentId()));
//        actionField.setAffiliation();
        actionField.setCoupon(paymentData.getCoupon());
        actionField.setRevenue(cartItem.getCartTotalAmount());
        actionField.setShipping(cartItem.getCartShippingRate());
        return actionField;
    }
}
