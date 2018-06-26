package com.tokopedia.analytics.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.tokopedia.analytics.R;
import com.tokopedia.analytics.debugger.ui.fragment.AnalyticsDebuggerFragment;

public class AnalyticsDebuggerActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, AnalyticsDebuggerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_debugger);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setSubtitle("Tokopedia");
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, AnalyticsDebuggerFragment.newInstance(), AnalyticsDebuggerFragment.TAG)
                    .commit();
        }
    }
}
