package com.tokopedia.analytics.debugger.ui.fragment;

import androidx.fragment.app.Fragment;

import com.tokopedia.analytics.debugger.di.AnalyticsDebuggerComponent;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;

public class AnalyticsDebuggerGtmErrorFragment
        extends BaseAnalyticsDebuggerFragment
        implements AnalyticsDebugger.View {

    public static Fragment newInstance() {
        return new AnalyticsDebuggerGtmErrorFragment();
    }

    @Override
    protected void injectToFragment(AnalyticsDebuggerComponent component) {
        presenter = component.getGtmErrorPresenter();
    }

}