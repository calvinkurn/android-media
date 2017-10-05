package com.tokopedia.core.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * @author by nisie on 8/1/17.
 */

public class ReputationRecyclerView extends RecyclerView {
    public ReputationRecyclerView(Context context) {
        super(context);
    }

    public ReputationRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReputationRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDetachedFromWindow() {
        try {
            super.onDetachedFromWindow();
        } catch (Exception e) {
            Log.e(ReputationRecyclerView.class.getSimpleName(), e.toString());
        }
    }
}
