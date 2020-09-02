package com.tokopedia.developer_options.presentation.activity;

import android.Manifest;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.NotificationManagerCompat;

import com.chuckerteam.chucker.api.Chucker;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.analyticsdebugger.debugger.ApplinkLogger;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.analyticsdebugger.debugger.IrisLogger;
import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.developer_options.R;
import com.tokopedia.developer_options.fakeresponse.FakeResponseActivityProvider;
import com.tokopedia.developer_options.notification.ReviewNotificationExample;
import com.tokopedia.developer_options.presentation.service.DeleteFirebaseTokenService;
import com.tokopedia.developer_options.remote_config.RemoteConfigFragmentActivity;
import com.tokopedia.developer_options.utils.OneOnClick;
import com.tokopedia.developer_options.utils.TimberWrapper;
import com.tokopedia.permissionchecker.PermissionCheckerHelper;
import com.tokopedia.url.Env;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import timber.log.Timber;

import static com.tokopedia.developer_options.config.DevOptConfig.CHUCK_ENABLED;
import static com.tokopedia.developer_options.config.DevOptConfig.IS_CHUCK_ENABLED;

public class DeveloperOptionActivity extends BaseActivity {

    public static final String GROUPCHAT_PREF = "com.tokopedia.groupchat.chatroom.view.presenter.GroupChatPresenter";
    public static final String IS_RELEASE_MODE = "IS_RELEASE_MODE";
    public static final String REMOTE_CONFIG_PREFIX = "remote_config_prefix";
    private static final String IP_GROUPCHAT = "ip_groupchat";
    private static final String LOG_GROUPCHAT = "log_groupchat";
    public static final String STAGING = "staging";
    public static final String LIVE = "live";
    public static final String CHANGEURL = "changeurl";
    public static final int DEFAULT_DELAY_UI_BLOCK = 500;

    private String CACHE_FREE_RETURN = "CACHE_FREE_RETURN";
    private String API_KEY_TRANSLATOR = "trnsl.1.1.20190508T115205Z.10630ca1780c554e.a7a33e218b8e806e8d38cb32f0ef91ae07d7ae49";

    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private TextView reviewNotifBtn;
    private AppCompatEditText remoteConfigPrefix;
    private AppCompatTextView remoteConfigStartButton;
    private ToggleButton toggleTimberDevOption;
    private Spinner spinnerEnvironmentChooser;

    private View sendTimberButton;
    private EditText editTextTimberMessage;

    private View sendFirebaseCrash;
    private EditText editTextFirebaseCrash;

    private View routeManagerButton;
    private EditText editTextRouteManager;
    private EditText editTextChangeVersionName;
    private EditText editTextChangeVersionCode;
    private View changeVersionButton;

    private TextView vGoToScreenRecorder;
    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private TextView vGoToTopAdsDebugger;
    private TextView vGoToApplinkDebugger;
    private TextView vGoToFpm;
    private TextView vGoToCassava;
    private TextView vGoToAnalytics;
    private TextView vGoToAnalyticsError;
    private TextView vGoToIrisSaveLogDB;
    private TextView vGoToIrisSendLogDB;
    private CheckBox toggleAnalytics;
    private CheckBox toggleApplinkNotif;
    private CheckBox toggleTopAdsNotif;
    private CheckBox toggleFpmNotif;
    private CheckBox toggleFpmAutoLogFile;

    private CheckBox toggleUiBlockDebugger;

    private AppCompatEditText ipGroupChat;
    private View saveIpGroupChat;
    private ToggleButton groupChatLogToggle;

    private UserSessionInterface userSession;
    private SharedPreferences groupChatSf;

    private boolean isUserEditEnvironment = true;
    private TextView accessTokenView;
    private TextView tvFakeResponse;

    private Button requestFcmToken;

    private PermissionCheckerHelper permissionCheckerHelper;
    private EditText etUIBlockDelay;

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
            boolean isChangeUrlApplink
                    = (uri.getPathSegments().size() == 3) && uri.getPathSegments().get(1).equals(CHANGEURL);

