package com.tokopedia.digital.cart.data.cache;

import android.content.Context;
import android.content.SharedPreferences;

public class DigitalLocalCache {
    private static final String CACHE_NAME = "DigitalLocalCache";
    private static final String PASCA_BAYAR_POP_UP = "PASCA_BAYAR_POP_UP";
    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPrefs;

    public DigitalLocalCache(Context context) {
        this.sharedPrefs = context.getSharedPreferences(CACHE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPrefs.edit();
    }

    public void setAlreadyShowPascaBayarPopUp(String userId) {
        editor
                .putBoolean(PASCA_BAYAR_POP_UP + userId,
                        true)
                .apply();
    }

    public boolean isAlreadyShowPascaBayarPopUp(String userId) {
        return sharedPrefs
                .getBoolean(PASCA_BAYAR_POP_UP + userId,
                        false
                );
    }
}
