package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.text.TextUtils;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.session.DialogLogoutFragment;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;

import static com.tokopedia.user.session.Constants.*;

@Deprecated
/**
 * Please use {@link com.tokopedia.user.session.UserSession} instead.
 */
public class SessionHandler {


    private Context context;


    public SessionHandler(Context context) {
        this.context = context;
    }

    public static String getTempLoginSession(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString("temp_login_id", "");

    }

    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String USER_DATA = "USER_DATA";
    private static final String USER_SCOPE = "USER_SCOPE";
    private static final String ACCESS_TOKEN_TOKOCASH = "ACCESS_TOKEN_TOKOCASH";
    private static final String MSISDN_SESSION = "MSISDN_SESSION";
    public static final String CACHE_PROMOTION_PRODUCT = "CACHE_PROMOTION_PRODUCT";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String TOKOCASH_SESSION = "TOKOCASH_SESSION";
    private static final String KEY_PROFILE_BUYER = "KEY_PROFILE_BUYER";
    private static final String KEY_AFFILIATE = "KEY_AFFILIATE";
    public static final String INSTAGRAM_CACHE_KEY = "instagram_cache_key";
    private static final String KEY_IV = "tokopedia1234567";

    private static UserSession userSession = null;

    public static UserSession getUserSession(Context context) {
        if(userSession == null){
            userSession = new UserSession(context);
        }
        return userSession;
    }

