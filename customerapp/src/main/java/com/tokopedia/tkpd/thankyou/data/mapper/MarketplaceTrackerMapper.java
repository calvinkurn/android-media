package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.gson.Gson;
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
    private GlobalCacheManager globalCacheManager;
    private Gson gson;

    public MarketplaceTrackerMapper(Gson gson) {
        this.globalCacheManager = new GlobalCacheManager();
        this.gson = gson;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> graphqlResponse) {
        if(graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null
                && graphqlResponse.body().getData().getPayment() != null) {
            List<CartItem> cartItemList = gson.fromJson(
                    globalCacheManager.getValueString(TkpdCache.Key.CART_CACHE_TRACKER),
                    new TypeToken<List<CartItem>>() {
                    }.getType()
            );

            for(CartItem cartItem: cartItemList) {
                MarketplaceTrackerData marketplaceTrackerData = getTrackingData(graphqlResponse.body().getData().getPayment(), cartItem);

                String rawTrackingData = gson.toJson(marketplaceTrackerData);
                Map<String, Object> trackingPayload = gson.fromJson(rawTrackingData, LinkedTreeMap.class);
                PurchaseTracking.marketplace("transaction", trackingPayload);
            }
            return true;
        }

        return false;
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
        ecommerce.setCurrencyCode("");
        ecommerce.setPurchase(
                getPurchaseData(paymentData,cartItem)
        );

        return ecommerce;
    }

    private Purchase getPurchaseData(PaymentData paymentData, CartItem cartItem) {
        Purchase purchase = new Purchase();
        purchase.setActionField(
                getActionField(paymentData, cartItem)
        );
        purchase.setProducts(
                getProductList(paymentData, cartItem)
        );
        return purchase;
    }

    private List<Product> getProductList(PaymentData paymentData, CartItem cartItem) {
        List<Product> products = new ArrayList<>();
        for(CartProduct cartProduct : cartItem.getCartProducts()) {
            Product product = new Product();
            product.setBrand("");
//            product.setCategory();
//            product.setCoupon();
            product.setId(cartProduct.getProductId());
            product.setName(cartProduct.getProductName());
            product.setPrice(cartProduct.getProductPrice());
            product.setQuantity(String.valueOf(cartProduct.getProductQuantity()));
//            product.setVariant(cartProduct.);
            products.add(product);
        }
        return products;
    }

    private ActionField getActionField(PaymentData paymentData, CartItem cartItem) {
        ActionField actionField = new ActionField();
//        actionField.setId();
//        actionField.setAffiliation();
//        actionField.setCoupon();
//        actionField.setRevenue();
        actionField.setShipping(cartItem.getCartShipments().getShipmentName());
//        actionField.setTax("");
        return actionField;
    }
}
