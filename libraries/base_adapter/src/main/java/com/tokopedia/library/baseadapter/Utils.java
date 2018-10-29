package com.tokopedia.library.baseadapter;

import android.content.Context;
import android.view.View;

import com.tokopedia.library.pagination.R;

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
