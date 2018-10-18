package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.google.firebase.perf.metrics.Trace;
import com.moe.pushlibrary.PayloadBuilder;
import com.moengage.push.PushManager;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.abstraction.common.utils.view.DateFormatUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 9/27/16.
 *         Modified by Hafizh Herdi
 *
 */

@Deprecated
public class TrackingUtils extends TrackingConfig {
    public static void eventCampaign(Context context, Campaign campaign) {
        Campaign temp = new Campaign(campaign);
        getGTMEngine(context)
                .sendCampaign(temp)
                .clearCampaign(campaign);
    }

    public static void activityBasedAFEvent(Context context, String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_HOME_ACTIVITY)) {
            afValue.put(AFInAppEventParameterName.PARAM_1, CommonUtils.getUniqueDeviceID(context));
        }
        getAFEngine(context).sendTrackEvent(AppScreen.convertAFActivityEvent(tag), afValue);
    }

    public static void setMoEngageExistingUser(Context context,boolean isLogin) {
        getMoEngine(context).isExistingUser(isLogin);
    }

    public static void setMoEngageUser(Context context,CustomerWrapper customerWrapper) {
        getMoEngine(context).setUserProfile(customerWrapper);
    }

    public static String getNetworkSpeed(Context context) {
        if (ConnectivityUtils.isConnected(context)) {
            return ConnectivityUtils.getConnectionType(context);
        } else {
            return ConnectivityUtils.CONN_UNKNOWN;
        }
    }

    public static void setMoEUserAttributes(Context context, @Nullable CustomerWrapper customerWrapper) {
        if(customerWrapper != null) {
            getMoEngine(context).setUserData(customerWrapper, "GRAPHQL");
        }
        if (!TextUtils.isEmpty(FCMCacheManager.getRegistrationId(context.getApplicationContext())))
                PushManager.getInstance().refreshToken(context.getApplicationContext(), FCMCacheManager.getRegistrationId(context.getApplicationContext()));
    }

    public static void setMoEUserAttributesLogin(Context context,
                                                 String userId,
                                                 String fullName,
                                                 String emailAddress,
                                                 String phoneNumber,
                                                 boolean isGoldMerchant,
                                                 String shopName,
                                                 String shopId,
                                                 boolean isSeller,
                                                 String method) {

        CustomerWrapper wrapper = new CustomerWrapper.Builder()
                .setCustomerId(userId)
                .setFullName(fullName)
                .setEmailAddress(emailAddress)
                .setPhoneNumber(normalizePhoneNumber(phoneNumber))
                .setGoldMerchant(isGoldMerchant)
                .setShopName(shopName)
                .setShopId(shopId)
                .setSeller(isSeller)
                .setFirstName(getFirstName(fullName))
                .setMethod(method)
                .build();

        getMoEngine(context).setUserData(wrapper, "LOGIN");
        sendMoEngageLoginEvent(context, wrapper);
    }


    public static void eventMoEngageLogoutUser(Context context) {
        getMoEngine(context).logoutEvent();
    }

    public static String extractFirstSegment(Context context,String inputString, String separator) {
        String firstSegment = "";
        if (!TextUtils.isEmpty(inputString)) {
            String token[] = inputString.split(separator);
            if (token.length > 1) {
                firstSegment = token[0];
            } else {
                firstSegment = separator;
            }
        }

        return firstSegment;
    }

    private static String getFirstName(String name) {
        String firstName = name;
        if (!TextUtils.isEmpty(name)) {
            String token[] = name.split(" ");
            if (token.length > 1) {
                firstName = token[0];
            }
        }

        return firstName;
    }

    public static String normalizePhoneNumber(String phoneNum) {
        if (!TextUtils.isEmpty(phoneNum))
            return phoneNum.replaceFirst("^0(?!$)", "62");
        else
            return "";
    }

    public static void sendMoEngageLoginEvent(Context context,CustomerWrapper customerWrapper) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.USER_ID, customerWrapper.getCustomerId());
        builder.putAttrString(AppEventTracking.MOENGAGE.MEDIUM, customerWrapper.getMethod());
        builder.putAttrString(AppEventTracking.MOENGAGE.EMAIL, customerWrapper.getEmailAddress());
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.LOGIN);
    }

    public static void sendMoEngageCreateShopEvent(Context context, String screenName){
        PayloadBuilder builder = new PayloadBuilder();
        SessionHandler sessionHandler = new SessionHandler(context);
        builder.putAttrString(AppEventTracking.MOENGAGE.SCREEN_NAME, screenName);
        builder.putAttrString(AppEventTracking.MOENGAGE.USER_ID, sessionHandler.getLoginID() );
        builder.putAttrString(AppEventTracking.MOENGAGE.EMAIL, sessionHandler.getEmail());
        builder.putAttrString(AppEventTracking.MOENGAGE.MOBILE_NUM, sessionHandler.getPhoneNumber());
        builder.putAttrString(AppEventTracking.MOENGAGE.APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        builder.putAttrString(AppEventTracking.MOENGAGE.PLATFORM, "android");
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_SHOP_SCREEN);
    }

    public static void sendMoEngageOpenHomeEvent(Context context) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, new SessionHandler(context).isV4Login());
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_BERANDA);
    }

    public static void sendMoEngageOpenFeedEvent(Context context, int feedSize) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, new SessionHandler(context).isV4Login());
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_FEED_EMPTY, feedSize == 0);
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_FEED);
    }

    public static void sendMoEngageOpenFavoriteEvent(Context context, int favoriteSize) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, new SessionHandler(context).isV4Login());
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_FAVORITE_EMPTY, favoriteSize == 0);
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_FAVORITE);
    }

    public static void sendMoEngageOpenProductEvent(Context context, ProductDetailData productData) {
        PayloadBuilder builder = new PayloadBuilder();
        if (productData.getBreadcrumb().size() > 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
            builder.putAttrString(
                    AppEventTracking.MOENGAGE.CATEGORY,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentName()
            );
            builder.putAttrString(
                    AppEventTracking.MOENGAGE.CATEGORY_ID,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentId()
            );
        } else if (productData.getBreadcrumb().size() == 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
        }

        if (productData.getInfo() != null) {
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, productData.getInfo().getProductName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, productData.getInfo().getProductId() + "");
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, productData.getInfo().getProductUrl());

            if (productData.getProductImages() != null
                    && productData.getProductImages().size() > 0
                    && productData.getProductImages().get(0) != null) {
                builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, productData.getProductImages().get(0).getImageSrc());
            }
            builder.putAttrInt(AppEventTracking.MOENGAGE.PRODUCT_PRICE, productData.getInfo().getProductPriceUnformatted());
        }

        if (productData.getShopInfo() != null) {
            builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, productData.getShopInfo().getShopIsOfficial() == 1);
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, productData.getShopInfo().getShopId());
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_NAME, productData.getShopInfo().getShopName());
        }

        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_PRODUCTPAGE);
    }

    public static void sendMoEngageClickHotListEvent(Context context, HotListModel hotListModel) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, new SessionHandler(context).isV4Login());
        builder.putAttrString(AppEventTracking.MOENGAGE.HOTLIST_NAME, hotListModel.getHotListName());
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.CLICK_HOTLIST);
    }

    public static void sendMoEngageOpenHotListEvent(Context context) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, new SessionHandler(context).isV4Login());
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_HOTLIST);
    }

    public static void sendMoEngageAddWishlistEvent(Context context, ProductDetailData productData) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, productData.getInfo().getProductName());
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, String.valueOf(productData.getInfo().getProductId()));
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, productData.getInfo().getProductUrl());
        builder.putAttrInt(AppEventTracking.MOENGAGE.PRODUCT_PRICE, productData.getInfo().getProductPriceUnformatted());

        builder.putAttrString(AppEventTracking.MOENGAGE.BRAND_NAME, productData.getInfo().getProductCatalogName());
        builder.putAttrString(AppEventTracking.MOENGAGE.BRAND_ID, productData.getInfo().getProductCatalogId());
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, productData.getShopInfo().getShopIsOfficial() == 1);

        if (productData.getBreadcrumb().size() > 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
            builder.putAttrString(
                    AppEventTracking.MOENGAGE.CATEGORY,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentName()
            );
            builder.putAttrString(
                    AppEventTracking.MOENGAGE.CATEGORY_ID,
                    productData.getBreadcrumb().get(productData.getBreadcrumb().size() - 1)
                            .getDepartmentId()
            );
        }

        if (productData.getBreadcrumb().size() == 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, productData.getBreadcrumb().get(0).getDepartmentId());
        }

        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.ADD_WISHLIST);
    }

    public static void sendMoEngageClickMainCategoryIcon(Context context,String categoryName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.CLICK_MAIN_CATEGORY_ICON);
    }

    public static void sendMoEngageOpenDigitalCatScreen(Context context,String categoryName, String id) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        builder.putAttrString(AppEventTracking.MOENGAGE.DIGITAL_CAT_ID, id);
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.DIGITAL_CAT_SCREEN_OPEN
        );
    }

    public static void sendMoEngageOpenCatScreen(Context context,String categoryName, String id) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, id);
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CAT_SCREEN_OPEN
        );
    }

    public static void sendMoEngageShippingReceivedEvent(Context context,boolean success) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_RECEIVED, success);
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.SHIPPING_CONFIRMED
        );
    }

    public static void sendMoEngageOpenSellerScreen(Context context) {
        PayloadBuilder builder = new PayloadBuilder();
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.SELLER_SCREEN_OPEN
        );
    }

    public static void sendMoEngageAddToCart(Context context, @NonNull Product product) {
        try {
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, product.getCategoryName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, product.getCategoryId());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, product.getName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, product.getId());

            builder.putAttrInt(
                    AppEventTracking.MOENGAGE.PRODUCT_PRICE,
                    CurrencyFormatHelper.convertRupiahToInt(product.getPrice())
            );

            getMoEngine(context).sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_ADDED_TO_CART
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMoEngageRemoveProductFromCart(Context context, @NonNull Product product) {
        try {
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, product.getName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, product.getId());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, product.getUrl());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, product.getImageUrl());
            builder.putAttrInt(
                    AppEventTracking.MOENGAGE.PRODUCT_PRICE,
                    CurrencyFormatHelper.convertRupiahToInt(product.getPrice())
            );
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, product.getShopId());
            getMoEngine(context).sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_REMOVED_FROM_CART
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMoEngageClickedNewOrder(Context context) {
        PayloadBuilder builder = new PayloadBuilder();
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.CLICKED_NEW_ORDER);
    }

    public static void sendMoEngageClickDiskusi(Context context, @NonNull ProductDetailData data) {
        PayloadBuilder builder = new PayloadBuilder();
        if (data.getBreadcrumb().size() > 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, data.getBreadcrumb().get(1).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, data.getBreadcrumb().get(1).getDepartmentId());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        } else if (data.getBreadcrumb().size() == 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        }

        if (data.getInfo() != null) {
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getInfo().getProductName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, data.getInfo().getProductId() + "");
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, data.getInfo().getProductUrl());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRICE, data.getInfo().getProductPrice());
        }

        if (data.getShopInfo() != null) {
            builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, data.getShopInfo().getShopIsOfficial() == 1);
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, data.getShopInfo().getShopId());
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_NAME, data.getShopInfo().getShopName());
        }

        if (data.getProductImages() != null
                && data.getProductImages().size() > 0
                && data.getProductImages().get(0) != null) {
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getProductImages().get(0).getImageSrc());
        }

        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CLICKED_DISKUSI_PDP
        );
    }

    public static void sendMoEngageClickUlasan(Context context, @NonNull ProductDetailData data) {
        PayloadBuilder builder = new PayloadBuilder();
        if (data.getBreadcrumb().size() > 1) {
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, data.getBreadcrumb().get(1).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, data.getBreadcrumb().get(1).getDepartmentId());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        } else if (data.getBreadcrumb().size() != 0) {
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, data.getBreadcrumb().get(0).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, data.getBreadcrumb().get(0).getDepartmentId());
        }

        if (data.getInfo() != null) {
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getInfo().getProductName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, data.getInfo().getProductId() + "");
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, data.getInfo().getProductUrl());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRICE, data.getInfo().getProductPrice());
        }

        if (data.getShopInfo() != null) {
            builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, data.getShopInfo().getShopIsOfficial() == 1);
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, data.getShopInfo().getShopId());
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_NAME, data.getShopInfo().getShopName());
        }

        if (data.getProductImages() != null
                && data.getProductImages().size() > 0
                && data.getProductImages().get(0) != null) {
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getProductImages().get(0).getImageSrc());
        }

        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CLICKED_ULASAN_PDP
        );
    }

    public static void sendMoEngageRemoveWishlist(Context context, Wishlist data) {
        if (data != null) {
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, data.getName());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, data.getId());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, data.getUrl());
            builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_IMAGE_URL, data.getImageUrl());
            builder.putAttrInt(AppEventTracking.MOENGAGE.PRODUCT_PRICE, data.getPrice());
            builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, data.getShop().getId());
            if (data.getShop() != null) {
                builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, data.getShop().getId());
            }

            getMoEngine(context).sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_REMOVED_FROM_WISHLIST
            );
        }
    }

    public static void sendMoEngageSearchAttempt(Context context,String keyword, boolean isResultFound) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.KEYWORD, keyword);
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound);
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.SEARCH_ATTEMPT
        );
    }

    public static void sendMoEngageOpenThankYouPage(Context context,String paymentType, String purchaseSite, double totalPrice) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.PAYMENT_TYPE, paymentType);
        builder.putAttrString(AppEventTracking.MOENGAGE.PURCHASE_SITE, purchaseSite);
        builder.putAttrDouble(AppEventTracking.MOENGAGE.TOTAL_PRICE, totalPrice);
        getMoEngine(context).sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.OPEN_THANKYOU_PAGE
        );
    }

    public static void sendMoEngagePurchaseReview(Context context,String reviewScore) {
        try {
            PayloadBuilder builder = new PayloadBuilder();
            builder.putAttrDouble(
                    AppEventTracking.MOENGAGE.REVIEW_SCORE,
                    Double.parseDouble(reviewScore)
            );
            getMoEngine(context).sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.SUCCESS_PURCHASE_REVIEW
            );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void sendMoEngageReferralScreenOpen(Context context,String screenName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.SCREEN_NAME, screenName);
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.REFERRAL_SCREEN_LAUNCHED);
    }

    public static void sendMoEngageReferralShareEvent(Context context,String channel) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CHANNEL, channel);
        getMoEngine(context).sendEvent(builder.build(), AppEventTracking.EventMoEngage.REFERRAL_SHARE_EVENT);
    }

    public static void fragmentBasedAFEvent(Context context,String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT)
                || tag.equals(AppScreen.IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "register_normal");
        } else if (tag.equals(AppScreen.IDENTIFIER_CATEGORY_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_HOME_MAIN);
        }

        getAFEngine(context).sendTrackEvent(AppScreen.convertAFFragmentEvent(tag), afValue);
    }

    public static String eventHTTP(Context context) {
        return getGTMEngine(context).eventHTTP();
    }

    public static void eventError(Context context,String className, String errorMessage) {
        getGTMEngine(context)
                .eventError(className, errorMessage);
    }

    public static void eventLogAnalytics(Context context,String className, String errorMessage) {
        getGTMEngine(context)
                .eventLogAnalytics(className, errorMessage);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventOnline(Context context,String uid) {
        getGTMEngine(context)
                .eventOnline(uid);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventPushUserID(Context context,String userId) {
        getGTMEngine(context)
                .pushUserId(userId);
    }

    public static void eventNetworkError(Context context,String error) {
        getGTMEngine(context).eventNetworkError(error);
    }

    static void eventAppsFlyerViewListingSearch(Context context,JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine(context).sendTrackEvent(AFInAppEventType.SEARCH, listViewEvent);
    }

    static void eventAppsFlyerContentView(Context context, JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine(context).sendTrackEvent(AFInAppEventType.CONTENT_VIEW, listViewEvent);
    }

    public static void sendGTMEvent(Context context, Map<String, Object> dataLayers) {
        getGTMEngine(context).sendEvent(dataLayers);
    }

    public static void sendAppsFlyerDeeplink(Activity activity) {
        getAFEngine(activity).sendDeeplinkData(activity);
    }

    public static String getClientID(Context context) {
        return getGTMEngine(context).getClientIDString();
    }

    public static String getGtmString(Context context, String key) {
        return getGTMEngine(context).getString(key);
    }

    public static boolean getBoolean(Context context, String key) {
        return getGTMEngine(context).getBoolean(key);
    }

    public static long getLong(Context context, String key) {
        return 0;
    }

    public static double getDouble(Context context, String key) {
        return getGTMEngine(context).getDouble(key);
    }

    public static String getAfUniqueId(Context context) {
        return getAFEngine(context).getUniqueId();
    }

    public static String getAdsId(Context context) {
        return getAFEngine(context).getAdsIdDirect();
    }

    public static Trace startTrace(Context context, String traceName) {
        return getFPMEngine(context,traceName).startTrace();
    }

    public static void eventClickHotlistProductFeatured(Context context, Hotlist hotlist) {
        getGTMEngine(context).eventClickHotlistProductFeatured(hotlist);
    }

    public static void eventImpressionHotlistProductFeatured(Context context, Hotlist hotlist) {
        getGTMEngine(context).eventImpressionHotlistProductFeatured(hotlist);
    }

    public static void impressionHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).impressionHotlistTracking(hotlistName, promoName, promoCode);
    }

    public static void clickCopyButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).clickCopyButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void clickTnCButtonHotlistPromo(Context context, String hotlistName, String promoName, String promoCode) {
        getGTMEngine(context).clickTncButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void eventClearEnhanceEcommerce(Context context) {
        getGTMEngine(context).clearEnhanceEcommerce();
    }

    public static void eventTrackingEnhancedEcommerce(Context context, Map<String, Object> trackingData) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventTrackingEnhancedEcommerce(trackingData);

    }

    public static void eventImpressionPromoList(Context context, List<Object> list, String promoName) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventImpressionPromoList(list, promoName);
    }

    public static void eventClickPromoListItem(Context context, List<Object> list, String promoName) {
        getGTMEngine(context).clearEnhanceEcommerce();
        getGTMEngine(context).eventClickPromoListItem(list, promoName);
    }


    public static void eventCategoryLifestyleImpression(Context context, List<Object> list) {
        getGTMEngine(context).eventImpressionCategoryLifestyle(list);
    }

    public static void eventCategoryLifestyleClick(Context context, String categoryUrl, List<Object> list) {
        getGTMEngine(context).eventClickCategoryLifestyle(categoryUrl, list);
    }

    public static void setMoEngagePushPreference(Context context, Boolean status) {
        getMoEngine(context).setPushPreference(status);
    }
}

