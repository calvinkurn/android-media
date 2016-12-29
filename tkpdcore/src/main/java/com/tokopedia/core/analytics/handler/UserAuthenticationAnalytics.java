package com.tokopedia.core.analytics.handler;

import android.os.Bundle;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.model.CustomerWrapper;
import com.tokopedia.core.app.MainApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by alvarisi on 12/28/16.
 */

public class UserAuthenticationAnalytics {
    private static LocalCacheHandler mLocalCacheHandler;

    public UserAuthenticationAnalytics() {
        mLocalCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), AppEventTracking.GTM_CACHE);
    }

    private static void checkNotNullCacheHandler(){
        if (mLocalCacheHandler == null){
            mLocalCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), AppEventTracking.GTM_CACHE);
        }
    }

    public static void setActiveLogin() {
        checkNotNullCacheHandler();
        setActiveAuthenticationState(AppEventTracking.GTMCacheValue.LOGIN);
    }

    public static void setActiveRegister() {
        checkNotNullCacheHandler();
        setActiveAuthenticationState(AppEventTracking.GTMCacheValue.REGISTER);
    }

    private static void setActiveAuthenticationState(String state) {
        mLocalCacheHandler.putString(AppEventTracking.GTMCacheKey.SESSION_STATE, state);
        mLocalCacheHandler.applyEditor();
    }

    public static void setActiveAuthenticationMedium(String medium) {
        checkNotNullCacheHandler();
        mLocalCacheHandler.putString(AppEventTracking.GTMCacheKey.MEDIUM, medium);
        mLocalCacheHandler.applyEditor();
    }

    public static void sendAnalytics(Bundle bundle) {
        checkNotNullCacheHandler();
        switch (mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.SESSION_STATE, "")) {
            case AppEventTracking.GTMCacheValue.LOGIN:
                UnifyTracking.eventLoginSuccess(mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                        AppEventTracking.DEFAULT_CHANNEL)
                );

                Map<String, String> loginAttr = new HashMap<String, String>();
                UnifyTracking.eventLoginLoca(new CustomerWrapper.Builder()
                        .setCustomerId(
                                bundle.getString(AppEventTracking.USER_ID_KEY,
                                        AppEventTracking.DEFAULT_CHANNEL)
                        )
                        .setFirstName(
                                bundle.getString(AppEventTracking.FULLNAME_KEY,
                                        AppEventTracking.DEFAULT_CHANNEL)
                        )
                        .setEmailAddress(
                                bundle.getString(AppEventTracking.EMAIL_KEY,
                                        AppEventTracking.DEFAULT_CHANNEL)
                        )
                        .setMethod(
                                mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                                        AppEventTracking.DEFAULT_CHANNEL)
                        )
                        .setAttr(loginAttr)
                        .build()
                );
                CommonUtils.dumper(bundle.toString());
                CommonUtils.dumper(mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                        AppEventTracking.DEFAULT_CHANNEL));
                break;
            case AppEventTracking.GTMCacheValue.REGISTER:
                UnifyTracking.eventRegisterSuccess(mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                        AppEventTracking.DEFAULT_CHANNEL)
                );

                Map<String, String> registerAttr = new HashMap<String, String>();
                UnifyTracking.eventRegisterLoca(
                        new CustomerWrapper.Builder()
                                .setCustomerId(
                                        bundle.getString(AppEventTracking.USER_ID_KEY,
                                                AppEventTracking.DEFAULT_CHANNEL)
                                )
                                .setFirstName(
                                        bundle.getString(AppEventTracking.FULLNAME_KEY,
                                                AppEventTracking.DEFAULT_CHANNEL)
                                )
                                .setEmailAddress(
                                        bundle.getString(AppEventTracking.EMAIL_KEY,
                                                AppEventTracking.DEFAULT_CHANNEL)
                                )
                                .setMethod(
                                        mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                                                AppEventTracking.DEFAULT_CHANNEL)
                                )
                                .setAttr(registerAttr)
                                .build()
                );
                CommonUtils.dumper(bundle.toString());
                CommonUtils.dumper(mLocalCacheHandler.getString(AppEventTracking.GTMCacheKey.MEDIUM,
                        AppEventTracking.DEFAULT_CHANNEL));
                break;
        }
    }
}
