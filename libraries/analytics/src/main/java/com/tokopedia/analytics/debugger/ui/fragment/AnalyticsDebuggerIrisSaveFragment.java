package com.tokopedia.analytics.debugger.ui.fragment;

import androidx.fragment.app.Fragment;

import com.tokopedia.analytics.debugger.di.AnalyticsDebuggerComponent;
import com.tokopedia.analytics.debugger.ui.AnalyticsDebugger;

public class AnalyticsDebuggerIrisSaveFragment
        extends BaseAnalyticsDebuggerFragment
        implements AnalyticsDebugger.View {

    public static Fragment newInstance() {
        return new AnalyticsDebuggerIrisSaveFragment();
    }

    @Override
    protected void injectToFragment(AnalyticsDebuggerComponent component) {
        presenter = component.getGtmIrisSavePresenter();
    }

}