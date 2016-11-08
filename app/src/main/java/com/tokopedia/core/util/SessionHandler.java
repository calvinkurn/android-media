package com.tokopedia.core.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.tokopedia.core.database.manager.ProductDetailCacheManager;
import com.tokopedia.core.database.manager.ProductOtherCacheManager;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.inboxreputation.interactor.CacheInboxReputationInteractorImpl;
import com.tokopedia.core.inboxreputation.interactor.InboxReputationCacheManager;
import com.tokopedia.core.instoped.presenter.InstagramMediaPresenterImpl;
import com.tokopedia.core.message.interactor.CacheInteractorImpl;
import com.tokopedia.core.msisdn.fragment.MsisdnVerificationFragment;
import com.tokopedia.core.prototype.InboxCache;
import com.tokopedia.core.prototype.ManageProductCache;
import com.tokopedia.core.prototype.PembelianCache;
import com.tokopedia.core.prototype.PenjualanCache;
import com.tokopedia.core.prototype.ProductCache;
import com.tokopedia.core.prototype.ShopCache;
import com.tokopedia.core.prototype.ShopSettingCache;
import com.tokopedia.core.session.DialogLogoutFragment;
import com.tokopedia.core.session.model.RegisterViewModel;
import com.tokopedia.core.session.presenter.RegisterNext;
import com.tokopedia.core.talk.inboxtalk.database.InboxTalkCacheManager;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;

import java.util.Arrays;

public class SessionHandler {
    private static final String IS_LOGIN = "IS_LOGIN";
    private static final String LOGIN_ID = "LOGIN_ID";
    private static final String SHOP_ID = "SHOP_ID";
    private static final String STATE_BROWSE = "STATE_BROWSE";
    private static final String FULL_NAME = "FULL_NAME";
    private static final String IS_GOLD_MERCHANT = "IS_GOLD_MERCHANT";
    private static final String LOGIN_SESSION = "LOGIN_SESSION";
    private static final String USER_AVATAR_URI = "USER_AVATAR_URI";
    private static final String SHOP_DOMAIN = "SHOP_DOMAIN";
    private static final String IS_FIRST_TIME_USER = "IS_FIRST_TIME";
    public static final String SAVE_REAL = "SAVE_REAL";
    public static final String IS_MSISDN_VERIFIED = "IS_MSISDN_VERIFIED";
    public static final String DONT_REMIND_LATER = "DONT_REMIND_LATER";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    private static final String MSISDN_SESSION = "MSISDN_SESSION";

    private static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    private static final String TOKEN_TYPE = "TOKEN_TYPE";
    private static final String IS_FIRST_TIME_STORAGE = "IS_FIRST_TIME_STORAGE";

    private Context context;


    public interface onLogoutListener {
        void onLogout(Boolean success);
    }

    public SessionHandler(Context context) {
        this.context = context;
    }

    public void setTempLoginSession(String u_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString("temp_login_id", u_id);
        editor.apply();
    }

    public static String getTempLoginSession(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString("temp_login_id", "");

    }

