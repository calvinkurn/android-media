package com.tokopedia.tkpdreactnative.react.common.data;

import com.tokopedia.tkpdreactnative.react.common.data.source.DataSourcePreference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class PreferenceRepositoryImpl implements PreferenceRepository {

    private DataSourcePreference dataSourcePreference;

    public PreferenceRepositoryImpl(DataSourcePreference dataSourcePreference) {
        this.dataSourcePreference = dataSourcePreference;
    }

    @Override
    public Observable<Boolean> savePreference(HashMap<String, Object> parameters) {
        Iterator it = parameters.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            dataSourcePreference.savePreference((String)pair.getKey(), pair.getValue());
        }
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> getPreferenceBoolean(String key) {
        return dataSourcePreference.getBoolean(key);
    }
}
