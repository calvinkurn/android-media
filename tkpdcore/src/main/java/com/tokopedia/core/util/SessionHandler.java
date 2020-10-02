package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.cachemanager.PersistentCacheManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core2.R;
import com.tokopedia.linker.LinkerConstants;
import com.tokopedia.linker.LinkerManager;
import com.tokopedia.linker.LinkerUtils;
import com.tokopedia.linker.model.UserData;
import com.tokopedia.track.TrackApp;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import timber.log.Timber;

@Deprecated
/**
 * Please use {@link com.tokopedia.user.session.UserSession} instead.
 */
public class SessionHandler {
    public static final String DRAWER_CACHE = "DRAWER_CACHE";
    public static final String DEFAULT_EMPTY_SHOP_ID = "0";
    public static final String CACHE_PROMOTION_PRODUCT = "CACHE_PROMOTION_PRODUCT";
    private static final String DEFAULT_EMPTY_SHOP_ID_ON_PREF = "-1";
    private static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    private static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String GTM_LOGIN_ID = "GTM_LOGIN_ID";
    private static final String SHOP_ID = "SHOP_ID";
    private static final String STATE_BROWSE = "STATE_BROWSE";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    protected static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String USER_AVATAR_URI = "USER_AVATAR_URI";
    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String MSISDN_SESSION = "MSISDN_SESSION";
    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String REFRESH_TOKEN_KEY = "REFRESH_TOKEN_KEY";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String USER_SCOPE = "USER_SCOPE";
    private static final String CACHE_PHONE_VERIF_TIMER = "CACHE_PHONE_VERIF_TIMER";
    private static final String USER_DATA = "USER_DATA";
    private static final String KEY_IV = "tokopedia1234567";
    private static final String TOKOCASH_SESSION = "TOKOCASH_SESSION";
    private static final String ACCESS_TOKEN_TOKOCASH = "ACCESS_TOKEN_TOKOCASH";
    private static final String SHOP_NAME = "SHOP_NAME";
    private static final String EMAIL = "EMAIL";
    private static final String PROFILE_PICTURE = "PROFILE_PICTURE";
    private static final String HAS_PASSWORD = "HAS_PASSWORD";
    private static final String KEY_PROFILE_BUYER = "KEY_PROFILE_BUYER";
    private static final String KEY_AFFILIATE = "KEY_AFFILIATE";
    public static final String INSTAGRAM_CACHE_KEY = "instagram_cache_key";

    private Context context;


    public SessionHandler(Context context) {
        this.context = context;
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
        TrackApp.getInstance().getGTM()
                .pushUserId(getGTMLoginID(context));
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
        LocalCacheHandler.clearCache(context, DRAWER_CACHE);
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
            removeAllCookies(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LocalCacheHandler.clearCache(context, DRAWER_CACHE);

        AppWidgetUtil.sendBroadcastToAppWidget(context);

        deleteCacheBalanceTokoCash();

        LocalCacheHandler.clearCache(context, TkpdCache.REFERRAL);
        deleteCacheTokoPoint();
        deleteCacheExploreData();
    }

    private static void removeAllCookies(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Timber.d("Success Clear Cookie");
                }
            });
        } else {
            CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(context);
            cookieSyncMngr.startSync();
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
        }
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
        UserSessionInterface userSession = new UserSession(context);
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, INSTAGRAM_CACHE_KEY);
        localCacheHandler.clearCache(INSTAGRAM_CACHE_KEY);
        if (userSession.isLoggedIn() && context instanceof AppCompatActivity) {
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

    public void setShopName(String name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString(SHOP_NAME, name).apply();
    }

    /**
     * Use shop info use case to get gold merchant status
     *
     * @param context
     * @return
     */
    @Deprecated
    public static boolean isGoldMerchant(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getBoolean(IS_GOLD_MERCHANT, false);
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

    public static void setPhoneNumber(String userPhone) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putString(PHONE_NUMBER, userPhone);
        cache.applyEditor();
    }

    public static String getRefreshTokenIV(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN_KEY, KEY_IV);
    }

    public void setShopId(String shopId) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, SHOP_ID, shopId);
        editor.apply();
    }

    public String getShopID() {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        String shopId = sharedPrefs.getString(SHOP_ID, DEFAULT_EMPTY_SHOP_ID);
        if (DEFAULT_EMPTY_SHOP_ID_ON_PREF.equals(shopId)) {
            shopId = DEFAULT_EMPTY_SHOP_ID;
        }
        return shopId;
    }

    public void setEmail(String email) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(EMAIL, email);
        editor.apply();
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

    @SuppressWarnings("unused")
    public void setLoginName(String u_name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(FULL_NAME, u_name);
        editor.apply();
    }

    public void setGoldMerchant(int goldMerchant) {
        setGoldMerchant(goldMerchant != -1 && goldMerchant != 0);
    }

    public void setGoldMerchant(boolean isGoldMerchant) {
        SharedPreferences sharedPrefs = this.context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor edit = sharedPrefs.edit();
        edit.putBoolean(IS_GOLD_MERCHANT, isGoldMerchant);
        edit.apply();
    }

    public void setToken(String accessToken, String tokenType, String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        setToken(accessToken, tokenType);
        saveToSharedPref(editor, REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    public void setScope(String scopeName) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, USER_SCOPE, scopeName);
        editor.apply();
    }

    public void setToken(String accessToken, String tokenType) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor, ACCESS_TOKEN, accessToken);
        saveToSharedPref(editor, TOKEN_TYPE, tokenType);
        editor.apply();
    }

    private void saveToSharedPref(Editor editor, String key, String value) {
        if (value != null) {
            editor.putString(key, value);
        }
    }
}