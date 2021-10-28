package com.tokopedia.developer_options.presentation.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import com.chuckerteam.chucker.api.Chucker;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.tokopedia.abstraction.base.view.activity.BaseActivity;
import com.tokopedia.analyticsdebugger.cassava.debugger.AnalyticsDebuggerActivity;
import com.tokopedia.analyticsdebugger.cassava.validator.MainValidatorActivity;
import com.tokopedia.analyticsdebugger.debugger.ApplinkLogger;
import com.tokopedia.analyticsdebugger.debugger.FpmLogger;
import com.tokopedia.analyticsdebugger.debugger.GtmLogger;
import com.tokopedia.analyticsdebugger.debugger.IrisLogger;
import com.tokopedia.analyticsdebugger.debugger.TopAdsLogger;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.coachmark.CoachMark2;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.developer_options.R;
import com.tokopedia.developer_options.ab_test_rollence.AbTestRollenceConfigFragmentActivity;
import com.tokopedia.developer_options.config.DevOptConfig;
import com.tokopedia.developer_options.fakeresponse.FakeResponseActivityProvider;
import com.tokopedia.developer_options.presentation.service.DeleteFirebaseTokenService;
import com.tokopedia.developer_options.remote_config.RemoteConfigFragmentActivity;
import com.tokopedia.developer_options.sharedpref.SharedPrefActivity;
import com.tokopedia.developer_options.utils.OneOnClick;
import com.tokopedia.developer_options.utils.SellerInAppReview;
import com.tokopedia.devicefingerprint.appauth.AppAuthKt;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigInstance;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.remoteconfig.RollenceKey;
import com.tokopedia.unifycomponents.TextFieldUnify;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.utils.permission.PermissionCheckerHelper;
import com.tokopedia.url.Env;
import com.tokopedia.url.TokopediaUrl;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.user.session.UserSessionInterface;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tokopedia.remoteconfig.RollenceKey.NAVIGATION_EXP_TOP_NAV;
import static com.tokopedia.remoteconfig.RollenceKey.NAVIGATION_VARIANT_REVAMP;

public class DeveloperOptionActivity extends BaseActivity {
    public static final String IS_RELEASE_MODE = "IS_RELEASE_MODE";
    public static final String REMOTE_CONFIG_PREFIX = "remote_config_prefix";
    public static final String SHARED_PREF_FILE = "shared_pref_file";
    public static final String STAGING = "staging";
    public static final String LIVE = "live";
    public static final String CHANGEURL = "changeurl";
    public static final String URI_HOME_MACROBENCHMARK = "home-macrobenchmark";
    public static final String URI_COACHMARK = "coachmark";
    public static final String URI_COACHMARK_ENABLE = "enable";
    public static final String URI_COACHMARK_DISABLE = "disable";

    String KEY_FIRST_VIEW_NAVIGATION = "KEY_FIRST_VIEW_NAVIGATION";
    String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING";
    String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1";
    String KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2 = "KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2";
    String KEY_P1_DONE_AS_NON_LOGIN = "KEY_P1_DONE_AS_NON_LOGIN";


    String PREF_KEY_HOME_COACHMARK = "PREF_KEY_HOME_COACHMARK";
    String PREF_KEY_HOME_COACHMARK_NAV = "PREF_KEY_HOME_COACHMARK_NAV";
    String PREF_KEY_HOME_COACHMARK_INBOX = "PREF_KEY_HOME_COACHMARK_INBOX";
    String PREF_KEY_HOME_COACHMARK_BALANCE = "PREF_KEY_HOME_COACHMARK_BALANCE";


    String PREFERENCE_NAME = "coahmark_choose_address";
    String EXTRA_IS_COACHMARK = "EXTRA_IS_COACHMARK";

    String EXP_TOP_NAV = NAVIGATION_EXP_TOP_NAV;
    String VARIANT_REVAMP = NAVIGATION_VARIANT_REVAMP;

    private final String LEAK_CANARY_TOGGLE_SP_NAME = "mainapp_leakcanary_toggle";
    private final String LEAK_CANARY_TOGGLE_KEY = "key_leakcanary_toggle";
    private final boolean LEAK_CANARY_DEFAULT_TOGGLE = true;

