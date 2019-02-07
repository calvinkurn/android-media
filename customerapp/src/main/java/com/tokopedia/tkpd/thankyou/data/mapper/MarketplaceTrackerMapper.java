package com.tokopedia.tkpd.thankyou.data.mapper;


import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.model.BranchIOPayment;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.LinkerCommerceData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.GraphqlResponse;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderDetail;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentMethod;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.functions.Func1;

import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_COUPON;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerMapper implements Func1<Response<GraphqlResponse<PaymentGraphql>>, Boolean> {

    public static final String DEFAULT_SHOP_TYPE = "default";
    private SessionHandler sessionHandler;
    private List<String> shopTypes;
    private PaymentData paymentData;
    private RequestParams requestParams;
    private static final String TOKOPEDIA_MARKETPLACE = "tokopediamarketplace";

    public MarketplaceTrackerMapper(SessionHandler sessionHandler, List<String> shopTypes, RequestParams requestParams) {
        this.sessionHandler = sessionHandler;
        this.shopTypes = shopTypes;
        this.requestParams = requestParams;
    }

    @Override
    public Boolean call(Response<GraphqlResponse<PaymentGraphql>> response) {
        if (isResponseValid(response)) {
            paymentData = response.body().getData().getPayment();

            if (paymentData.getOrders() != null) {
                int indexOrdersData = 0;
                for (OrderData orderData : paymentData.getOrders()) {
                    PurchaseTracking.marketplace(MainApplication.getAppContext(), getTrackignData(orderData, indexOrdersData, getCouponCode(paymentData), getTax(paymentData)));
                    LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_COMMERCE_VAL,
                            getTrackignBranchIOData(orderData)));
                    indexOrdersData++;
                }

                PurchaseTracking.appsFlyerPurchaseEvent(MainApplication.getAppContext(), getAppsFlyerTrackingData(paymentData.getOrders()),"MarketPlace");

            }
            return true;
        }

        return false;
    }

    private String getTax(PaymentData paymentData) {
        float tax = paymentData.getFeeAmount();
        if(paymentData.getPaymentGateway() != null) {
            tax += paymentData.getPaymentGateway().getGatewayFee();
        }
        return Float.toString(tax);
    }

    private Purchase getAppsFlyerTrackingData(List<OrderData> orders) {
        Purchase purchase = new Purchase();
        float price = 0;
        List<String> orderIds = new ArrayList<>();
        float shipping = 0;
        for (OrderData orderData : orders) {
            orderIds.add(String.valueOf(orderData.getOrderId()));
            shipping +=orderData.getShippingPrice();
            price += orderData.getItemPrice();
            for (Product product : getProductList(orderData)) {
                purchase.addProduct(product.getProduct());
            }
        }
        purchase.setEventLabel(PurchaseTracking.EVENT_LABEL);
        purchase.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        purchase.setTransactionID(orderIds);
        purchase.setUserId(sessionHandler.getLoginID());
        purchase.setShipping(String.valueOf(shipping));

        purchase.setRevenue(String.valueOf(paymentData.getPaymentAmount()));

        purchase.setCurrency(Purchase.DEFAULT_CURRENCY_VALUE);
        return purchase;
    }

    private Purchase getTrackignData(OrderData orderData, Integer position, String couponCode, String tax) {
        Purchase purchase = new Purchase();
        purchase.setEvent(PurchaseTracking.TRANSACTION);
        purchase.setEventCategory(PurchaseTracking.EVENT_CATEGORY);
        purchase.setEventLabel(PurchaseTracking.EVENT_LABEL);
        purchase.setShopType(checkShopTypeForMarketplace(shopTypes.get(position)));
        purchase.setShopId(getShopId(orderData));
        purchase.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        purchase.setPaymentType(getPaymentType(paymentData.getPaymentMethod()));
        purchase.setTransactionID(String.valueOf(orderData.getOrderId()));
        purchase.setLogisticType(getLogisticType(orderData));
        purchase.setUserId(sessionHandler.getLoginID());
        purchase.setShipping(String.valueOf(orderData.getShippingPrice()));
        purchase.setRevenue(String.valueOf(paymentData.getPaymentAmount()));
        purchase.setAffiliation(getShopName(orderData));
        purchase.setTax(tax);
        purchase.setCouponCode(couponCode);
        purchase.setItemPrice(String.valueOf(orderData.getItemPrice()));
        purchase.setCurrency(Purchase.DEFAULT_CURRENCY_VALUE);
        purchase.setCurrentSite(TOKOPEDIA_MARKETPLACE);

        for (Product product : getProductList(orderData)) {
            purchase.addProduct(addCouponToProduct(product.getProduct(), couponCode));
        }

        return purchase;
    }


    private Map<String, Object> addCouponToProduct(Map<String, Object> product, String coupon){
        product.put(KEY_COUPON, coupon);
        return product;
    }

    private String checkShopTypeForMarketplace(String s){
        if (s.isEmpty()){
            return DEFAULT_SHOP_TYPE;
        }

        return s;
    }

    private boolean isResponseValid(Response<GraphqlResponse<PaymentGraphql>> graphqlResponse) {
        return graphqlResponse != null
                && graphqlResponse.body() != null
                && graphqlResponse.body().getData() != null
                && graphqlResponse.body().getData().getPayment() != null;
    }

    private String getShopId(OrderData orderData) {
        if (orderData.getShop() != null) {
            return String.valueOf(orderData.getShop().getShopId());
        }

        return "";
    }


    private String getCouponCode(PaymentData paymentDatas) {
        if (paymentDatas.getVoucher() != null){
            return paymentDatas.getVoucher().getVoucherCode();
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

    private List<Product> getProductList(OrderData orderData) {
        List<Product> products = new ArrayList<>();
        for (OrderDetail orderDetail : orderData.getOrderDetail()) {
            Product product = new Product();
            product.setProductID(String.valueOf(orderDetail.getProductId()));
            product.setProductName(getProductName(orderDetail));
            product.setPrice(String.valueOf(orderDetail.getProductPrice()));
            product.setCategory(getProductCategory(orderDetail));
            product.setQty(String.valueOf(orderDetail.getQuantity()));

            products.add(product);
        }
        return products;
    }

    private String getProductName(OrderDetail orderDetail) {
        if (orderDetail.getProduct() != null) {
            return orderDetail.getProduct().getProductName();
        }

        return "";
    }

    private String getProductCategory(OrderDetail orderDetail) {
        if (orderDetail.getProduct() != null
                && orderDetail.getProduct().getProductCategory() != null) {
            return orderDetail.getProduct().getProductCategory().getCategoryNameLevel1()
                    + "/" + orderDetail.getProduct().getProductCategory().getCategoryNameLevel2()
                    + "/" + orderDetail.getProduct().getProductCategory().getCategoryNameLevel3();
        }

        return "";
    }

    private String getShopName(OrderData orderData) {
        if (orderData.getShop() != null) {
            return orderData.getShop().getShopName();
        }

        return "";
    }

    private LinkerCommerceData getTrackignBranchIOData(OrderData orderData) {

        LinkerCommerceData linkerCommerceData = new LinkerCommerceData();

        UserSession userSession = new UserSession(LinkerManager.getInstance().getContext());

        UserData userData = new UserData();
        userData.setUserId(userSession.getUserId());
        userData.setPhoneNumber(userSession.getPhoneNumber());
        userData.setName(userSession.getName());
        userData.setEmail(userSession.getEmail());

        linkerCommerceData.setUserData(userData);

        com.tokopedia.linker.model.PaymentData branchIOPayment = new com.tokopedia.linker.model.PaymentData();

        branchIOPayment.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        branchIOPayment.setOrderId(String.valueOf(orderData.getOrderId()));
        branchIOPayment.setShipping(String.valueOf((int)orderData.getShippingPrice()));
        branchIOPayment.setRevenue(String.valueOf((int)paymentData.getPaymentAmount()));
        branchIOPayment.setProductType(LinkerConstants.PRODUCTTYPE_MARKETPLACE);
        branchIOPayment.setItemPrice(String.valueOf(orderData.getItemPrice()));
        for (OrderDetail orderDetail : orderData.getOrderDetail()) {
            HashMap<String, String> product = new HashMap<>();
            product.put(LinkerConstants.ID, String.valueOf(orderDetail.getProductId()));
            product.put(LinkerConstants.NAME, getProductName(orderDetail));
            product.put(LinkerConstants.PRICE, String.valueOf((int)orderDetail.getProductPrice()));
            product.put(LinkerConstants.PRICE_IDR_TO_DOUBLE,String.valueOf(CurrencyFormatHelper.convertRupiahToLong(
                    String.valueOf(orderDetail.getProductPrice()))));
            product.put(LinkerConstants.QTY, String.valueOf(orderDetail.getQuantity()));
            product.put(LinkerConstants.CATEGORY, String.valueOf(orderDetail.getProduct().getProductCategory().getCategoryName()));

            branchIOPayment.setProduct(product);
        }

        linkerCommerceData.setPaymentData(branchIOPayment);

        return linkerCommerceData;
    }
}
