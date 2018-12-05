package com.tokopedia.digital.cart.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class DigitalPostPaidLocalCache {
    private static final String CACHE_NAME = "DigitalPostPaid";
    private static final String POST_PAID_POP_UP = "POST_PAID_POP_UP";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    private static DigitalPostPaidLocalCache instance;

    public static synchronized DigitalPostPaidLocalCache newInstance(Context context){
        if (instance == null)
            instance = new DigitalPostPaidLocalCache(context);
        return instance;
    }

    private DigitalPostPaidLocalCache(Context context) {
        this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
    }

    public void setAlreadyShowPostPaidPopUp(String userId) {
        editor
                .putBoolean(POST_PAID_POP_UP + userId,
                        true)
                .apply();
    }

    public boolean isAlreadyShowPostPaidPopUp(String userId) {
        return sharedPrefs
                .getBoolean(POST_PAID_POP_UP + userId,
                        false
                );
    }
}
