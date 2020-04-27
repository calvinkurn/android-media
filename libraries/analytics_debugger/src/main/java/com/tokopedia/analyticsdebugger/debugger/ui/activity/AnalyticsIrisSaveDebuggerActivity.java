package com.tokopedia.analyticsdebugger.debugger.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.tokopedia.analyticsdebugger.R;
import com.tokopedia.analyticsdebugger.debugger.ui.fragment.AnalyticsDebuggerIrisSaveFragment;

public class AnalyticsIrisSaveDebuggerActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, AnalyticsIrisSaveDebuggerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                    .add(R.id.container, AnalyticsDebuggerIrisSaveFragment.newInstance(), AnalyticsDebuggerIrisSaveFragment.TAG)
                    .commit();
        }
    }
}
