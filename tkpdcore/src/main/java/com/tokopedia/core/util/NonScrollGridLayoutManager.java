package com.tokopedia.core.util;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Created by nisie on 9/29/16.
 */

public class NonScrollGridLayoutManager extends GridLayoutManager {

    public NonScrollGridLayoutManager(Context context, int spanCount, int vertical, boolean b) {
        super(context, spanCount, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

}
