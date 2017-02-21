package com.tokopedia.core;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.chuck.Chuck;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.OneOnClick;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.instoped.InstagramAuth;
import com.tokopedia.core.instoped.fragment.InstagramMediaFragment;
import com.tokopedia.core.instoped.model.InstagramMediaModel;
import com.tokopedia.core.network.BasicNetworkHandler;
import com.tokopedia.core.network.TkpdNetworkURLHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.onboarding.ConstantOnBoarding;
import com.tokopedia.core.onboarding.FreeReturnOnboardingActivity;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.util.PasswordGenerator;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.var.TkpdCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tkpd.library.kirisame.network.util.VolleyNetworkRequestQueue;


public class DeveloperOptions extends TActivity implements SessionHandler.onLogoutListener {
    public static final String DOMAIN_WS_4 = "DOMAIN_WS4";
    public static final String DOMAIN_WS_41 = "DOMAIN_WS_4";
    //developer test
    private EditText vHost;
    private EditText vAddress;
    private EditText vPort;
    private TextView vLastBuildTime;
    private TextView vBuildNum;
    private TextView vMessageBuild;
    private TextView vSubmitBut;
    private TextView vCheckBut;
    private TextView vDownloadBut;
    private TextView vLastBuildTimeStable;
    private TextView vBuildNumStable;
    private TextView vMessageBuildStable;
    private TextView vCheckButStable;
    private TextView vDownloadButStable;
    private TextView vCustomIntent;
    private TextView resetOnBoarding;
    private TextView testOnBoarding;
    private TextView vForceCrash;
    private CheckBox toggleHttp;
    private View vMaintenance;
    private View saveSetting;
    private EditText etWs4;
    private TextView saveWs4;
    private TextView vGoTochuck;
    private CheckBox toggleChuck;

    private RadioGroup rgWs4;
    private RadioButton rbWs4Live;
    private RadioButton rbWs4Staging;
    private RadioButton rbWs4Alpha;

    public static class URLDeveloper {
        public static String UPDATE_URL = "http://android-jenkins.office.tokopedia.com:8080/job/android-tkpd/lastSuccessfulBuild/api/json";
        public static String APK_URL = "http://android-jenkins.office.tokopedia.com:8080/job/android-tkpd/lastSuccessfulBuild/artifact/build/outputs/apk/dev-debug.apk";
        public static String UPDATE_STABLE_URL = "http://android-jenkins.office.tokopedia.com:8080/job/android-tkpd-release/lastSuccessfulBuild/api/json";
        public static String APK_STABLE_URL = "http://android-jenkins.office.tokopedia.com:8080/job/android-tkpd-release/lastSuccessfulBuild/artifact/build/outputs/apk/release-debug.apk";
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_DEVELOPER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);

        vHost = (EditText) findViewById(R.id.base_url);
        vSubmitBut = (TextView) findViewById(R.id.submit_but);
        vLastBuildTime = (TextView) findViewById(R.id.last_update);
        vBuildNum = (TextView) findViewById(R.id.build_num);
        vMessageBuild = (TextView) findViewById(R.id.msg);
        vCheckBut = (TextView) findViewById(R.id.check_but);
        vDownloadBut = (TextView) findViewById(R.id.download_but);
        vLastBuildTimeStable = (TextView) findViewById(R.id.last_update_stable);
        vBuildNumStable = (TextView) findViewById(R.id.build_num_stable);
        vMessageBuildStable = (TextView) findViewById(R.id.msg_stable);
        vCheckButStable = (TextView) findViewById(R.id.check_but_stable);
        vForceCrash = (TextView) findViewById(R.id.force_crash);
        toggleHttp = (CheckBox) findViewById(R.id.toggle_http);
        vAddress = (EditText) findViewById(R.id.proxy_add);
        vPort = (EditText) findViewById(R.id.proxy_port);
        vDownloadButStable = (TextView) findViewById(R.id.download_but_stable);
        vMaintenance = findViewById(R.id.maintenance);
        saveSetting = findViewById(R.id.save_setting);
        vHost.setText(TkpdNetworkURLHandler.getHost(this));
        toggleHttp.setChecked(TkpdNetworkURLHandler.getProtocolHttp(this));
        vAddress.setText(TkpdNetworkURLHandler.getProxyAddress(this, ""));
        vPort.setText(TkpdNetworkURLHandler.getProxyPort(this) + "");
        vCustomIntent = (TextView) findViewById(R.id.custom_intent);
        resetOnBoarding = (TextView) findViewById(R.id.reset_onboarding);
        testOnBoarding = (TextView) findViewById(R.id.test_onboarding);
        etWs4 = (EditText) findViewById(R.id.et_dev_ws4);
        saveWs4 = (TextView) findViewById(R.id.btn_save_dev_ws4);

