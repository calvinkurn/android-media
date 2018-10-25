package com.tokopedia.core.deprecated;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.utils.RouterUtils;
import com.tokopedia.core.var.TkpdCache;

/**
 * most of the codes is no-op that need to defined at the application.
 */
public class SessionHandler implements UserSession {
    private final GCMHandler gcmHandler;
    protected Context context;
    private final SessionHandler sessionHandler;

    public SessionHandler(Context context) {
        this.context = context;

        /**
         * get implementation here using router
         */
        sessionHandler = RouterUtils.getRouterFromContext(context).legacySessionHandler();
        gcmHandler = RouterUtils.getRouterFromContext(context).legacyGCMHandler();
    }

    public String getLoginName(){
        return sessionHandler.getLoginName();
    }
    public String getGTMLoginID(){
        return sessionHandler.getGTMLoginID();
    }
    public String getShopID(){
        return sessionHandler.getShopID();
    }
    public String getLoginID(){
        return sessionHandler.getLoginID();
    }
    public boolean isUserHasShop(){
        return sessionHandler.isUserHasShop();
    }

    public String getAdsId(){
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);
        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
            return adsId;
        }else{
            return null;
        }
    }

    public String getAndroidId(){
        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ANDROID_ID);
        String androidId = localCacheHandler.getString(TkpdCache.Key.KEY_ANDROID_ID);
        if (androidId != null && !"".equalsIgnoreCase(androidId.trim())) {
            return androidId;
        } else {
            String android_id = AuthUtil.md5(Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID));
            if (!TextUtils.isEmpty(android_id)) {
                localCacheHandler.putString(TkpdCache.Key.KEY_ANDROID_ID, android_id);
                localCacheHandler.applyEditor();
            }
            return android_id;
        }
    }

    public boolean isV4Login(){
        return sessionHandler.isV4Login();
    }

    public String getPhoneNumber() {
        return sessionHandler.getPhoneNumber();
    }

    public String getEmail() {
        return sessionHandler.getEmail();
    }

    public String getRefreshToken() {
        return sessionHandler.getRefreshToken();
    }

    public String getAccessToken() {
        return sessionHandler.getAccessToken();
    }

    @Override
    public String getFreshToken() {
        return sessionHandler.getFreshToken();
    }

    @Override
    public String getUserId() {
        return getLoginID();
    }

    @Override
    public String getDeviceId() {
        return gcmHandler.getRegistrationId();
    }

    @Override
    public boolean isLoggedIn() {
        return isV4Login();
    }

    @Override
    public String getShopId() {
        return getShopID();
    }

    @Override
    public boolean hasShop() {
        return isUserHasShop();
    }

    @Override
    public String getName() {
        return getLoginName();
    }

    @Override
    public String getProfilePicture() {
        return sessionHandler.getProfilePicture();
    }

    @Override
    public boolean isMsisdnVerified() {
        return sessionHandler.isMsisdnVerified();
    }

    @Override
    public boolean isHasPassword() {
        return sessionHandler.isHasPassword();
    }
}
