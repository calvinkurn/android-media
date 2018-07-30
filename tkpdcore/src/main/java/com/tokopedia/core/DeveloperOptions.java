package com.tokopedia.core;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.readystatesoftware.chuck.Chuck;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.SessionHandler;

@DeepLink("tokopedia://setting/dev-opts")
public class DeveloperOptions extends TActivity implements SessionHandler.onLogoutListener {
    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String IS_CHUCK_ENABLED = "is_enable";
    //developer test

    private TextView vCustomIntent;
    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private View vMaintenance;
    private AppCompatEditText remoteConfigKeyEditText;
    private AppCompatEditText remoteConfigValueEditText;
    private AppCompatButton remoteConfigCheckBtn;
    private AppCompatButton remoteConfigSaveBtn;

    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToAnalytics;
    private CheckBox toggleAnalytics;

    private static TkpdCoreRouter tkpdCoreRouter;

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEVELOPER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);

        setupView();
        initListener();
        initView();
    }

    private void setupView() {
        vForceCrash = (TextView) findViewById(R.id.force_crash);

        vMaintenance = findViewById(R.id.maintenance);

        vCustomIntent = (TextView) findViewById(R.id.custom_intent);
        resetOnBoarding = (TextView) findViewById(R.id.reset_onboarding);
        testOnBoarding = (TextView) findViewById(R.id.test_onboarding);

        vGoTochuck = (TextView) findViewById(R.id.goto_chuck);
        toggleChuck = (CheckBox) findViewById(R.id.toggle_chuck);

        vGoToAnalytics = (TextView) findViewById(R.id.goto_analytics);
        toggleAnalytics = (CheckBox) findViewById(R.id.toggle_analytics);

        remoteConfigKeyEditText = findViewById(R.id.et_remote_config_key);
        remoteConfigValueEditText = findViewById(R.id.et_remote_config_value);
        remoteConfigCheckBtn = findViewById(R.id.btn_remote_config_check);
        remoteConfigSaveBtn = findViewById(R.id.btn_remote_config_save);
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
                startActivityForResult(InboxRouter.getFreeReturnOnBoardingActivityIntent(getBaseContext(), "1234"), 789);
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
                GtmLogger.getInstance().enableNotification(DeveloperOptions.this, state);
            }
        });

        vGoToAnalytics.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                GtmLogger.getInstance().openActivity(DeveloperOptions.this);
            }
        });

        remoteConfigCheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionCheckValueRemoteConfig();
            }
        });
        remoteConfigSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSaveValueRemoteConfig();
            }
        });
    }

    private void actionSaveValueRemoteConfig() {
        String key = remoteConfigKeyEditText.getText().toString().trim();
        String value = remoteConfigValueEditText.getText().toString().trim();
        if (key.isEmpty() || value.isEmpty()) {
            Toast.makeText(this, "Please check the input should not empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        coreRouter(getApplicationContext()).setStringRemoteConfigLocal(key, value);
        remoteConfigKeyEditText.setText(null);
        remoteConfigValueEditText.setText(null);
    }

    private void actionCheckValueRemoteConfig() {
        String key = remoteConfigKeyEditText.getText().toString().trim();
        if (key.isEmpty()) {
            Toast.makeText(this, "Please type the key", Toast.LENGTH_SHORT).show();
            return;
        }
        remoteConfigValueEditText.setText(String.valueOf(coreRouter(getApplicationContext()).getStringRemoteConfig(key)));
    }

    private static TkpdCoreRouter coreRouter(Context applicationContext) {
        if (tkpdCoreRouter == null) {
            tkpdCoreRouter = (TkpdCoreRouter) applicationContext;
        }
        return tkpdCoreRouter;
    }

    public void initView() {
        LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), CHUCK_ENABLED);
        toggleChuck.setChecked(cache.getBoolean(IS_CHUCK_ENABLED, false));

        toggleAnalytics.setChecked(GtmLogger.getInstance().isNotificationEnabled(this));
    }

    private void setMaintenance() {
        startActivity(MaintenancePage.createIntentFromNetwork(this, ""));
    }

}
