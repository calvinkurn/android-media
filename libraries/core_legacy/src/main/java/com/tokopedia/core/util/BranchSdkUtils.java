package com.tokopedia.core.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.BaseAbstractionRouter;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.model.BranchIOPayment;
import com.tokopedia.core.analytics.nishikino.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Purchase;
import com.tokopedia.core.deprecated.Constants;
import com.tokopedia.core.deprecated.LocalCacheHandler;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.model.share.ShareData;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
    private static final String PAYMENT_KEY = "paymentID";
    private static final String PRODUCTTYPE_KEY = "productType";
    private static final String USERID_KEY = "userId";
    public static final String PRODUCTTYPE_DIGITAL = "digital";
    public static final String PRODUCTTYPE_MARKETPLACE = "marketplace";
    private static final String BRANCH_PROMOCODE_KEY = "branch_promo";
    public static String REFERRAL_ADVOCATE_PROMO_CODE = "";
    private static final String BRANCH_ANDROID_DESKTOP_URL_KEY = "$android_url";
    private static final String BRANCH_IOS_DESKTOP_URL_KEY = "$ios_url";
    private static final String ProductCategory = "ProductCategory";
    private static final String FIREBASE_KEY_INCLUDEMOBILEWEB = "app_branch_include_mobileweb";
    private static final String CHALLENGES_DESKTOP_URL = "https://m.tokopedia.com/kontes";
    private static final String REFERRAL_DESKTOP_URL = "https://www.tokopedia.com/referral";


    private static BranchUniversalObject createBranchUniversalObject(ShareData data) {
        BranchUniversalObject branchUniversalObject = new BranchUniversalObject()
                .setCanonicalIdentifier(data.getId() == null ? data.getType() : data.getId())
                .setTitle(data.getName())
                .setContentDescription(data.getDescription())
                .setContentImageUrl(data.getImgUri())
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC);
        return branchUniversalObject;
    }

    public static void generateBranchLink(final ShareData data, final Activity activity, final GenerateShareContents ShareContentsCreateListener) {

        if (isBranchUrlActivated(activity, data.getType()) && !ShareData.RIDE_TYPE.equalsIgnoreCase(data.getType())) {
            if (ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType()) && !TextUtils.isEmpty(data.getshareUrl())) {
                ShareContentsCreateListener.onCreateShareContents(data.getTextContentForBranch(""), data.getTextContentForBranch(""), data.getshareUrl());
            } else {
                BranchUniversalObject branchUniversalObject = createBranchUniversalObject(data);
                LinkProperties linkProperties = createLinkProperties(activity, RouterUtils.getRouterFromContext(activity).legacySessionHandler().getLoginName(), data, data.getSource(), activity);
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

    private static LinkProperties createLinkProperties(Context context, String loginName, ShareData data, String channel, Activity activity) {
        LinkProperties linkProperties = new LinkProperties();


        linkProperties.setCampaign(getCampaignName(data.getType()));
        linkProperties.setChannel(ShareData.ARG_UTM_SOURCE);
        linkProperties.setFeature(ShareData.ARG_UTM_MEDIUM);
        linkProperties.addControlParameter("$og_url", data.getOgUrl());
        linkProperties.addControlParameter("$og_title", getOgTitle(data));
        linkProperties.addControlParameter("$og_image_url", getOgImage(data));
        linkProperties.addControlParameter("$og_description", getOgDesc(data));

        String deeplinkPath;
        String desktopUrl = null;

        linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri());

        if (ShareData.PRODUCT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.PRODUCT_INFO, data.getId());
        } else if (isAppShowReferralButtonActivated(activity) && ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.REFERRAL_WELCOME, data.getId());
            deeplinkPath = deeplinkPath.replaceFirst("\\{.*?\\} ?", loginName == null ? "" : loginName);
        } else if (ShareData.SHOP_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.SHOP, data.getId());//"shop/" + data.getId();
        } else if (ShareData.HOTLIST_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_HOTLIST_DETAIL, data.getId());//"hot/" + data.getId();
        } else if (ShareData.CATALOG_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.DISCOVERY_CATALOG, data.getId());
        } else if (ShareData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.GROUPCHAT, data.getId());
            if (activity.getApplication() instanceof TkpdCoreRouter) {
                desktopUrl = ((TkpdCoreRouter) activity.getApplication())
                        .getDesktopLinkGroupChat();
                linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addControlParameter(BRANCH_ANDROID_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addControlParameter(BRANCH_IOS_DESKTOP_URL_KEY, desktopUrl);
                linkProperties.addTag(String.format("%s - %s", data.getId(), data.getSource()));
                linkProperties.setFeature(data.getPrice());
                linkProperties.setCampaign(String.format("%s - %s", data.getType(), data.getId()));
                linkProperties.setChannel(String.format("%s - Android", data.getType()));
                linkProperties.addControlParameter("$uri_redirect_mode", "2");
            }
        } else if (ShareData.PROMO_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = getApplinkPath(Constants.Applinks.PROMO_DETAIL, data.getId());
        } else if (ShareData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType())) {
            deeplinkPath = data.getDeepLink();
        } else {
            deeplinkPath = getApplinkPath(data.renderShareUri(), "");
        }

        if (ShareData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, CHALLENGES_DESKTOP_URL);
        } else if (ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType())) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, REFERRAL_DESKTOP_URL);
        } else if (desktopUrl == null) {
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, data.renderShareUri());
        }
        if (isAndroidIosUrlActivated(context) && !(ShareData.REFERRAL_TYPE.equalsIgnoreCase(data.getType()) ||
                ShareData.INDI_CHALLENGE_TYPE.equalsIgnoreCase(data.getType()) ||
                ShareData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType()))) {
            linkProperties.addControlParameter(BRANCH_ANDROID_DESKTOP_URL_KEY, data.renderShareUri());
            linkProperties.addControlParameter(BRANCH_IOS_DESKTOP_URL_KEY, data.renderShareUri());
        }

        linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, deeplinkPath == null ? "" : deeplinkPath);
        linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, deeplinkPath == null ? "" : deeplinkPath);

        if (ShareData.GROUPCHAT_TYPE.equalsIgnoreCase(data.getType())) {
            String connector = "";
            String renderedUrl = "";
            String tempUri = data.getUri();

            if (tempUri.contains("?")) {
                connector = "&";
            } else {
                connector = "?";
            }
            String tags = "";
            if (linkProperties.getTags().size() > 0) {
                tags = linkProperties.getTags().get(0);
            }
            Uri uri = Uri.parse(String.format("groupchat/%s%sutm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s",
                    tempUri, connector, linkProperties.getChannel(), linkProperties.getFeature(), linkProperties.getCampaign(), tags));
            
            String temp = String.format("utm_source=%s&utm_medium=%s&utm_campaign=%s&utm_content=%s",
                    linkProperties.getChannel(), linkProperties.getFeature(), linkProperties.getCampaign(), tags);

            try {
                renderedUrl = uri.toString().replace(" ", "%20");
                temp = temp.replace(" ", "%20");
            } catch (Exception e) {
                e.printStackTrace();
                renderedUrl = uri.toString();
            }
            
            linkProperties.addControlParameter(BRANCH_DESKTOP_URL_KEY, String.format("%s?%s", desktopUrl, temp));
            linkProperties.addControlParameter(BRANCH_ANDROID_DEEPLINK_PATH_KEY, renderedUrl);
            linkProperties.addControlParameter(BRANCH_IOS_DEEPLINK_PATH_KEY, renderedUrl);
        }

        return linkProperties;
    }

    public static final String MAINAPP_ACTIVATE_BRANCH_LINKS = "mainapp_activate_branch_links";

    private static boolean isBranchUrlActivated(Activity activity, String type) {
        if (ShareData.APP_SHARE_TYPE.equalsIgnoreCase(type)
                || ShareData.REFERRAL_TYPE.equalsIgnoreCase(type)
                || ShareData.GROUPCHAT_TYPE.equalsIgnoreCase(type)) {
            return true;
        } else {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(activity);
            return remoteConfig.getBoolean(MAINAPP_ACTIVATE_BRANCH_LINKS, true);
        }
    }

    public static String WEB_DOMAIN = "https://www.tokopedia.com/";
    public static String MOBILE_DOMAIN = "https://m.tokopedia.com/";

    private static String getApplinkPath(String url, String id) {
        if (url.contains(Constants.Schemes.APPLINKS + "://")) {
            url = url.replace(Constants.Schemes.APPLINKS + "://", "");
            url = url.replaceFirst("\\{.*?\\} ?", id == null ? "" : id);
        } else if (url.contains(WEB_DOMAIN)) {
            url = url.replace(WEB_DOMAIN, "");
        } else if (url.contains(MOBILE_DOMAIN)) {
            url = url.replace(MOBILE_DOMAIN, "");
        }
        return url;
    }

    public static void sendCommerceEvent(Context context, Purchase purchase, String productType) {
        if(context instanceof Application && context instanceof BaseAbstractionRouter) {
            AbstractionRouter router = (AbstractionRouter) context;
            UserSession session = router.getSession();

            try {
                if (purchase != null && purchase.getListProduct() != null) {
                    List<BranchUniversalObject> branchUniversalObjects = new ArrayList<>();
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
                                                .addCustomMetadata(ProductCategory, String.valueOf(product.get(Product.KEY_CAT)))
                                                .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT));
                        branchUniversalObjects.add(buo);
                    }

                    double revenuePrice;
                    double shippingPrice;
                    if (PRODUCTTYPE_MARKETPLACE.equalsIgnoreCase(productType)) {
                        revenuePrice = Double.parseDouble(String.valueOf(purchase.getRevenue()));
                        shippingPrice = Double.parseDouble(String.valueOf(purchase.getShipping()));
                    } else {
                        revenuePrice = convertIDRtoDouble(String.valueOf(purchase.getRevenue()));
                        shippingPrice = convertIDRtoDouble(String.valueOf(purchase.getShipping()));
                    }

                    new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                            .setTransactionID(String.valueOf(purchase.getTransactionID()))
                            .setCurrency(CurrencyType.IDR)
                            .setShipping(shippingPrice)
                            .setRevenue(revenuePrice)
                            .addCustomDataProperty(PAYMENT_KEY, purchase.getPaymentId())
                            .addCustomDataProperty(PRODUCTTYPE_KEY, productType)
                            .addCustomDataProperty(USERID_KEY, session.getUserId())
                            .addContentItems(branchUniversalObjects)
                            .logEvent(context);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    public static void sendCommerceEvent(Context context, BranchIOPayment branchIOPayment) {
        try {
            List<BranchUniversalObject> branchUniversalObjects = new ArrayList<>();
            SessionHandler sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();

            for (HashMap<String, String> product : branchIOPayment.getProducts()) {
                BranchUniversalObject buo = new BranchUniversalObject()
                        .setTitle(product.get(BranchIOPayment.KEY_NAME))
                        .setContentMetadata(
                                new ContentMetadata()
                                        .setPrice(convertIDRtoDouble(product.get(BranchIOPayment.KEY_PRICE)), CurrencyType.IDR)
                                        .setProductName(product.get(BranchIOPayment.KEY_NAME))
                                        .setQuantity(convertStringToDouble(product.get(BranchIOPayment.KEY_QTY)))
                                        .setSku(product.get(BranchIOPayment.KEY_ID))
                                        .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT)
                                        .addCustomMetadata(ProductCategory, String.valueOf(product.get(BranchIOPayment.KEY_CATEGORY))));

                branchUniversalObjects.add(buo);
            }

            double revenuePrice;
            double shippingPrice;
            if (PRODUCTTYPE_MARKETPLACE.equalsIgnoreCase(branchIOPayment.getProductType())) {
                revenuePrice = Double.parseDouble(branchIOPayment.getItemPrice());
                shippingPrice = Double.parseDouble(branchIOPayment.getShipping());
            } else {
                revenuePrice = convertIDRtoDouble(branchIOPayment.getRevenue());
                shippingPrice = convertIDRtoDouble(branchIOPayment.getShipping());
            }

            new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                    .setTransactionID(branchIOPayment.getOrderId())
                    .setCurrency(CurrencyType.IDR)
                    .setShipping(shippingPrice)
                    .setRevenue(revenuePrice)
                    .addCustomDataProperty(PAYMENT_KEY, branchIOPayment.getPaymentId())
                    .addCustomDataProperty(PRODUCTTYPE_KEY, branchIOPayment.getProductType())
                    .addCustomDataProperty(USERID_KEY, sessionHandler.getLoginID())
                    .addContentItems(branchUniversalObjects)
                    .logEvent(context);

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

        SessionHandler sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
        new BranchEvent(AppEventTracking.EventBranch.EVENT_LOGIN)
                .addCustomDataProperty(AppEventTracking.Branch.EMAIL, sessionHandler.getEmail())
                .addCustomDataProperty(AppEventTracking.Branch.PHONE, normalizePhoneNumber(sessionHandler.getPhoneNumber()))
                .logEvent(context);

    }

    public static void sendRegisterEvent(Context context, String email, String phone) {

        new BranchEvent(AppEventTracking.EventBranch.EVENT_REGISTER)
                .addCustomDataProperty(AppEventTracking.Branch.EMAIL, email)
                .addCustomDataProperty(AppEventTracking.Branch.PHONE, normalizePhoneNumber(phone))
                .logEvent(context);

    }

    private static double convertIDRtoDouble(String value) {
        double result = 0;
        try {
            result = convertRupiahToLong(value);
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static long convertRupiahToLong(String rupiah) {
        rupiah = rupiah.replace("Rp", "");
        rupiah = rupiah.replace(".", "");
        rupiah = rupiah.replace(" ", "");
        return Long.parseLong(rupiah);
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

    public static final String APP_SHOW_REFERRAL_BUTTON = "app_show_referral_button";

    public static Boolean isAppShowReferralButtonActivated(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(APP_SHOW_REFERRAL_BUTTON);
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

    private static String getOgTitle(ShareData data) {
        if (TextUtils.isEmpty(data.getOgTitle())) {
            return data.getName();
        } else {
            return data.getOgTitle();
        }
    }

    private static String getOgDesc(ShareData data) {
        if (TextUtils.isEmpty(data.getOgDescription())) {
            return data.getDescription();
        } else {
            return data.getOgDescription();
        }
    }

    private static String getOgImage(ShareData data) {
        if (TextUtils.isEmpty(data.getOgImageUrl())) {
            return data.getImgUri();
        } else {
            return data.getOgImageUrl();
        }
    }

    public static String getCampaignName(String type) {
        String campaign = "Product Share";
        if (type != null)
            campaign = type + " Share";
        return campaign;
    }

    public interface GenerateShareContents {
        void onCreateShareContents(String shareContents, String shareUri, String branchUrl);
    }

    public static Boolean isAndroidIosUrlActivated(Context context) {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(context);
        return remoteConfig.getBoolean(FIREBASE_KEY_INCLUDEMOBILEWEB, true);
    }
}