        rgWs4 = (RadioGroup) findViewById(R.id.rg_dev_ws4);
        rbWs4Alpha = (RadioButton) findViewById(R.id.rb_dev_ws4_alpha);
        rbWs4Staging = (RadioButton) findViewById(R.id.rb_dev_ws4_staging);
        rbWs4Live = (RadioButton) findViewById(R.id.rb_dev_ws4_live);

        vGoTochuck = (TextView) findViewById(R.id.goto_chuck);
        toggleChuck = (CheckBox) findViewById(R.id.toggle_chuck);

        initListener();
        initView();

        TrackingUtils.eventLocaInAppMessaging("in-app : Clicked Developer Options");
        TrackingUtils.eventLocaInApp("event : Clicked Developer Options");

        CommonUtils.dumper("LocalTag : Clicked Developer Options");

    }

    private void initListener() {
        vSubmitBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidForm()) {
                    validateLogin();
                }
            }
        });
        vCheckBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForUpdate();
            }
        });
        vDownloadBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadLastUpdate();
            }
        });
        vCheckButStable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkForStableUpdate();
            }
        });
        vDownloadButStable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadLastStableUpdate();
            }
        });
        vMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMaintenance();
            }
        });
        saveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveSetting();
            }
        });
        vCustomIntent.setOnClickListener(onCustomClick());

        SharedPreferences pref = getApplicationContext().getSharedPreferences(DOMAIN_WS_41,
                MODE_PRIVATE);
        etWs4.setText(pref.getString(DOMAIN_WS_4, TkpdBaseURL.BASE_DOMAIN));
