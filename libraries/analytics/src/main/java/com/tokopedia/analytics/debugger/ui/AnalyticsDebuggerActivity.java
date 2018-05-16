package com.tokopedia.analytics.debugger.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.tokopedia.analytics.R;

public class AnalyticsDebuggerActivity extends AppCompatActivity {

    public static Intent newInstance(Context context) {
        return new Intent(context, AnalyticsDebuggerActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_debugger);
    }
}
