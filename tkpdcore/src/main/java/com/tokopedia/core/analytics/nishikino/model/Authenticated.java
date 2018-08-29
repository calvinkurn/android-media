package com.tokopedia.core.analytics.nishikino.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by herdimac on 6/6/16.
 */
public class Authenticated extends BaseGTMModel {

    public static final String KEY_CD_NAME = "authenticated";

    public static final String KEY_CONTACT_INFO =       "contactInfo";
    public static final String KEY_USER_DEF_SHIPPING =  "userDefaultShipping";

    private static final String KEY_USER_SELLER =        "userSeller";
    private static final String KEY_USER_FULLNAME =      "userFullName";
    private static final String KEY_USER_EMAIL =         "userEmail";
    private static final String KEY_SHOP_LOC =           "shopLocation";

    private static final String KEY_COUNTRY =            "country";
    private static final String KEY_DISTRICT =           "district";
    private static final String KEY_CITY =               "city";
    private static final String KEY_PROVINCE =           "province";
    private static final String KEY_ZIPCODE =            "postalCode";

    private static final String KEY_USER_ID =            "userId";
    private static final String KEY_EMAIL_CRYPT =        "emailCrypt";
    private static final String KEY_LOGIN_TYPE =         "loginType";
    private static final String KEY_EC_256 =             "ec256";
    private static final String KEY_USER_MSIS_VER =      "userMSISNVerified";
    private static final String KEY_SHOP_PRODUCT_COUNT = "shopProductCount";
    private static final String KEY_USER_STATUS =        "userStatus";
    private static final String KEY_IS_QC_ACC =          "isQcAccount";
    private static final String KEY_SHOP_ID =            "shopID";
    public static final String KEY_SHOP_ID_SELLER =      "shopId";
    public static final String KEY_SHOP_TYPE =          "shopType";
    private static final String KEY_GENDER =             "gender";
    private static final String KEY_AF_UNIQUE_ID =       "afUniqueId";
    public static final String KEY_NETWORK_SPEED =      "networkSpeed";
    public static final String KEY_COMPETITOR_INTELLIGENCE =      "cIntel";
    public static final String KEY_PAGE_TYPE = "pageType";
    public static final String KEY_PRODUCT_ID = "productId";

    public static final String ANDROID_ID = "androidId";
    public static final String ADS_ID = "adsId";

    private Map<String, Object> contactInfo = new HashMap<>();
    private Map<String, Object> userDefaultShipping = new HashMap<>();

    public Map<String, Object> getAuthDataLayar(){
        setUserDefaultShipping();
        return contactInfo;
    }

    private String shopId;
    private String shopType;
    private String networkSpeed;
    private String cIntel;
    private String productId;
    private String pageType;
    private String androidId;
    private String adsId;

    public void setUserDefaultShipping(){
        contactInfo.put(KEY_USER_DEF_SHIPPING, userDefaultShipping);
    }

    public void setUserSeller(Object sellerStatus){
        contactInfo.put(KEY_USER_SELLER, sellerStatus);
    }

    public void setUserFullName(Object fullName){
        contactInfo.put(KEY_USER_FULLNAME, fullName);
    }

    public void setUserEmail(Object email){
        contactInfo.put(KEY_USER_EMAIL, email);
    }

    public void setShopLocation(Object shopLocation){
        contactInfo.put(KEY_SHOP_LOC, shopLocation);
    }

    public void setUserID(Object userID){
        contactInfo.put(KEY_USER_ID, userID);
    }

    public void setNetworkSpeed(String networkSpeed){
        this.networkSpeed = networkSpeed;
    }

    public void setKeyCompetitorIntelligence(String cIntelData) {
        this.cIntel = cIntelData;
    }

    public String getcIntel() {
        return cIntel;
    }

    public String getNetworkSpeed() {
        return networkSpeed;
    }

    public void setEmailCrypt(Object emailCrypt){
        contactInfo.put(KEY_EMAIL_CRYPT, emailCrypt);
    }

    public void setLoginType(Object loginType){
        contactInfo.put(KEY_LOGIN_TYPE, loginType);
    }

    public void setec256(Object ec256){
        contactInfo.put(KEY_EC_256, ec256);
    }

    public void setUserMSISNVer(Object userMSISNVer){
        contactInfo.put(KEY_USER_MSIS_VER, userMSISNVer);
    }

    public void setShopProductCount(Object shopProductCount){
        contactInfo.put(KEY_SHOP_PRODUCT_COUNT, shopProductCount);
    }

    public void setUserStatus(Object userStatus){
        contactInfo.put(KEY_USER_STATUS, userStatus);
    }

    public void setisQCAcc(Object isQcAcc){
        contactInfo.put(KEY_IS_QC_ACC, isQcAcc);
    }

    public void setShopID(Object shopID){
        contactInfo.put(KEY_SHOP_ID, shopID);
    }

    public void setGender(Object gender){
        contactInfo.put(KEY_GENDER, gender);
    }

    public void setAfUniqueId(Object gender){
        contactInfo.put(KEY_AF_UNIQUE_ID, gender);
    }

    public void setUDSCountry(Object country){
        userDefaultShipping.put(KEY_COUNTRY, country);
    }

    public void setUDSDistrict(Object district){
        userDefaultShipping.put(KEY_DISTRICT, district);
    }

    public void setUDSCity(Object city){
        userDefaultShipping.put(KEY_CITY, city);
    }

    public void setUDSProvince(Object province){
        userDefaultShipping.put(KEY_PROVINCE, province);
    }

    public void setUDSPostalCode(Object zipCode){
        userDefaultShipping.put(KEY_ZIPCODE, zipCode);
    }

    public void clearAuth(){

    }

    public String getPageType() {
        return pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setShopType(String shopType) {
        this.shopType = shopType;
    }

    public String getShopType() {
        return shopType;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopId() {
        return shopId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getAdsId() {
        return adsId;
    }

    public void setAdsId(String adsId) {
        this.adsId = adsId;
    }
}
