package com.tokopedia.core.gcm.interactor.source;

import android.content.Context;

import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.util.PasswordGenerator;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author  by alvarisi on 1/5/17.
 */

public class DiskPushNotificationDataStore {
    private final Context mContext;
    public DiskPushNotificationDataStore(Context context) {
        mContext = context;
    }

    public Observable<String> deviceRegistration(){
        return Observable.just(true).map(new Func1<Boolean, String>() {
            @Override
            public String call(Boolean aBoolean) {
                return PasswordGenerator.getAppId(mContext);
            }
        });
    }

    public Observable<Boolean> saveRegistrationDevice(String registrationDevice){
        return Observable.just(registrationDevice).map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String req) {
                FCMCacheManager.storeRegId(req, mContext);
                return true;
            }
        });
    }
}
