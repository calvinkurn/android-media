package com.tokopedia.digital.newcart.data.cache;

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

    public void setDontShowAgain(String userId, boolean dontShowAgain) {
        editor
                .putBoolean(POST_PAID_POP_UP + userId,
                        dontShowAgain)
                .apply();
    }

    public boolean isAlreadyShowPostPaidPopUp(String userId) {
        return sharedPrefs
                .getBoolean(POST_PAID_POP_UP + userId,
                        false
                );
    }
}
