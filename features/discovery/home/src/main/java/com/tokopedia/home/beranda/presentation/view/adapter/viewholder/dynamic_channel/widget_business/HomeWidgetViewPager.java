package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel.widget_business;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
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