    private String CACHE_FREE_RETURN = "CACHE_FREE_RETURN";
    private String API_KEY_TRANSLATOR = "trnsl.1.1.20190508T115205Z.10630ca1780c554e.a7a33e218b8e806e8d38cb32f0ef91ae07d7ae49";

    private UnifyButton resetOnBoarding;
    private UnifyButton vForceCrash;
    private TextFieldUnify remoteConfigPrefix;
    private UnifyButton remoteConfigStartButton;
    private UnifyButton abTestRollenceEditorStartButton;
    private Spinner spinnerEnvironmentChooser;

    private View sendTimberButton;
    private View sharedPrefButton;
    private TextFieldUnify editTextTimberMessage;

    private UnifyButton sendFirebaseCrash;
    private TextFieldUnify editTextFirebaseCrash;

    private UnifyButton routeManagerButton;
    private TextFieldUnify editTextRouteManager;
    private TextFieldUnify editTextChangeVersionName;
    private TextFieldUnify editTextChangeVersionCode;
    private View changeVersionButton;

    private UnifyButton vGoToScreenRecorder;
    private UnifyButton vGoTochuck;
    private CheckBox toggleChuck;

    private UnifyButton vGoToTopAdsDebugger;
    private UnifyButton vGoToApplinkDebugger;
    private UnifyButton vGoToFpm;
    private UnifyButton vGoToCassava;
    private UnifyButton vGoToAnalytics;
    private UnifyButton vGoToIrisSaveLogDB;
    private UnifyButton vGoToIrisSendLogDB;
    private CheckBox toggleDarkMode;
    private CheckBox toggleAnalytics;
    private CheckBox toggleApplinkNotif;
    private CheckBox toggleTopAdsNotif;
    private CheckBox toggleFpmNotif;
    private CheckBox toggleFpmAutoLogFile;
    private CheckBox toggleSellerAppReview;
    private CheckBox toggleLeakCanary;

    private UserSessionInterface userSession;

    private boolean isUserEditEnvironment = true;
    private UnifyButton accessTokenView;
    private UnifyButton appAuthSecretView;
    private UnifyButton tvFakeResponse;

    private UnifyButton requestFcmToken;

    private PermissionCheckerHelper permissionCheckerHelper;

    private UnifyButton alwaysOldBalanceWidget;

