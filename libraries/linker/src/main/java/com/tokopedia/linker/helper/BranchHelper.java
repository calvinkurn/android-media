package com.tokopedia.linker.helper;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.analyticsdebugger.AnalyticsSource;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.LinkerData;
import com.tokopedia.linker.model.PaymentData;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.linker.validation.BranchHelperValidation;

import java.lang.reflect.Type;
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
import timber.log.Timber;

public class BranchHelper {
    static Gson gson = new Gson();
    static Type type = new TypeToken<HashMap<String, Object>>() {
    }.getType();

    //Set userId to Branch.io sdk, userId, 127 chars or less
    public static void sendIdentityEvent(String userId) {
        if (Branch.getInstance() != null) {
            Branch.getInstance().setIdentity(userId);
        }
    }

    public static void setCommonLinkProperties(LinkProperties linkProperties, LinkerData data) {
        linkProperties.setCampaign(data.getCampaignName());
        linkProperties.setChannel(LinkerData.ARG_UTM_SOURCE);
        linkProperties.setFeature(LinkerData.ARG_UTM_MEDIUM);
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_URL, data.getOgUrl());
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_TITLE, LinkerUtils.getOgTitle(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_IMAGE_URL, LinkerUtils.getOgImage(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_OG_DESC, LinkerUtils.getOgDesc(data));
        linkProperties.addControlParameter(LinkerConstants.KEY_DESKTOP_URL, data.renderShareUri());
    }

