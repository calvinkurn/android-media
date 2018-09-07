package com.tokopedia.tkpdreactnative.react.common.data.source;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class DataSourcePreference {

    private LocalCacheHandler localCacheHandler;

    @Inject
    public DataSourcePreference(LocalCacheHandler localCacheHandler) {
        this.localCacheHandler = localCacheHandler;
    }

    public Observable<Boolean> getBoolean(String key) {
        return Observable.just(localCacheHandler.getBoolean(key));
    }

    public void savePreference(String key, Object value) {
        if(value instanceof Boolean) {
            localCacheHandler.putBoolean(key, (Boolean)value);
        }else if(value instanceof String){
            localCacheHandler.putString(key, (String) value);
        }
        localCacheHandler.applyEditor();
    }
}