            if(isChangeUrlApplink) {
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

        resetOnBoarding = findViewById(R.id.reset_onboarding);
        testOnBoarding = findViewById(R.id.test_onboarding);

        vGoToScreenRecorder = findViewById(R.id.goto_screen_recorder);

        vGoTochuck = findViewById(R.id.goto_chuck);
        toggleChuck = findViewById(R.id.toggle_chuck);

        vGoToTopAdsDebugger = findViewById(R.id.goto_topads_debugger);
        vGoToApplinkDebugger = findViewById(R.id.goto_applink_debugger);
        vGoToFpm = findViewById(R.id.goto_fpm);
        vGoToCassava = findViewById(R.id.goto_cassava);
        vGoToAnalytics = findViewById(R.id.goto_analytics);
        vGoToAnalyticsError = findViewById(R.id.goto_analytics_error);
        vGoToIrisSaveLogDB = findViewById(R.id.goto_iris_save_log);
        vGoToIrisSendLogDB = findViewById(R.id.goto_iris_send_log);

        toggleAnalytics = findViewById(R.id.toggle_analytics);
        toggleApplinkNotif = findViewById(R.id.toggle_applink_debugger_notif);
        toggleTopAdsNotif = findViewById(R.id.toggle_topads_debugger_notif);
        toggleFpmNotif = findViewById(R.id.toggle_fpm_notif);
        toggleFpmAutoLogFile = findViewById(R.id.toggle_fpm_auto_file_log);

        etUIBlockDelay = findViewById(R.id.et_block_canary_delay);
        toggleUiBlockDebugger = findViewById(R.id.toggle_ui_block_debugger);

        remoteConfigPrefix = findViewById(R.id.remote_config_prefix);
        remoteConfigStartButton = findViewById(R.id.remote_config_start);

        reviewNotifBtn = findViewById(R.id.review_notification);

        TextView deviceId = findViewById(R.id.device_id);
        deviceId.setText(String.format("DEVICE ID: %s", GlobalConfig.DEVICE_ID));

        toggleTimberDevOption = findViewById(R.id.toggle_timber_dev_options);
        toggleTimberDevOption.setChecked(false);

        editTextTimberMessage = findViewById(R.id.et_timber_send);
        sendTimberButton = findViewById(R.id.btn_send_timber);

        editTextFirebaseCrash = findViewById(R.id.et_firebase_crash);
        sendFirebaseCrash = findViewById(R.id.btn_send_firebase_crash);

        editTextRouteManager = findViewById(R.id.et_route_manager);
        routeManagerButton = findViewById(R.id.btn_route_manager);

        editTextChangeVersionName = findViewById(R.id.et_change_version_name);
        editTextChangeVersionCode = findViewById(R.id.et_change_version_code);
        changeVersionButton = findViewById(R.id.btn_change_version);
        editTextChangeVersionName.setText(GlobalConfig.VERSION_NAME);
        editTextChangeVersionCode.setText(String.valueOf(GlobalConfig.VERSION_CODE));

        ipGroupChat = findViewById(R.id.ip_groupchat);
        saveIpGroupChat = findViewById(R.id.ip_groupchat_save);
        groupChatLogToggle = findViewById(R.id.groupchat_log);

        accessTokenView = findViewById(R.id.access_token);
        requestFcmToken = findViewById(R.id.requestFcmToken);

        spinnerEnvironmentChooser = findViewById(R.id.spinner_env_chooser);
        ArrayAdapter<Env> envSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Env.values());
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnvironmentChooser.setAdapter(envSpinnerAdapter);

