package com.tokopedia.core.analytics;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.moe.pushlibrary.PayloadBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.drawer.model.profileinfo.ProfileData;
import com.tokopedia.core.home.model.HotListModel;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.model.AccountsParameter;
import com.tokopedia.core.util.DateFormatUtils;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author  by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

public class TrackingUtils extends TrackingConfig {
    public static void eventCampaign(Campaign campaign){
        Campaign temp = new Campaign(campaign);
        getGTMEngine()
                .sendCampaign(temp)
                .clearCampaign(campaign);
    }


    public static void activityBasedAFEvent(String tag){
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(HomeRouter.IDENTIFIER_HOME_ACTIVITY)){
            afValue.put(AFInAppEventParameterName.PARAM_1, CommonUtils.getUniqueDeviceID(MainApplication.getAppContext()));
        }
        getAFEngine().sendTrackEvent(AppScreen.convertAFActivityEvent(tag), afValue);
    }

    public static void setMoEngageExistingUser(){
        getMoEngine().isExistingUser(SessionHandler.isV4Login(MainApplication.getAppContext()));
    }

    public static void setMoEngageUser(CustomerWrapper customerWrapper){
        getMoEngine().setUserProfile(customerWrapper);
    }

    public static void setMoEUserAttributes(ProfileData profileData){

        CommonUtils.dumper("MoEngage called app launch events "+profileData.getUserInfo().getUserBirth());

        CustomerWrapper customerWrapper = new CustomerWrapper.Builder()
                .setFullName(profileData.getUserInfo().getUserName())
                .setEmailAddress(profileData.getUserInfo().getUserEmail())
                .setPhoneNumber(normalizePhoneNumber(profileData.getUserInfo().getUserPhone()))
                .setCustomerId(profileData.getUserInfo().getUserId())
                .setShopId(profileData.getShopInfo().getShopId())
                .setSeller(!TextUtils.isEmpty(profileData.getShopInfo().getShopId()))
                .setShopName(profileData.getShopInfo().getShopName())
                //.setDateOfBirth(profileData.getUserInfo().getUserBirth())
                .setFirstName(extractFirstSegment(profileData.getUserInfo().getUserName()," "))
                .build();

        getMoEngine().setUserData(customerWrapper);
    }

    public static void setMoEUserAttributes(Bundle bundle, String label){

        AccountsParameter accountsParameter = bundle.getParcelable(AppEventTracking.ACCOUNTS_KEY);

        CommonUtils.dumper("MoEngage called login events "+bundle+" data "+accountsParameter.getInfoModel().getBday());

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
                .setPhoneNumber(normalizePhoneNumber(accountsParameter.getInfoModel().getPhone()))
                .setGoldMerchant(accountsParameter.getAccountsModel().getShopIsGold() == 1)
                .setShopName(accountsParameter.getAccountsModel().getShopName())
                .setShopId(String.valueOf(accountsParameter.getAccountsModel().getShopId()))
                .setSeller(!TextUtils.isEmpty(accountsParameter.getAccountsModel().getShopName()))
                .setFirstName(extractFirstSegment(accountsParameter.getAccountsModel().getFullName()," "))
                .setDateOfBirth(DateFormatUtils.formatDate(DateFormatUtils.FORMAT_YYYY_MM_DD,DateFormatUtils.FORMAT_DD_MM_YYYY,extractFirstSegment(accountsParameter.getInfoModel().getBday(),"T")))
                .setMethod(label)
                .build();

        getMoEngine().setUserData(wrapper);
        sendMoEngageLoginEvent(wrapper);
    }

    public static void eventMoEngageLogoutUser(){
        getMoEngine().logoutEvent();
    }

    private static String extractFirstSegment(String inputString, String separator){
        String firstSegment = "";
        if(!TextUtils.isEmpty(inputString)){
            String token[] = inputString.split(separator);
            if(token.length>1){
                firstSegment = token[0];
            }else{
                firstSegment = separator;
            }
        }

        return firstSegment;
    }

    private static String normalizePhoneNumber(String phoneNum){
        return phoneNum.replaceFirst("^0(?!$)", "62");
    }

    public static void sendMoEngageLoginEvent(CustomerWrapper customerWrapper){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.USER_ID, customerWrapper.getCustomerId());
        builder.putAttrString(AppEventTracking.MOENGAGE.MEDIUM, customerWrapper.getMethod());
        builder.putAttrString(AppEventTracking.MOENGAGE.EMAIL, customerWrapper.getEmailAddress());
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_LOGIN);
    }

    public static void sendMoEngageOpenHomeEvent(){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_OPEN_BERANDA);
    }

    public static void sendMoEngageOpenFeedEvent(int feedSize){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrInt(AppEventTracking.MOENGAGE.PRODUCTS_NUMBER, feedSize);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_OPEN_FEED);
    }

    public static void sendMoEngageOpenFavoriteEvent(int feedSize){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrInt(AppEventTracking.MOENGAGE.PRODUCTS_NUMBER, feedSize);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_OPEN_FAVORITE);
    }

    public static void sendMoEngageOpenProductEvent(ProductDetailData productData){
        PayloadBuilder builder = new PayloadBuilder();
        if(productData.getBreadcrumb().size()>1)
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(1).getDepartmentName());
        else
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_OPEN_PRODUCTPAGE);
    }

    public static void sendMoEngageClickHotListEvent(HotListModel hotListModel){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, hotListModel.getHotListName());
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_CLICK_HOTLIST);
    }

    public static void sendMoEngageOpenHotListEvent(){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrBoolean(AppEventTracking.MOENGAGE.LOGIN_STATUS, SessionHandler.isV4Login(MainApplication.getAppContext()));
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_OPEN_HOTLIST);
    }

    public static void sendMoEngageAddWishlistEvent(ProductDetailData productData){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_NAME, productData.getInfo().getProductName());
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_ID, String.valueOf(productData.getInfo().getProductId()));
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_URL, productData.getInfo().getProductUrl());
        builder.putAttrString(AppEventTracking.MOENGAGE.PRODUCT_PRICE, productData.getInfo().getProductPrice());

        builder.putAttrString(AppEventTracking.MOENGAGE.BRAND_NAME, productData.getInfo().getProductCatalogName());
        builder.putAttrString(AppEventTracking.MOENGAGE.BRAND_ID, productData.getInfo().getProductCatalogId());

        if(productData.getBreadcrumb().size()>1)
        {
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY, productData.getBreadcrumb().get(1).getDepartmentName());
            builder.putAttrString(AppEventTracking.MOENGAGE.SUBCATEGORY_ID, productData.getBreadcrumb().get(1).getDepartmentId());
        }

        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, productData.getBreadcrumb().get(0).getDepartmentName());

        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_ADD_WISHLIST);
    }

    public static void sendMoEngageClickMainCategoryIcon(String categoryName){
        PayloadBuilder builder = new PayloadBuilder();
        builder.putAttrString(AppEventTracking.MOENGAGE.CATEGORY, categoryName);
        getMoEngine().sendEvent(builder.build(), AppEventTracking.MOENGAGE.EVENT_CLICK_MAIN_CATEGORY_ICON);
    }

    public static void fragmentBasedAFEvent(String tag){
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(SessionRouter.IDENTIFIER_REGISTER_NEWNEXT_FRAGMENT) || tag.equals(SessionRouter.IDENTIFIER_REGISTER_PASSPHONE_FRAGMENT)){
            afValue.put(AFInAppEventParameterName.REGSITRATION_METHOD,"register_normal");
        } else if (tag.equals(HomeRouter.IDENTIFIER_CATEGORY_FRAGMENT)){
            afValue.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_HOME_MAIN);
        }

        getAFEngine().sendTrackEvent(AppScreen.convertAFFragmentEvent(tag), afValue);
    }

    public static String eventHTTP(){
        return getGTMEngine().eventHTTP();
    }

    public static void eventError(String className, String errorMessage){
        getGTMEngine()
                .eventError(className, errorMessage);
    }

    public static void eventLogAnalytics(String className, String errorMessage){
        getGTMEngine()
                .eventLogAnalytics(className, errorMessage);
    }

    public static void eventOnline(){
        getGTMEngine()
                .eventOnline(SessionHandler.getGTMLoginID(MainApplication.getAppContext()));
    }

    public static void eventPushUserID(){
        getGTMEngine()
                .pushUserId(SessionHandler.getGTMLoginID(MainApplication.getAppContext()));
    }

    public static void eventNetworkError(String error){
        getGTMEngine().eventNetworkError(error);
    }

    static void eventAppsFlyerViewListingSearch(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
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
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine().sendTrackEvent(AFInAppEventType.CONTENT_VIEW, listViewEvent);
    }


    public static void eventLocaNotificationCallback(Intent intent){
        getLocaEngine().sendNotificationCallback(intent);
    }

    public static void eventLocaNotificationReceived(Bundle data){
        getLocaEngine().sendReceiveNotification(data);
    }

    public static void eventLocaNotification(String eventName, Map<String, String> params){
        eventLoca(eventName, params);
        eventLocaInAppMessaging(eventName);
    }

    public static void eventLoca(String eventName){
        getLocaEngine().tagEvent(eventName);
    }

    public static void eventLoca(String eventName, Map<String, String> params){
        getLocaEngine().tagEvent(eventName, params);
    }

    static void eventLoca(String eventName, Map<String, String> params, long value){
        getLocaEngine().tagEvent(eventName, params, value);
    }

    public static void eventLocaUserAttributes(String loginID, String username, String email){
        getLocaEngine().tagUserAttributes(loginID, username, email);
    }

    public static void eventLocaInApp(String eventName){
        getLocaEngine().triggerInAppMessage(eventName);
    }

    public static void eventLocaInAppMessaging(String eventName){
        getLocaEngine().tageEventandInApp(eventName);
    }

    public static void eventLocaSearched(String keyword){
        getLocaEngine().sendEventSearchProduct(keyword,"product",null,null);
    }

    static void sendGTMEvent(Map<String, Object> dataLayers){
        getGTMEngine().sendEvent(dataLayers);
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
        return getGTMEngine().getLong(key);
    }

    public static double getDouble(String key) {
        return getGTMEngine().getDouble(key);
    }

    public static String getAfUniqueId(){
        return getAFEngine().getUniqueId();
    }

}

