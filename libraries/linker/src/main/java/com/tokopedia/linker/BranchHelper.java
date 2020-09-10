package com.tokopedia.linker;

import android.content.Context;

import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.PaymentData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.linker.validation.BranchHelperValidation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchContentSchema;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.LinkProperties;

public class BranchHelper {

    //Set userId to Branch.io sdk, userId, 127 chars or less
    public static void sendIdentityEvent(String userId) {
        if (Branch.getInstance() != null) {
            Branch.getInstance().setIdentity(userId);
        }
    }

    public static void setCommonLinkProperties(LinkProperties linkProperties, LinkerData data){
        linkProperties.setCampaign(data.getCampaignName());
        linkProperties.setChannel(LinkerData.ARG_UTM_SOURCE);
        linkProperties.setFeature(LinkerData.ARG_UTM_MEDIUM);
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_URL, data.getOgUrl());
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_TITLE, LinkerUtils.getOgTitle(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_IMAGE_URL, LinkerUtils.getOgImage(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_DESC, LinkerUtils.getOgDesc(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, data.renderShareUri());
    }

    public static void sendCommerceEvent(PaymentData branchIOPayment, Context context, UserData userData){
        try {
            List<BranchUniversalObject> branchUniversalObjects = new ArrayList<>();

            for (HashMap<String, String> product : branchIOPayment.getProducts()) {

                BranchUniversalObject buo = new BranchUniversalObject()
                        .setTitle(product.get(LinkerConstants.NAME))
                        .setContentMetadata(
                                new ContentMetadata()
                                        .setPrice(LinkerUtils.convertToDouble(product.get(LinkerConstants.PRICE_IDR_TO_DOUBLE),"Product price"), CurrencyType.IDR)
                                        .setProductName(product.get(LinkerConstants.NAME))
                                        .setQuantity(LinkerUtils.convertStringToDouble(product.get(LinkerConstants.QTY)))
                                        .setSku(product.get(LinkerConstants.ID))
                                        .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                                        .addCustomMetadata(LinkerConstants.ProductCategory, String.valueOf(product.get(LinkerConstants.CATEGORY))));

                branchUniversalObjects.add(buo);
            }

            double revenuePrice;
            if (!branchIOPayment.isFromNative
                    && LinkerConstants.PRODUCTTYPE_MARKETPLACE.equalsIgnoreCase(branchIOPayment.getProductType())) {
                revenuePrice = Double.parseDouble(branchIOPayment.getItemPrice());
            } else {
                revenuePrice = LinkerUtils.convertToDouble(branchIOPayment.getRevenue(), "Revenue");
            }
            double shippingPrice = LinkerUtils.convertToDouble(branchIOPayment.getShipping(), "Shipping");

            new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                    .setTransactionID(branchIOPayment.getOrderId())
                    .setCurrency(CurrencyType.IDR)
                    .setShipping(shippingPrice)
                    .setRevenue(revenuePrice)
                    .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, branchIOPayment.getPaymentId())
                    .addCustomDataProperty(LinkerConstants.KEY_PRODUCTTYPE, branchIOPayment.getProductType())
                    .addCustomDataProperty(LinkerConstants.KEY_USERID, userData.getUserId())
                    .addContentItems(branchUniversalObjects)
                    .addCustomDataProperty(LinkerConstants.KEY_NEW_BUYER, String.valueOf(branchIOPayment.isNewBuyer()))
                    .addCustomDataProperty(LinkerConstants.KEY_MONTHLY_NEW_BUYER, String.valueOf(branchIOPayment.isMonthlyNewBuyer()))
                    .logEvent(context);

            if (branchIOPayment.isNewBuyer()) {
                sendMarketPlaceFirstTxnEvent(context, branchIOPayment, userData.getUserId(), revenuePrice, shippingPrice);
            }
            new BranchHelperValidation().validatePurchaseEvent(branchIOPayment,revenuePrice,shippingPrice);
        } catch (Exception ex) {
            ex.printStackTrace();
            new BranchHelperValidation().exceptionToSendEvent("" + ex.getMessage(), BRANCH_STANDARD_EVENT.PURCHASE.getName());
        }
    }

    public static void sendLogoutEvent() {
        if (Branch.getInstance() != null) {
            Branch.getInstance().logout();
        }
    }

