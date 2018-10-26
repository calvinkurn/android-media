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
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.model.Hotlist;
import com.tokopedia.core.analytics.model.Product;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer2.data.pojo.UserData;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.network.entity.wishlist.Wishlist;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

public class TrackingUtils extends TrackingConfig {
    public static void eventCampaign(Campaign campaign) {
        Campaign temp = new Campaign(campaign);
        getGTMEngine()
                .sendCampaign(temp)
                .clearCampaign(campaign);
    }

    public static void activityBasedAFEvent(String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(HomeRouter.IDENTIFIER_HOME_ACTIVITY)) {
            afValue.put(AFInAppEventParameterName.PARAM_1, CommonUtils.getUniqueDeviceID(MainApplication.getAppContext()));
        }
        getAFEngine().sendTrackEvent(AppScreen.convertAFActivityEvent(tag), afValue);
    }

    public static void setMoEngageExistingUser() {
        getMoEngine().isExistingUser(SessionHandler.isV4Login(MainApplication.getAppContext()));
    }

    public static void setMoEngageUser(CustomerWrapper customerWrapper) {
        getMoEngine().setUserProfile(customerWrapper);
    }

    public static String getNetworkSpeed(Context context) {
        if (ConnectivityUtils.isConnected(context)) {
            return ConnectivityUtils.getConnectionType(context);
        } else {
            return ConnectivityUtils.CONN_UNKNOWN;
        }
    }

    public static void setMoEUserAttributes(Context context, @Nullable CustomerWrapper customerWrapper) {
        if (customerWrapper != null) {
            getMoEngine().setUserData(customerWrapper, "GRAPHQL");
        }
        if (!TextUtils.isEmpty(FCMCacheManager.getRegistrationId(context.getApplicationContext())))
            PushManager.getInstance().refreshToken(context.getApplicationContext(), FCMCacheManager.getRegistrationId(context.getApplicationContext()));
    }

    public static void setMoEUserAttributes(UserData profileData) {
        if (profileData != null) {
            try {
                CustomerWrapper customerWrapper = new CustomerWrapper.Builder()
                        .setTotalItemSold(profileData.getShopInfoMoengage().getStats() != null ? profileData.getShopInfoMoengage().getStats().getShopItemSold() : "0")
                        .setRegDate(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, extractFirstSegment(profileData.getProfile().getRegisterDate(), "T")))
                        .setDateShopCreated(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getDateShopCreated() : "")
                        .setShopLocation(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getShopLocation() : "")
                        .setTokocashAmt(profileData.getWallet() != null ? profileData.getWallet().getRawBalance() + "" : "")
                        .setSaldoAmt(profileData.getSaldo() != null ? profileData.getSaldo().getDeposit() + "" : "")
                        .setTopAdsAmt(profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getTopadsAmount() + "" : "")
                        .setTopadsUser(profileData.getTopadsDeposit() != null ? profileData.getTopadsDeposit().getIsTopadsUser() : false)
                        .setHasPurchasedMarketplace(profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() != null ? profileData.getPaymentAdminProfile().getIsPurchasedMarketplace() : false)
                        .setHasPurchasedDigital(profileData.getPaymentAdminProfile().getIsPurchasedDigital() != null ? profileData.getPaymentAdminProfile().getIsPurchasedDigital() : false)
                        .setHasPurchasedTiket(profileData.getPaymentAdminProfile().getIsPurchasedTicket() != null ? profileData.getPaymentAdminProfile().getIsPurchasedTicket() : false)
                        .setLastTransactionDate(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, extractFirstSegment(profileData.getPaymentAdminProfile() != null ? profileData.getPaymentAdminProfile().getLastPurchaseDate() : "", "T")))
                        .setTotalActiveProduct(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getTotalActiveProduct() + "" : "")
                        .setShopScore(profileData.getShopInfoMoengage().getInfo() != null ? profileData.getShopInfoMoengage().getInfo().getShopScore() + "" : "")
                        .setDateOfBirth(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD, DateFormatUtils.FORMAT_DD_MM_YYYY, extractFirstSegment(profileData.getProfile().getBday() != null ? profileData.getProfile().getBday() : "", "T")))
                        .setGender(profileData.getProfile().getGender() != null ? profileData.getProfile().getGender() : "0")
                        .setFullName(profileData.getProfile() == null ? "" : profileData.getProfile().getFullName())
                        .setEmailAddress(profileData.getProfile() == null ? "" : profileData.getProfile().getEmail())
                        .setPhoneNumber(normalizePhoneNumber(profileData.getProfile() == null || profileData.getProfile().getPhone() == null ? "" : profileData.getProfile().getPhone()))
                        .setCustomerId(profileData.getProfile() == null ? "" : profileData.getProfile().getUserId())
                        .setShopId(profileData.getShopInfoMoengage() != null ? profileData.getShopInfoMoengage().getInfo().getShopId() : "")
                        .setSeller((profileData.getShopInfoMoengage() != null &&
                                profileData.getShopInfoMoengage().getOwner() != null &&
                                profileData.getShopInfoMoengage().getOwner().getIsSeller() != null) ? profileData.getShopInfoMoengage().getOwner().getIsSeller() : false)
                        .setShopName(profileData.getShopInfoMoengage() != null ? profileData.getShopInfoMoengage().getInfo().getShopName() : "")
                        .setFirstName(profileData.getProfile() != null && profileData.getProfile().getFirstName() != null ? profileData.getProfile().getFirstName() : "")
                        .build();

                getMoEngine().setUserData(customerWrapper, "GRAPHQL");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (!TextUtils.isEmpty(FCMCacheManager.getRegistrationId(MainApplication.getAppContext())))
            PushManager.getInstance().refreshToken(MainApplication.getAppContext(), FCMCacheManager.getRegistrationId(MainApplication.getAppContext()));
    }

    public static void setMoEUserAttributes(Bundle bundle, String label) {
        AccountsParameter accountsParameter = bundle.getParcelable(AppEventTracking.ACCOUNTS_KEY);

        if (accountsParameter != null) {
            CustomerWrapper wrapper = new CustomerWrapper.Builder()
                    .setCustomerId(
                            bundle.getString(com.tokopedia.core.analytics.AppEventTracking.USER_ID_KEY,
                                    com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                    )
                    .setFullName(
                            bundle.getString(com.tokopedia.core.analytics.AppEventTracking.FULLNAME_KEY,
                                    com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                    )
                    .setEmailAddress(
                            bundle.getString(com.tokopedia.core.analytics.AppEventTracking.EMAIL_KEY,
                                    com.tokopedia.core.analytics.AppEventTracking.DEFAULT_CHANNEL)
                    )
                    .setPhoneNumber(normalizePhoneNumber(accountsParameter.getInfoModel() != null ? accountsParameter.getInfoModel().getPhone() : ""))
                    .setGoldMerchant(accountsParameter.getAccountsModel() != null && accountsParameter.getAccountsModel().getShopIsGold() == 1)
                    .setShopName(accountsParameter.getAccountsModel() != null ? accountsParameter.getAccountsModel().getShopName() : "")
                    .setShopId(String.valueOf(accountsParameter.getAccountsModel() != null ? accountsParameter.getAccountsModel().getShopId() : ""))
                    .setSeller(!TextUtils.isEmpty(accountsParameter.getAccountsModel() != null ? accountsParameter.getAccountsModel().getShopName() : ""))
                    .setFirstName(getFirstName(accountsParameter.getAccountsModel() != null ? accountsParameter.getAccountsModel().getFullName() : ""))
                    .setMethod(label)
                    .build();

            getMoEngine().setUserData(wrapper, "LOGIN");
            sendMoEngageLoginEvent(wrapper);
        }
    }

    public static void setMoEUserAttributesLogin(String userId,
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

        getMoEngine().setUserData(wrapper, "LOGIN");
        sendMoEngageLoginEvent(wrapper);
    }


    public static void eventMoEngageLogoutUser() {
        getMoEngine().logoutEvent();
    }

    public static String extractFirstSegment(String inputString, String separator) {
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

    public static void sendMoEngageLoginEvent(CustomerWrapper customerWrapper) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.USER_ID, customerWrapper.getCustomerId());
        builder.putAttrString(AppEventTracking.MOENGAGE.MEDIUM, customerWrapper.getMethod());
        builder.putAttrString(AppEventTracking.MOENGAGE.EMAIL, customerWrapper.getEmailAddress());
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.LOGIN);
    }

    public static void sendMoEngageCreateShopEvent(String screenName) {
        PayloadBuilder builder = new PayloadBuilder();
        SessionHandler sessionHandler = new SessionHandler(MainApplication.getAppContext());
        builder.putAttrString(AppEventTracking.MOENGAGE.SCREEN_NAME, screenName);
        builder.putAttrString(AppEventTracking.MOENGAGE.USER_ID, sessionHandler.getLoginID());
        builder.putAttrString(AppEventTracking.MOENGAGE.EMAIL, sessionHandler.getEmail());
        builder.putAttrString(AppEventTracking.MOENGAGE.MOBILE_NUM, SessionHandler.getPhoneNumber());
        builder.putAttrString(AppEventTracking.MOENGAGE.APP_VERSION, String.valueOf(GlobalConfig.VERSION_CODE));
        builder.putAttrString(AppEventTracking.MOENGAGE.PLATFORM, "android");
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_SHOP_SCREEN);
    }

    public static void sendMoEngageOpenHomeEvent() {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_BERANDA);
    }

    public static void sendMoEngageOpenFeedEvent(int feedSize) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_FEED_EMPTY, feedSize == 0);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_FEED);
    }

    public static void sendMoEngageOpenFavoriteEvent(int favoriteSize) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_FAVORITE_EMPTY, favoriteSize == 0);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_FAVORITE);
    }

    public static void sendMoEngageOpenProductEvent(ProductDetailData productData) {
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

        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_PRODUCTPAGE);
    }

    public static void sendMoEngageClickHotListEvent(HotListModel hotListModel) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrString(AppEventTracking.MOENGAGE.HOTLIST_NAME, hotListModel.getHotListName());
        builder.putAttrString(AppEventTracking.MOENGAGE.HOTLIST_ID, hotListModel.getHotListId());
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.CLICK_HOTLIST);
    }

    public static void sendMoEngageOpenHotListEvent() {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.OPEN_HOTLIST);
    }

    public static void sendMoEngageClickMainCategoryIcon(String categoryName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.CLICK_MAIN_CATEGORY_ICON);
    }

    public static void sendMoEngageOpenDigitalCatScreen(String categoryName, String id) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        builder.putAttrString(AppEventTracking.MOENGAGE.DIGITAL_CAT_ID, id);
        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.DIGITAL_CAT_SCREEN_OPEN
        );
    }

    public static void sendMoEngageOpenCatScreen(String categoryName, String id) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, id);
        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CAT_SCREEN_OPEN
        );
    }

    public static void sendMoEngageFavoriteEvent(String shopName, String shopID, String shopDomain, String shopLocation, boolean isShopOfficaial, boolean isFollowed) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_NAME, shopName);
        builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_ID, shopID);
        builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_LOCATION, shopLocation);
        builder.putAttrString(AppEventTracking.MOENGAGE.SHOP_URL_SLUG, shopDomain);
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_OFFICIAL_STORE, isShopOfficaial);
        getMoEngine().sendEvent(
                builder.build(),
                isFollowed ?
                        AppEventTracking.EventMoEngage.SELLER_REMOVE_FAVORITE :
                        AppEventTracking.EventMoEngage.SELLER_ADDED_FAVORITE
        );
    }

    public static void sendMoEngageAddressEvent(String address) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CITY, address);
        getMoEngine().sendEvent(
                builder.build(), AppEventTracking.MOENGAGE.ADDRESS_ADDED
        );
    }

    public static void sendMoEngageAddToCart(@NonNull Product product) {
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

            getMoEngine().sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_ADDED_TO_CART
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMoEngageCategoryEvent(String categoryId, String categoryName, String subCategoryId, String subCategoryName, String productId, String productName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY_ID, categoryId);
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_GROUP_NAME, productName);
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_GROUP_ID, productId);
        builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, subCategoryId);
        builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, subCategoryName);
        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CAT_SCREEN_OPEN
        );

    }

    public static void sendMoEngageRemoveProductFromCart(@NonNull Product product) {
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
            getMoEngine().sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_REMOVED_FROM_CART
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMoEngageClickDiskusi(@NonNull ProductDetailData data) {
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

        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CLICKED_DISKUSI_PDP
        );
    }

    public static void sendMoEngageClickUlasan(@NonNull ProductDetailData data) {
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

        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.CLICKED_ULASAN_PDP
        );
    }

    public static void sendMoEngageRemoveWishlist(Wishlist data) {
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

            getMoEngine().sendEvent(
                    builder.build(),
                    AppEventTracking.EventMoEngage.PRODUCT_REMOVED_FROM_WISHLIST
            );
        }
    }

    public static void sendMoEngageSearchAttempt(String keyword, boolean isResultFound, HashMap<Integer, String> category) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.KEYWORD, keyword);
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.IS_RESULT_FOUND, isResultFound);
        if (category != null) {
            builder.putAttrJSONArray(AppEventTracking.MOENGAGE.CATEGORY_ID_MAPPING, new JSONArray(Arrays.asList(category.keySet().toArray())));
            builder.putAttrJSONArray(AppEventTracking.MOENGAGE.CATEGORY_NAME_MAPPING, new JSONArray((category.values())));
        }
        getMoEngine().sendEvent(
                builder.build(),
                AppEventTracking.EventMoEngage.SEARCH_ATTEMPT
        );
    }

    public static void sendMoEngageReferralScreenOpen(String screenName) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.SCREEN_NAME, screenName);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.REFERRAL_SCREEN_LAUNCHED);
    }

    public static void sendMoEngageReferralShareEvent(String channel) {
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CHANNEL, channel);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.EventMoEngage.REFERRAL_SHARE_EVENT);
    }

    public static void fragmentBasedAFEvent(String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT)
                || tag.equals(AppScreen.IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.REGSITRATION_METHOD, "register_normal");
        } else if (tag.equals(HomeRouter.IDENTIFIER_CATEGORY_FRAGMENT)) {
            afValue.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_HOME_MAIN);
        }

        getAFEngine().sendTrackEvent(AppScreen.convertAFFragmentEvent(tag), afValue);
    }

    public static String eventHTTP() {
        return getGTMEngine().eventHTTP();
    }

    public static void eventError(String className, String errorMessage) {
        getGTMEngine()
                .eventError(className, errorMessage);
    }

    public static void eventLogAnalytics(String className, String errorMessage) {
        getGTMEngine()
                .eventLogAnalytics(className, errorMessage);
    }

    public static void eventOnline() {
        getGTMEngine()
                .eventOnline(SessionHandler.getGTMLoginID(MainApplication.getAppContext()));
    }

    public static void eventPushUserID() {
        getGTMEngine()
                .pushUserId(SessionHandler.getGTMLoginID(MainApplication.getAppContext()));
    }

    public static void eventNetworkError(String error) {
        getGTMEngine().eventNetworkError(error);
    }

    public static void eventAppsFlyerViewListingSearch(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
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

        getAFEngine().sendTrackEvent(AFInAppEventType.SEARCH, listViewEvent);
    }

    static void eventAppsFlyerContentView(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine().sendTrackEvent(AFInAppEventType.CONTENT_VIEW, listViewEvent);
    }

    public static void sendGTMEvent(Map<String, Object> dataLayers) {
        getGTMEngine().sendEvent(dataLayers);
    }

    public static void sendAppsFlyerDeeplink(Activity activity) {
        getAFEngine().sendDeeplinkData(activity);
    }

    public static String getClientID() {
        return getGTMEngine().getClientIDString();
    }

    public static String getGtmString(String key) {
        return getGTMEngine().getString(key);
    }

    public static boolean getBoolean(String key) {
        return getGTMEngine().getBoolean(key);
    }

    public static long getLong(String key) {
        return 0;
    }

    public static double getDouble(String key) {
        return getGTMEngine().getDouble(key);
    }

    public static String getAfUniqueId() {
        return getAFEngine().getUniqueId();
    }

    public static String getAdsId() {
        return getAFEngine().getAdsIdDirect();
    }

    public static Trace startTrace(String traceName) {
        return getFPMEngine(traceName).startTrace();
    }

    public static void eventClickHotlistProductFeatured(Hotlist hotlist) {
        getGTMEngine().eventClickHotlistProductFeatured(hotlist);
    }

    public static void eventImpressionHotlistProductFeatured(Hotlist hotlist) {
        getGTMEngine().eventImpressionHotlistProductFeatured(hotlist);
    }

    public static void impressionHotlistPromo(String hotlistName, String promoName, String promoCode) {
        getGTMEngine().impressionHotlistTracking(hotlistName, promoName, promoCode);
    }

    public static void clickCopyButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        getGTMEngine().clickCopyButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void clickTnCButtonHotlistPromo(String hotlistName, String promoName, String promoCode) {
        getGTMEngine().clickTncButtonHotlistPromo(hotlistName, promoName, promoCode);
    }

    public static void eventClearEnhanceEcommerce() {
        getGTMEngine().clearEnhanceEcommerce();
    }

    public static void eventTrackingEnhancedEcommerce(Map<String, Object> trackingData) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventTrackingEnhancedEcommerce(trackingData);

    }

    public static void eventImpressionPromoList(List<Object> list, String promoName) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventImpressionPromoList(list, promoName);
    }

    public static void eventClickPromoListItem(List<Object> list, String promoName) {
        getGTMEngine().clearEnhanceEcommerce();
        getGTMEngine().eventClickPromoListItem(list, promoName);
    }


    public static void eventCategoryLifestyleImpression(List<Object> list) {
        getGTMEngine().eventImpressionCategoryLifestyle(list);
    }

    public static void eventCategoryLifestyleClick(String categoryUrl, List<Object> list) {
        getGTMEngine().eventClickCategoryLifestyle(categoryUrl, list);
    }

    public static void setMoEngagePushPreference(Boolean status) {
        getMoEngine().setPushPreference(status);
    }

    public static void sendMoEngageEvents(String eventName, Map<String, Object> values) {
        PayloadBuilder builder = new PayloadBuilder();
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            builder.putAttrObject(entry.getKey(), entry.getValue());
        }
        getMoEngine().sendEvent(builder.build(), eventName);
    }
}

