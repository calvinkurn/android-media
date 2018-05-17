package com.tokopedia.analytics.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerDetailFragment;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;

import static com.tokopedia.analytics.debugger.AnalyticsDebuggerConst.DATA_DETAIL;

/**
 * @author okasurya on 5/17/18.
 */
public class AnalyticsDebuggerDetailActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context, AnalyticsDebuggerViewModel viewModel) {
        Intent intent = new Intent(context, AnalyticsDebuggerDetailActivity.class);
        intent.putExtra(DATA_DETAIL, viewModel);

        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return AnalyticsDebuggerDetailFragment.newInstance(getIntent().getExtras());
    }
}
