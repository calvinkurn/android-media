package com.tokopedia.tkpd.thankyou.data.mapper;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.gson.Gson;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderDetail;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentMethod;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;

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
    private static final String EVENT_CATEGORY = "eventCategory";
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

    private SessionHandler sessionHandler;

    private PaymentData paymentData;

    public MarketplaceTrackerMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> response) {
        if (isResponseValid(response)) {

            paymentData = response.body().getData().getPayment();

            if (paymentData.getOrders() != null) {
                for (OrderData orderData : paymentData.getOrders()) {
                    PurchaseTracking.marketplace(getTrackingData(orderData));
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

    private Map<String, Object> getTrackingData(OrderData orderData) {
        return DataLayer.mapOf(
                EVENT, PurchaseTracking.TRANSACTION,
                EVENT_CATEGORY, PURCHASE,
                SHOP_ID, getShopId(orderData),
                PAYMENT_ID, String.valueOf(paymentData.getPaymentId()),
                PAYMENT_TYPE, getPaymentType(paymentData.getPaymentMethod()),
                LOGISTIC_TYPE, getLogisticType(orderData),
                USER_ID, sessionHandler.getLoginID(),
                ECOMMERCE, getEcommerceData(orderData)
        );
    }

    private Object getShopId(OrderData orderData) {
        if(orderData.getShop() != null) {
            return orderData.getShop().getShopId();
        }

        return "";
    }

    private String getLogisticType(OrderData orderData) {
        if (orderData.getShipping() != null) {
            return orderData.getShipping().getShippingName();
        }

        return "";
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

    private Map<String, Object> getEcommerceData(OrderData orderData) {
        return DataLayer.mapOf(
                CURRENCY_CODE, "IDR",
                PURCHASE, getPurchaseData(orderData)
        );
    }

    private Map<String, Object> getPurchaseData(OrderData orderData) {
        List<Object> products = getProductList(orderData);
        return DataLayer.mapOf(
                ACTION_FIELD, getActionField(orderData),
                PRODUCTS, DataLayer.listOf(products.toArray(new Object[products.size()]))
        );
    }

    private List<Object> getProductList(OrderData orderData) {
        List<Object> products = new ArrayList<>();
        for (OrderDetail orderDetail : orderData.getOrderDetail()) {

            products.add(DataLayer.mapOf(
                    ID, orderDetail.getProductId(),
                    NAME, getProductName(orderDetail),
                    PRICE, orderDetail.getProductPrice(),
                    BRAND, "other",
                    CATEGORY, getProductCategory(orderDetail),
                    VARIANT, "other",
                    QUANTITY, orderDetail.getQuantity()
            ));
        }
        return products;
    }

    private String getProductName(OrderDetail orderDetail) {
        if(orderDetail.getProduct() != null) {
            return orderDetail.getProduct().getProductName();
        }

        return "";
    }

    private String getProductCategory(OrderDetail orderDetail) {
        if(orderDetail.getProduct() != null
                && orderDetail.getProduct().getProductCategory() != null) {
            return orderDetail.getProduct().getProductCategory().getCategoryName();
        }

        return "";
    }

    private Map<String, Object> getActionField(OrderData orderData) {
        return DataLayer.mapOf(
                ID, orderData.getOrderId(),
                AFFILIATION, getShopName(orderData),
                REVENUE, paymentData.getPaymentAmount(),
                SHIPPING, orderData.getShippingPrice(),
                COUPON, paymentData.getCoupon()
        );
    }

    private String getShopName(OrderData orderData) {
        if(orderData.getShop() != null) {
            return orderData.getShop().getShopName();
        }

        return "";
    }


}