    public static void sendLoginEvent(Context context, UserData userData) {
        new BranchEvent(LinkerConstants.EVENT_LOGIN_LABLE)
                .addCustomDataProperty(LinkerConstants.EMAIL_LABLE, userData.getEmail())
                .addCustomDataProperty(LinkerConstants.USER_ID, userData.getUserId())
                .addCustomDataProperty(LinkerConstants.PHONE_LABLE, LinkerUtils.normalizePhoneNumber(userData.getPhoneNumber()))
                .logEvent(context);

    }

    public static void sendRegisterEvent(UserData userData, Context context) {
        new BranchEvent(BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION)
                .addCustomDataProperty(LinkerConstants.EMAIL_LABLE, userData.getEmail())
                .addCustomDataProperty(LinkerConstants.USER_ID, userData.getUserId())
                .addCustomDataProperty(LinkerConstants.PHONE_LABLE, LinkerUtils.normalizePhoneNumber(userData.getPhoneNumber()))
                .logEvent(context);

    }

    public static void sendItemViewEvent(Context context, LinkerData linkerData){
        new BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
                .addCustomDataProperty(LinkerConstants.PRODUCT_ID, linkerData.getId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice())
                .addCustomDataProperty(LinkerConstants.CATEGORY_LEVEL_1, linkerData.getCatLvl1())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.DESCRIPTION, linkerData.getDescription())
                .addCustomDataProperty(LinkerConstants.SHOP_ID, linkerData.getShopId())
                .addCustomDataProperty(LinkerConstants.CURRENCY, linkerData.getCurrency())
                .logEvent(context);
    }

    public static void sendAddToCartEvent(Context context, LinkerData linkerData){
        new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART)
                .addCustomDataProperty(LinkerConstants.PRODUCT_ID, linkerData.getId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice())
                .addCustomDataProperty(LinkerConstants.CATEGORY_LEVEL_1, linkerData.getCatLvl1())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.QTY, linkerData.getQuantity())
                .addCustomDataProperty(LinkerConstants.CURRENCY, linkerData.getCurrency())
                .logEvent(context);
        new BranchHelperValidation().validateCartQuantity( linkerData.getQuantity());
    }

    public static void sendAddToWishListEvent(Context context, LinkerData linkerData){
        new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_WISHLIST)
                .addCustomDataProperty(LinkerConstants.PRODUCT_ID, linkerData.getId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice())
                .addCustomDataProperty(LinkerConstants.CATEGORY_LEVEL_1, linkerData.getCatLvl1())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.CURRENCY, linkerData.getCurrency())
                .logEvent(context);
    }

    public static void sendFlightPurchaseEvent(Context context, LinkerData linkerData){
        new BranchEvent(LinkerConstants.EVENT_FLIGHT_PURCHASE)
                .addCustomDataProperty(LinkerConstants.PRODUCT_CATEGORY, linkerData.getProductCategory())
                .addCustomDataProperty(LinkerConstants.PRODUCT_NAME, linkerData.getProductName())
                .addCustomDataProperty(LinkerConstants.JOURNEY_ID, linkerData.getJourneyId())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.INVOICE_ID, linkerData.getInvoiceId())
                .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, linkerData.getPaymentId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice())
                .logEvent(context);
    }
    private static void sendMarketPlaceFirstTxnEvent(Context context, PaymentData branchIOPayment,  String userId, double revenuePrice,double shippingPrice){
        new BranchEvent(LinkerConstants.EVENT_MARKETPLACE_FIRST_TXN)
                .setTransactionID(branchIOPayment.getOrderId())
                .setCurrency(CurrencyType.IDR)
                .setShipping(shippingPrice)
                .setRevenue(revenuePrice)
                .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, branchIOPayment.getPaymentId())
                .addCustomDataProperty(LinkerConstants.KEY_PRODUCTTYPE, branchIOPayment.getProductType())
                .addCustomDataProperty(LinkerConstants.KEY_USERID, userId)
                .addCustomDataProperty(LinkerConstants.KEY_NEW_BUYER, String.valueOf(branchIOPayment.isNewBuyer()))
                .addCustomDataProperty(LinkerConstants.KEY_MONTHLY_NEW_BUYER, String.valueOf(branchIOPayment.isMonthlyNewBuyer()))
                .logEvent(context);
    }

}
