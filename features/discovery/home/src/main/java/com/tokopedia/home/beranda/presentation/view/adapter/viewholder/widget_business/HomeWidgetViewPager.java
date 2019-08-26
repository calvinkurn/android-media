package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.widget_business;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

public class HomeWidgetViewPager extends ViewPager {
    private View currentView;
    private boolean canScrollHorizontal = true;

    public HomeWidgetViewPager(Context context) {
        super(context);
    }

    public HomeWidgetViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return canScrollHorizontal && super.canScrollHorizontally(direction);
    }

    public void setCanScrollHorizontal(boolean canScrollHorizontal) {
        this.canScrollHorizontal = canScrollHorizontal;
    }
}

