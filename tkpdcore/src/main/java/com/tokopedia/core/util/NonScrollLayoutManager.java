package com.tokopedia.core.util;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.tkpd.library.ui.view.LinearLayoutManager;

/**
 * Created by nisie on 9/29/16.
 */

public class NonScrollLayoutManager extends LinearLayoutManager {

    public NonScrollLayoutManager(Context context) {
        super(context);
    }

    public NonScrollLayoutManager(FragmentActivity activity, int vertical, boolean b) {
        super(activity, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

}
