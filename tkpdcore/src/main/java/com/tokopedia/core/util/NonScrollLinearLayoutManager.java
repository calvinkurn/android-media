package com.tokopedia.core.util;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;

import com.tkpd.library.ui.view.LinearLayoutManager;

/**
 * Created by nisie on 9/29/16.
 */

public class NonScrollLinearLayoutManager extends LinearLayoutManager {

    public NonScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public NonScrollLinearLayoutManager(Context context, int vertical, boolean b) {
        super(context, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

}
