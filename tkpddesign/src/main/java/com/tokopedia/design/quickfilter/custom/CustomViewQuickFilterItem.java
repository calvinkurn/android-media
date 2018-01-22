package com.tokopedia.design.quickfilter.custom;

import android.view.View;

import com.tokopedia.design.quickfilter.QuickFilterItem;

/**
 * Created by nabillasabbaha on 11/22/17.
 */

public class CustomViewQuickFilterItem extends QuickFilterItem {

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