    @Override
    public String getScreenName() {
        return getString(R.string.screen_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GlobalConfig.isAllowDebuggingTools()) {
            userSession = new UserSession(this);

            Intent intent = getIntent();
            Uri uri = null;
            boolean isChangeUrlApplink = false;
            boolean isCoachmarkApplink = false;
            boolean isHomeMacrobenchmarkApplink = false;
            if (intent != null) {
                uri = intent.getData();
                if (uri != null) {
                    isChangeUrlApplink = (uri.getPathSegments().size() == 3) &&
                            uri.getPathSegments().get(1).equals(CHANGEURL);
                    isCoachmarkApplink = (uri.getPathSegments().size() == 3) &&
                            uri.getPathSegments().get(1).equals(URI_COACHMARK);
                    isHomeMacrobenchmarkApplink = (uri.getPathSegments().size() == 3) &&
                            uri.getPathSegments().get(1).equals(URI_HOME_MACROBENCHMARK);
                }
            }
            if (isChangeUrlApplink) {
                handleUri(uri);
            } else if (isHomeMacrobenchmarkApplink) {
                handleHomeMacrobenchmarkUri(userSession);
            } else if (isCoachmarkApplink) {
                handleCoachmarkUri(uri);
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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        System.out.println("++ line 206");
        alwaysOldBalanceWidget.setEllipsize(null);
    }

    private void handleUri(Uri uri) {
        if (uri.getLastPathSegment().startsWith(STAGING)) {
            TokopediaUrl.Companion.setEnvironment(DeveloperOptionActivity.this, Env.STAGING);
        } else if (uri.getLastPathSegment().startsWith(LIVE)) {
            TokopediaUrl.Companion.setEnvironment(DeveloperOptionActivity.this, Env.LIVE);
        }
        TokopediaUrl.Companion.deleteInstance();
        TokopediaUrl.Companion.init(DeveloperOptionActivity.this);
        userSession.logoutSession();
        new Handler().postDelayed(() -> restart(DeveloperOptionActivity.this), 500);
    }

    private void handleCoachmarkUri(Uri uri) {
        if (uri.getLastPathSegment().startsWith(URI_COACHMARK_DISABLE)) {
            CoachMark2.Companion.setCoachmmarkShowAllowed(false);

            SharedPreferences sharedPrefs = getSharedPreferences(
                    KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE);
            sharedPrefs.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
                    .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
                    .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
                    .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true).apply();
        }
        finish();
    }

    private void handleHomeMacrobenchmarkUri(UserSessionInterface userSession) {
        CoachMark2.Companion.setCoachmmarkShowAllowed(false);

        SharedPreferences sharedPrefs = getSharedPreferences(
                KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE);
        sharedPrefs.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, false)
                .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, false)
                .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, true).apply();

        userSession.setFirstTimeUserOnboarding(false);

        RemoteConfigInstance.getInstance().getABTestPlatform().setString(EXP_TOP_NAV, VARIANT_REVAMP);
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

        vGoToScreenRecorder = findViewById(R.id.goto_screen_recorder);

        vGoTochuck = findViewById(R.id.goto_chuck);
        toggleChuck = findViewById(R.id.toggle_chuck);

        vGoToTopAdsDebugger = findViewById(R.id.goto_topads_debugger);
        vGoToApplinkDebugger = findViewById(R.id.goto_applink_debugger);
        vGoToFpm = findViewById(R.id.goto_fpm);
        vGoToCassava = findViewById(R.id.goto_cassava);
        vGoToAnalytics = findViewById(R.id.goto_analytics);
        vGoToIrisSaveLogDB = findViewById(R.id.goto_iris_save_log);
        vGoToIrisSendLogDB = findViewById(R.id.goto_iris_send_log);

        toggleDarkMode = findViewById(R.id.toggle_dark_mode);
        toggleAnalytics = findViewById(R.id.toggle_analytics);
        toggleApplinkNotif = findViewById(R.id.toggle_applink_debugger_notif);
        toggleTopAdsNotif = findViewById(R.id.toggle_topads_debugger_notif);
        toggleFpmNotif = findViewById(R.id.toggle_fpm_notif);
        toggleFpmAutoLogFile = findViewById(R.id.toggle_fpm_auto_file_log);
        toggleSellerAppReview = findViewById(R.id.toggle_seller_app_review);
        toggleLeakCanary = findViewById(R.id.toggle_leak_canary);

        remoteConfigPrefix = findViewById(R.id.remote_config_prefix);
        remoteConfigStartButton = findViewById(R.id.remote_config_start);
        abTestRollenceEditorStartButton = findViewById(R.id.ab_test_rollence_editor_start);

        TextView deviceId = findViewById(R.id.device_id);
        deviceId.setText(String.format("DEVICE ID: %s", GlobalConfig.DEVICE_ID));

        editTextTimberMessage = findViewById(R.id.et_timber_send);
        sendTimberButton = findViewById(R.id.btn_send_timber);
        sharedPrefButton = findViewById(R.id.btn_shared_pref_editor);

        editTextFirebaseCrash = findViewById(R.id.et_firebase_crash);
        sendFirebaseCrash = findViewById(R.id.btn_send_firebase_crash);

        editTextRouteManager = findViewById(R.id.et_route_manager);
        routeManagerButton = findViewById(R.id.btn_route_manager);

        editTextChangeVersionName = findViewById(R.id.et_change_version_name);
        editTextChangeVersionCode = findViewById(R.id.et_change_version_code);
        changeVersionButton = findViewById(R.id.btn_change_version);
        editTextChangeVersionName.getTextFieldInput().setText(GlobalConfig.VERSION_NAME);
        editTextChangeVersionCode.getTextFieldInput().setText(String.valueOf(GlobalConfig.VERSION_CODE));

        accessTokenView = findViewById(R.id.access_token);
        appAuthSecretView = findViewById(R.id.app_auth_secret);
        appAuthSecretView.setVisibility(View.GONE);
        requestFcmToken = findViewById(R.id.requestFcmToken);

        spinnerEnvironmentChooser = findViewById(R.id.spinner_env_chooser);
        ArrayAdapter<Env> envSpinnerAdapter = new ArrayAdapter<>(this, R.layout.customized_spinner_item, Env.values());
        envSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEnvironmentChooser.setAdapter(envSpinnerAdapter);

        tvFakeResponse = findViewById(R.id.tv_fake_response);

        UnifyButton buttonResetOnboardingNavigation = findViewById(R.id.resetOnboardingNavigation);
        UnifyButton alwaysOldButton = findViewById(R.id.buttonAlwaysOldNavigation);
        UnifyButton alwaysNewNavigation = findViewById(R.id.buttonAlwaysNewNavigation);
        alwaysOldBalanceWidget = findViewById(R.id.buttonAlwaysOldBalanceWidget);
        System.out.println("++ line 332");
        UnifyButton alwaysNewBalanceWidget = findViewById(R.id.buttonAlwaysNewBalanceWidget);

        setupNewInboxAbButton();

        TextFieldUnify inputRollenceKey = findViewById(R.id.input_rollence_key);
        TextFieldUnify inputRollenceVariant = findViewById(R.id.input_rollence_variant);
        UnifyButton btnApplyRollence = findViewById(R.id.btn_apply_rollence);

        findViewById(R.id.pdp_dev_btn).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductDetailDevActivity.class);
            startActivity(intent);
        });

        buttonResetOnboardingNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPrefs = getSharedPreferences(
                        KEY_FIRST_VIEW_NAVIGATION, Context.MODE_PRIVATE);
                sharedPrefs.edit().putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING, true)
                        .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P1, true)
                        .putBoolean(KEY_FIRST_VIEW_NAVIGATION_ONBOARDING_NAV_P2, true)
                        .putBoolean(KEY_P1_DONE_AS_NON_LOGIN, false).apply();


                SharedPreferences homePref = getSharedPreferences(
                        PREF_KEY_HOME_COACHMARK, Context.MODE_PRIVATE);
                homePref.edit().putBoolean(PREF_KEY_HOME_COACHMARK_NAV, false)
                        .putBoolean(PREF_KEY_HOME_COACHMARK_INBOX, false)
                        .putBoolean(PREF_KEY_HOME_COACHMARK_BALANCE, false).apply();


                SharedPreferences chooseAddressPref = getSharedPreferences(
                        PREFERENCE_NAME, Context.MODE_PRIVATE);
                chooseAddressPref.edit().putBoolean(EXTRA_IS_COACHMARK, true).apply();

                Toast.makeText(DeveloperOptionActivity.this, "Onboarding and home coachmark reset ssuccessfully!", Toast.LENGTH_SHORT).show();
            }
        });

        String EXP_TOP_NAV = RollenceKey.NAVIGATION_EXP_TOP_NAV;
        String VARIANT_OLD = RollenceKey.NAVIGATION_VARIANT_OLD;
        String VARIANT_REVAMP = RollenceKey.NAVIGATION_VARIANT_REVAMP;

        String EXP_BALANCE_WIDGET = RollenceKey.BALANCE_EXP;
        String BALANCE_WIDGET_VARIANT_OLD = RollenceKey.BALANCE_VARIANT_OLD;
        String BALANCE_WIDGET_VARIANT_REVAMP = RollenceKey.BALANCE_VARIANT_NEW;

        alwaysOldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteConfigInstance.getInstance().getABTestPlatform().setString(EXP_TOP_NAV, VARIANT_OLD);
                Toast.makeText(DeveloperOptionActivity.this, "Navigation: Old", Toast.LENGTH_SHORT).show();
            }
        });

        alwaysNewNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteConfigInstance.getInstance().getABTestPlatform().setString(EXP_TOP_NAV, VARIANT_REVAMP);
                Toast.makeText(DeveloperOptionActivity.this, "Navigation: Revamped", Toast.LENGTH_SHORT).show();
            }
        });

        alwaysOldBalanceWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteConfigInstance.getInstance().getABTestPlatform().setString(EXP_BALANCE_WIDGET, BALANCE_WIDGET_VARIANT_OLD);
                Toast.makeText(DeveloperOptionActivity.this, "balance widget: Old", Toast.LENGTH_SHORT).show();
            }
        });

        alwaysNewBalanceWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoteConfigInstance.getInstance().getABTestPlatform().setString(EXP_BALANCE_WIDGET, BALANCE_WIDGET_VARIANT_REVAMP);
                Toast.makeText(DeveloperOptionActivity.this, "balance widget: Revamped", Toast.LENGTH_SHORT).show();
            }
        });

        btnApplyRollence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputRollenceKey.getTextFieldInput().getText().length() < 1) {
                    Toast.makeText(DeveloperOptionActivity.this, "Please Insert Rollence Key", Toast.LENGTH_SHORT).show();
                } else if (inputRollenceVariant.getTextFieldInput().getText().length() < 1) {
                    Toast.makeText(DeveloperOptionActivity.this, "Please Insert Rollence Variant", Toast.LENGTH_SHORT).show();
                } else {
                    RemoteConfigInstance.getInstance().getABTestPlatform().setString(inputRollenceKey.getTextFieldInput().getText().toString().trim(), inputRollenceVariant.getTextFieldInput().getText().toString().trim());
                    Toast.makeText(DeveloperOptionActivity.this, "Rollence Key Applied", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.btn_always_old_cart_checkout).setOnClickListener(v -> {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
            remoteConfig.setString(RemoteConfigKey.ENABLE_CART_CHECKOUT_BUNDLING, "false");
            Toast.makeText(DeveloperOptionActivity.this, "Applied", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btn_always_new_cart_checkout).setOnClickListener(v -> {
            RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
            remoteConfig.setString(RemoteConfigKey.ENABLE_CART_CHECKOUT_BUNDLING, "true");
            Toast.makeText(DeveloperOptionActivity.this, "Applied", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupNewInboxAbButton() {
        UnifyButton oldInboxBtn = findViewById(R.id.btn_always_old_inbox);
        UnifyButton newInboxBtn = findViewById(R.id.btn_always_new_inbox);

        oldInboxBtn.setOnClickListener(v -> {
            RemoteConfigInstance.getInstance()
                    .getABTestPlatform()
                    .setString(
                            RollenceKey.KEY_AB_INBOX_REVAMP,
                            RollenceKey.VARIANT_OLD_INBOX
                    );
            Toast.makeText(DeveloperOptionActivity.this, "Inbox: Old", Toast.LENGTH_SHORT).show();
        });
        newInboxBtn.setOnClickListener(v -> {
            RemoteConfigInstance.getInstance()
                    .getABTestPlatform()
                    .setString(
                            RollenceKey.KEY_AB_INBOX_REVAMP,
                            RollenceKey.VARIANT_NEW_INBOX
                    );
            Toast.makeText(DeveloperOptionActivity.this, "Inbox: New", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("SetTextI18n")
    private void initListener() {
        remoteConfigStartButton.setOnClickListener(v -> {
            Editable prefix = remoteConfigPrefix.getTextFieldInput().getEditableText();

            startRemoteConfigEditor(prefix != null ? prefix.toString() : "");
        });

        abTestRollenceEditorStartButton.setOnClickListener(v -> {
            startAbTestRollenceEditor();
        });

        vForceCrash.setOnClickListener(v -> {
            throw new RuntimeException("Throw Runtime Exception");
        });

        resetOnBoarding.setOnClickListener(v -> {
            userSession.setFirstTimeUser(true);
            getSharedPreferences(CACHE_FREE_RETURN).edit().clear().apply();
            Toast.makeText(this, "Reset Onboarding", Toast.LENGTH_SHORT).show();
        });

        sendTimberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timberMessage = editTextTimberMessage.getTextFieldInput().getText().toString();
                if (TextUtils.isEmpty(timberMessage)) {
                    Toast.makeText(DeveloperOptionActivity.this,
                            "Timber message should not empty", Toast.LENGTH_SHORT).show();
                } else {
                    //"Pno##TAG##message (message: abc=123##edf=456) (required Server Logging ON)"
                    int priorityIndex = 0;
                    int tagIndex = 1;
                    String priority = "";
                    String tag = "";
                    String delimiterMessage = "##";
                    String regexEqualSign = "=([*]*)";
                    Map<String, String> messageMap = new HashMap<>();
                    String[] splitMessage = timberMessage.split(delimiterMessage);
                    for (int i = 0; i < splitMessage.length; i++) {
                        if (i == priorityIndex) {
                            priority = splitMessage[priorityIndex];
                        } else if (i == tagIndex) {
                            tag = splitMessage[tagIndex];
                        } else {
                            String message = splitMessage[i];
                            if (!TextUtils.isEmpty(message)) {
                                String[] keyValue = message.split(regexEqualSign);
                                if (getOrNull(keyValue, 0) != null && getOrNull(keyValue, 1) != null) {
                                    messageMap.put(keyValue[0], keyValue[1]);
                                } else {
                                    Toast.makeText(DeveloperOptionActivity.this,
                                            "Invalid timber message format", Toast.LENGTH_LONG).show();
                                    return;
                                }
                            }
                        }
                    }

                    Priority priorityLogger = null;
                    if (priority.equals("P1")) {
                        priorityLogger = Priority.P1;
                    } else if (priority.equals("P2")) {
                        priorityLogger = Priority.P2;
                    }
                    if (priorityLogger != null) {
                        ServerLogger.log(priorityLogger, tag, messageMap);
                        Toast.makeText(DeveloperOptionActivity.this,
                                timberMessage + " has been sent", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        sharedPrefButton.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                startActivity(new Intent(DeveloperOptionActivity.this, SharedPrefActivity.class));
            }
        });

        sendFirebaseCrash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String crashMessage = editTextFirebaseCrash.getTextFieldInput().getText().toString();
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
                String routeManagerString = editTextRouteManager.getTextFieldInput().getText().toString();
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
                String versionCode = editTextChangeVersionCode.getTextFieldInput().getText().toString();
                String versionName = editTextChangeVersionName.getTextFieldInput().getText().toString();
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

        SharedPreferences cache = getSharedPreferences(DevOptConfig.CHUCK_ENABLED);

        toggleChuck.setChecked(cache.getBoolean(DevOptConfig.IS_CHUCK_ENABLED, false));

        toggleChuck.setOnCheckedChangeListener((compoundButton, state) -> {
            cache.edit().putBoolean(DevOptConfig.IS_CHUCK_ENABLED, state).apply();
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

        toggleDarkMode.setChecked((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);
        toggleDarkMode.setOnCheckedChangeListener((view, state) -> AppCompatDelegate.setDefaultNightMode(state ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO));

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

        toggleSellerAppReview.setVisibility(GlobalConfig.isSellerApp() ? View.VISIBLE : View.GONE);
        toggleSellerAppReview.setChecked(SellerInAppReview.getSellerAppReviewDebuggingEnabled(getApplicationContext()));
        toggleSellerAppReview.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SellerInAppReview.setSellerAppReviewDebuggingEnabled(getApplicationContext(), isChecked);
        });

        vGoToFpm.setOnClickListener(v -> FpmLogger.getInstance().openActivity());

        toggleAnalytics.setChecked(GtmLogger.getInstance(this).isNotificationEnabled());

        toggleAnalytics.setOnCheckedChangeListener((compoundButton, state) -> GtmLogger.getInstance(this).enableNotification(state));

        vGoToCassava.setOnClickListener(v -> startActivity(MainValidatorActivity.newInstance(this)));
        vGoToAnalytics.setOnClickListener(v -> startActivity(AnalyticsDebuggerActivity.newInstance(this)));

        vGoToIrisSaveLogDB.setOnClickListener(v -> {
            IrisLogger.getInstance(DeveloperOptionActivity.this).openSaveActivity();
        });

        vGoToIrisSendLogDB.setOnClickListener(v -> {
            IrisLogger.getInstance(DeveloperOptionActivity.this).openSendActivity();
        });


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

        appAuthSecretView.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            String decoder = AppAuthKt.getDecoder(this);
            ClipData clip = ClipData.newPlainText("Copied Text", decoder);
            if (clipboard != null) {
                clipboard.setPrimaryClip(clip);
            }
            Toast.makeText(this, decoder, Toast.LENGTH_LONG).show();
        });

        findViewById(R.id.get_system_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                List<ApplicationInfo> apps = DeveloperOptionActivity.this.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
                StringBuilder systemApps = new StringBuilder();
                for (ApplicationInfo applicationInfo : apps) {
                    if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM) {
                        if (!"".equals(systemApps)) {
                            systemApps.append(",");
                        }
                        systemApps.append(applicationInfo.packageName);
                    }
                }
                String text = systemApps.toString();
                ClipData clip = ClipData.newPlainText("Copied Text", text);
                if (clipboard != null) {
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(DeveloperOptionActivity.this, text, Toast.LENGTH_LONG).show();
            }
        });

        findViewById(R.id.get_system_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApps(true);
            }
        });

        findViewById(R.id.get_non_system_apps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showApps(false);
            }
        });

        requestFcmToken.setOnClickListener(v -> {
            Intent intent = new Intent(this, DeleteFirebaseTokenService.class);
            startService(intent);
        });

        tvFakeResponse.setOnClickListener(v -> {
            new FakeResponseActivityProvider().startActivity(this);
        });

        toggleLeakCanary.setVisibility(GlobalConfig.isSellerApp() ? View.GONE : View.VISIBLE);
        toggleLeakCanary.setChecked(getLeakCanaryToggleValue());
        toggleLeakCanary.setOnCheckedChangeListener((buttonView, isChecked) -> {
            getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).edit().putBoolean(LEAK_CANARY_TOGGLE_KEY, isChecked).apply();
            Toast.makeText(DeveloperOptionActivity.this, "Please Restart the App", Toast.LENGTH_SHORT).show();
        });
    }

    private void showApps(boolean isSystemApps) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        List<ApplicationInfo> apps = DeveloperOptionActivity.this.getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuilder systemApps = new StringBuilder();
        for (ApplicationInfo applicationInfo : apps) {
            boolean check;
            if (isSystemApps) {
                check = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;
            } else {
                check = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != ApplicationInfo.FLAG_SYSTEM;
            }
            if (check) {
                if (systemApps.length() != 0) {
                    systemApps.append(",");
                }
                systemApps.append(applicationInfo.packageName);
            }
        }
        String text = systemApps.toString();
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        if (clipboard != null) {
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(DeveloperOptionActivity.this, text, Toast.LENGTH_LONG).show();
    }

    private boolean getLeakCanaryToggleValue() {
        return getSharedPreferences(LEAK_CANARY_TOGGLE_SP_NAME, MODE_PRIVATE).getBoolean(LEAK_CANARY_TOGGLE_KEY, LEAK_CANARY_DEFAULT_TOGGLE);
    }

    public Object getOrNull(String[] list, int index) {
        if (index >= 0 && index <= list.length - 1) {
            return list[index];
        } else {
            return null;
        }
    }

    private void startAbTestRollenceEditor() {
        Intent intent = new Intent(DeveloperOptionActivity.this, AbTestRollenceConfigFragmentActivity.class);
        startActivity(intent);
    }

    private int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
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

    private SharedPreferences getSharedPreferences(String name) {
        return getSharedPreferences(name, Context.MODE_PRIVATE);
    }

    private void initTranslator() {
        new com.tokopedia.translator.manager.TranslatorManager().init(this.getApplication(), API_KEY_TRANSLATOR);
    }

    private class DeveloperOptionException extends RuntimeException {
        public DeveloperOptionException(String message) {
            super(message);
        }
    }
}