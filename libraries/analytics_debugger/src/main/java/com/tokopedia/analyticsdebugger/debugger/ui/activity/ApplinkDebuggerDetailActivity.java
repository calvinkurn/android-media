package com.tokopedia.analyticsdebugger.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.ApplinkDebuggerDetailFragment;
import com.tokopedia.analyticsdebugger.debugger.ui.model.ApplinkDebuggerViewModel;

import static com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.DATA_DETAIL;
import static com.tokopedia.analyticsdebugger.debugger.AnalyticsDebuggerConst.EVENT_NAME;

public class ApplinkDebuggerDetailActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context, ApplinkDebuggerViewModel viewModel) {
        Intent intent = new Intent(context, ApplinkDebuggerDetailActivity.class);
        intent.putExtra(DATA_DETAIL, viewModel);
        intent.putExtra(EVENT_NAME, "Applink Debugger");

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
        return ApplinkDebuggerDetailFragment.newInstance(getIntent().getExtras());
    }
}
