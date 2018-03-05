package com.tokopedia.sellerapp;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.pojo.profile.ProfileModel;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.util.SessionHandler;

/**
 * Created by zulfikarrahman on 12/4/17.
 */

public class UserSessionImpl implements UserSession {

    private SessionHandler sessionHandler;
    private FCMCacheManager fcmCacheManager;
    private Context context;

    public UserSessionImpl(Context context) {
        this.sessionHandler = new SessionHandler(context);
        this.fcmCacheManager = new FCMCacheManager(context);
        this.context = context;
    }

    @Override
    public String getAccessToken() {
        return sessionHandler.getAuthAccessToken();
    }

    @Override
    public String getFreshToken() {
        return sessionHandler.getAuthRefreshToken();
    }

    @Override
    public String getUserId() {
        return sessionHandler.getLoginID();
    }

    @Override
    public boolean isLoggedIn() {
        return sessionHandler.isV4Login();
    }

    @Override
    public String getDeviceId() {
        return fcmCacheManager.getRegistrationId();
    }

    @Override
    public String getShopId() {
        return sessionHandler.getShopID();
    }

    @Override
    public boolean hasShop() {
        return sessionHandler.isUserHasShop();
    }

    @Override
    public String getName() {
        return sessionHandler.getLoginName();
    }

    @Override
    public String getProfilePicture() {
        ProfileModel profileModel = CacheUtil.convertStringToModel(new GlobalCacheManager().getValueString(ProfileSourceFactory.KEY_PROFILE_DATA),
                new TypeToken<ProfileModel>() {
                }.getType());
        return profileModel.getProfileData().getUserInfo().getUserImage();
    }
}