    public static void clearUserData(Context context) {

        logoutInstagram(context);
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, null);
        editor.putString(SHOP_DOMAIN, null);
        editor.putString(SHOP_ID, null);
        editor.putString(SHOP_NAME, null);
        editor.putBoolean(IS_LOGIN, false);
        editor.putBoolean(IS_MSISDN_VERIFIED, false);
        editor.putString(PHONE_NUMBER, null);
        editor.putString(USER_DATA, null);
        editor.putString(REFRESH_TOKEN, null);
        editor.putString(USER_SCOPE, null);
        editor.putString(ACCESS_TOKEN_TOKOCASH, null);
        editor.putString(TOKEN_TYPE, null);
        editor.putString(ACCESS_TOKEN, null);
        editor.putBoolean(HAS_PASSWORD, true);
        editor.putString(PROFILE_PICTURE, null);

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
        LocalCacheHandler.clearCache(context, KEY_PROFILE_BUYER);
        LocalCacheHandler.clearCache(context, KEY_AFFILIATE);
        logoutInstagram(context);
        try {
            MethodChecker.removeAllCookies(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalCacheHandler.clearCache(context, DrawerHelper.DRAWER_CACHE);

        AppWidgetUtil.sendBroadcastToAppWidget(context);

        deleteCacheBalanceTokoCash();

        LocalCacheHandler.clearCache(context, TkpdCache.REFERRAL);
        deleteCacheTokoPoint();
        deleteCacheExploreData();
    }

    private static void deleteCacheTokoPoint() {
        GlobalCacheManager cacheBalanceTokoCash = new GlobalCacheManager();
        cacheBalanceTokoCash.delete(TkpdCache.Key.KEY_TOKOPOINT_DRAWER_DATA);
    }

    private static void deleteCacheExploreData() {
        GlobalCacheManager cacheExploreData = new GlobalCacheManager();
        cacheExploreData.delete(TkpdCache.Key.EXPLORE_DATA_CACHE);
    }

    private static void deleteCacheBalanceTokoCash() {
        PersistentCacheManager.instance.delete(TkpdCache.Key.KEY_TOKOCASH_BALANCE_CACHE);
    }

    private static void logoutInstagram(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, INSTAGRAM_CACHE_KEY);
        localCacheHandler.clearCache(INSTAGRAM_CACHE_KEY);
        if (isV4Login(context) && context instanceof AppCompatActivity) {
            ((AppCompatActivity) context).setContentView(R.layout.activity_webview_general);
            WebView webView = (WebView) ((AppCompatActivity) context).findViewById(R.id.webview);
            WebSettings ws = webView.getSettings();
            ws.setAppCacheEnabled(false);
            ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
            ws.setSaveFormData(false);
            ws.setSavePassword(false);
            webView.clearCache(true);
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
    }

    public static String getLoginID(Context context) {
        return getUserSession(MainApplication.getAppContext()).getUserId();
    }

    public static String getGTMLoginID(Context context) {
        return getUserSession(MainApplication.getAppContext()).getUserId();
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

    public static String getShopID(Context context) {
        return getUserSession(MainApplication.getAppContext()).getShopId();
    }

    public void setShopName(String name) {
        getUserSession(MainApplication.getAppContext()).setShopName(name);
    }

    public static String getShopName(Context context) {
        return getUserSession(MainApplication.getAppContext()).getShopName();
    }

    public static String getLoginName(Context context) {
        return getUserSession(MainApplication.getAppContext()).getName();
    }

    public static boolean isGoldMerchant(Context context) {
        return getUserSession(MainApplication.getAppContext()).isGoldMerchant();
    }

    public static void setGoldMerchant(Context context, int goldMerchant) {
        getUserSession(MainApplication.getAppContext()).setIsGoldMerchant(goldMerchant != -1 && goldMerchant != 0);
    }

    public static boolean isV4Login(Context context) {
        return getUserSession(MainApplication.getAppContext()).isLoggedIn();
    }

    public static boolean isMsisdnVerified() {
        return getUserSession(MainApplication.getAppContext()).isMsisdnVerified();
    }

    public static void setIsMSISDNVerified(boolean verified) {
        getUserSession(MainApplication.getAppContext()).setIsMSISDNVerified(verified);
    }

    public static String getPhoneNumber() {
        return getUserSession(MainApplication.getAppContext()).getPhoneNumber();
    }

    public static void setPhoneNumber(String userPhone) {
        getUserSession(MainApplication.getAppContext()).setTempPhoneNumber(userPhone);
    }

    public static String getTempPhoneNumber(Context context) {
        return getUserSession(MainApplication.getAppContext()).getTempPhoneNumber();
    }

    public static String getAccessToken() {
        return getUserSession(MainApplication.getAppContext()).getAccessToken();
    }

    public static String getRefreshTokenIV(Context context) {
        return getUserSession(MainApplication.getAppContext()).getFreshToken();
    }

    public static boolean isUserHasShop(Context context) {
        return getUserSession(context).hasShop();
    }

    public static String getAccessToken(Context context) {
        return getUserSession(MainApplication.getAppContext()).getAccessToken();
    }

    public String getShopName() {
        return getUserSession(MainApplication.getAppContext()).getShopName();
    }

    public void setShopId(String shopId) {
        getUserSession(MainApplication.getAppContext()).setShopId(shopId);
    }

    public String getShopID() {
        return getUserSession(MainApplication.getAppContext()).getShopId();
    }

    public void setTempLoginEmail(String tempLoginEmail) {
        getUserSession(MainApplication.getAppContext()).setTempLoginEmail(tempLoginEmail);
    }

    public String getTempEmail() {
        return getUserSession(MainApplication.getAppContext()).getTempEmail();
    }

    public String getEmail() {
        return getUserSession(MainApplication.getAppContext()).getEmail();
    }

    public void setEmail(String email) {
        getUserSession(MainApplication.getAppContext()).setEmail(email);
    }

    public String getAuthRefreshToken() {
        return getUserSession(MainApplication.getAppContext()).getFreshToken();
    }

    public boolean isUserHasShop() {
        return getUserSession(MainApplication.getAppContext()).hasShop();
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
        TrackApp.getInstance().getGTM()
                .pushUserId(getGTMLoginID(context));
        if (!GlobalConfig.DEBUG) Crashlytics.setUserIdentifier(u_id);

        UserData userData = new UserData();
        userData.setUserId(u_id);

        LinkerManager.getInstance().sendEvent(LinkerUtils.createGenericRequest(LinkerConstants.EVENT_USER_IDENTITY,
                userData));

        //return status;
    }

    public void Logout(Context context) {
        if (context != null && context instanceof AppCompatActivity && context instanceof onLogoutListener) {
            if (((AppCompatActivity) context).getFragmentManager().findFragmentByTag(DialogLogoutFragment.FRAGMENT_TAG) == null) {
                DialogLogoutFragment dialogLogoutFragment = new DialogLogoutFragment();
                dialogLogoutFragment.show(((AppCompatActivity) context).getFragmentManager(), DialogLogoutFragment.FRAGMENT_TAG);
                if (!GlobalConfig.DEBUG) Crashlytics.setUserIdentifier("");
            }
        }

        //Set logout to Branch.io sdk,
        LinkerManager.getInstance().sendEvent(
                LinkerUtils.createGenericRequest(LinkerConstants.EVENT_LOGOUT_VAL,
                        null)
        );
    }

    private void clearUserData() {
        clearUserData(context);
    }

    public void forceLogout() {
        if (context != null) {
            PasswordGenerator.clearTokenStorage(context);
            TrackApp.getInstance().getMoEngage().logoutEvent();
        }
        clearUserData();
    }

    public boolean isV4Login() {
        return isV4Login(context);
    }

    public String getLoginID() {
        return getLoginID(context);
    }

    public String getLoginName() {
        return getUserSession(MainApplication.getAppContext()).getName();
    }

    public void setGoldMerchant(int goldMerchant) {
        setGoldMerchant(goldMerchant != -1 && goldMerchant != 0);
    }

    public void setGoldMerchant(boolean isGoldMerchant) {
        getUserSession(MainApplication.getAppContext()).setIsGoldMerchant(isGoldMerchant);
    }

    public void setTempPhoneNumber(String userPhone) {
        getUserSession(MainApplication.getAppContext()).setTempPhoneNumber(userPhone);
    }

    public void setTempLoginName(String userPhone) {
        getUserSession(MainApplication.getAppContext()).setTempLoginName(userPhone);
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        getUserSession(MainApplication.getAppContext()).setToken(accessToken, tokenType, refreshToken);
    }

    public void setToken(String accessToken, String tokenType) {
        getUserSession(MainApplication.getAppContext()).setToken(accessToken, tokenType);
    }

    private void saveToSharedPref(Editor editor, String key, String value) {
        if (value != null) {
            editor.putString(key, value);
        }
    }

    public String getTokenType(Context context) {
        return getUserSession(MainApplication.getAppContext()).getTokenType();
    }

    public String getUUID() {
        return new LocalCacheHandler(context, LOGIN_UUID_KEY)
                .getString(UUID_KEY, "");
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

    public String getProfilePicture() {
        return getUserSession(MainApplication.getAppContext()).getProfilePicture();
    }

    public void setProfilePicture(String profilePicture) {
        getUserSession(MainApplication.getAppContext()).setProfilePicture(profilePicture);
    }

    public void setTempLoginSession(String u_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString("temp_login_id", u_id);
        editor.apply();
    }

    public interface onLogoutListener {
        void onLogout(Boolean success);
    }

    public void setHasPassword(boolean hasPassword) {
        getUserSession(MainApplication.getAppContext()).setHasPassword(hasPassword);
    }

    public boolean isHasPassword() {
        return getUserSession(MainApplication.getAppContext()).hasPassword();
    }
}
