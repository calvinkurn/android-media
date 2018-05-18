package com.tokopedia.analytics.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerFragment;

public class AnalyticsDebuggerActivity extends BaseSimpleActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, AnalyticsDebuggerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle("Analytics Debugger");
        }
    }

    @Override
    protected Fragment getNewFragment() {
        return AnalyticsDebuggerFragment.newInstance();
    }

    @Override
    protected String getTagFragment() {
        return AnalyticsDebuggerFragment.TAG;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
