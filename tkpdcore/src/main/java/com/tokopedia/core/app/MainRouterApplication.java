package com.tokopedia.core.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.deprecated.SessionHandler;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.base.IAppNotificationReceiver;
import com.tokopedia.core.gcm.model.NotificationPass;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;

/**
 * this code is bridging for old code and latest codes.
 */
public abstract class MainRouterApplication extends CoreNetworkApplication implements TkpdCoreRouter {
    GCMHandler gcmHandler;
    SessionHandler sessionHandler;

    @Override
    public SessionHandler legacySessionHandler() {
        if(sessionHandler == null) {
            final com.tokopedia.core.util.SessionHandler mSessionHandler
                    = new com.tokopedia.core.util.SessionHandler(this);
            return sessionHandler = new SessionHandler(this) {


                @Override
                public String getAccessToken() {
                    return mSessionHandler.getAccessToken(MainRouterApplication.this);
                }

                @Override
                public String getFreshToken() {
                    return mSessionHandler.getAuthRefreshToken();
                }

                @Override
                public String getUserId() {
                    return mSessionHandler.getLoginID();
                }

                @Override
                public String getLoginName() {
                    return mSessionHandler.getLoginName();
                }

                @Override
                public String getGTMLoginID() {
                    return mSessionHandler.getGTMLoginID(context);
                }

                @Override
                public String getShopID() {
                    return mSessionHandler.getShopID();
                }

                @Override
                public String getLoginID() {
                    return mSessionHandler.getLoginID();
                }

                @Override
                public boolean isUserHasShop() {
                    return mSessionHandler.isUserHasShop();
                }

                @Override
                public boolean isV4Login() {
                    return mSessionHandler.isV4Login();
                }

                @Override
                public String getPhoneNumber() {
                    return mSessionHandler.getPhoneNumber();
                }

                @Override
                public String getEmail() {
                    return mSessionHandler.getEmail();
                }

                @Override
                public String getRefreshToken() {
                    return mSessionHandler.getAuthRefreshToken();
                }

                @Override
                public String getDeviceId() {
                    return legacyGCMHandler().getRegistrationId();
                }

                @Override
                public String getProfilePicture() {
                    return mSessionHandler.getProfilePicture();
                }

                @Override
                public boolean isMsisdnVerified() {
                    return mSessionHandler.isMsisdnVerified();
                }

                @Override
                public boolean isHasPassword() {
                    return mSessionHandler.isHasPassword();
                }
            };
        }else{
            return sessionHandler;
        }
    }

    @Override
    public GCMHandler legacyGCMHandler() {
        if(gcmHandler==null){
            return gcmHandler = new GCMHandler(this);
        }else {
            return gcmHandler;
        }
    }

    public static synchronized TkpdCoreRouter getTkpdCoreRouter(){
        return MainApplication.getInstance();
    }
}