    public static void sendCommerceEvent(PaymentData branchIOPayment, Context context, UserData userData) {
        try {
            List<BranchUniversalObject> branchUniversalObjects = new ArrayList<>();

            for (HashMap<String, String> product : branchIOPayment.getProducts()) {

                BranchUniversalObject buo = new BranchUniversalObject()
                        .setTitle(product.get(LinkerConstants.NAME))
                        .setContentMetadata(
                                new ContentMetadata()
                                        .setPrice(LinkerUtils.convertToDouble(product.get(LinkerConstants.PRICE_IDR_TO_DOUBLE), "Product price-PURCHASE"), CurrencyType.IDR)
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
                revenuePrice = LinkerUtils.convertToDouble(branchIOPayment.getRevenue(), "Revenue-PURCHASE");
            }
            double shippingPrice = LinkerUtils.convertToDouble(branchIOPayment.getShipping(), "Shipping-PURCHASE");

            BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                    .setTransactionID(branchIOPayment.getOrderId())
                    .setCurrency(CurrencyType.IDR)
                    .setShipping(shippingPrice)
                    .setRevenue(revenuePrice)
                    .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, branchIOPayment.getPaymentId())
                    .addCustomDataProperty(LinkerConstants.KEY_PRODUCTTYPE, branchIOPayment.getProductType())
                    .addCustomDataProperty(LinkerConstants.KEY_USERID, userData.getUserId())
                    .addContentItems(branchUniversalObjects)
                    .addCustomDataProperty(LinkerConstants.KEY_NEW_BUYER, String.valueOf(branchIOPayment.isNewBuyer()))
                    .addCustomDataProperty(LinkerConstants.KEY_MONTHLY_NEW_BUYER, String.valueOf(branchIOPayment.isMonthlyNewBuyer()));
            branchEvent.logEvent(context);
            saveBranchEvent(branchEvent);

            if (branchIOPayment.isNewBuyer()) {
                sendMarketPlaceFirstTxnEvent(context, branchIOPayment, userData.getUserId(), revenuePrice, shippingPrice);
            }
            new BranchHelperValidation().validatePurchaseEvent(branchIOPayment, revenuePrice, shippingPrice);
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
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.LOGIN)
                .addCustomDataProperty(LinkerConstants.USER_ID, userData.getUserId())
                .addCustomDataProperty(LinkerConstants.MEDIUM, userData.getMedium());
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);
    }

    public static void sendRegisterEvent(UserData userData, Context context) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.COMPLETE_REGISTRATION)
                .addCustomDataProperty(LinkerConstants.USER_ID, userData.getUserId())
                .addCustomDataProperty(LinkerConstants.MEDIUM, userData.getMedium());
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);
    }

    public static void sendItemViewEvent(Context context, LinkerData linkerData) {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(linkerData.getProductName())
                .setContentMetadata(
                        new ContentMetadata()
                                .setPrice(LinkerUtils.convertToDouble(linkerData.getPrice(), "Product price-ITEM_VIEW"), CurrencyType.IDR)
                                .setProductName(linkerData.getProductName())
                                .setQuantity(LinkerUtils.convertToDouble(linkerData.getQuantity(), "Product quantity-ITEM_VIEW"))
                                .setSku(linkerData.getSku())
                                .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                                .addCustomMetadata(LinkerConstants.ProductCategory, String.valueOf(linkerData.getLevel3Name())));
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.SHOP_ID, linkerData.getShopId())
                .addCustomDataProperty(LinkerConstants.CURRENCY, linkerData.getCurrency())
                .addCustomDataProperty(LinkerConstants.CONTENT, linkerData.getContent())
                .addCustomDataProperty(LinkerConstants.CONTENT_TYPE, linkerData.getContentType())
                .addCustomDataProperty(LinkerConstants.LEVEL1_ID, linkerData.getLevel1Id())
                .addCustomDataProperty(LinkerConstants.LEVEL2_NAME, linkerData.getLevel2Name())
                .addCustomDataProperty(LinkerConstants.LEVEL2_ID, linkerData.getLevel2Id())
                .addCustomDataProperty(LinkerConstants.LEVEL3_NAME, linkerData.getLevel3Name())
                .addCustomDataProperty(LinkerConstants.LEVEL3_ID, linkerData.getLevel3Id())
                .addCustomDataProperty(LinkerConstants.CONTENT_ID, linkerData.getContentId())
                .setRevenue(0)
                .setCurrency(CurrencyType.IDR)
                .addContentItems(buo);
        branchEvent.logEvent(context);
        new BranchHelperValidation().validateItemViewEvent(linkerData);
        saveBranchEvent(branchEvent);

    }

    public static void sendAddToCartEvent(Context context, LinkerData linkerData) {
        BranchUniversalObject buo = new BranchUniversalObject()
                .setTitle(linkerData.getProductName())
                .setContentMetadata(
                        new ContentMetadata()
                                .setPrice(LinkerUtils.convertToDouble(linkerData.getPrice(), "Product price-ADD_TO_CART"), CurrencyType.IDR)
                                .setProductName(linkerData.getProductName())
                                .setQuantity(LinkerUtils.convertToDouble(linkerData.getQuantity(), "Product quantity-ADD_TO_CART"))
                                .setSku(linkerData.getId())
                                .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                                .addCustomMetadata(LinkerConstants.ProductCategory, String.valueOf(linkerData.getCatLvl1())));
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART)
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.PRODUCT_ID, linkerData.getSku())
                .addCustomDataProperty(LinkerConstants.CONTENT_TYPE, linkerData.getContentType())
                .addCustomDataProperty(LinkerConstants.CONTENT, linkerData.getContent())
                .addCustomDataProperty(LinkerConstants.LEVEL1_NAME, linkerData.getLevel1Name())
                .addCustomDataProperty(LinkerConstants.LEVEL1_ID, linkerData.getLevel1Id())
                .addCustomDataProperty(LinkerConstants.LEVEL2_NAME, linkerData.getLevel2Name())
                .addCustomDataProperty(LinkerConstants.LEVEL2_ID, linkerData.getLevel2Id())
                .addCustomDataProperty(LinkerConstants.LEVEL3_NAME, linkerData.getLevel3Name())
                .addCustomDataProperty(LinkerConstants.LEVEL3_ID, linkerData.getLevel3Id())
                .addCustomDataProperty(LinkerConstants.SKU, linkerData.getId())
                .addCustomDataProperty(LinkerConstants.CONTENT_ID, linkerData.getContentId())
                .setRevenue(Double.parseDouble(linkerData.getQuantity()) * Double.parseDouble(linkerData.getPrice()))
                .setCurrency(CurrencyType.IDR)
                .addContentItems(buo);
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);

        new BranchHelperValidation().validateATCEvent(linkerData);
    }

    public static void sendAddToWishListEvent(Context context, LinkerData linkerData) {
        BranchEvent branchEvent = new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_WISHLIST)
                .addCustomDataProperty(LinkerConstants.PRODUCT_ID, linkerData.getId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice())
                .addCustomDataProperty(LinkerConstants.CATEGORY_LEVEL_1, linkerData.getCatLvl1())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.CURRENCY, linkerData.getCurrency());
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);
    }

    public static void sendFlightPurchaseEvent(Context context, LinkerData linkerData) {
        BranchEvent branchEvent = new BranchEvent(LinkerConstants.EVENT_FLIGHT_PURCHASE)
                .addCustomDataProperty(LinkerConstants.PRODUCT_CATEGORY, linkerData.getProductCategory())
                .addCustomDataProperty(LinkerConstants.PRODUCT_NAME, linkerData.getProductName())
                .addCustomDataProperty(LinkerConstants.JOURNEY_ID, linkerData.getJourneyId())
                .addCustomDataProperty(LinkerConstants.USER_ID, linkerData.getUserId())
                .addCustomDataProperty(LinkerConstants.INVOICE_ID, linkerData.getInvoiceId())
                .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, linkerData.getPaymentId())
                .addCustomDataProperty(LinkerConstants.PRICE, linkerData.getPrice());
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);
    }

    private static void sendMarketPlaceFirstTxnEvent(Context context, PaymentData branchIOPayment, String userId, double revenuePrice, double shippingPrice) {
        BranchEvent branchEvent = new BranchEvent(LinkerConstants.EVENT_MARKETPLACE_FIRST_TXN)
                .setTransactionID(branchIOPayment.getOrderId())
                .setCurrency(CurrencyType.IDR)
                .setShipping(shippingPrice)
                .setRevenue(revenuePrice)
                .addCustomDataProperty(LinkerConstants.KEY_PAYMENT, branchIOPayment.getPaymentId())
                .addCustomDataProperty(LinkerConstants.KEY_PRODUCTTYPE, branchIOPayment.getProductType())
                .addCustomDataProperty(LinkerConstants.KEY_USERID, userId)
                .addCustomDataProperty(LinkerConstants.KEY_NEW_BUYER, String.valueOf(branchIOPayment.isNewBuyer()))
                .addCustomDataProperty(LinkerConstants.KEY_MONTHLY_NEW_BUYER, String.valueOf(branchIOPayment.isMonthlyNewBuyer()));
        branchEvent.logEvent(context);
        saveBranchEvent(branchEvent);
    }

    private static void saveBranchEvent(BranchEvent branchEvent) {
        if (!GlobalConfig.isAllowDebuggingTools()) {
            return;
        }
        try {
            HashMap<String, Object> map = gson.fromJson(gson.toJson(branchEvent), type);
            String eventName = (String) map.get("eventName");
            GtmLogger.getInstance(LinkerManager.getInstance().getContext()).save(eventName, map, AnalyticsSource.BRANCH_IO);
        } catch (Throwable throwable) {
            Timber.d(throwable);
        }
    }

}
