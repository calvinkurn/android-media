package com.tokopedia.sellerapp.home.utils;

import java.util.HashMap;

/**
 * Created by normansyahputa on 8/30/16.
 */

public class NonNullStringMap extends HashMap<String, String> {
    @Override
    public String put(String key, String value) {
        if(value == null){
            return null;
        }
        return super.put(key, value);
    }
}
