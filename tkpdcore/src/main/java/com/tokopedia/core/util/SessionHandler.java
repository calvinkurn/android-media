package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.base.common.dbManager.FeedDbManager;
import com.tokopedia.core.base.common.dbManager.RecentProductDbManager;
import com.tokopedia.core.base.common.dbManager.TopAdsDbManager;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.database.manager.ProductDetailCacheManager;
import com.tokopedia.core.database.manager.ProductOtherCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.message.interactor.CacheInteractorImpl;
import com.tokopedia.core.prototype.InboxCache;
import com.tokopedia.core.prototype.ManageProductCache;
import com.tokopedia.core.prototype.PembelianCache;
import com.tokopedia.core.prototype.PenjualanCache;
import com.tokopedia.core.prototype.ProductCache;
import com.tokopedia.core.prototype.ShopCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.session.DialogLogoutFragment;
import com.tokopedia.core.talk.cache.database.InboxTalkCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;

public class SessionHandler {
    private static final String DEFAULT_EMPTY_SHOP_ID = "0";
    private static final String DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1";

    private static final String SAVE_REAL = "SAVE_REAL";
    private static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    public static final String CACHE_PROMOTION_PRODUCT = "CACHE_PROMOTION_PRODUCT";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String TEMP_PHONE_NUMBER = "TEMP_PHONE_NUMBER";
    private static final String TEMP_NAME = "TEMP_NAME";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String GTM_LOGIN_ID = "GTM_LOGIN_ID";
    private static final String SHOP_ID = "SHOP_ID";
    private static final String STATE_BROWSE = "STATE_BROWSE";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String USER_AVATAR_URI = "USER_AVATAR_URI";
    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String IS_FIRST_TIME_USER = "IS_FIRST_TIME";
    private static final String IS_FIRST_TIME_USER_NEW_ONBOARDING = "IS_FIRST_TIME_NEW_ONBOARDING";
    private static final String MSISDN_SESSION = "MSISDN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String WALLET_REFRESH_TOKEN = "WALLET_REFRESH_TOKEN";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String IS_FIRST_TIME_STORAGE = "IS_FIRST_TIME_STORAGE";
    private static final String LOGIN_UUID_KEY = "LOGIN_UUID";
    private static final String UUID_KEY = "uuid";
    private static final String DEFAULT_UUID_VALUE = "";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String USER_DATA = "USER_DATA";
    private static final String KEY_IV = "tokopedia1234567";
    private static final String TOKOCASH_SESSION = "TOKOCASH_SESSION";
    private static final String ACCESS_TOKEN_TOKOCASH = "ACCESS_TOKEN_TOKOCASH";
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String TEMP_EMAIL = "TEMP_EMAIL";
    private static final String EMAIL = "EMAIL";

    private Context context;
    private String email;
    private String shopName;
    private String tempLoginEmail;


    public SessionHandler(Context context) {
        this.context = context;
    }

    public static String getTempLoginSession(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString("temp_login_id", "");

    }

