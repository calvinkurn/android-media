package com.tokopedia.library.pagination;

import android.content.Context;
import android.view.View;

public class Utils {

    /**
     * Default retry view
     *
     * @param context
     * @return View
     */
    public static View getRetryView(Context context) {
        return View.inflate(context, R.layout.pg_retry, null);
    }


    /**
     * Default loader view
     *
     * @param context
     * @return View
     */
    public static View getLoaderView(Context context) {
        return View.inflate(context, R.layout.pg_progress, null);
    }
}
