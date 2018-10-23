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

/**
 * this code is bridging for old code and latest codes.
 */
public class MainRouterApplication extends BaseMainApplication implements TkpdCoreRouter {
    GCMHandler gcmHandler;
    SessionHandler sessionHandler;

    @Override
    public Class<?> getInboxTalkActivityClass() {
        return InboxRouter.getInboxTalkActivityClass();
    }

    @Override
    public Intent getSellerHomeActivity() {
        return null;
    }

    @Override
    public Intent getInboxTalkActivityIntent() {
        return null;
    }

    @Override
    public IAppNotificationReceiver getAppNotificationReceiver() {
        return null;
    }

    @Override
    public Class<?> getInboxMessageActivityClass() {
        return null;
    }

    @Override
    public Class<?> getInboxResCenterActivityClassReal() {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionShippingStatusReal(Context mContext) {
        return null;
    }

    @Override
    public Class getSellingActivityClassReal() {
        return null;
    }

    @Override
    public Intent getActivitySellingTransactionListReal(Context mContext) {
        return null;
    }

    @Override
    public String getDesktopLinkGroupChat() {
        return null;
    }

    @Override
    public Intent getHomeIntent(Context context) {
        return null;
    }

    @Override
    public Class<?> getHomeClass(Context context) throws ClassNotFoundException {
        return null;
    }

    @Override
    public NotificationPass setNotificationPass(Context mContext, NotificationPass mNotificationPass, Bundle data, String notifTitle) {
        return null;
    }

    @Override
    public Intent getInboxMessageIntent(Context mContext) {
        return null;
    }

    @Override
    public SessionHandler legacySessionHandler() {
        if(sessionHandler == null)
        return sessionHandler = new SessionHandler(this){
            com.tokopedia.core.util.SessionHandler sessionHandler
                    = new com.tokopedia.core.util.SessionHandler(MainRouterApplication.this);
            @Override
            public String getAccessToken() {
                return sessionHandler.getAccessToken(context);
            }

            @Override
            public String getLoginName() {
                return sessionHandler.getLoginName();
            }

            @Override
            public String getGTMLoginID() {
                return sessionHandler.getGTMLoginID(context);
            }

            @Override
            public String getShopID() {
                return sessionHandler.getShopID();
            }

            @Override
            public String getLoginID() {
                return sessionHandler.getLoginID();
            }

            @Override
            public boolean isUserHasShop() {
                return sessionHandler.isUserHasShop();
            }

            @Override
            public boolean isV4Login() {
                return sessionHandler.isV4Login();
            }

            @Override
            public String getPhoneNumber() {
                return sessionHandler.getPhoneNumber();
            }

            @Override
            public String getEmail() {
                return sessionHandler.getEmail();
            }

            @Override
            public String getDeviceId() {
                return legacyGCMHandler().getRegistrationId();
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
        };
        else{
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