        tvFakeResponse = findViewById(R.id.tv_fake_response);
    }

    private void initListener() {
        remoteConfigStartButton.setOnClickListener(v -> {
            Editable prefix = remoteConfigPrefix.getText();

            startRemoteConfigEditor(prefix != null ? prefix.toString() : "");
        });

        vForceCrash.setOnClickListener(v -> {
            throw new RuntimeException("Throw Runtime Exception");
        });

        resetOnBoarding.setOnClickListener(v -> {
            userSession.setFirstTimeUser(true);
            getSharedPreferences(CACHE_FREE_RETURN).edit().clear().apply();
            Toast.makeText(this, "Reset Onboarding", Toast.LENGTH_SHORT).show();
        });

        toggleTimberDevOption.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                String remoteConfigValue = TimberWrapper.REMOTE_CONFIG_KEY_LOG_CUSTOMER_APP;
                if (GlobalConfig.isSellerApp()) {
                    remoteConfigValue = TimberWrapper.REMOTE_CONFIG_KEY_LOG_SELLER_APP;
                }
                TimberWrapper.initByRemoteConfig(this, remoteConfigValue);
                Toast.makeText(this, "Timber is enabled", Toast.LENGTH_SHORT).show();
            } else {
                Timber.uprootAll();
                Timber.plant(new Timber.DebugTree() {
                    @Override
                    protected String createStackElementTag(@NotNull StackTraceElement element) {
                        return String.format("[%s:%s:%s]",
                                super.createStackElementTag(element),
                                element.getMethodName(),
                                element.getLineNumber());
                    }
                });
                Toast.makeText(this, "Timber is disabled", Toast.LENGTH_SHORT).show();
            }
        });

        sendTimberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timberMessage = editTextTimberMessage.getText().toString();
                if (TextUtils.isEmpty(timberMessage)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Timber message should not empty", Toast.LENGTH_SHORT).show();
                } else {
                    Timber.w(timberMessage);
                    Toast.makeText(DeveloperOptionActivity.this,
                            timberMessage + " has been sent" , Toast.LENGTH_LONG).show();
                }
            }
        });

        sendFirebaseCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crashMessage = editTextFirebaseCrash.getText().toString();
                if (TextUtils.isEmpty(crashMessage)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Crash message should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseCrashlytics.getInstance().recordException(new DeveloperOptionException(crashMessage));
                }
            }
        });

        routeManagerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String routeManagerString = editTextRouteManager.getText().toString();
                if (TextUtils.isEmpty(routeManagerString)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Route Manager String should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    RouteManager.route(DeveloperOptionActivity.this, routeManagerString);
                }
            }
        });

        changeVersionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String versionCode = editTextChangeVersionCode.getText().toString();
                String versionName = editTextChangeVersionName.getText().toString();
                if (TextUtils.isEmpty(versionCode)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Version Code should not be empty", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(versionName)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Version Name should not be empty", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        GlobalConfig.VERSION_NAME = versionName;
                        GlobalConfig.VERSION_CODE = Integer.parseInt(versionCode);
                        Toast.makeText(DeveloperOptionActivity.this,
                                "Version has been changed: " + versionName + " - " + versionCode, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(DeveloperOptionActivity.this,
                                e.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        SharedPreferences cache = getSharedPreferences(CHUCK_ENABLED);

        toggleChuck.setChecked(cache.getBoolean(IS_CHUCK_ENABLED, false));

        toggleChuck.setOnCheckedChangeListener((compoundButton, state) -> {
            cache.edit().putBoolean(IS_CHUCK_ENABLED, state).apply();
        });

        vGoToScreenRecorder.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                RouteManager.route(DeveloperOptionActivity.this, ApplinkConstInternalGlobal.SCREEN_RECORDER);
            }
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

        toggleTopAdsNotif.setChecked(TopAdsLogger.getInstance(this).isNotificationEnabled());
        toggleTopAdsNotif.setOnCheckedChangeListener((compoundButton, state) -> TopAdsLogger.getInstance(this).enableNotification(state));

        vGoToTopAdsDebugger.setOnClickListener(v -> TopAdsLogger.getInstance(this).openActivity());

        toggleApplinkNotif.setChecked(ApplinkLogger.getInstance(this).isNotificationEnabled());
        toggleApplinkNotif.setOnCheckedChangeListener((compoundButton, state) -> ApplinkLogger.getInstance(this).enableNotification(state));

        vGoToApplinkDebugger.setOnClickListener(v -> ApplinkLogger.getInstance(this).openActivity());

        toggleFpmNotif.setChecked(FpmLogger.getInstance().isNotificationEnabled());
        toggleFpmNotif.setOnCheckedChangeListener((compoundButton, state) -> FpmLogger.getInstance().enableNotification(state));

        toggleFpmAutoLogFile.setChecked(FpmLogger.getInstance().isAutoLogFileEnabled());
        toggleFpmAutoLogFile.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    requestPermissionWriteFile();
                } else {
                    FpmLogger.getInstance().enableAutoLogFile(false);
                }
            }
        });

        vGoToFpm.setOnClickListener(v -> FpmLogger.getInstance().openActivity());

        toggleAnalytics.setChecked(GtmLogger.getInstance(this).isNotificationEnabled());

        toggleAnalytics.setOnCheckedChangeListener((compoundButton, state) -> GtmLogger.getInstance(this).enableNotification(state));

        vGoToCassava.setOnClickListener(v -> GtmLogger.getInstance(this).navigateToValidator());
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

        toggleUiBlockDebugger.setOnCheckedChangeListener((compoundButton, state) -> {
            String delayStr = etUIBlockDelay.getText().toString();
            int delay = toInt(delayStr);
            if (delay <= 0) {
                delay = DEFAULT_DELAY_UI_BLOCK;
            }
            if (state) {
                Toast.makeText(DeveloperOptionActivity.this,
                        "(TODO) UI Block is enabled with delay " + delay , Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(DeveloperOptionActivity.this,
                        "(TODO) UI Block is disabled" , Toast.LENGTH_LONG).show();
            }
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

        tvFakeResponse.setOnClickListener(v -> {
            new FakeResponseActivityProvider().startActivity(this);
        });
    }

    private int toInt(String str) {
        try {
            return Integer.parseInt(str);
        }catch (Exception e) {
            return 0;
        }
    }

    private void requestPermissionWriteFile() {
        permissionCheckerHelper = new PermissionCheckerHelper();
        permissionCheckerHelper.checkPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionCheckerHelper.PermissionCheckListener() {
                    @Override
                    public void onPermissionDenied(@NotNull String permissionText) {
                        toggleFpmAutoLogFile.setChecked(false);
                    }

                    @Override
                    public void onNeverAskAgain(@NotNull String permissionText) {

                    }

                    @Override
                    public void onPermissionGranted() {
                        FpmLogger.getInstance().enableAutoLogFile(true);
                    }
                },
                "Please give storage access permission to write log file"
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionCheckerHelper.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
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

    private SharedPreferences getSharedPreferences(String name) {
        return getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private void initTranslator() {
        new com.tokopedia.translator.manager.TranslatorManager().init(this.getApplication(), API_KEY_TRANSLATOR);
    }

    private class DeveloperOptionException extends RuntimeException{
        public DeveloperOptionException(String message) {
            super(message);
        }
    }
}