    /**
     * @param context Non Null context
     * @param isLogin flag to determine user is login or not
     * @param user_id valid user id
     */
    @SuppressWarnings("unused")
    public static void setIsLogin(Context context, boolean isLogin, int user_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, user_id + "");
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.apply();
        TrackingUtils.eventPushUserID();
    }

    public static void clearUserData(Context context) {

        logoutInstagram(context);
        InboxCache.ClearCache(context);
        PenjualanCache.ClearCache(context);
        PembelianCache.ClearCache(context);
        ShopSettingCache.ClearCache(context);
        ProductCache.ClearCache(context);
        ShopCache.ClearCache(context);
        ManageProductCache.ClearCache(context);
        InboxTalkCacheManager.ClearCache();
        CacheInteractorImpl messageCacheInteractor = new CacheInteractorImpl();
        messageCacheInteractor.deleteCache();
        new ProductDetailCacheManager().deleteAll();
        new ProductOtherCacheManager().deleteAll();
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, null);
        editor.putString(FULL_NAME, null);
        editor.putString(SHOP_ID, null);
        editor.putBoolean(IS_LOGIN, false);
        editor.putBoolean(IS_MSISDN_VERIFIED, false);
        editor.putString(PHONE_NUMBER, null);
        editor.putString(USER_DATA, null);
        editor.putString(REFRESH_TOKEN, null);
        editor.putString(ACCESS_TOKEN_TOKOCASH, null);
        editor.putString(TOKEN_TYPE, null);
        editor.putString(ACCESS_TOKEN, null);
        editor.apply();
        LocalCacheHandler.clearCache(context, MSISDN_SESSION);
        LocalCacheHandler.clearCache(context, TkpdState.CacheName.CACHE_USER);
        LocalCacheHandler.clearCache(context, DrawerHelper.DRAWER_CACHE);
        LocalCacheHandler.clearCache(context, "ETALASE_ADD_PROD");
        LocalCacheHandler.clearCache(context, "REGISTERED");
        LocalCacheHandler.clearCache(context, TkpdCache.DIGITAL_WIDGET_LAST_ORDER);
        LocalCacheHandler.clearCache(context, TkpdCache.CACHE_RECHARGE_WIDGET_TAB_SELECTION);
        LocalCacheHandler.clearCache(context, TkpdState.CacheName.CACHE_MAIN);
        LocalCacheHandler.clearCache(context, CACHE_PROMOTION_PRODUCT);
        LocalCacheHandler.clearCache(context, CACHE_PHONE_VERIF_TIMER);
        LocalCacheHandler.clearCache(context, TkpdCache.DIGITAL_INSTANT_CHECKOUT_HISTORY);
        LocalCacheHandler.clearCache(context, TkpdCache.DIGITAL_LAST_INPUT_CLIENT_NUMBER);
        LocalCacheHandler.clearCache(context, TOKOCASH_SESSION);
        logoutInstagram(context);
        MethodChecker.removeAllCookies(context);
        LocalCacheHandler.clearCache(context, DrawerHelper.DRAWER_CACHE);


        clearFeedCache();
        AppWidgetUtil.sendBroadcastToAppWidget(context);

        deleteCacheBalanceTokoCash();

        LocalCacheHandler.clearCache(context,TkpdCache.REFERRAL);
        deleteCacheTokoPoint();
    }

    private static void deleteCacheTokoPoint() {
        GlobalCacheManager cacheBalanceTokoCash = new GlobalCacheManager();
        cacheBalanceTokoCash.delete(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
    }


    public void clearToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TOKEN_TYPE, null);
        editor.putString(ACCESS_TOKEN, null);
        editor.apply();
    }

    private static void deleteCacheBalanceTokoCash() {
        GlobalCacheManager cacheBalanceTokoCash = new GlobalCacheManager();
        cacheBalanceTokoCash.delete(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
    }

    private static void logoutInstagram(Context context) {
        if (isV4Login(context) && context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).setContentView(R.layout.activity_webview_general);
            WebView webView = (WebView) ((AppCompatActivity) context).findViewById(R.id.webview);
            WebSettings ws = webView.getSettings();
            ws.setAppCacheEnabled(false);
            ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
            ws.setSaveFormData(false);
            ws.setSavePassword(false);
            webView.clearCache(true);
//            webView.setWebViewClient(new InstagramAuthenticationDialog.InstagramWebViewClient(new OnRequestTokenCodeListener() {
//                @Override
//                public void onSuccess(String code) {
//
//                }
//            }));
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return super.shouldOverrideUrlLoading(view, url);
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    handler.cancel();
                }
            });
            webView.loadUrl("https://instagram.com/accounts/logout/");
            webView.setVisibility(View.GONE);
        }
        if (context.getApplicationContext() instanceof TkpdCoreRouter) {
            ((TkpdCoreRouter) context.getApplicationContext()).removeInstopedToken();
        }
    }

    public static String getLoginID(Context context) {
        String u_id;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, "");
        return u_id;
    }

    public static String getGTMLoginID(Context context) {
        String u_id;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(GTM_LOGIN_ID, "");
        if (TextUtils.isEmpty(u_id)) {
            if (!TextUtils.isEmpty(SessionHandler.getLoginID(context))) {
                SessionHandler.setGTMLoginID(context, SessionHandler.getLoginID(context));
                return SessionHandler.getLoginID(context);
            } else {
                return "";
            }

        } else {
            return u_id;
        }
    }

    public static void setGTMLoginID(Context context, String userID) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(GTM_LOGIN_ID, userID).apply();
    }

    public static void setShopDomain(Context context, String domain) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(SHOP_DOMAIN, domain).apply();
    }

    public static String getShopDomain(Context context) {
        String domain = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        domain = sharedPrefs.getString(SHOP_DOMAIN, "");
        return domain;
    }

    public String getShopName() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        String shopName = sharedPrefs.getString(SHOP_NAME, "");
        return shopName;
    }

    public void setShopId(String shopId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, SHOP_ID, shopId);
        editor.apply();
    }

    public static String getShopID(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        String shopId = sharedPrefs.getString(SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    public String getShopID() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        String shopId = sharedPrefs.getString(SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    public static String getLoginName(Context context) {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    /**
     * Use shop info use case to get gold merchant status
     * @param context
     * @return
     */
    @Deprecated
    public static boolean isGoldMerchant(Context context) {
        Boolean isGoldMerchant = false;
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        int isGM = sharedPrefs.getInt(IS_GOLD_MERCHANT, -1);
        isGoldMerchant = (isGM != (-1) && isGM != 0);
        return isGoldMerchant;
    }

    /**
     * Use shop info use case to get gold merchant status
     * @param context
     * @param goldMerchant
     */
    public static void setGoldMerchant(Context context, int goldMerchant) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        Editor edit = sharedPrefs.edit();
        edit.putInt(IS_GOLD_MERCHANT, goldMerchant);
        edit.apply();
    }

    /**
     * replacement of isLogin for v$ Login
     *
     * @param context
     * @return
     */
    public static boolean isV4Login(Context context) {
        String u_id = null;
        boolean isLogin = false;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        isLogin = sharedPrefs.getBoolean(IS_LOGIN, false);
        return isLogin && u_id != null;
    }

    public static boolean isV2Login(Context context) {
        String u_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        return u_id != null;
    }

    /**
     * login json currently give user id even in security question
     *
     * @param context Non Null context
     * @return always false
     */
//	public static Boolean isLogin (Context context) {
//		String u_id = null;
//		 SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
//		 u_id = sharedPrefs.getString(LOGIN_ID, null);
////		 if (u_id == null) {
//			 return false;
////		 }
////		 return true;
//	}
    public static void setUserAvatarUri(Context context, String avatar_uri) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(USER_AVATAR_URI, avatar_uri);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public static String getUserAvatarUri(Context context) {
        String avatar_uri = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        avatar_uri = sharedPrefs.getString(USER_AVATAR_URI, null);
        if (avatar_uri == null) {
            return null;
        }
        return avatar_uri;
    }

    @SuppressWarnings("unused")
    public static void setGridPref(Context context, int Pref) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putInt("STATE", Pref);
        editor.apply();
    }

    @SuppressWarnings("unused")
    public static int getGridPref(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        return sharedPrefs.getInt("STATE", 1);
    }

    public static boolean isFirstTimeUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME_USER_NEW_ONBOARDING, true);
    }

    public static boolean setFirstTimeUser(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER, isFirstTime).commit();
    }

    public static boolean setFirstTimeUserNewOnboard(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER_NEW_ONBOARDING, isFirstTime).commit();
    }

    public static boolean isMsisdnVerified() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        return cache.getBoolean(IS_MSISDN_VERIFIED, false);
    }

    public static void setIsMSISDNVerified(boolean verified) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putBoolean(IS_MSISDN_VERIFIED, verified);
        cache.applyEditor();
    }

    public static String getPhoneNumber() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        return cache.getString(PHONE_NUMBER, "");
    }

    public static void setPhoneNumber(String userPhone) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putString(PHONE_NUMBER, userPhone);
        cache.applyEditor();
    }

    public static String getTempPhoneNumber(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_PHONE_NUMBER, "");
    }

    public void setTempLoginEmail(String tempLoginEmail) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_EMAIL, tempLoginEmail);
        editor.apply();
    }

    public String getTempEmail() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TEMP_EMAIL, "");
    }

    public void setEmail(String email) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(EMAIL, "");
    }

    public static String getAccessToken() {
        SharedPreferences sharedPrefs = MainApplication.getAppContext().getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public String getAuthAccessToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public String getAuthRefreshToken() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public static String getRefreshTokenIV(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, KEY_IV);
    }

    public static boolean isFirstTimeAskedPermissionStorage(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_FIRST_TIME_STORAGE, true);
    }

    public static boolean isUserHasShop(Context context) {
        String shopID = SessionHandler.getShopID(context);
        return isShopIdValid(shopID);
    }

    public boolean isUserHasShop() {
        String shopID = getShopID();
        return isShopIdValid(shopID);
    }

    private static boolean isShopIdValid(String shopId) {
        return !TextUtils.isEmpty(shopId) && !DEFAULT_EMPTY_SHOP_ID.equals(shopId);
    }

    public static String getUUID(Context context) {
        return new LocalCacheHandler(context, LOGIN_UUID_KEY)
                .getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    private static void clearFeedCache() {
        new FeedDbManager().delete();
        new RecentProductDbManager().delete();
        new TopAdsDbManager().delete();
    }

    public void setTempLoginSession(String u_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString("temp_login_id", u_id);
        editor.apply();
    }

    public void setLoginSession(boolean isLogin, String u_id, String u_name, String shop_id, boolean isMsisdnVerified, String shopName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(LOGIN_ID, u_id);
        editor.putString(GTM_LOGIN_ID, u_id);
        editor.putString(FULL_NAME, u_name);
        editor.putString(SHOP_ID, shop_id);
        editor.putString(SHOP_NAME, shopName);
        editor.putBoolean(IS_MSISDN_VERIFIED, isMsisdnVerified);
        editor.apply();
        TrackingUtils.eventPushUserID();
        Crashlytics.setUserIdentifier(u_id);

        BranchSdkUtils.sendLoginEvent(u_id);

        //return status;
    }

    public void Logout(Context context) {
        if (context != null && context instanceof AppCompatActivity && context instanceof onLogoutListener) {
            if (((AppCompatActivity) context).getFragmentManager().findFragmentByTag(DialogLogoutFragment.FRAGMENT_TAG) == null) {
                DialogLogoutFragment dialogLogoutFragment = new DialogLogoutFragment();
                dialogLogoutFragment.show(((AppCompatActivity) context).getFragmentManager(), DialogLogoutFragment.FRAGMENT_TAG);
                Crashlytics.setUserIdentifier("");
            }
        }

        //Set logout to Branch.io sdk,
        BranchSdkUtils.sendLogoutEvent();
    }

    private void clearUserData() {
        clearUserData(context);
    }

    public void forceLogout() {
        Crashlytics.log(1, "FORCE LOGOUT",
                "User Id: " + getLoginID(context) +
                        " Device Id: " + GCMHandler.getRegistrationId(context));
        PasswordGenerator.clearTokenStorage(context);
        TrackingUtils.eventMoEngageLogoutUser();
        clearUserData();
    }

    public boolean isV4Login() {
        return isV4Login(context);
    }

    public String getLoginID() {
        return getLoginID(context);
    }

    public String getLoginName() {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    @SuppressWarnings("unused")
    public void setLoginName(String u_name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(FULL_NAME, u_name);
        editor.apply();
    }

    public void setGoldMerchant(int goldMerchant) {
        SharedPreferences sharedPrefs = this.context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        Editor edit = sharedPrefs.edit();
        edit.putInt(IS_GOLD_MERCHANT, goldMerchant);
        edit.apply();
    }

    public void setTempPhoneNumber(String userPhone) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_PHONE_NUMBER, userPhone);
        editor.apply();
    }

    public void setTempLoginName(String userPhone) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(TEMP_NAME, userPhone);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        setToken(accessToken, tokenType);
        saveToSharedPref(editor, REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, ACCESS_TOKEN, accessToken);
        saveToSharedPref(editor, TOKEN_TYPE, tokenType);
        editor.apply();
    }

    public void setWalletRefreshToken(String walletRefreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, WALLET_REFRESH_TOKEN, walletRefreshToken);
        editor.apply();
    }

    private void saveToSharedPref(Editor editor, String key, String value) {
        if (value != null) {
            editor.putString(key, value);
        }
    }

    public static String getAccessToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public Context getActiveContext() {
        return this.context;
    }

    public String getWalletRefreshToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(WALLET_REFRESH_TOKEN, "");
    }

    public String getTokenType(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TOKEN_TYPE, "");
    }

    public String getUUID() {
        return new LocalCacheHandler(context, LOGIN_UUID_KEY)
                .getString(UUID_KEY, DEFAULT_UUID_VALUE);
    }

    public void setTokenTokoCash(String token) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TOKOCASH_SESSION);
        localCacheHandler.putString(ACCESS_TOKEN_TOKOCASH, token);
        localCacheHandler.applyEditor();
    }

    public static String getAccessTokenTokoCash() {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), TOKOCASH_SESSION);
        return localCacheHandler.getString(ACCESS_TOKEN_TOKOCASH, "");
    }

    public void setUUID(String uuid) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(),
                LOGIN_UUID_KEY);
        String prevUUID = cache.getString(UUID_KEY, "");
        String currUUID;
        if (prevUUID.equals("")) {
            currUUID = uuid;
        } else {
            currUUID = prevUUID + "*~*" + uuid;
        }
        cache.putString(UUID_KEY, currUUID);
        cache.applyEditor();
    }

    public String getTempLoginSession() {
       return getTempLoginSession(context);
    }

    public interface onLogoutListener {
        void onLogout(Boolean success);
    }
}
