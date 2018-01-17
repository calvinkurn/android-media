package com.tokopedia.core.network.retrofit.utils;

import java.util.HashMap;

/**
 * TKPDMapParam
 * Created by anggaprasetiyo on 10/5/16.
 */
@Deprecated
public class TKPDMapParam<K, V> extends HashMap<K, V> {
    /**
     * @param key   key parameter
     * @param value value nya, valuenya ga boleh null, kalo null terpaksa dibuat error
     * @return @override super
     */
    @Override
    public V put(K key, V value) {
        if (value == null) throw new RuntimeException("Value ga boleh null coy!! |KEY = " + key);
        return super.put(key, value);
    }
}
