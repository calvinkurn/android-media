package com.tokopedia.home.util;

/**
 * Created by errysuprayogi on 3/16/18.
 */

import android.content.Context;

public class DimensionUtils {

    public static float getDensityMatrix(Context context){
        return context.getResources().getDisplayMetrics().density;
    }
}