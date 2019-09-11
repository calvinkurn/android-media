package com.tokopedia.home.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

public class ToggleableSwipeRefreshLayout extends SwipeRefreshLayout {

    private boolean canChildScrollUp = false;

    public ToggleableSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public ToggleableSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCanChildScrollUp(boolean canChildScrollUp) {
        this.canChildScrollUp = canChildScrollUp;
    }

    @Override
    public boolean canChildScrollUp() {
        return canChildScrollUp;
    }
}