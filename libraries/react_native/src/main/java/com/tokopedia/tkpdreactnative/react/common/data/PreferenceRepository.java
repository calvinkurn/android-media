package com.tokopedia.tkpdreactnative.react.common.data;

import java.util.HashMap;

import rx.Observable;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public interface PreferenceRepository {
    Observable<Boolean> savePreference(HashMap<String, Object> parameters);

    Observable<Boolean> getPreferenceBoolean(String key);
}
