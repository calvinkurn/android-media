package com.tokopedia.linker;

import android.content.Context;
import android.util.Log;

import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.PaymentData;
import com.tokopedia.linker.model.UserData;

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
                                        .setPrice(LinkerUtils.convertToDouble(product.get(LinkerConstants.PRICE_IDR_TO_DOUBLE)), CurrencyType.IDR)
                                        .setProductName(product.get(LinkerConstants.NAME))
                                        .setQuantity(LinkerUtils.convertStringToDouble(product.get(LinkerConstants.QTY)))
                                        .setSku(product.get(LinkerConstants.ID))
                                        .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                                        .addCustomMetadata(LinkerConstants.ProductCategory, String.valueOf(product.get(LinkerConstants.CATEGORY))));

                branchUniversalObjects.add(buo);
            }

            double revenuePrice;
            double shippingPrice;
            if (LinkerConstants.PRODUCTTYPE_MARKETPLACE.equalsIgnoreCase(branchIOPayment.getProductType())) {
                revenuePrice = Double.parseDouble(branchIOPayment.getItemPrice());
                shippingPrice = Double.parseDouble(branchIOPayment.getShipping());
            } else {
                revenuePrice = LinkerUtils.convertToDouble(branchIOPayment.getRevenue());
                shippingPrice = LinkerUtils.convertToDouble(branchIOPayment.getShipping());
            }

            new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                    .setTransactionID(branchIOPayment.getOrderId())
                    .setCurrency(CurrencyType.IDR)
                    .setShipping(shippingPrice)
                    .setRevenue(revenuePrice)
                    .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, branchIOPayment.getPaymentId())
                    .addCustomDataProperty(LinkerConstants.KEY_PRODUCTTYPE, branchIOPayment.getProductType())
                    .addCustomDataProperty(LinkerConstants.KEY_USERID, userData.getUserId())
                    .addContentItems(branchUniversalObjects)
                    .logEvent(context);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("payment data", "product type=" + branchIOPayment.getProductType() + " , revenuePrice= " + branchIOPayment.getRevenue()+ " , shippingPrice= " + branchIOPayment.getShipping()
                + " , PAYMENT_KEY= " + branchIOPayment.getPaymentId() + " , TransactionID - orderid= " + branchIOPayment.getOrderId() + " , USERID_KEY= " + userData.getUserId());
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

}
