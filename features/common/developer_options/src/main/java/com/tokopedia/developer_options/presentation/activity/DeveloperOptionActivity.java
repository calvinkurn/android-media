package com.tokopedia.developer_options.presentation.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.readystatesoftware.chuck.Chuck;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.analytics.debugger.GtmLogger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.config.url.Env;
import com.tokopedia.config.url.TokopediaUrl;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.developer_options.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;


@DeepLink(ApplinkConst.DEVELOPER_OPTIONS)
public class DeveloperOptionActivity extends BaseActivity {

    public static final String CHUCK_ENABLED = "CHUCK_ENABLED";
    public static final String GROUPCHAT_PREF = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter";
    public static final String IS_CHUCK_ENABLED = "is_enable";
    public static final String SP_REACT_DEVELOPMENT_MODE = "SP_REACT_DEVELOPMENT_MODE";
    public static final String SP_REACT_ENABLE_SHAKE = "SP_REACT_ENABLE_SHAKE";
    public static final String IS_RELEASE_MODE = "IS_RELEASE_MODE";
    public static final String IS_ENABLE_SHAKE_REACT = "IS_ENABLE_SHAKE_REACT";
    public static final String RN_DEV_LOGGER = "rn_dev_logger";
    private static final String IP_GROUPCHAT = "ip_groupchat";
    private static final String LOG_GROUPCHAT = "log_groupchat";

    private String CACHE_FREE_RETURN = "CACHE_FREE_RETURN";

    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private TextView vDevOptionRN;
    private AppCompatEditText remoteConfigKeyEditText;
    private AppCompatEditText remoteConfigValueEditText;
    private AppCompatButton remoteConfigCheckBtn;
    private AppCompatButton remoteConfigSaveBtn;
    private ToggleButton toggleReactDeveloperMode;
    private ToggleButton toggleReactEnableDeveloperOptions;
    private Spinner spinnerEnvironmentChooser;

    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToAnalytics;
    private CheckBox toggleAnalytics;

    private AppCompatEditText ipGroupChat;
    private View saveIpGroupChat;
    private ToggleButton groupChatLogToggle;

    private UserSessionInterface userSession;
    private static TkpdCoreRouter tkpdCoreRouter;
    private SharedPreferences groupChatSf;

    private boolean isUserEditEnvironment = true;

