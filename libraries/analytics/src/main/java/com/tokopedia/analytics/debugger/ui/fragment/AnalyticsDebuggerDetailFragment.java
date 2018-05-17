package com.tokopedia.analytics.debugger.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.R;

/**
 * @author okasurya on 5/17/18.
 */
public class AnalyticsDebuggerDetailFragment extends BaseDaggerFragment {
    public static Fragment newInstance(Bundle extras) {
        AnalyticsDebuggerDetailFragment fragment = new AnalyticsDebuggerDetailFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_analytics_debugger_detail, container);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    protected String getScreenName() {
        return AnalyticsDebuggerDetailFragment.class.getSimpleName();
    }
}
