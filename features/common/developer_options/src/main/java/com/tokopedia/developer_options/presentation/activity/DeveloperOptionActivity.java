package com.tokopedia.developer_options.presentation.activity;

import android.app.Activity;
import android.app.Notification;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationManagerCompat;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.chuckerteam.chucker.api.Chucker;
import com.tokopedia.analytics.debugger.FpmLogger;
import com.tokopedia.developer_options.utils.OneOnClick;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.analyticsdebugger.debugger.IrisLogger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.developer_options.R;
import com.tokopedia.developer_options.presentation.service.DeleteFirebaseTokenService;
import com.tokopedia.developer_options.notification.ReviewNotificationExample;
import com.tokopedia.developer_options.remote_config.RemoteConfigFragmentActivity;
import com.tokopedia.url.Env;
import com.tokopedia.url.TokopediaUrl;
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
    public static final String REMOTE_CONFIG_PREFIX = "remote_config_prefix";
    private static final String IP_GROUPCHAT = "ip_groupchat";
    private static final String LOG_GROUPCHAT = "log_groupchat";
    public static final String STAGING = "staging";
    public static final String LIVE = "live";
    public static final String DEVELOPEROPTION = "developeroption";

    private String CACHE_FREE_RETURN = "CACHE_FREE_RETURN";
    private String API_KEY_TRANSLATOR = "trnsl.1.1.20190508T115205Z.10630ca1780c554e.a7a33e218b8e806e8d38cb32f0ef91ae07d7ae49";

    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private TextView vDevOptionRN;
    private TextView reviewNotifBtn;
    private AppCompatEditText remoteConfigPrefix;
    private AppCompatTextView remoteConfigStartButton;
    private ToggleButton toggleReactDeveloperMode;
    private ToggleButton toggleReactEnableDeveloperOptions;
    private Spinner spinnerEnvironmentChooser;

    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToFpm;
    private TextView vGoToAnalytics;
    private TextView vGoToAnalyticsError;
    private TextView vGoToIrisSaveLogDB;
    private TextView vGoToIrisSendLogDB;
    private CheckBox toggleAnalytics;
    private CheckBox toggleFpm;

    private CheckBox toggleUiBlockDebugger;

    private AppCompatEditText ipGroupChat;
    private View saveIpGroupChat;
    private ToggleButton groupChatLogToggle;

    private UserSessionInterface userSession;
    private static TkpdCoreRouter tkpdCoreRouter;
    private SharedPreferences groupChatSf;

    private boolean isUserEditEnvironment = true;
    private TextView accessTokenView;

    private Button requestFcmToken;

    @Override
    public String getScreenName() {
        return getString(R.string.screen_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalConfig.isAllowDebuggingTools() && getIntent()!=null && getIntent().getData()!=null) {
            userSession = new UserSession(this);
            Uri uri = getIntent().getData();
            if(uri.getHost().equals(DEVELOPEROPTION)) {
                handleUri(uri);
            } else {
                setContentView(R.layout.activity_developer_options);
                setupView();
                initListener();
                initTranslator();
            }
        } else {
            finish();
        }
    }

    private void handleUri(Uri uri) {
        if(uri.getLastPathSegment().startsWith(STAGING)){
            TokopediaUrl.Companion.setEnvironment(DeveloperOptionActivity.this, Env.STAGING);
        } else if (uri.getLastPathSegment().startsWith(LIVE)){
            TokopediaUrl.Companion.setEnvironment(DeveloperOptionActivity.this, Env.LIVE);
        }
        TokopediaUrl.Companion.deleteInstance();
        TokopediaUrl.Companion.init(DeveloperOptionActivity.this);
        userSession.logoutSession();
        new Handler().postDelayed(() -> restart(DeveloperOptionActivity.this), 500);
    }

    /**
     * Call to restart the application process using the specified intents.
     * <p>
     * Behavior of the current process after invoking this method is undefined.
     */
    private void restart(Context context) {
        Intent intent = RouteManager.getIntent(context, ApplinkConst.HOME)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        if (context instanceof Activity) {
            ((Activity) context).finish();
        }
        Process.killProcess(Process.myPid());
    }

    private void setupView() {
        vForceCrash = findViewById(R.id.force_crash);
        vDevOptionRN = findViewById(R.id.rn_dev_options);

        resetOnBoarding = findViewById(R.id.reset_onboarding);
        testOnBoarding = findViewById(R.id.test_onboarding);

        vGoTochuck = findViewById(R.id.goto_chuck);
        toggleChuck = findViewById(R.id.toggle_chuck);

        vGoToFpm = findViewById(R.id.goto_fpm);
        vGoToAnalytics = findViewById(R.id.goto_analytics);
        vGoToAnalyticsError = findViewById(R.id.goto_analytics_error);
        vGoToIrisSaveLogDB = findViewById(R.id.goto_iris_save_log);
        vGoToIrisSendLogDB = findViewById(R.id.goto_iris_send_log);

        toggleAnalytics = findViewById(R.id.toggle_analytics);
        toggleFpm = findViewById(R.id.toggle_fpm);

        toggleUiBlockDebugger = findViewById(R.id.toggle_ui_block_debugger);

        remoteConfigPrefix = findViewById(R.id.remote_config_prefix);
        remoteConfigStartButton = findViewById(R.id.remote_config_start);

        reviewNotifBtn = findViewById(R.id.review_notification);

        TextView deviceId = findViewById(R.id.device_id);
        deviceId.setText(String.format("DEVICE ID: %s", GlobalConfig.DEVICE_ID));

        toggleReactDeveloperMode = findViewById(R.id.toggle_reactnative_mode);
        toggleReactEnableDeveloperOptions = findViewById(R.id.toggle_reactnative_dev_options);
        toggleReactEnableDeveloperOptions.setChecked(true);

        ipGroupChat = findViewById(R.id.ip_groupchat);
        saveIpGroupChat = findViewById(R.id.ip_groupchat_save);
        groupChatLogToggle = findViewById(R.id.groupchat_log);

        accessTokenView = findViewById(R.id.access_token);
        requestFcmToken = findViewById(R.id.requestFcmToken);

        spinnerEnvironmentChooser = findViewById(R.id.spinner_env_chooser);
        ArrayAdapter<Env> envSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Env.values());
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnvironmentChooser.setAdapter(envSpinnerAdapter);

    }

    private void initListener() {
        remoteConfigStartButton.setOnClickListener(v -> {
            Editable prefix = remoteConfigPrefix.getText();

            startRemoteConfigEditor(prefix != null ? prefix.toString() : "");
        });

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

        SharedPreferences rnSharedPref = getSharedPreferences(SP_REACT_DEVELOPMENT_MODE);
        if (rnSharedPref.contains(IS_RELEASE_MODE)) {
            boolean stateReleaseMode = rnSharedPref.getBoolean(IS_RELEASE_MODE, false);
            toggleReactDeveloperMode.setChecked(stateReleaseMode);
        }

        toggleReactDeveloperMode.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = rnSharedPref.edit();
            if (isChecked) {
                Toast.makeText(this, "React Native set to released mode", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_RELEASE_MODE, true).apply();
            } else {
                Toast.makeText(this, "React Native set to development mode", Toast.LENGTH_SHORT).show();
                editor.putBoolean(IS_RELEASE_MODE, false).apply();
            }
        });

        SharedPreferences rnShakeReact = getSharedPreferences(SP_REACT_ENABLE_SHAKE);
        if (rnShakeReact.contains(IS_ENABLE_SHAKE_REACT)) {
            boolean stateReleaseMode = rnShakeReact.getBoolean(IS_ENABLE_SHAKE_REACT, false);
            toggleReactEnableDeveloperOptions.setChecked(stateReleaseMode);
        }

        toggleReactEnableDeveloperOptions.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            SharedPreferences.Editor editor = rnShakeReact.edit();
            if (isChecked) {
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
                startActivity(Chucker.getLaunchIntent(getApplicationContext(), Chucker.SCREEN_HTTP));
            }
        });

        reviewNotifBtn.setOnClickListener(v ->{
            Notification notifReview = ReviewNotificationExample.createReviewNotification(getApplicationContext());
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            notificationManagerCompat.notify(777,notifReview);
                });

        toggleFpm.setChecked(FpmLogger.getInstance(this).isNotificationEnabled());

        toggleFpm.setOnCheckedChangeListener((compoundButton, state) -> FpmLogger.getInstance(this).enableNotification(state));

        vGoToFpm.setOnClickListener(v -> FpmLogger.getInstance(DeveloperOptionActivity.this).openActivity());

        toggleAnalytics.setChecked(GtmLogger.getInstance(this).isNotificationEnabled());

        toggleAnalytics.setOnCheckedChangeListener((compoundButton, state) -> GtmLogger.getInstance(this).enableNotification(state));

        vGoToAnalytics.setOnClickListener(v -> GtmLogger.getInstance(DeveloperOptionActivity.this).openActivity());
        vGoToAnalyticsError.setOnClickListener(v -> {
            GtmLogger.getInstance(DeveloperOptionActivity.this).openErrorActivity();
        });

        vGoToIrisSaveLogDB.setOnClickListener(v -> {
            IrisLogger.getInstance(DeveloperOptionActivity.this).openSaveActivity();
        });

        vGoToIrisSendLogDB.setOnClickListener(v -> {
            IrisLogger.getInstance(DeveloperOptionActivity.this).openSendActivity();
        });

        SharedPreferences uiBlockDebuggerPref = getSharedPreferences("UI_BLOCK_DEBUGGER");
        toggleUiBlockDebugger.setChecked(uiBlockDebuggerPref.getBoolean("isEnabled", false));
        toggleUiBlockDebugger.setOnCheckedChangeListener((compoundButton, state) -> {
            uiBlockDebuggerPref.edit().putBoolean("isEnabled", state).apply();
        });

        saveIpGroupChat.setOnClickListener(v -> actionSaveIpGroupChat());
        groupChatLogToggle.setOnCheckedChangeListener((buttonView, isChecked) -> actionLogGroupChat(isChecked));

        groupChatSf = getSharedPreferences(GROUPCHAT_PREF);

        ipGroupChat.setText(groupChatSf.getString(IP_GROUPCHAT, ""));
        groupChatLogToggle.setChecked(groupChatSf.getBoolean(LOG_GROUPCHAT, false));

        Env currentEnv = TokopediaUrl.Companion.getInstance().getTYPE();
        for (int i = 0; i < Env.values().length; i++) {
            if (currentEnv == Env.values()[i]) {
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
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        accessTokenView.setText("Access token : " + userSession.getAccessToken());
        accessTokenView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Copied Text", userSession.getAccessToken());
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
        });

        requestFcmToken.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteFirebaseTokenService.class);
            startService(intent);
        });
    }

    private void startRemoteConfigEditor(String prefix) {
        Intent intent = new Intent(DeveloperOptionActivity.this, RemoteConfigFragmentActivity.class);
        intent.putExtra(REMOTE_CONFIG_PREFIX, prefix.trim());

        startActivity(intent);
    }

    private void actionSaveIpGroupChat() {
        String ip = ipGroupChat.getText().toString();
        SharedPreferences.Editor editor = groupChatSf.edit();
        if (TextUtils.isEmpty(ip)) {
            editor.putString(IP_GROUPCHAT, null);
        } else {
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

    private void initTranslator() {
//        new com.tokopedia.translator.manager.TranslatorManager().init(this.getApplication(), API_KEY_TRANSLATOR);
    }
}
