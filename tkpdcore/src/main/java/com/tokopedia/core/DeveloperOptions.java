package com.tokopedia.core;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.chuck.Chuck;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.analytics.debugger.ui.activity.AnalyticsDebuggerActivity;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.SessionHandler;


public class DeveloperOptions extends TActivity implements SessionHandler.onLogoutListener {
    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String IS_CHUCK_ENABLED = "is_enable";
    public static final String ANALYTICS_DEBUGGER = "ANALYTICS_DEBUGGER";
    public static final String IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED = "is_notif_enabled";
    //developer test

    private TextView vCustomIntent;
    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private View vMaintenance;

    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToAnalytics;
    private CheckBox toggleAnalytics;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEVELOPER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);


        vForceCrash = (TextView) findViewById(R.id.force_crash);

        vMaintenance = findViewById(R.id.maintenance);

        vCustomIntent = (TextView) findViewById(R.id.custom_intent);
        resetOnBoarding = (TextView) findViewById(R.id.reset_onboarding);
        testOnBoarding = (TextView) findViewById(R.id.test_onboarding);

        vGoTochuck = (TextView) findViewById(R.id.goto_chuck);
        toggleChuck = (CheckBox) findViewById(R.id.toggle_chuck);

        vGoToAnalytics = (TextView) findViewById(R.id.goto_analytics);
        toggleAnalytics = (CheckBox) findViewById(R.id.toggle_analytics);

        initListener();
        initView();
    }

    private void initListener() {
        vMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaintenance();
            }
        });

        vForceCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                throw new RuntimeException("HAHAHAHAH");
            }
        });
        resetOnBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionHandler.setFirstTimeUser(DeveloperOptions.this, true);
                Toast.makeText(DeveloperOptions.this, "OnBoarding Resetted", Toast.LENGTH_SHORT).show();
                LocalCacheHandler.clearCache(DeveloperOptions.this,
                        ConstantOnBoarding.CACHE_FREE_RETURN);



            }
        });
        testOnBoarding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(InboxRouter.getFreeReturnOnBoardingActivityIntent(getBaseContext(), "1234"),789);
            }
        });

        toggleChuck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), CHUCK_ENABLED);
                cache.putBoolean(IS_CHUCK_ENABLED, state);
                cache.applyEditor();
            }
        });

        vGoTochuck.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                startActivity(Chuck.getLaunchIntent(getApplicationContext()));
            }
        });

        toggleAnalytics.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean state) {
                LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), ANALYTICS_DEBUGGER);
                cache.putBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, state);
                cache.applyEditor();
            }
        });

        vGoToAnalytics.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                startActivity(AnalyticsDebuggerActivity.newInstance(DeveloperOptions.this));
            }
        });

    }

    public void initView() {
        LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), CHUCK_ENABLED);
        toggleChuck.setChecked(cache.getBoolean(IS_CHUCK_ENABLED, false));

        LocalCacheHandler analyticsCache = new LocalCacheHandler(getApplicationContext(), ANALYTICS_DEBUGGER);
        toggleChuck.setChecked(cache.getBoolean(IS_ANALYTICS_DEBUGGER_NOTIF_ENABLED, false));
    }

    private void setMaintenance() {
        startActivity(MaintenancePage.createIntentFromNetwork(this, ""));
    }

}
