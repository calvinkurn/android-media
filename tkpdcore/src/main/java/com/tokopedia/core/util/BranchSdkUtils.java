package com.tokopedia.core.util;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.app.MainApplication;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.design.utils.CurrencyFormatHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchContentSchema;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.LinkProperties;

/**
 * Created by ashwanityagi on 04/10/17.
 */

public class BranchSdkUtils {
    private static final String BRANCH_ANDROID_DEEPLINK_PATH_KEY = "$android_deeplink_path";
    private static final String BRANCH_IOS_DEEPLINK_PATH_KEY = "$ios_deeplink_path";
    private static final String BRANCH_DESKTOP_URL_KEY = "$desktop_url";
    private static final String CAMPAIGN_NAME = "Android App";
    private static final String PAYMENT_KEY = "paymentID";
    private static final String PRODUCTTYPE_KEY = "productType";
    private static final String USERID_KEY = "userId";
    public static final String PRODUCTTYPE_DIGITAL = "digital";
    public static final String PRODUCTTYPE_MARKETPLACE = "marketplace";
    private static final String BRANCH_PROMOCODE_KEY = "branch_promo";
    public static String REFERRAL_ADVOCATE_PROMO_CODE = "";

    private static BranchUniversalObject createBranchUniversalObject(ShareData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(data.getType())
                .setTitle(data.getName())
                .setContentDescription(data.getDescription())
                .setContentImageUrl(data.getImgUri())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
        return branchUniversalObject;
    }

    public static void generateBranchLink(final ShareData data, final Activity activity, final GenerateShareContents ShareContentsCreateListener) {

        if (isBranchUrlActivated(activity, data.getType()) && !ShareData.RIDE_TYPE.equalsIgnoreCase(data.getType())) {
            if (ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
                ShareContentsCreateListener.onCreateShareContents(data.getTextContentForBranch(""), "", "");

            }else{
                BranchUniversalObject branchUniversalObject = createBranchUniversalObject(data);
                LinkProperties linkProperties = createLinkProperties(data, data.getSource(), activity);
                branchUniversalObject.generateShortUrl(activity, linkProperties, new Branch.BranchLinkCreateListener() {
                    @Override
                    public void onLinkCreate(String url, BranchError error) {
                        if (error == null) {
                            ShareContentsCreateListener.onCreateShareContents(data.getTextContentForBranch(url), url, url);
                        } else {
                            ShareContentsCreateListener.onCreateShareContents(data.getTextContent(activity), data.renderShareUri(), url);
                        }
                    }
                });
            }

        } else {
            ShareContentsCreateListener.onCreateShareContents(data.getTextContent(activity), data.renderShareUri(), data.renderShareUri());

        }
    }

