package com.tokopedia.train.reviewdetail.presentation;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Rizky on 13/07/18.
 */
public class NonScrollableLinearLayoutManager extends LinearLayoutManager {

    public NonScrollableLinearLayoutManager(Context context) {
        super(context);
    }

    public NonScrollableLinearLayoutManager(FragmentActivity activity, int vertical, boolean b) {
        super(activity, vertical, b);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

}
