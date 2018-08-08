package com.tokopedia.digital.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

/**
 * @author anggaprasetiyo on 5/2/17.
 */

public class LinearLayoutManagerNonScroll extends LinearLayoutManager {

    public LinearLayoutManagerNonScroll(Context context) {
        super(context);
    }

    public LinearLayoutManagerNonScroll(FragmentActivity activity, int vertical, boolean b) {
        super(activity, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }
}