    @Override
    public String getScreenName() {
        return getString(R.string.screen_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(GlobalConfig.isAllowDebuggingTools()) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_developer_options);

            userSession = new UserSession(this);

            setupView();
            initListener();
        } else {
            finish();
        }
    }

    private void setupView() {
        vForceCrash = findViewById(R.id.force_crash);
        vDevOptionRN = findViewById(R.id.rn_dev_options);

        resetOnBoarding = findViewById(R.id.reset_onboarding);
        testOnBoarding = findViewById(R.id.test_onboarding);

        vGoTochuck = findViewById(R.id.goto_chuck);
        toggleChuck = findViewById(R.id.toggle_chuck);

        vGoToAnalytics = findViewById(R.id.goto_analytics);
        toggleAnalytics = findViewById(R.id.toggle_analytics);

        remoteConfigKeyEditText = findViewById(R.id.et_remote_config_key);
        remoteConfigValueEditText = findViewById(R.id.et_remote_config_value);
        remoteConfigCheckBtn = findViewById(R.id.btn_remote_config_check);
        remoteConfigSaveBtn = findViewById(R.id.btn_remote_config_save);

        toggleReactDeveloperMode = findViewById(R.id.toggle_reactnative_mode);
        toggleReactEnableDeveloperOptions = findViewById(R.id.toggle_reactnative_dev_options);
        toggleReactEnableDeveloperOptions.setChecked(true);

        ipGroupChat = findViewById(R.id.ip_groupchat);
        saveIpGroupChat = findViewById(R.id.ip_groupchat_save);
        groupChatLogToggle = findViewById(R.id.groupchat_log);

        spinnerEnvironmentChooser = findViewById(R.id.spinner_env_chooser);
        ArrayAdapter<Env> envSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Env.values());
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnvironmentChooser.setAdapter(envSpinnerAdapter);

    }

    private void initListener() {

        vForceCrash.setOnClickListener(v -> {
            throw new RuntimeException("Throw Runtime Exception");
        });

        vDevOptionRN.setOnClickListener(v ->
                RouteManager.route(this,
                ApplinkConst.SETTING_DEVELOPER_OPTIONS
                        .replace("{type}", RN_DEV_LOGGER)
        ));

        resetOnBoarding.setOnClickListener(v -> {
            userSession.setFirstTimeUser(true);
            getSharedPreferences(CACHE_FREE_RETURN).edit().clear().apply();
            Toast.makeText(this, "Reset Onboarding", Toast.LENGTH_SHORT).show();
        });

        testOnBoarding.setOnClickListener(v -> startActivityForResult(InboxRouter.getFreeReturnOnBoardingActivityIntent(getBaseContext(), "1234"), 789));

        SharedPreferences rnSharedPref = getSharedPreferences(SP_REACT_DEVELOPMENT_MODE);
        if (rnSharedPref.contains(IS_RELEASE_MODE)){
            boolean stateReleaseMode = rnSharedPref.getBoolean(IS_RELEASE_MODE, false);
            toggleReactDeveloperMode.setChecked(stateReleaseMode);
        }

        toggleReactDeveloperMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = rnSharedPref.edit();
            if (isChecked){
                Toast.makeText(this, "React Native set to released mode", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_RELEASE_MODE, true).apply();
            } else {
                Toast.makeText(this, "React Native set to development mode", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_RELEASE_MODE, false).apply();
            }
        });

        SharedPreferences rnShakeReact = getSharedPreferences(SP_REACT_ENABLE_SHAKE);
        if (rnShakeReact.contains(IS_ENABLE_SHAKE_REACT)){
            boolean stateReleaseMode = rnShakeReact.getBoolean(IS_ENABLE_SHAKE_REACT, false);
            toggleReactEnableDeveloperOptions.setChecked(stateReleaseMode);
        }

        toggleReactEnableDeveloperOptions.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = rnShakeReact.edit();
            if (isChecked){
                Toast.makeText(this, "RN Dev Options is disabled", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_ENABLE_SHAKE_REACT, true);
                editor.apply();
            } else {
                Toast.makeText(this, "RN Dev Options is enabled", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_ENABLE_SHAKE_REACT, false);
                editor.apply();
            }
        });

        SharedPreferences cache = getSharedPreferences(CHUCK_ENABLED);

        toggleChuck.setChecked(cache.getBoolean(IS_CHUCK_ENABLED, false));

        toggleChuck.setOnCheckedChangeListener((compoundButton, state) -> {
            cache.edit().putBoolean(IS_CHUCK_ENABLED, state).apply();
        });

        vGoTochuck.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                startActivity(Chuck.getLaunchIntent(getApplicationContext()));
            }
        });

        toggleAnalytics.setChecked(GtmLogger.getInstance(this).isNotificationEnabled());

        toggleAnalytics.setOnCheckedChangeListener((compoundButton, state) -> GtmLogger.getInstance(this).enableNotification(state));

        vGoToAnalytics.setOnClickListener(v -> GtmLogger.getInstance(DeveloperOptionActivity.this).openActivity());

        remoteConfigCheckBtn.setOnClickListener(view -> actionCheckValueRemoteConfig());
        remoteConfigSaveBtn.setOnClickListener(view -> actionSaveValueRemoteConfig());

        saveIpGroupChat.setOnClickListener(v -> actionSaveIpGroupChat());
        groupChatLogToggle.setOnCheckedChangeListener((buttonView, isChecked) -> actionLogGroupChat(isChecked));

        groupChatSf = getSharedPreferences(GROUPCHAT_PREF);

        ipGroupChat.setText(groupChatSf.getString(IP_GROUPCHAT,""));
        groupChatLogToggle.setChecked(groupChatSf.getBoolean(LOG_GROUPCHAT, false));

        Env currentEnv = TokopediaUrl.Companion.getInstance().getTYPE();
        for(int i = 0; i < Env.values().length; i++) {
            if(currentEnv == Env.values()[i]) {
                isUserEditEnvironment = false;
                spinnerEnvironmentChooser.setSelection(i);
                break;
            }
        }

        spinnerEnvironmentChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isUserEditEnvironment) {
                    TokopediaUrl.Companion.setEnvironment(DeveloperOptionActivity.this, Env.values()[position]);
                    TokopediaUrl.Companion.deleteInstance();
                    TokopediaUrl.Companion.init(DeveloperOptionActivity.this);
                    userSession.logoutSession();
                    Toast.makeText(DeveloperOptionActivity.this, "Please Restart the App", Toast.LENGTH_SHORT).show();
                }
                isUserEditEnvironment = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void actionCheckValueRemoteConfig() {
        String key = remoteConfigKeyEditText.getText().toString().trim();
        if (key.isEmpty()) {
            Toast.makeText(this, "Please type the key", Toast.LENGTH_SHORT).show();
            return;
        }
        remoteConfigValueEditText.setText(String.valueOf(coreRouter(getApplicationContext()).getStringRemoteConfig(key)));
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

    private void actionSaveIpGroupChat() {
        String ip = ipGroupChat.getText().toString();
        SharedPreferences.Editor editor = groupChatSf.edit();
        if(TextUtils.isEmpty(ip)){
            editor.putString(IP_GROUPCHAT, null);
        }else {
            editor.putString(IP_GROUPCHAT, ip);
        }
        editor.apply();
        Toast.makeText(this, ip + " saved", Toast.LENGTH_SHORT).show();
    }

    private void actionLogGroupChat(boolean check) {
        SharedPreferences.Editor editor = groupChatSf.edit();
        editor.putBoolean(LOG_GROUPCHAT, check);
        editor.apply();
    }

    private static TkpdCoreRouter coreRouter(Context applicationContext) {
        if (tkpdCoreRouter == null) {
            tkpdCoreRouter = (TkpdCoreRouter) applicationContext;
        }
        return tkpdCoreRouter;
    }

    private SharedPreferences getSharedPreferences(String name) {
        return getSharedPreferences(name, Context.MODE_PRIVATE);
    }
}