    public void SetLoginSession(String u_id, String u_name, String shop_id, boolean isMsisdnVerified) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, u_id);
        editor.putString(FULL_NAME, u_name);
        editor.putString(SHOP_ID, shop_id);
        editor.putBoolean(IS_MSISDN_VERIFIED, isMsisdnVerified);
        editor.commit();
        TrackingUtils.eventPushUserID();
        Crashlytics.setUserIdentifier(u_id);
        //return status;
    }

    public void SetLoginSession(boolean isLogin, String u_id, String u_name, String shop_id, boolean isMsisdnVerified) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(LOGIN_ID, u_id);
        editor.putString(FULL_NAME, u_name);
        editor.putString(SHOP_ID, shop_id);
        editor.putBoolean(IS_MSISDN_VERIFIED,isMsisdnVerified);
        editor.commit();
        TrackingUtils.eventPushUserID();
        Crashlytics.setUserIdentifier(u_id);
        //return status;
    }

    /**
     * @param context Non Null context
     * @param isLogin flag to determine user is login or not
     * @param user_id valid user id
     */
    public static void setIsLogin(Context context, boolean isLogin, int user_id) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(LOGIN_ID, user_id + "");
        editor.putBoolean(IS_LOGIN, isLogin);
        editor.commit();
        TrackingUtils.eventPushUserID();
    }

    public void Logout(Context context) {
        if (context != null && context instanceof AppCompatActivity && context instanceof onLogoutListener) {
            if (((AppCompatActivity) context).getFragmentManager().findFragmentByTag(DialogLogoutFragment.FRAGMENT_TAG) == null) {
                DialogLogoutFragment dialogLogoutFragment = new DialogLogoutFragment();
                dialogLogoutFragment.show(((AppCompatActivity) context).getFragmentManager(), DialogLogoutFragment.FRAGMENT_TAG);
                Crashlytics.setUserIdentifier("");
            }
        }
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
        editor.commit();
        LocalCacheHandler.clearCache(context, MSISDN_SESSION);
        LocalCacheHandler.clearCache(context, TkpdState.CacheName.CACHE_USER);
        LocalCacheHandler.clearCache(context, TkpdCache.NOTIFICATION_DATA);
        LocalCacheHandler.clearCache(context, "ETALASE_ADD_PROD");
        LocalCacheHandler.clearCache(context, "REGISTERED");
        LocalCacheHandler.clearCache(context, TkpdState.CacheName.CACHE_MAIN);
        LocalCacheHandler.clearCache(context, MsisdnVerificationFragment.PHONE_VERIFICATION);
        CacheInboxReputationInteractorImpl reputationCache = new CacheInboxReputationInteractorImpl();
        reputationCache.deleteCache();
        InboxReputationCacheManager reputationDetailCache = new InboxReputationCacheManager();
        reputationDetailCache.deleteAll();
        logoutInstagram(context);

    }

    private static void logoutInstagram(Context context) {
        if(isV4Login(context) && context instanceof AppCompatActivity) {
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
            webView.setWebViewClient(new WebViewClient(){
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
        InstagramMediaPresenterImpl.removeToken();
    }

    private void clearUserData() {
        clearUserData(context);
    }

    public void forceLogout() {
        Crashlytics.log(1, "FORCE LOGOUT",
                "User Id: "+getLoginID(context)+
                " Device Id: "+GCMHandler.getRegistrationId(context));
        PasswordGenerator.clearTokenStorage(context);
        GCMHandler.clearRegistrationId(context);
        clearUserData();
    }

    public boolean isV4Login() {
        return isV4Login(context);
    }

    public String getLoginID() {
        String u_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, null);
        return u_id;
    }

    public static String getLoginID(Context context) {
        String u_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_id = sharedPrefs.getString(LOGIN_ID, "");
        return u_id;
    }

    public static void setShopDomain(Context context, String domain) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putString("SHOP_DOMAIN", domain);
        sharedPrefs.edit().commit();
    }

    public static String getShopDomain(Context context) {
        String domain = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        domain = sharedPrefs.getString("SHOP_DOMAIN", "");
        return domain;
    }

    public String getShopID() {
        String shop_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        shop_id = sharedPrefs.getString(SHOP_ID, "0");
        return shop_id;
    }

    public static String getShopID(Context context) {
        String shop_id = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        shop_id = sharedPrefs.getString(SHOP_ID, "0");
        return shop_id;
    }

    public String getLoginName() {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    public void setLoginName(String u_name) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putString(FULL_NAME, u_name);
        editor.commit();
    }

    public static String getLoginName(Context context) {
        String u_name = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        u_name = sharedPrefs.getString(FULL_NAME, null);
        return u_name;
    }

    public static boolean isGoldMerchant(Context context){
        Boolean isGoldMerchant = false;
        SharedPreferences sharedPrefs = context.getSharedPreferences(SHOP_DOMAIN, Context.MODE_PRIVATE);
        int isGM = sharedPrefs.getInt(IS_GOLD_MERCHANT, -1);
        isGoldMerchant = (isGM != (-1) && isGM != 0);
        return isGoldMerchant;
    }

    public static void setGoldMerchant(Context context, int goldMerchant){
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
    @Deprecated
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
        editor.commit();
    }

    public static String getUserAvatarUri(Context context) {
        String avatar_uri = null;
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        avatar_uri = sharedPrefs.getString(USER_AVATAR_URI, null);
        if (avatar_uri == null) {
            return null;
        }
        return avatar_uri;
    }

    public static void setGridPref(Context context, int Pref) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        editor.putInt("STATE", Pref);
        editor.commit();
    }

    public static int getGridPref(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(STATE_BROWSE, Context.MODE_PRIVATE);
        return sharedPrefs.getInt("STATE", 1);
    }

    public static void saveRegisterNext(Context context, String fullName, String phoneNumber, int gender, String ttl) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(SAVE_REAL, true);
        editor.putString(RegisterNext.FULLNAME, fullName);
        editor.putString(RegisterNext.PHONE, phoneNumber);
        editor.putInt(RegisterNext.GENDER, gender);
        editor.putString(RegisterNext.BIRTHDAY, ttl);
        editor.apply();
    }

    @Deprecated
    public static void saveRegisterNext(Context context, boolean isChecked, String password, String confirmPassword, String email) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        Editor editor = sharedPreferences.edit();
        editor.putBoolean(SAVE_REAL, true);
        editor.putBoolean(RegisterNext.CHECK_T_AND_COND_STRING, isChecked);
        editor.putString(RegisterNext.PASSWORD, password);
        editor.putString(RegisterNext.CONFIRM_PASSWORD, confirmPassword);
        editor.putString(RegisterNext.EMAIL, email);
        editor.apply();
    }

    public static void deleteRegisterNext(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        sharedPreferences.edit().clear().apply();
    }

    public static RegisterViewModel getRegisterNext(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        RegisterViewModel registerViewModel = new RegisterViewModel();
        registerViewModel.setmName(sharedPreferences.getString(RegisterNext.FULLNAME, ""));
        registerViewModel.setmPhone(sharedPreferences.getString(RegisterNext.PHONE, ""));
        registerViewModel.setmGender(sharedPreferences.getInt(RegisterNext.GENDER, 0));
        String birthDay = sharedPreferences.getString(RegisterNext.BIRTHDAY, "");
        String[] pecah = birthDay.split("/");
        Log.d("MNORMANSYAH", "slice : " + Arrays.toString(pecah));
        if (pecah.length == 3) {
            registerViewModel.setmDateDay(Integer.parseInt(pecah[0].trim()));
            registerViewModel.setmDateMonth(Integer.parseInt(pecah[1].trim()));
            registerViewModel.setmDateYear(Integer.parseInt(pecah[2].trim()));
        } else {
            return null;
        }
        return registerViewModel;
    }

    public static boolean isRegisterNextEnter(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("REGISTER_NEXT", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE_REAL, false);
    }

    public static boolean isFirstTimeUser(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(IS_FIRST_TIME_USER, true);
    }

    public static boolean setFirstTimeUser(Context context, boolean isFirstTime) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPreferences.edit().putBoolean(IS_FIRST_TIME_USER, isFirstTime).commit();
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

    public static void setDontRemindLater(boolean isChecked) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), MSISDN_SESSION);
        cache.putBoolean(DONT_REMIND_LATER, isChecked);
        cache.setExpire(86400);
        cache.applyEditor();
    }

    public static boolean canRemind() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), MSISDN_SESSION);
        return !cache.getBoolean(DONT_REMIND_LATER, false) || cache.isExpired();
    }

    public static void setPhoneNumber(String userPhone) {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        cache.putString(PHONE_NUMBER, userPhone);
        cache.applyEditor();
    }

    public static String getPhoneNumber() {
        LocalCacheHandler cache = new LocalCacheHandler(MainApplication.getAppContext(), LOGIN_SESSION);
        return cache.getString(PHONE_NUMBER, "");
    }


    public void setToken(String accessToken, String tokenType, String refreshToken) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        Editor editor = sharedPrefs.edit();
        saveToSharedPref(editor,ACCESS_TOKEN, accessToken);
        saveToSharedPref(editor,TOKEN_TYPE, tokenType);
        saveToSharedPref(editor,REFRESH_TOKEN, refreshToken);
        editor.apply();
    }

    private void saveToSharedPref(Editor editor, String key, String value) {
        if(value!=null) {
            editor.putString(key, value);
        }
    }

    public String getAccessToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(ACCESS_TOKEN, "");
    }

    public String getTokenType(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(TOKEN_TYPE, "");
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        return sharedPrefs.getString(REFRESH_TOKEN, "");
    }

    public static boolean isFirstTimeAskedPermissionStorage(Context context) {
        SharedPreferences sharedPrefs = context.getSharedPreferences(LOGIN_SESSION, Context.MODE_PRIVATE);
        boolean isFirst = sharedPrefs.getBoolean(IS_FIRST_TIME_STORAGE, true);
        return isFirst;
    }
}
