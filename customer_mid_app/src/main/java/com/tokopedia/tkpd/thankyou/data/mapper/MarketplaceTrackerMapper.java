package com.tokopedia.tkpd.thankyou.data.mapper;


import android.os.Bundle;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.network.CoreNetworkApplication;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.LinkerCommerceData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.PaymentGraphql;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.BenefitByOrder;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.MonthlyBuyerBase;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderDetail;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.OrderLevel;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentData;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentMethod;
import com.tokopedia.tkpd.thankyou.data.pojo.marketplace.payment.PaymentType;
import com.tokopedia.tkpd.thankyou.data.source.MonthlyNewBuyerSource;
import com.tokopedia.tkpd.thankyou.domain.model.ThanksTrackerConst;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlin.Pair;
import rx.Subscriber;
import rx.functions.Func1;

import static com.tokopedia.core.analytics.nishikino.model.Product.KEY_COUPON;

/**
 * Created by okasurya on 12/7/17.
 */

public class MarketplaceTrackerMapper implements Func1<PaymentGraphql, Boolean> {

    private static final String DEFAULT_SHOP_TYPE = "default";
    private static final String PAYMENT_TYPE_COD = "COD";
    private static final String TOKOPEDIA_MARKETPLACE = "tokopediamarketplace";
    private static final String TYPE_GLOBAL_VOUCHER = "global";
    private static final String TYPE_PAYMENT_VOUCHER = "payment";
    private static final String TYPE_MERCHANT_VOUCHER = "merchant";
    private static final String TYPE_LOGISTIC_VOUCHER = "logistic";
    private static final String NOT_SET = "(not set)";
    private static final String EVENT_LABEL_REGULAR_CHECKOUT = "regular checkout";
    private static final String EVENT_LABEL_OCC = "occ";
    private UserSessionInterface userSessionInterface;
    private List<String> shopTypes;
    private PaymentData paymentData;
    private boolean monthlyNewBuyerFlag;

    public MarketplaceTrackerMapper(UserSessionInterface userSessionInterface, List<String> shopTypes, RequestParams requestParams) {
        this.userSessionInterface = userSessionInterface;
        this.shopTypes = shopTypes;
    }

    @Override
    public Boolean call(PaymentGraphql response) {
        if (isResponseValid(response)) {
            paymentData = response.getPayment();
            String orderId = getOrderId(response);
            if (!TextUtils.isEmpty(orderId)) {
                new MonthlyNewBuyerSource().executeMonthlyNewBuyerCheck(CoreNetworkApplication.getAppContext(), getMonthlyNewBuyerSubscriber(),
                        orderId);
            } else {
                executeSendPaymentTracking(false);
            }
            return true;
        }
        return false;
    }

    private String getOrderId(PaymentGraphql response) {
        if (response != null && response.getPayment() != null && response.getPayment().getOrders() != null &&
                response.getPayment().getOrders().size() > 0) {
            return String.valueOf(response.getPayment().getOrders().get(0).getOrderId());
        } else {
            return "";
        }
    }

    private void executeSendPaymentTracking(boolean monthlyNewbuyer) {
        this.monthlyNewBuyerFlag = monthlyNewbuyer;
        String paymentType = generatePaymentType(paymentData.getPaymentType());
        if (paymentData.getOrders() != null) {
            int indexOrdersData = 0;
            for (OrderData orderData : paymentData.getOrders()) {
                PurchaseTracking.marketplace(
                        CoreNetworkApplication.getAppContext(),
                        getTrackingData(
                                orderData, indexOrdersData, getListCouponCode(paymentData, orderData.getOrderId()
                                ), getTax(paymentData), paymentType
                        )
                );
                LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_COMMERCE_VAL,
                        getTrackignBranchIOData(orderData)));
                indexOrdersData++;
            }

