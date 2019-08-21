package com.tokopedia.tkpd.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;

import static android.content.Context.MODE_PRIVATE;

/**
 * @author okasurya on 2019-07-02.
 */
public class UIBlockDebugger {
    public static void init(Context context) {
        SharedPreferences pref = context.getSharedPreferences("UI_BLOCK_DEBUGGER", MODE_PRIVATE);
        if(pref.getBoolean("isEnabled", false)) {
            BlockCanary.install(context, new BlockCanaryContext()).start();
        }
    }
}
