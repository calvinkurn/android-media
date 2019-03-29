package com.tokopedia.tkpd.thankyou.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.analytics.PurchaseTracking;
import com.tokopedia.core.analytics.model.BranchIOPayment;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.CurrencyFormatHelper;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.LinkerCommerceData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.DigitalDataWrapper;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.response.Product;
import com.tokopedia.tkpd.thankyou.data.pojo.digital.response.PurchaseData;
import com.tokopedia.user.session.UserSession;

import java.util.HashMap;
import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by okasurya on 12/7/17.
 */

public class DigitalTrackerMapper implements Func1<Response<DigitalDataWrapper<PurchaseData>>, Boolean> {
    // apparently digital doesnt need shipping but this value seems to be mandatory on EE
    private static final String DEFAULT_DIGITAL_SHIPPING = "0";
    private static final String TOKOPEDIA_DIGITAL = "tokopediadigital";

    private SessionHandler sessionHandler;

    public DigitalTrackerMapper(SessionHandler sessionHandler) {
        this.sessionHandler = sessionHandler;
    }

    @Override
    public Boolean call(Response<DigitalDataWrapper<PurchaseData>> response) {
        if (response != null && response.body() != null && response.body().getData() != null) {
            PurchaseTracking.digital(MainApplication.getAppContext(), getMappedData(response.body().getData()));
            LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_COMMERCE_VAL,
                    getTrackignBranchIOData(response.body().getData())));
            return true;
        }

        return false;
    }

    private Purchase getMappedData(PurchaseData data) {
        Purchase purchase = new Purchase();
        purchase.setEvent(PurchaseTracking.TRANSACTION);
        purchase.setShopId(data.getShopId());
        purchase.setPaymentId(data.getPaymentId());
        purchase.setPaymentType(data.getPaymentType());
        purchase.setPaymentStatus(data.getPaymentStatus());
        purchase.setUserId(sessionHandler.getLoginID());
        purchase.setCurrentSite(TOKOPEDIA_DIGITAL);
        purchase.setItemPrice(String.valueOf(parseStringToInt(data.getEcommerce().getPurchase().getActionField().getRevenue())));
        if (isActionFieldValid(data)) {
            purchase.setTransactionID(data.getEcommerce().getPurchase().getActionField().getId());
            purchase.setShipping(DEFAULT_DIGITAL_SHIPPING);
            purchase.setVoucherCode(data.getEcommerce().getPurchase().getActionField().getCoupon());
            purchase.setRevenue(data.getEcommerce().getPurchase().getActionField().getRevenue());
        }
        purchase.setCurrency(Purchase.DEFAULT_CURRENCY_VALUE);

        if (isPurchaseValid(data) && data.getEcommerce().getPurchase().getProducts() != null) {
            for (Product product : data.getEcommerce().getPurchase().getProducts()) {
                purchase.addProduct(mapProduct(product).getProduct());
            }
        }

        return purchase;
    }

    private com.tokopedia.core.analytics.nishikino.model.Product mapProduct(Product product) {
        com.tokopedia.core.analytics.nishikino.model.Product productTracker = new com.tokopedia.core.analytics.nishikino.model.Product();
        productTracker.setProductID(product.getId());
        productTracker.setProductName(product.getName());
        productTracker.setPrice(product.getPrice());
        productTracker.setCategory(product.getCategory());
        productTracker.setQty(String.valueOf(product.getQuantity()));
        return productTracker;
    }

    private boolean isActionFieldValid(PurchaseData data) {
        return isPurchaseValid(data)
                && data.getEcommerce().getPurchase().getActionField() != null;
    }

    private boolean isPurchaseValid(PurchaseData data) {
        return data.getEcommerce() != null
                && data.getEcommerce().getPurchase() != null;
    }

    private LinkerCommerceData getTrackignBranchIOData(PurchaseData data) {

        LinkerCommerceData linkerCommerceData = new LinkerCommerceData();

        UserSession userSession = new UserSession(LinkerManager.getInstance().getContext());

        UserData userData = new UserData();
        userData.setUserId(userSession.getUserId());
        userData.setPhoneNumber(userSession.getPhoneNumber());
        userData.setName(userSession.getName());
        userData.setEmail(userSession.getEmail());

        linkerCommerceData.setUserData(userData);

        com.tokopedia.linker.model.PaymentData branchIOPayment = new com.tokopedia.linker.model.PaymentData();

        branchIOPayment.setPaymentId(String.valueOf(data.getPaymentId()));
        if (isActionFieldValid(data)) {
            branchIOPayment.setShipping(DEFAULT_DIGITAL_SHIPPING);
            branchIOPayment.setRevenue(String.valueOf(parseStringToInt(data.getEcommerce().getPurchase().getActionField().getRevenue())));
            branchIOPayment.setOrderId(data.getEcommerce().getPurchase().getActionField().getId());
        }
        branchIOPayment.setProductType(LinkerConstants.PRODUCTTYPE_DIGITAL);
        if (isPurchaseValid(data) && data.getEcommerce().getPurchase().getProducts() != null) {
            for (Product product : data.getEcommerce().getPurchase().getProducts()) {
                HashMap<String, String> localProduct = new HashMap<>();
                localProduct.put(LinkerConstants.ID, product.getId());
                localProduct.put(LinkerConstants.NAME, product.getName());
                localProduct.put(LinkerConstants.PRICE,String.valueOf(parseStringToInt(product.getPrice())));
                localProduct.put(LinkerConstants.PRICE_IDR_TO_DOUBLE,String.valueOf(CurrencyFormatHelper.convertRupiahToLong(product.getPrice())));
                localProduct.put(LinkerConstants.QTY, String.valueOf(product.getQuantity()));
                String category = product.getCategory();
                if(TextUtils.isEmpty(category)){
                    category = product.getName();
                }
                localProduct.put(LinkerConstants.CATEGORY, category);
                branchIOPayment.setProduct(localProduct);
            }
        }

        linkerCommerceData.setPaymentData(branchIOPayment);

        return linkerCommerceData;
    }

    private  int parseStringToInt(String input){

        try{
            return Integer.parseInt(input);
        }catch(NumberFormatException e){
            return 0;
        }
    }
}
