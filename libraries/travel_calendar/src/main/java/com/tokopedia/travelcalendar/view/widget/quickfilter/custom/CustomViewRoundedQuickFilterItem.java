package com.tokopedia.travelcalendar.view.widget.quickfilter.custom;

import android.view.View;

import com.tokopedia.travelcalendar.view.widget.quickfilter.QuickFilterItem;

public class CustomViewRoundedQuickFilterItem extends QuickFilterItem {

    private View selectedView;
    private View defaultView;

    public View getSelectedView() {
        return selectedView;
    }

    public void setSelectedView(View selectedView) {
        this.selectedView = selectedView;
    }

    public View getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(View defaultView) {
        this.defaultView = defaultView;
    }
}
