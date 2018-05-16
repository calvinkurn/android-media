package com.tokopedia.analytics.debugger.ui;

import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.analytics.debugger.ui.adapter.AnalyticsDebuggerTypeFactory;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

/**
 * @author okasurya on 5/16/18.
 */
public class AnalyticsDebuggerFragment extends BaseSearchListFragment<AnalyticsDebuggerViewModel, AnalyticsDebuggerTypeFactory> {
    public static Fragment newInstance() {
        return new AnalyticsDebuggerFragment();
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    protected AnalyticsDebuggerTypeFactory getAdapterTypeFactory() {
        return new AnalyticsDebuggerTypeFactory();
    }

    @Override
    public void onItemClicked(AnalyticsDebuggerViewModel analyticsDebuggerViewModel) {

    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return AnalyticsDebuggerFragment.class.getSimpleName();
    }

    @Override
    public void onSearchSubmitted(String text) {

    }

    @Override
    public void onSearchTextChanged(String text) {

    }
}
