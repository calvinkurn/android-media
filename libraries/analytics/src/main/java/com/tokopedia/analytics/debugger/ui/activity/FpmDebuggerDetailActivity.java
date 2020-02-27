package com.tokopedia.analytics.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerDetailFragment;
import com.tokopedia.analytics.debugger.ui.fragment.FpmDebuggerDetailFragment;
import com.tokopedia.analytics.debugger.ui.model.AnalyticsDebuggerViewModel;
import com.tokopedia.analytics.debugger.ui.model.FpmDebuggerViewModel;

import static com.tokopedia.analytics.debugger.AnalyticsDebuggerConst.DATA_DETAIL;
import static com.tokopedia.analytics.debugger.AnalyticsDebuggerConst.EVENT_NAME;

public class FpmDebuggerDetailActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context, FpmDebuggerViewModel viewModel) {
        Intent intent = new Intent(context, FpmDebuggerDetailActivity.class);
        intent.putExtra(DATA_DETAIL, viewModel);
        intent.putExtra(EVENT_NAME, viewModel.getName());

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getIntent() != null
                && getIntent().getExtras() != null
                && !TextUtils.isEmpty(getIntent().getExtras().getString(EVENT_NAME))
                && getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getIntent().getExtras().getString(EVENT_NAME));
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return FpmDebuggerDetailFragment.newInstance(getIntent().getExtras());
    }
}
