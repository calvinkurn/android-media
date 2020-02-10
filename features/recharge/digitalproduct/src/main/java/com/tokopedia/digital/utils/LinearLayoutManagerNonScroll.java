package com.tokopedia.digital.utils;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
