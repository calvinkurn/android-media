package com.tokopedia.core.util;

import android.content.Context;
import androidx.recyclerview.widget.GridLayoutManager;

import com.tkpd.library.ui.view.LinearLayoutManager;

/**
 * Created by nisie on 9/29/16.
 */

public class NonScrollGridLayoutManager extends GridLayoutManager {

    public NonScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NonScrollGridLayoutManager(Context context, int spanCount, int vertical, boolean b) {
        super(context, spanCount, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

}