//        switch (pref.getString(DOMAIN_WS_4, TkpdBaseURL.BASE_DOMAIN)) {
//            case TkpdBaseURL.LIVE_DOMAIN:
//                rgWs4.check(R.id.rb_dev_ws4_live);
//                break;
//            case TkpdBaseURL.STAGE_DOMAIN:
//                rgWs4.check(R.id.rb_dev_ws4_staging);
//                break;
//            case TkpdBaseURL.ALPHA_DOMAIN:
//                rgWs4.check(R.id.rb_dev_ws4_live);
//                break;
//            default:
//                break;
//        }

        saveWs4.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                SharedPreferences pref = getApplicationContext().getSharedPreferences(DOMAIN_WS_41,
                        MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(DOMAIN_WS_4, etWs4.getText().toString());
                if (editor.commit())
                    Toast.makeText(DeveloperOptions.this, etWs4.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rgWs4.setVisibility(View.GONE);
        rgWs4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //   saveWs4Domain(checkedId);
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
                LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), "CHUCK_ENABLED");
                cache.putBoolean("is_enable", state);
                cache.applyEditor();
            }
        });

        vGoTochuck.setOnClickListener(new OneOnClick() {
            @Override
            public void oneOnClick(View view) {
                startActivity(Chuck.getLaunchIntent(getApplicationContext()));
            }
        });

    }

    public void initView() {
        LocalCacheHandler cache = new LocalCacheHandler(getApplicationContext(), "CHUCK_ENABLED");
        toggleChuck.setChecked(cache.getBoolean("is_enable", false));
    }

    public static final String getWsV4Domain(Context context) {
        return context.getSharedPreferences(DOMAIN_WS_41,
                MODE_PRIVATE).getString(DOMAIN_WS_4, TkpdBaseURL.LIVE_DOMAIN);
    }


    public static String getUrlWsV4(Context context) {
        SharedPreferences pref = context.getSharedPreferences("DOMAIN_WS_4",
                MODE_PRIVATE);
        return pref.getString("DOMAIN_WS4", TkpdBaseURL.LIVE_DOMAIN);
    }

    private void saveWs4Domain(int checkedId) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(DOMAIN_WS_41,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        switch (checkedId) {
            case R2.id.rb_dev_ws4_alpha:
                editor.putString(DOMAIN_WS_4, TkpdBaseURL.ALPHA_DOMAIN);
                if (editor.commit())
                    Toast.makeText(this, TkpdBaseURL.ALPHA_DOMAIN, Toast.LENGTH_SHORT).show();
                break;
            case R2.id.rb_dev_ws4_staging:
                editor.putString(DOMAIN_WS_4, TkpdBaseURL.STAGE_DOMAIN);
                if (editor.commit())
                    Toast.makeText(this, TkpdBaseURL.STAGE_DOMAIN, Toast.LENGTH_SHORT).show();
                break;
            case R2.id.rb_dev_ws4_live:
                editor.putString(DOMAIN_WS_4, TkpdBaseURL.LIVE_DOMAIN);
                if (editor.commit())
                    Toast.makeText(this, TkpdBaseURL.LIVE_DOMAIN, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    InstagramAuth auth = new InstagramAuth();

    private View.OnClickListener onCustomClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.setGetMediaListener(new InstagramMediaFragment.OnGetInstagramMediaListener() {
                    @Override
                    public void onSuccess(SparseArray<InstagramMediaModel> selectedModel) {
                        selectedModel.size();
                    }
                });
                auth.getMedias(getSupportFragmentManager());
            }
        };
    }

    private Boolean isValidForm() {
        vHost.setError(null);
        Boolean isValid = true;
        if (vHost.getText() == null || vHost.getText().toString().equals("")) {
            isValid = false;
            vHost.setError(getResources().getString(R.string.error_field_required));
        }

        return isValid;
    }

    private void validateLogin() {
        if (SessionHandler.isV4Login(this)) {
            logoutCurrentUser();
        } else {
            changeHost(vHost.getText().toString());
            restartApp();
        }
    }

    private void changeHost(String host) {
        TkpdNetworkURLHandler.setHost(this, host);
    }

    private void restartApp() {
        LocalCacheHandler.clearCache(this, TkpdCache.INDEX);
        PasswordGenerator.clearTokenStorage(this);
        startActivity(new Intent(this, SplashScreen.class));
    }

    private void logoutCurrentUser() {
        SessionHandler session = new SessionHandler(this);
        session.Logout(this);
    }


    @Override
    public void onLogout(Boolean success) {
        changeHost(vHost.getText().toString());
        restartApp();
    }

    public void checkForUpdate() {
        BasicNetworkHandler network = new BasicNetworkHandler(this, URLDeveloper.UPDATE_URL);
        network.commit(new BasicNetworkHandler.BasicNetworkResponse() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void onResponse(String Result) {
                try {
                    JSONObject ResultJSON = new JSONObject(Result);
                    long time = Long.parseLong(ResultJSON.getString("timestamp"));
                    vLastBuildTime.setText("Last Build Time: " + CommonUtils.getDate(time));
                    vBuildNum.setText("Build Number: " + ResultJSON.getString("number"));
                    JSONObject ChangeSet = new JSONObject(ResultJSON.getString("changeSet"));
                    JSONArray Items = new JSONArray(ChangeSet.getString("items"));
                    JSONObject DetailChanged = new JSONObject(Items.getString(Items.length() - 1));
                    vMessageBuild.setText("Message: " + DetailChanged.get("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String Msg) {

            }
        });
    }

    private void downloadLastUpdate() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLDeveloper.APK_URL));
        startActivity(intent);
    }

    public void checkForStableUpdate() {
        BasicNetworkHandler network = new BasicNetworkHandler(this, URLDeveloper.UPDATE_STABLE_URL);
        network.commit(new BasicNetworkHandler.BasicNetworkResponse() {
            @Override
            public void onSuccess(Boolean status) {

            }

            @Override
            public void onResponse(String Result) {
                try {
                    JSONObject ResultJSON = new JSONObject(Result);
                    long time = Long.parseLong(ResultJSON.getString("timestamp"));
                    vLastBuildTimeStable.setText("Last Build Time: " + CommonUtils.getDate(time));
                    vBuildNumStable.setText("Build Number: " + ResultJSON.getString("number"));
                    JSONObject ChangeSet = new JSONObject(ResultJSON.getString("changeSet"));
                    JSONArray Items = new JSONArray(ChangeSet.getString("items"));
                    JSONObject DetailChanged = new JSONObject(Items.getString(Items.length() - 1));
                    vMessageBuildStable.setText("Message: " + DetailChanged.get("msg"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String Msg) {

            }
        });
    }

    private void downloadLastStableUpdate() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URLDeveloper.APK_STABLE_URL));
        startActivity(intent);
    }

    private void setMaintenance() {
        startActivity(MaintenancePage.createIntentFromNetwork(this, ""));
    }

    private void onSaveSetting() {
        TkpdNetworkURLHandler.setProtocolHttp(this, toggleHttp.isChecked());
        int port = Integer.parseInt(vPort.getText().toString());
        String address = vAddress.getText().toString();
        if (address.isEmpty())
            address = null;
        VolleyNetworkRequestQueue.getInstance(this).setProxy(address, port);
        TkpdNetworkURLHandler.setProxyAddress(this, address);
        TkpdNetworkURLHandler.setProxyPort(this, port);
        Toast.makeText(this, "Proxy set to " + address + ":" + port, Toast.LENGTH_SHORT).show();
    }
}
