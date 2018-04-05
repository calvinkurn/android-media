package com.tokopedia.core.analytics.fingerprint.domain.usecase;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.fingerprint.domain.FingerprintRepository;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.database.manager.GlobalCacheManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 4/5/18.
 */

public class CacheGetFingerprintUseCase extends GetFingerprintUseCase {

    public static final String FINGERPRINT_KEY_NAME = "FINGERPRINT_KEY_NAME";
    public static final String FINGERPRINT_USE_CASE = "FINGERPRINT_USE_CASE";
    private final LocalCacheHandler localCacheHandler;

    public CacheGetFingerprintUseCase(FingerprintRepository fpRepository) {
        super(fpRepository);
        localCacheHandler = new LocalCacheHandler(MainApplication.getAppContext(), FINGERPRINT_KEY_NAME);
        localCacheHandler.setExpire(3600);
    }

    @Override
    public Observable<String> createObservable(RequestParams requestParams) {

        String cache =  localCacheHandler.getString(FINGERPRINT_USE_CASE);
        if(TextUtils.isEmpty(cache) || localCacheHandler.isExpired()){
            return super.createObservable(requestParams);
        }

        return Observable.just(cache);
    }

    @NonNull
    @Override
    protected Func1<String, String> unnecessaryMap() {
        return new Func1<String, String>() {
            @Override
            public String call(String s) {
                localCacheHandler.putString(FINGERPRINT_USE_CASE, s);
                localCacheHandler.applyEditor();
                return s;
            }
        };
    }
}
