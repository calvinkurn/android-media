package com.tokopedia.core;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.readystatesoftware.chuck.Chuck;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core2.R;

@DeepLink("tokopedia://setting/dev-opts")
public class DeveloperOptions extends TActivity implements SessionHandler.onLogoutListener {
    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String GROUPCHAT_PREF = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter";
    public static final String IS_CHUCK_ENABLED = "is_enable";
    public static final String SP_REACT_DEVELOPMENT_MODE = "SP_REACT_DEVELOPMENT_MODE";
    public static final String SP_REACT_ENABLE_SHAKE = "SP_REACT_ENABLE_SHAKE";
    public static final String IS_RELEASE_MODE = "IS_RELEASE_MODE";
    public static final String RN_DEV_LOGGER = "rn_dev_logger";
    private static final String IP_GROUPCHAT = "ip_groupchat";
    private static final String LOG_GROUPCHAT = "log_groupchat";
    //developer test

    private TextView vCustomIntent;
    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private TextView vDevOptionRN;
    private View vMaintenance;
    private AppCompatEditText remoteConfigKeyEditText;
    private AppCompatEditText remoteConfigValueEditText;
    private AppCompatButton remoteConfigCheckBtn;
    private AppCompatButton remoteConfigSaveBtn;
    private ToggleButton toggleReactDeveloperMode;
    private ToggleButton toggleReactEnableDeveloperOptions;
    private SharedPreferences sharedPreferences;

    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToAnalytics;
    private CheckBox toggleAnalytics;

    private AppCompatEditText ipGroupChat;
    private View saveIpGroupChat;
    private ToggleButton groupChatLogToggle;

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
        vDevOptionRN = findViewById(R.id.rn_dev_options);
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

        toggleReactDeveloperMode = findViewById(R.id.toggle_reactnative_mode);
        toggleReactEnableDeveloperOptions = findViewById(R.id.toggle_reactnative_dev_options);
        toggleReactEnableDeveloperOptions.setChecked(false);

        ipGroupChat = findViewById(R.id.ip_groupchat);
        saveIpGroupChat = findViewById(R.id.ip_groupchat_save);
        groupChatLogToggle = findViewById(R.id.groupchat_log);
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

        vDevOptionRN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteManager.route(
                        DeveloperOptions.this,
                        ApplinkConst.SETTING_DEVELOPER_OPTIONS
                        .replace("{type}", RN_DEV_LOGGER)
                );
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

        sharedPreferences = getSharedPreferences(SP_REACT_DEVELOPMENT_MODE, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(IS_RELEASE_MODE)){
            boolean stateReleaseMode = sharedPreferences.getBoolean(IS_RELEASE_MODE, false);
            toggleReactDeveloperMode.setChecked(stateReleaseMode);
        }

        toggleReactDeveloperMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(DeveloperOptions.this, "React Native set to released mode", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_RELEASE_MODE, true);
                    editor.apply();
                } else {
                    Toast.makeText(DeveloperOptions.this, "React Native set to development mode", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(IS_RELEASE_MODE, false);
                    editor.apply();
                }
            }
        });

        if (sharedPreferences.contains(SP_REACT_ENABLE_SHAKE)){
            boolean stateDeveloperOptions = sharedPreferences.getBoolean(SP_REACT_ENABLE_SHAKE, false);
            toggleReactEnableDeveloperOptions.setChecked(stateDeveloperOptions);
        }

        toggleReactEnableDeveloperOptions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    Toast.makeText(DeveloperOptions.this, "React Native Dev Options is enabled", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SP_REACT_ENABLE_SHAKE, true);
                    editor.apply();
                } else {
                    Toast.makeText(DeveloperOptions.this, "React Native Dev Options is disabled", Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(SP_REACT_ENABLE_SHAKE, false);
                    editor.apply();
                }
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

        saveIpGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionSaveIpGroupChat();
            }
        });

        groupChatLogToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                actionLogGroupChat(isChecked);
            }
        });


        LocalCacheHandler groupChatPreference = new LocalCacheHandler(getApplicationContext(), GROUPCHAT_PREF);
        ipGroupChat.setText(groupChatPreference.getString(IP_GROUPCHAT,""));
        groupChatLogToggle.setChecked(groupChatPreference.getBoolean(LOG_GROUPCHAT, false));
    }

    private void actionLogGroupChat(boolean check) {
        LocalCacheHandler editor = new LocalCacheHandler(getApplicationContext(), GROUPCHAT_PREF);
        editor.putBoolean(LOG_GROUPCHAT, check);
        editor.applyEditor();
    }

    private void actionSaveIpGroupChat() {
        String ip = ipGroupChat.getText().toString();
        LocalCacheHandler editor = new LocalCacheHandler(getApplicationContext(), GROUPCHAT_PREF);
        if(TextUtils.isEmpty(ip)){
            editor.putString(IP_GROUPCHAT, null);
        }else {
            editor.putString(IP_GROUPCHAT, ip);
        }
        editor.applyEditor();
        Toast.makeText(this, ip + " saved", Toast.LENGTH_SHORT).show();
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