    private static LinkProperties createLinkProperties(ShareData data, String channel, Activity activity) {
        LinkProperties linkProperties = new LinkProperties();
        String deeplinkPath;
        String desktopUrl = null;
        if (ShareData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.PRODUCT_INFO, data.getId());
        } else if (isAppShowReferralButtonActivated(activity) && ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.REFERRAL_WELCOME, data.getId());
            deeplinkPath = deeplinkPath.replaceFirst("\\{.*?\\} ?", SessionHandler.getLoginName(activity) == null ? "" : SessionHandler.getLoginName(activity));
        } else if (ShareData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.SHOP, data.getId());//"shop/" + data.getId();
        } else if (ShareData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else if (ShareData.CATALOG_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_CATALOG, data.getId());
        } else {
            deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        }

        if (desktopUrl == null) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri());

        }

        linkProperties.setCampaign(CAMPAIGN_NAME);
        linkProperties.setChannel(channel);
        linkProperties.setFeature(data.getType());
        linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, data.renderBranchShareUri(deeplinkPath));
        return linkProperties;
    }

    private static boolean isBranchUrlActivated(Activity activity, String type) {
        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(type) || ShareData.REFERRAL_TYPE.equalsIgnoreCase(type) ) {
            return true;
        } else {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
            return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_ACTIVATE_BRANCH_LINKS, true);
        }
    }

    private static String getApplinkPath(String url, String id) {
        if (url.contains(Constants.Schemes.APPLINKS + "://")) {
            url = url.replace(Constants.Schemes.APPLINKS + "://", "");
            url = url.replaceFirst("\\{.*?\\} ?", id == null ? "" : id);
        } else if (url.contains(TkpdBaseURL.WEB_DOMAIN)) {
            url = url.replace(TkpdBaseURL.WEB_DOMAIN, "");
        } else if (url.contains(TkpdBaseURL.MOBILE_DOMAIN)) {
            url = url.replace(TkpdBaseURL.MOBILE_DOMAIN, "");
        }
        return url;
    }

    public static void sendCommerceEvent(Purchase purchase, String productType) {
        try {
            if ( purchase != null && purchase.getListProduct() != null) {
                List<BranchUniversalObject> branchUniversalObjects =new ArrayList<>();
                SessionHandler sessionHandler =new SessionHandler(MainApplication.getAppContext());

                for (Object objProduct : purchase.getListProduct()) {
                    Map<String, Object> product = (Map<String, Object>) objProduct;
                    BranchUniversalObject buo = new BranchUniversalObject()

                            .setTitle(String.valueOf(product.get(Product.KEY_NAME)))
                            .setContentMetadata(
                                    new ContentMetadata()
                                            .setPrice(convertIDRtoDouble(String.valueOf(product.get(Product.KEY_PRICE))), CurrencyType.IDR)
                                            .setProductBrand(String.valueOf(product.get(Product.KEY_BRAND)))
                                            .setProductName(String.valueOf(product.get(Product.KEY_NAME)))
                                            .setProductVariant(String.valueOf(product.get(Product.KEY_VARIANT)))
                                            .setQuantity(convertStringToDouble(String.valueOf(product.get(Product.KEY_QTY))))
                                            .setSku(String.valueOf(product.get(Product.KEY_ID)))
                                            .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT));
                    branchUniversalObjects.add(buo);
                }
                new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                        .setTransactionID(String.valueOf(purchase.getTransactionID()))
                        .setCurrency(CurrencyType.IDR)
                        .setShipping(convertIDRtoDouble(String.valueOf(purchase.getShipping())))
                        .setRevenue(convertIDRtoDouble(String.valueOf(purchase.getRevenue())))
                        .addCustomDataProperty(PAYMENT_KEY, purchase.getPaymentId())
                        .addCustomDataProperty(PRODUCTTYPE_KEY, productType)
                        .addCustomDataProperty(USERID_KEY, sessionHandler.getLoginID())
                        .addContentItems(branchUniversalObjects)
                        .logEvent(MainApplication.getAppContext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Set userId to Branch.io sdk, userId, 127 chars or less
    public static void sendIdentityEvent(String userId) {
        if (Branch.getInstance() != null) {
            Branch.getInstance().setIdentity(userId);
        }
    }

    public static void sendLogoutEvent() {
        if (Branch.getInstance() != null) {
            Branch.getInstance().logout();
        }
    }

    public static void sendLoginEvent(Context context) {

        SessionHandler sessionHandler =new SessionHandler(context);
        new BranchEvent(AppEventTracking.EventBranch.EVENT_LOGIN)
                .addCustomDataProperty(AppEventTracking.Branch.EMAIL, sessionHandler.getEmail())
                .addCustomDataProperty(AppEventTracking.Branch.PHONE, normalizePhoneNumber(sessionHandler.getPhoneNumber()))
                .logEvent(MainApplication.getAppContext());

    }

    public static void sendRegisterEvent(String email, String phone) {

        new BranchEvent(AppEventTracking.EventBranch.EVENT_REGISTER)
                .addCustomDataProperty(AppEventTracking.Branch.EMAIL, email)
                .addCustomDataProperty(AppEventTracking.Branch.PHONE, normalizePhoneNumber(phone))
                .logEvent(MainApplication.getAppContext());

    }

    private static double convertIDRtoDouble(String value) {
        double result = 0;
        try {
            result =  CurrencyFormatHelper.convertRupiahToInt(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private static double convertStringToDouble(String value) {
        double result = 0;
        try {
            result = Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static Boolean isAppShowReferralButtonActivated(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON);
    }

    private static String normalizePhoneNumber(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum))
            return phoneNum.replaceFirst("^0(?!$)", "62");
        else
            return "";
    }

    public static String getAutoApplyCouponIfAvailable(Context context) {
        if (TextUtils.isEmpty(REFERRAL_ADVOCATE_PROMO_CODE)) {
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_PROMO_CODE);
            return localCacheHandler.getString(TkpdCache.Key.KEY_CACHE_PROMO_CODE);
        } else {
            return BranchSdkUtils.REFERRAL_ADVOCATE_PROMO_CODE;
        }
    }

    public static void removeCouponCode(Context context) {
        REFERRAL_ADVOCATE_PROMO_CODE = "";
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_PROMO_CODE);
        localCacheHandler.clearCache(TkpdCache.Key.KEY_CACHE_PROMO_CODE);
    }


    public static void storeWebToAppPromoCodeIfExist(JSONObject referringParams, Context context) {
        try {
            String branch_promo = referringParams.optString(BRANCH_PROMOCODE_KEY);
            if (!TextUtils.isEmpty(branch_promo)) {
                LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_PROMO_CODE);
                localCacheHandler.putString(TkpdCache.Key.KEY_CACHE_PROMO_CODE, branch_promo);
                localCacheHandler.applyEditor();
            }

        } catch (Exception e) {

        }
    }

    public interface GenerateShareContents {
        void onCreateShareContents(String shareContents, String shareUri, String branchUrl);
    }
}