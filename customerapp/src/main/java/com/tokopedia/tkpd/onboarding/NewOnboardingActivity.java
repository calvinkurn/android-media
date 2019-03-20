package com.tokopedia.tkpd.onboarding;

import android.content.Context;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;
import com.tokopedia.tkpd.BuildConfig;
import com.tokopedia.tkpd.R;

import com.github.paolorotolo.appintro.AppIntro;
import com.tokopedia.tkpd.onboarding.util.CustomAnimationPageTransformer;
import com.tokopedia.tkpd.ConsumerRouterApplication;
import com.tokopedia.tkpd.onboarding.analytics.ConsumerOnboardingAnalytics;
import com.tokopedia.tkpd.onboarding.fragment.NewOnBoardingFragment;
import com.tokopedia.user.session.UserSession;

import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by steven on 7/25/2017.
 */
public class NewOnboardingActivity extends AppIntro {

    private static final String SCREEN_NAME = "Screen OnBoarding - ";
    protected View indicator;
    protected View bottom;
    private TextView skipView;
    private ImageButton nextView;
    private int[] fragmentColor;
    private boolean isNextPressed = false;
    protected View decorView;
    private UserSession userSession;
    private ConsumerOnboardingAnalytics analytics;
    private boolean isAppsflyerCallbackHandled;

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new UserSession(this);
        analytics = new ConsumerOnboardingAnalytics();
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);

        initView();
        addSlides();
        pager.setOffscreenPageLimit(1);
        setSkip();
        setNext();
        pager.setPageTransformer(false, new CustomAnimationPageTransformer());

        if(GlobalConfig.IS_PREINSTALL) {
            TrackingUtils.sendInstallSourceEvent(this);
        }
    }

    private void initView() {
        indicator = findViewById(R.id.indicator_container);
        bottom = findViewById(R.id.bottom);
        nextView = (ImageButton) nextButton;
        skipView = (TextView) skipButton;

        decorView = getWindow().getDecorView();
        showStatusBar(false);

        fragmentColor = new int[]{R.color.green_nob,
                R.color.blue_nob,
                R.color.orange_nob,
                R.color.green_nob,
                R.color.blue_nob};
    }

    private void addSlides() {
        RemoteConfig remoteConfig = new FirebaseRemoteConfigImpl(this);
        addSlide(NewOnBoardingFragment.newInstance(remoteConfig.getString(RemoteConfigKey.NONB1_TTL,
                getString(R.string.nonb_1_title)),
                remoteConfig.getString(RemoteConfigKey.NONB1_DESC, getString(R.string.nonb_1_desc)),
                "onboarding1.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[0]),
                NewOnBoardingFragment.VIEW_DEFAULT, 0));
        addSlide(NewOnBoardingFragment.newInstance(remoteConfig.getString(RemoteConfigKey.NONB2_TTL,
                getString(R.string.nonb_2_title)),
                remoteConfig.getString(RemoteConfigKey.NONB2_DESC, getString(R.string.nonb_2_desc)),
                "onboarding2.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[1]),
                NewOnBoardingFragment.VIEW_DEFAULT, 1));
        addSlide(NewOnBoardingFragment.newInstance(remoteConfig.getString(RemoteConfigKey.NONB3_TTL,
                getString(R.string.nonb_3_title)),
                remoteConfig.getString(RemoteConfigKey.NONB3_DESC, getString(R.string.nonb_3_desc)),
                "onboarding3.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[2]),
                NewOnBoardingFragment.VIEW_DEFAULT, 2));
        addSlide(NewOnBoardingFragment.newInstance(remoteConfig.getString(RemoteConfigKey.NONB4_TTL,
                getString(R.string.nonb_4_title)),
                remoteConfig.getString(RemoteConfigKey.NONB4_DESC, getString(R.string.nonb_4_desc)),
                "onboarding4.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[3]),
                NewOnBoardingFragment.VIEW_DEFAULT, 3));
        addSlide(NewOnBoardingFragment.newInstance(remoteConfig.getString(RemoteConfigKey.NONB5_TTL,
                getString(R.string.nonb_5_title)),
                remoteConfig.getString(RemoteConfigKey.NONB5_DESC, getString(R.string.nonb_5_desc)),
                "onboarding5.json",
                ContextCompat.getColor(getApplicationContext(), fragmentColor[4]),
                NewOnBoardingFragment.VIEW_ENDING, 4));
    }

    private void setSkip() {
        showSkipButton(false);
        setSkipText("Lewati");
        skipView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen
                .sp_14));
        skipView.setTypeface(Typeface.DEFAULT_BOLD);
        skipView.setGravity(Gravity.LEFT | Gravity.CENTER);
        skipView.setAllCaps(false);

        float density = getResources().getDisplayMetrics().density;
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) skipView.getLayoutParams();
        params.leftMargin = (int) (20 * density);
        setSeparatorColor(MethodChecker.getColor(this, R.color.transparent));
    }


    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finishOnboard();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finishOnboard();
    }

    private void finishOnboard() {
        analytics.eventOnboardingSkip(getApplicationContext(), pager.getCurrentItem() + 1);
        userSession.setFirstTimeUserOnboarding(false);

        Intent intent = ((ConsumerRouterApplication) getApplicationContext()).getHomeIntent(this);
        if (!TextUtils.isEmpty(((ConsumerRouterApplication) getApplicationContext())
                        .getDefferedDeeplinkPathIfExists())) {
            intent.putExtra("EXTRA_APPLINK", ((ConsumerRouterApplication) getApplicationContext())
                        .getDefferedDeeplinkPathIfExists());
        }
        startActivity(intent);
        finish();

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if (pager.getCurrentItem() == fragments.size() - 1) {
            setButtonVisibility(bottom, GONE);
            setButtonVisibility(skipButton, GONE);
            setButtonVisibility(indicator, GONE);
        } else {
            setButtonVisibility(bottom, VISIBLE);
            setButtonVisibility(skipButton, VISIBLE);
            setButtonVisibility(indicator, VISIBLE);
        }
        int pageNumber = pager.getCurrentItem() + 1;
        analytics.sendScreen(getApplicationContext(), SCREEN_NAME + pageNumber);
    }

    private void setButtonVisibility(View view, int visible) {
        if (view != null) {
            view.setVisibility(visible);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        userSession.setFirstTimeUser(false);
        finishOnboard();
    }

    private void setNext() {
        nextView.setImageResource(R.drawable.next_ic);
        nextView.setMinimumWidth(0);
        FrameLayout.LayoutParams nextViewLayoutParams =
                (FrameLayout.LayoutParams) nextView.getLayoutParams();
        float density = getResources().getDisplayMetrics().density;
        nextViewLayoutParams.rightMargin = (int) (20 * density);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_onboarding;
    }
}