            PurchaseTracking.appsFlyerPurchaseEvent(CoreNetworkApplication.getAppContext(), getAppsFlyerTrackingData(paymentData.getOrders()), "MarketPlace");

        }
    }

    private Subscriber getMonthlyNewBuyerSubscriber() {
        return new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                executeSendPaymentTracking(false);
            }

            @Override
            public void onNext(GraphqlResponse graphqlResponse) {
                MonthlyBuyerBase monthlyNewBuyer = graphqlResponse.getData(MonthlyBuyerBase.class);
                executeSendPaymentTracking("1".equalsIgnoreCase(monthlyNewBuyer.getMonthlyNewBuyer().getIsMonthlyFirstTransaction()));
            }
        };
    }

    private String generatePaymentType(PaymentType paymentType) {
        if (paymentType != null && paymentType.getTypes() != null && !paymentType.getTypes().isEmpty()) {
            for (String type : paymentType.getTypes()) {
                if (type.equalsIgnoreCase(PAYMENT_TYPE_COD)) {
                    return PAYMENT_TYPE_COD;
                }
            }
        }
        return DEFAULT_SHOP_TYPE;
    }

    private String getTax(PaymentData paymentData) {
        float tax = paymentData.getFeeAmount();
        if (paymentData.getPaymentGateway() != null) {
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
            shipping += orderData.getOrderInfo().getShipping().getShippingPrice();
            price += orderData.getItemPrice();
            for (Product product : getProductList(orderData)) {
                purchase.addProduct(product.getProduct());
            }
        }
        purchase.setEventLabel(PurchaseTracking.EVENT_LABEL);
        purchase.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        purchase.setTransactionID(orderIds);
        purchase.setUserId(userSessionInterface.getUserId());
        purchase.setShipping(String.valueOf(shipping));

        purchase.setRevenue(String.valueOf(paymentData.getPaymentAmount()));

        purchase.setCurrency(Purchase.DEFAULT_CURRENCY_VALUE);
        return purchase;
    }

    private Pair<Purchase, Bundle> getTrackingData(OrderData orderData, Integer position, String couponCode, String tax, String paymentType) {
        Purchase purchase = new Purchase();
        purchase.setEvent(PurchaseTracking.TRANSACTION);
        purchase.setEventCategory(PurchaseTracking.EVENT_CATEGORY);
        if (paymentData.getPaymentFeatures() != null && paymentData.getPaymentFeatures().contains(EVENT_LABEL_OCC)) {
            purchase.setEventLabel(EVENT_LABEL_OCC);
        } else {
            purchase.setEventLabel(EVENT_LABEL_REGULAR_CHECKOUT);
        }
        purchase.setShopType(checkShopTypeForMarketplace(shopTypes.get(position)));
        purchase.setEventAction(generateEventAction(checkShopTypeForMarketplace(shopTypes.get(position)), paymentType));
        purchase.setShopId(getShopId(orderData));
        purchase.setPaymentId(String.valueOf(paymentData.getPaymentId()));
        purchase.setPaymentType(getPaymentType(paymentData.getPaymentMethod()));
        purchase.setTransactionID(String.valueOf(orderData.getOrderId()));
        purchase.setLogisticType(getLogisticType(orderData));
        purchase.setUserId(userSessionInterface.getUserId());
        purchase.setShipping(String.valueOf(orderData.getOrderInfo().getShipping().getShippingPrice()));
        purchase.setRevenue(String.valueOf(paymentData.getPaymentAmount()));
        purchase.setAffiliation(getShopName(orderData));
        purchase.setTax(tax);
        purchase.setCouponCode(couponCode);
        purchase.setItemPrice(String.valueOf(orderData.getItemPrice()));
        purchase.setCurrency(Purchase.DEFAULT_CURRENCY_VALUE);
        purchase.setCurrentSite(TOKOPEDIA_MARKETPLACE);

        Bundle bundle = new Bundle();
        ArrayList<Bundle> bundles = new ArrayList<>();
        for (Pair<Product, Bundle> productBundlePair : getProductBundleList(orderData)) {
            purchase.addProduct(productBundlePair.getFirst().getProduct());
            bundles.add(productBundlePair.getSecond());
        }
        bundle.putParcelableArrayList("products", bundles);
        return new Pair<>(purchase, bundle);
    }

    private String generateEventAction(String checkShopTypeForMarketplace, String paymentType) {
        if (paymentType.equalsIgnoreCase(PAYMENT_TYPE_COD)) {
            return PurchaseTracking.EVENT_ACTION_COD;
        }
        return checkShopTypeForMarketplace;
    }


    private Map<String, Object> addCouponToProduct(Map<String, Object> product, String coupon) {
        product.put(KEY_COUPON, coupon);
        return product;
    }

    private String checkShopTypeForMarketplace(String s) {
        if (s.isEmpty()) {
            return DEFAULT_SHOP_TYPE;
        }

        return s;
    }

    private boolean isResponseValid(PaymentGraphql graphqlResponse) {
        return graphqlResponse != null;
    }

    private String getShopId(OrderData orderData) {
        if (orderData.getShop() != null) {
            return String.valueOf(orderData.getShop().getShopId());
        }

        return "";
    }


    private String getCouponCode(PaymentData paymentDatas) {
        if (paymentDatas.getVoucher() != null) {
            return paymentDatas.getVoucher().getVoucherCode();
        }

        return "";
    }

    private String getListCouponCode(PaymentData paymentData, int orderId) {
        // coupon = [Global Voucher], [Merchant Voucher], [Logistic Voucher], [Payment Voucher]
        String allCoupons, globalVoucher = NOT_SET, merchantVoucher = NOT_SET, logisticVoucher = NOT_SET, paymentVoucher = NOT_SET;

        if (paymentData != null && paymentData.getStackedPromos() != null && paymentData.getStackedPromos().getListBenefitByOrders() != null) {
            for (BenefitByOrder benefitByOrder : paymentData.getStackedPromos().getListBenefitByOrders()) {
                if (benefitByOrder != null && benefitByOrder.getOrderId() == orderId) {
                    if (benefitByOrder.getListGlobalLevel() != null) {
                        for (OrderLevel orderLevel : benefitByOrder.getListGlobalLevel()) {
                            if (orderLevel.getType() != null) {
                                if (orderLevel.getType().equalsIgnoreCase(TYPE_GLOBAL_VOUCHER)) {
                                    globalVoucher = orderLevel.getPromoCode();
                                } else if (orderLevel.getType().equalsIgnoreCase(TYPE_PAYMENT_VOUCHER)) {
                                    paymentVoucher = orderLevel.getPromoCode();
                                }
                            }
                        }
                    }

                    if (benefitByOrder.getListOrderLevel() != null) {
                        for (OrderLevel orderLevel : benefitByOrder.getListOrderLevel()) {
                            if (orderLevel.getType() != null) {
                                if (orderLevel.getType().equalsIgnoreCase(TYPE_MERCHANT_VOUCHER)) {
                                    merchantVoucher = orderLevel.getPromoCode();
                                } else if (orderLevel.getType().equalsIgnoreCase(TYPE_LOGISTIC_VOUCHER)) {
                                    logisticVoucher = orderLevel.getPromoCode();
                                }
                            }
                        }
                    }
                }
                break;
            }
        }

        allCoupons = globalVoucher + " , " + merchantVoucher + " , " + logisticVoucher + " , " + paymentVoucher;
        return allCoupons;
    }

    private String getLogisticType(OrderData orderData) {
        if (orderData.getOrderInfo().getShipping() != null) {
            return orderData.getOrderInfo().getShipping().getShippingName();
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
        for (OrderDetail orderDetail : orderData.getOrderInfo().getOrderDetail()) {
            Product product = new Product();
            product.setProductID(String.valueOf(orderDetail.getProductId()));
            product.setProductName(getProductName(orderDetail));
            product.setPrice(String.valueOf(orderDetail.getProductPrice()));
            product.setCategory(orderDetail.getCategoryName());
            product.setQty(String.valueOf(orderDetail.getQuantity()));
            product.setDimension54(getDimension54Value(orderData.isFulfillment()));
            product.setDimension83(getDimension83Value(orderData.isFreeShipping()));

            products.add(product);
        }
        return products;
    }

    private List<Pair<Product, Bundle>> getProductBundleList(OrderData orderData) {
        List<Pair<Product, Bundle>> products = new ArrayList<>();
        for (OrderDetail orderDetail : orderData.getOrderInfo().getOrderDetail()) {
            Product product = new Product();
            Bundle bundle = new Bundle();
            product.setProductID(String.valueOf(orderDetail.getProductId()));
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(orderDetail.getProductId()));
            product.setProductName(getProductName(orderDetail));
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, getProductName(orderDetail));
            product.setPrice(String.valueOf(orderDetail.getProductPrice()));
            bundle.putDouble(FirebaseAnalytics.Param.PRICE, orderDetail.getProductPrice());
            product.setCategory(orderDetail.getCategoryName());
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, orderDetail.getCategoryName());
            product.setQty(String.valueOf(orderDetail.getQuantity()));
            bundle.putInt(FirebaseAnalytics.Param.QUANTITY, orderDetail.getQuantity());
            product.setDimension54(getDimension54Value(orderData.isFulfillment()));
            bundle.putString(Product.KEY_DIMENSION_54, getDimension54Value(orderData.isFulfillment()));
            product.setDimension83(getDimension83Value(orderData.isFreeShipping()));
            bundle.putString(Product.KEY_DIMENSION_83, getDimension83Value(orderData.isFreeShipping()));

            products.add(new Pair<>(product, bundle));
        }
        return products;
    }

    private String getProductName(OrderDetail orderDetail) {
        if (orderDetail != null) {
            return orderDetail.getProductName();
        }

        return "";
    }

    private String getDimension83Value(boolean isFreeShipping) {
        if (isFreeShipping) {
            return "bebas ongkir";
        } else {
            return "none/others";
        }
    }

    private String getDimension54Value(boolean isFulfillment) {
        return isFulfillment ? "tokopedia" : "regular";
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
        branchIOPayment.setShipping(String.valueOf((long) orderData.getOrderInfo().getShipping().getShippingPrice()));
        branchIOPayment.setRevenue(String.valueOf((long) paymentData.getPaymentAmount()));
        branchIOPayment.setProductType(LinkerConstants.PRODUCTTYPE_MARKETPLACE);
        branchIOPayment.setItemPrice(String.valueOf((long) orderData.getItemPrice()));
        branchIOPayment.setNewBuyer(orderData.isNewBuyer());
        branchIOPayment.setMonthlyNewBuyer(monthlyNewBuyerFlag);
        for (OrderDetail orderDetail : orderData.getOrderInfo().getOrderDetail()) {
            HashMap<String, String> product = new HashMap<>();
            product.put(LinkerConstants.ID, String.valueOf(orderDetail.getProductId()));
            product.put(LinkerConstants.NAME, getProductName(orderDetail));
            product.put(LinkerConstants.PRICE, String.valueOf((long) orderDetail.getProductPrice()));
            product.put(LinkerConstants.PRICE_IDR_TO_DOUBLE, String.valueOf(CurrencyFormatHelper.convertRupiahToLong(
                    String.valueOf((long) orderDetail.getProductPrice()))));
            product.put(LinkerConstants.QTY, String.valueOf(orderDetail.getQuantity()));
            if (orderDetail.getCategoryName() != null) {
                product.put(LinkerConstants.CATEGORY, orderDetail.getCategoryName());
            } else {
                product.put(LinkerConstants.CATEGORY, "");
            }

            branchIOPayment.setProduct(product);
        }

        linkerCommerceData.setPaymentData(branchIOPayment);

        return linkerCommerceData;
    }
}
