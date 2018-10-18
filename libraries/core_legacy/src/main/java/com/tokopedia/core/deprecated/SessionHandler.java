package com.tokopedia.core.deprecated;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;

/**
 * most of the codes is no-op that need to defined at the application.
 */
public class SessionHandler implements UserSession {
    private Context context;

    public SessionHandler(Context context) {
        this.context = context;
    }

    public String getLoginName(){
        return null;
    }
    public String getGTMLoginID(){
        return null;
    }
    public String getShopID(){
        return null;
    }
    public String getLoginID(){
        return null;
    }
    public boolean isUserHasShop(){
        return false;
    }
    public String getAdsId(){
//        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ADVERTISINGID);
//        String adsId = localCacheHandler.getString(TkpdCache.Key.KEY_ADVERTISINGID);
//        if (adsId != null && !"".equalsIgnoreCase(adsId.trim())) {
//            authenticated.setAdsId(adsId);
//        }
        return null;
    }

    public String getAndroidId(){
//        final LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.ANDROID_ID);
//
//        String androidId = localCacheHandler.getString(TkpdCache.Key.KEY_ANDROID_ID);
//        if (androidId != null && !"".equalsIgnoreCase(androidId.trim())) {
//            return androidId;
//        } else {
//            String android_id = AuthUtil.md5(Settings.Secure.getString(context.getContentResolver(),
//                    Settings.Secure.ANDROID_ID));
//            if (!TextUtils.isEmpty(android_id)) {
//                localCacheHandler.putString(TkpdCache.Key.KEY_ANDROID_ID, android_id);
//                localCacheHandler.applyEditor();
//            }
//            return android_id;
//        }
        return null;
    }
    public boolean isV4Login(){
        return false;
    }

    public String getPhoneNumber() {
        return "";
    }

    public String getEmail() {
        return "";
    }

    public String getRefreshToken() {
        return "";
    }

    public String getAccessToken() {
        return "";
    }

    @Override
    public String getFreshToken() {
        return null;
    }

    @Override
    public String getUserId() {
        return null;
    }

    @Override
    public String getDeviceId() {
        return null;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public String getShopId() {
        return null;
    }

    @Override
    public boolean hasShop() {
        return false;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getProfilePicture() {
        return null;
    }

    @Override
    public boolean isMsisdnVerified() {
        return false;
    }

    @Override
    public boolean isHasPassword() {
        return false;
    }
}
