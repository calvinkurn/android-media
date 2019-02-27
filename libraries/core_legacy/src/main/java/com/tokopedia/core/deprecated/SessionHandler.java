package com.tokopedia.core.deprecated;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.utils.network.AuthUtil;
import com.tokopedia.core.var.TkpdCache;

/**
 * most of the codes is no-op that need to defined at the application.
 */
public abstract class SessionHandler implements UserSession {
    protected Context context;

    public SessionHandler(Context context) {
        this.context = context;

    }

    public abstract String getLoginName();
    public abstract String getGTMLoginID();
    public abstract String getShopID();
    public abstract String getLoginID();
    public abstract boolean isUserHasShop();

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

    public abstract boolean isV4Login();

    public abstract String getPhoneNumber();

    public abstract String getEmail();

    public abstract String getRefreshToken();

    public abstract String getAccessToken();

    @Override
    public abstract String getFreshToken();

    @Override
    public abstract String getUserId();

    @Override
    public abstract String getDeviceId();

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
    public abstract String getProfilePicture();

    @Override
    public abstract boolean isMsisdnVerified();

    @Override
    public abstract boolean isHasPassword() ;
}
